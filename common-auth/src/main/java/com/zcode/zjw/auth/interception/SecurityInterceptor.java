package com.zcode.zjw.auth.interception;

import com.zcode.zjw.auth.annotation.LoginRequired;
import com.zcode.zjw.auth.annotation.RequiresPermission;
import com.zcode.zjw.auth.common.AuthInfoStoreTypeEnum;
import com.zcode.zjw.auth.common.WebConstant;
import com.zcode.zjw.auth.pojo.User;
import com.zcode.zjw.cache.redis.utils.RedisCache;
import com.zcode.zjw.common.utils.common.ThreadLocalUtil;
import com.zcode.zjw.common.utils.encrypt.JwtUtil;
import com.zcode.zjw.web.common.BizException;
import com.zcode.zjw.web.common.ExceptionCodeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Import;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;
import java.util.Set;

@Component
@Import(RedisCache.class)
public class SecurityInterceptor implements HandlerInterceptor {

    @Autowired
    private RedisCache redisCache;

    @Value("${auth.infoStore.type:local}")
    private String authStoreType;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 不拦截跨域请求相关
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 如果类或方法上没有加@LoginRequired或@RequiredPermission（上面叠加了@LoginRequired），直接放行
        if (isLoginFree(handler)) {
            return true;
        }

        // 登录校验，session里如果没有用户信息，就抛异常给globalExceptionHandler提示“需要登录”
        User user = handleLogin(request, response);
        ThreadLocalUtil.put(WebConstant.USER_INFO, user);

        // 权限校验，校验不通过就抛异常，交给全局异常处理
        checkPermission(user, handler, request);

        // 放行到Controller
        //return super.preHandle(request, response, handler);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 及时移除，避免ThreadLocal内存泄漏
        ThreadLocalUtil.remove(WebConstant.USER_INFO);
        //super.afterCompletion(request, response, handler, ex);
    }

    /**
     * 接口是否免登录（支持Controller上添加@LoginRequired）
     *
     * @param handler 被拦截的方法
     * @return 是否免登录
     */
    private boolean isLoginFree(Object handler) {
        // 判断是否支持免登录
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;

            // 类上是否有@LoginRequired
            Class<?> controllerClazz = handlerMethod.getBeanType();
            LoginRequired ControllerLogin = AnnotationUtils.findAnnotation(controllerClazz, LoginRequired.class);

            // 方法上是否有@LoginRequired
            Method method = handlerMethod.getMethod();
            LoginRequired methodLogin = AnnotationUtils.getAnnotation(method, LoginRequired.class);

            return ControllerLogin == null && methodLogin == null;
        }

        return true;
    }

    /**
     * 登录校验
     *
     * @param request  请求对象
     * @param response 响应对象
     * @return 登录校验成功后的用户对象
     */
    private User handleLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        HttpSession session = request.getSession();
        User currentUser = (User) session.getAttribute(WebConstant.CURRENT_USER_IN_SESSION);
        // 判断其他两种
        if (AuthInfoStoreTypeEnum.TOKEN.getKey().equals(authStoreType)) {
            // 获取token
            String tokenByHeader = request.getHeader(WebConstant.TOKEN_AUTH_HEADER_KEY);
            // 解析token
            String userId = JwtUtil.parseJWT(tokenByHeader).getId();
            // 从缓存中验证是否有该用户
            User userByCache = redisCache.getCacheMapValue(userId, WebConstant.CURRENT_USER_IN_SESSION);
            if (userByCache == null) {
                // 抛异常，请先登录
                throw new BizException(ExceptionCodeEnum.NEED_LOGIN);
            }
            return userByCache;
        } else { // 由于缓存类型也需要依赖session，所以对于缓存需要先判断本地session
            // 对本地session进行判断
            if (currentUser == null) {
                // 抛异常，请先登录
                throw new BizException(ExceptionCodeEnum.NEED_LOGIN);
            }
            // 对缓存进行判断
            if (AuthInfoStoreTypeEnum.CACHE.getKey().equals(authStoreType)) {
                User redisUser = redisCache.getCacheMapValue(String.valueOf(currentUser.getId()), WebConstant.CURRENT_USER_IN_SESSION);
                if (redisUser == null) {
                    // 抛异常，请先登录
                    throw new BizException(ExceptionCodeEnum.NEED_LOGIN);
                }
                return redisUser;
            }
        }

        return currentUser;
    }

    /**
     * 权限校验
     *
     * @param user    需要校验的用户
     * @param handler 被拦截的方法
     */
    private void checkPermission(User user, Object handler, HttpServletRequest request) {
        // 如果类和当前方法上都没有加@RequiresPermission，说明不需要权限校验，直接放行
        if (isPermissionFree(handler)) {
            return;
        }

        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            Class<?> controllerClazz = handlerMethod.getBeanType();

            // 代码走到这，已经很明确，当前方法需要权限才能访问，那么当前用户有没有权限呢？
            Set<String> userPermissionMethods;
            if (AuthInfoStoreTypeEnum.LOCAL.getKey().equals(authStoreType)) {
                userPermissionMethods = (Set<String>) request.getSession().getAttribute(WebConstant.USER_PERMISSIONS);
            } else {
                userPermissionMethods = redisCache.getCacheMapValue(String.valueOf(user.getId()), WebConstant.USER_PERMISSIONS);
            }
            String currentRequestMethod = "#" + method.getName();
            String currentClassName = controllerClazz.getName();
            for (String userPermissionMethod : userPermissionMethods) {
                if (userPermissionMethod.contains(currentRequestMethod) && userPermissionMethod.contains(currentClassName)) {
                    return;
                }
            }

            // 当前访问的方法需要权限，但是当前用户不具备该权限
            throw new BizException(ExceptionCodeEnum.PERMISSION_DENY);
        }
    }

    /**
     * 是否需要权限校验
     *
     * @param handler 被拦截的方法
     * @return 是否通过
     */
    private boolean isPermissionFree(Object handler) {
        // 判断是否需要权限认证
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Class<?> controllerClazz = handlerMethod.getBeanType();
            Method method = handlerMethod.getMethod();
            RequiresPermission controllerPermission = AnnotationUtils.getAnnotation(controllerClazz, RequiresPermission.class);
            RequiresPermission methodPermission = AnnotationUtils.getAnnotation(method, RequiresPermission.class);
            // 没有加@RequiresPermission，不需要权限认证
            return controllerPermission == null && methodPermission == null;
        }

        return true;
    }

}