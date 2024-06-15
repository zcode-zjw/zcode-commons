package com.zcode.zjw.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.zcode.zjw.auth.annotation.LoginRequired;
import com.zcode.zjw.auth.annotation.RequiresPermission;
import com.zcode.zjw.auth.common.AuthInfoStoreTypeEnum;
import com.zcode.zjw.auth.common.LoginExtendPointType;
import com.zcode.zjw.auth.common.WebConstant;
import com.zcode.zjw.auth.interception.BaseLoginExtend;
import com.zcode.zjw.auth.mapper.*;
import com.zcode.zjw.auth.pojo.*;
import com.zcode.zjw.cache.redis.utils.RedisCache;
import com.zcode.zjw.common.utils.common.ConvertUtil;
import com.zcode.zjw.common.utils.encrypt.JwtUtil;
import com.zcode.zjw.log.api.annotation.ApiLog;
import com.zcode.zjw.log.user.annotation.UserLog;
import com.zcode.zjw.log.user.common.ModuleEnum;
import com.zcode.zjw.log.user.common.OperationEnum;
import com.zcode.zjw.web.common.Result;
import com.zcode.zjw.web.datamask.annotation.DataMasking;
import com.zcode.zjw.web.datamask.common.DataMaskingFunc;
import com.zcode.zjw.web.encrypt.annotation.ApiDecrypt;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
@ApiLog
@DataMasking(maskFunc = DataMaskingFunc.ALL_MASK)
@ApiDecrypt
public class UserController {

    @Value("${auth.infoStore.type:local}")
    private String authStoreType;

    @Value("${auth.login.expireTimeMillis:86400}")
    private Long authLoginExpireTime; // 默认一天后登录过期

    @Value("${auth.infoStore.token.expireTimeMillis:3600}")
    private Integer authStoreTokenExpireTime; // 默认一个小时后token过期

    @Resource
    private UserMapper userMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private SysMenuMapper sysMenuMapper;
    @Resource
    private SysRoleMenuRelMapper sysRoleMenuRelMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private RolePermissionMapper rolePermissionMapper;
    @Resource
    private PermissionMapper permissionMapper;
    @Autowired
    private RedisCache redisCache;
    @Autowired(required = false)
    private BaseLoginExtend loginExtend;

    @GetMapping("test/permission")
    @LoginRequired
    @RequiresPermission
    public Result<?> testPermissions() {
        return Result.success();
    }

    @GetMapping("test/login")
    @LoginRequired
    public Result<?> testLogin() {
        return Result.success();
    }

    @PostMapping("logout")
    @UserLog(module = ModuleEnum.USER, title = "用户登出", type = OperationEnum.LOGIN)
    public Result<Object> logout(@RequestBody User user, HttpServletRequest request) {
        // 删除本地Session的用户信息
        request.getSession().getAttribute(WebConstant.CURRENT_USER_IN_SESSION);
        // 删除缓存中的用户信息
        if (!AuthInfoStoreTypeEnum.LOCAL.getKey().equals(authStoreType)) {
            redisCache.delCacheMapValue(WebConstant.CURRENT_USER_IN_SESSION, String.valueOf(user.getId()));
        }

        return Result.success("登出成功");
    }

    @PostMapping("/login")
    @UserLog(module = ModuleEnum.USER, title = "用户登录", type = OperationEnum.LOGIN)
    public Result<Object> login(@RequestBody User loginInfo, HttpServletRequest request, HttpServletResponse response) {
        if (loginExtend == null) {
            return loginMainFlow(loginInfo, request);
        }
        LoginExtendPointType loginExtendPointType = loginExtend.point();
        if (loginExtendPointType != null) {
            // 在主流程之前执行扩展方法
            if (loginExtendPointType.equals(LoginExtendPointType.BEFORE)) {
                if (loginExtend.flowPass(loginInfo, null, request, response)) {
                    return loginMainFlow(loginInfo, request);
                } else {
                    return Result.error(loginExtend.failureMsg());
                }
            } else {
                // 在主流程之后执行扩展方法
                Result<Object> objectResult = loginMainFlow(loginInfo, request);
                if (loginExtend.flowPass(loginInfo, objectResult, request, response)) {
                    return objectResult;
                } else {
                    return Result.error(loginExtend.failureMsg());
                }
            }
        } else {
            return loginMainFlow(loginInfo, request);
        }
    }

    /**
     * 获取用户信息
     *
     * @param request
     * @return
     */
    @PostMapping("info")
    @ResponseBody
    public Result<?> userInfo(HttpServletRequest request) {
        String userid = getUserIdByToken(request);
        if (null == userid) {
            return Result.error("身份过期，请重新登陆！");
        }
        //从redis中获取用户信息
        User loginUser = redisCache.getCacheMapValue(userid, WebConstant.CURRENT_USER_IN_SESSION);
        return Result.success(loginUser);

    }

    /**
     * 获取用户所属角色下所有的功能菜单
     *
     * @param request
     * @return
     */
    @PostMapping("menu/list")
    @ResponseBody
    public Result<Object> findUserMenuList(HttpServletRequest request) {
        String userId = getUserIdByToken(request);
        if (null == userId) {
            return Result.error("身份过期，请重新登陆！");
        }
        // 查询每个角色下拥有的所有菜单
        return Result.success(sysMenuMapper.findAllMenuForUser(userId));
    }

