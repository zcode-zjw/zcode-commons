package com.zcode.zjw.auth.interception;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.zcode.zjw.auth.annotation.RequiresPermission;
import com.zcode.zjw.auth.mapper.PermissionMapper;
import com.zcode.zjw.auth.pojo.Permission;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 系统启动时收集全部的权限方法，同步到t_permission
 */
@Component
public class PermissionMethodCollectionListener implements ApplicationListener<ContextRefreshedEvent>, ApplicationContextAware {

    private List<Permission> insertPermissionCache = new ArrayList<>();

    @PostConstruct
    public void initInsertPermissionCache() {
        Wrapper<Permission> queryWrapper = new QueryWrapper<>();
        insertPermissionCache = permissionMapper.selectList(queryWrapper);
    }

    /**
     * 这里演示通过ApplicationContextAware注入，你也可以直接使用@AutoWired
     */
    private ApplicationContext applicationContext;

    @Resource
    private PermissionMapper permissionMapper;

    @Value("${spring.application.name}")
    private String applicationName;

    @Override
    public void setApplicationContext(@NotNull ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void onApplicationEvent(@NotNull ContextRefreshedEvent contextRefreshedEvent) {
        // 得到t_permission已有的所有权限方法
        Set<String> permissionsFromDB = new HashSet<>();
        List<Permission> permissions = permissionMapper.selectList(new QueryWrapper<>());
        if (CollectionUtils.isNotEmpty(permissions)) {
            permissions.forEach(permission -> {
                String methodName = permission.getMethod();
                permissionsFromDB.add(methodName);
            });
        }

        // 遍历所有Controller
        Map<String, Object> beanMap = applicationContext.getBeansWithAnnotation(Controller.class);
        Collection<Object> beans = beanMap.values();
        for (Object bean : beans) {
            Class<?> controllerClazz = bean.getClass();

            // 如果Controller上有@RequiresPermission，那么所有接口都要收集(isApiMethod)，否则只收集打了@Permission的接口(hasPermissionAnnotation)
            Predicate<Method> filter = AnnotationUtils.findAnnotation(controllerClazz, RequiresPermission.class) != null
                    ? this::isApiMethod
                    : this::hasPermissionAnnotation;

            // 过滤出Controller中需要权限验证的method
            Set<String> permissionMethodsWithinController = getPermissionMethodsWithinController(
                    controllerClazz.getName(),
                    controllerClazz.getMethods(),
                    filter
            );

            for (String permissionMethodInMemory : permissionMethodsWithinController) {
                // 如果是新增的权限方法
                if (!permissionsFromDB.contains(permissionMethodInMemory)) {
                    Permission permission = new Permission();
                    permission.setModule(getModuleName());
                    permission.setName(applicationName);
                    // 方法名称含有代理对象标识，去掉再添加
                    String realMethodName = "";
                    String[] splitArray = permissionMethodInMemory.split("\\$\\$");
                    if (splitArray.length > 2) {
                        String[] classMethodSplitArray = splitArray[2].split("\\#");
                        if (classMethodSplitArray.length > 1) {
                            realMethodName = splitArray[0] + "#" + classMethodSplitArray[1];
                        } else {
                            realMethodName = splitArray[0] + splitArray[2];
                        }
                    } else {
                        realMethodName = permissionMethodInMemory;
                    }
                    permission.setMethod(realMethodName);

                    if (!insertPermissionCache.contains(permission)) { // 不重复添加
                        permissionMapper.insert(permission);
                        insertPermissionCache.add(permission);
                    }
                }
            }
        }

    }

    private Set<String> getPermissionMethodsWithinController(String controllerName, Method[] methods, Predicate<Method> filter) {

        return Arrays.stream(methods)
                .filter(filter)
                .map(method -> {
                    StringBuilder sb = new StringBuilder();
                    String methodName = method.getName();
                    return sb.append(controllerName).append("#").append(methodName).toString();
                })
                .collect(Collectors.toSet());
    }

    private boolean hasPermissionAnnotation(Method method) {
        return AnnotationUtils.findAnnotation(method, RequestMapping.class) != null
                && AnnotationUtils.findAnnotation(method, RequiresPermission.class) != null;
    }

    private boolean isApiMethod(Method method) {
        return AnnotationUtils.findAnnotation(method, RequestMapping.class) != null;
    }

    private static String getModuleName() {
        String basePath = System.getProperty("user.dir");
        String[] pathSegments = basePath.split("/");
        return pathSegments[pathSegments.length - 1];
    }

}