    /**
     * 获取用户所属角色下所有的功能菜单
     *
     * @param request
     * @return
     */
    @PostMapping("full/info")
    @ResponseBody
    public Result<Object> finUserFullInfo(HttpServletRequest request) {
        String userId = getUserIdByToken(request);
        if (null == userId) {
            return Result.error("身份过期，请重新登陆！");
        }
        // 先查询角色
        UserFullInfoVO userFullInfoVO = userMapper.finUserFullInfo(userId);
        List<RoleFullInfoVO> roles = userFullInfoVO.getRoles();
        roles.forEach(role -> role.setMenus(sysMenuMapper.findAllMenuByRoleId(String.valueOf(role.getId()))));

        return Result.success(userFullInfoVO);
    }

    /**
     * 通过解析token获取用户ID
     *
     * @param request
     * @return
     */
    private static String getUserIdByToken(HttpServletRequest request) {
        //解析token
        String token = request.getHeader(WebConstant.TOKEN_AUTH_HEADER_KEY);
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getId();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return userid;
    }

    /**
     * 登录主流程
     *
     * @param loginInfo 需要登录的用户信息对象
     * @param request   请求对象
     * @return 返回给前端的信息
     */
    private Result<Object> loginMainFlow(User loginInfo, HttpServletRequest request) {
        LambdaQueryWrapper<User> lambdaQuery = Wrappers.lambdaQuery();
        lambdaQuery.eq(User::getUsername, loginInfo.getUsername());
        lambdaQuery.eq(User::getPassword, loginInfo.getPassword());

        User user = userMapper.selectOne(lambdaQuery);
        if (user == null) {
            return Result.error("用户名或密码错误");
        }

        // 记录状态信息，并返回
        return Result.success(recordAuthInfoStatus(request, user));
    }

    /**
     * 记录认证后的信息/状态
     *
     * @param request 请求对象
     * @param user    成功认证后的用户
     * @return 返回给前端的信息
     */
    private Object recordAuthInfoStatus(HttpServletRequest request, User user) {
        if (AuthInfoStoreTypeEnum.TOKEN.getKey().equals(authStoreType)) {
            // 1、生成token
            String token = JwtUtil.createJWT(String.valueOf(user.getId()), WebConstant.USER_TOKEN_SUBJECT,
                    authStoreTokenExpireTime * 1000L);
            // 2、将用户信息放入redis缓存中
            redisCache.setCacheMapValue(String.valueOf(user.getId()), WebConstant.CURRENT_USER_IN_SESSION, user);
            redisCache.expire(String.valueOf(user.getId()), authLoginExpireTime); // 设置过期时间
            // 3.Session记录登录状态
            request.getSession().setAttribute(WebConstant.CURRENT_USER_IN_SESSION, user);
            // 4、记录权限信息
            redisCache.setCacheMapValue(String.valueOf(user.getId()), WebConstant.USER_PERMISSIONS, getUserPermissions(user.getId()));
            // 5、返回token
            return token;
        } else if (AuthInfoStoreTypeEnum.CACHE.getKey().equals(authStoreType)) {
            // 1.Session记录登录状态
            request.getSession().setAttribute(WebConstant.CURRENT_USER_IN_SESSION, user);
            // 2、redis记录登录状态
            redisCache.setCacheMapValue(String.valueOf(user.getId()), WebConstant.CURRENT_USER_IN_SESSION, user);
            // 3.redis缓存用户拥有的权限
            redisCache.setCacheMapValue(String.valueOf(user.getId()), WebConstant.USER_PERMISSIONS, getUserPermissions(user.getId()));
        } else {
            // 1.Session记录登录状态
            request.getSession().setAttribute(WebConstant.CURRENT_USER_IN_SESSION, user);
            // 2.Session缓存用户拥有的权限
            request.getSession().setAttribute(WebConstant.USER_PERMISSIONS, getUserPermissions(user.getId()));
        }

        return "登录成功";
    }

    /**
     * 获取登录用户的权限
     *
     * @param uid 用户ID
     * @return 用户具有的权限方法集合
     */
    private Set<String> getUserPermissions(Long uid) {
        // 用户拥有的角色
        LambdaQueryWrapper<UserRole> userRoleQuery = Wrappers.lambdaQuery();
        userRoleQuery.eq(UserRole::getUserId, uid);
        List<UserRole> userRoles = userRoleMapper.selectList(userRoleQuery);
        List<Long> roleIds = ConvertUtil.resultToList(userRoles, UserRole::getRoleId);
        if (CollectionUtils.isEmpty(roleIds)) {
            // 没有角色，所有没有权限
            return new HashSet<>();
        }

        // 角色拥有的权限
        List<Long> permissionIds = new ArrayList<>();
        rolePermissionMapper.selectBatchIds(roleIds);
        roleIds.forEach(roleId -> {
            LambdaQueryWrapper<RolePermission> rolePermissionQuery = Wrappers.lambdaQuery();
            rolePermissionQuery.eq(RolePermission::getRoleId, roleId);
            List<RolePermission> rolePermissions = rolePermissionMapper.selectList(rolePermissionQuery);
            permissionIds.addAll(ConvertUtil.resultToList(rolePermissions, RolePermission::getPermissionId));
        });
        if (CollectionUtils.isEmpty(permissionIds)) {
            // 角色都没有分配权限
            return new HashSet<>();
        }

        // 查询权限对应的method
        return Optional.ofNullable(permissionMapper.selectBatchIds(permissionIds))
                .map(permissionList -> permissionList.stream().map(Permission::getMethod).collect(Collectors.toSet()))
                .orElse(Collections.emptySet());

    }
}