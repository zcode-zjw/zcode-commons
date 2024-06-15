package com.zcode.zjw.common.utils.bean;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 动态手动注册Spring Bean
 */
public class DynamicRegisterBeanUtil {

    /**
     * 主动向Spring容器中注册bean
     *
     * @param applicationContext Spring容器
     * @param name               BeanName
     * @param clazz              注册的bean的类性
     * @return 返回注册到容器中的bean对象
     */
    public static <T> T registerBean(ConfigurableApplicationContext applicationContext, String name,
                                     Class<T> clazz, Supplier<T> instanceSupplier) {
        if (applicationContext.containsBean(name)) {
            Object bean = applicationContext.getBean(name);
            if (bean.getClass().isAssignableFrom(clazz)) {
                return (T) bean;
            } else {
                throw new RuntimeException("BeanName 重复 " + name);
            }
        }
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz, instanceSupplier);
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
        beanFactory.registerBeanDefinition(name, beanDefinition);
        return applicationContext.getBean(name, clazz);
    }

    /**
     * 主动向Spring容器中注册bean
     *
     * @param applicationContext Spring容器
     * @param name               BeanName
     * @param clazz              注册的bean的类性
     * @param <T>
     * @return 返回注册到容器中的bean对象
     */
    public static <T> T registerBean(ConfigurableApplicationContext applicationContext, String name, Class<T> clazz, List<Object> args) {
        if (applicationContext.containsBean(name)) {
            Object bean = applicationContext.getBean(name);
            if (bean.getClass().isAssignableFrom(clazz)) {
                return (T) bean;
            } else {
                throw new RuntimeException("BeanName 重复 " + name);
            }
        }
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        // 构造参数
        for (Object arg : args) {
            beanDefinitionBuilder.addConstructorArgValue(arg);
        }
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
        beanFactory.registerBeanDefinition(name, beanDefinition);
        return applicationContext.getBean(name, clazz);
    }

    /**
     * 主动向Spring容器中注册bean
     *
     * @param applicationContext Spring容器
     * @param name               BeanName
     * @param clazz              注册的bean的类性
     * @param args               构造方法的必要参数，顺序和类型要求和clazz中定义的一致
     * @param <T>
     * @return 返回注册到容器中的bean对象
     */
    public static <T> T registerBean(ConfigurableApplicationContext applicationContext, String name, Class<T> clazz,
                                     List<Object> args, Map<String, Object> autowiredProperties) {
        if (applicationContext.containsBean(name)) {
            Object bean = applicationContext.getBean(name);
            if (bean.getClass().isAssignableFrom(clazz)) {
                return (T) bean;
            } else {
                throw new RuntimeException("BeanName 重复 " + name);
            }
        }
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        // 构造参数
        for (Object arg : args) {
            beanDefinitionBuilder.addConstructorArgValue(arg);
        }
        // 依赖注入
        for (String propertyName : autowiredProperties.keySet()) {
            //beanDefinitionBuilder.addAutowiredProperty(propertyName);
            beanDefinitionBuilder.addPropertyValue(propertyName, autowiredProperties.get(propertyName));
        }

        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
        beanFactory.registerBeanDefinition(name, beanDefinition);
        return applicationContext.getBean(name, clazz);
    }

    /**
     * 主动向Spring容器中注册bean
     *
     * @param applicationContext  Spring容器
     * @param name                BeanName
     * @param clazz               注册的bean的类性
     * @param autowiredProperties 注入的属性集
     * @param dependOns           注入依赖集
     * @param args                构造方法的必要参数，顺序和类型要求和clazz中定义的一致
     * @return 返回注册到容器中的bean对象
     */
    public static <T> T registerBean(ConfigurableApplicationContext applicationContext, String name, Class<T> clazz,
                                     List<Object> args, Map<String, Object> autowiredProperties, List<String> dependOns) {
        if (applicationContext.containsBean(name)) {
            Object bean = applicationContext.getBean(name);
            if (bean.getClass().isAssignableFrom(clazz)) {
                return (T) bean;
            } else {
                throw new RuntimeException("BeanName 重复 " + name);
            }
        }
        BeanDefinitionBuilder beanDefinitionBuilder = BeanDefinitionBuilder.genericBeanDefinition(clazz);
        // 构造参数
        for (Object arg : args) {
            beanDefinitionBuilder.addConstructorArgValue(arg);
        }
        // 属性注入
        for (String propertyName : autowiredProperties.keySet()) {
            //beanDefinitionBuilder.addAutowiredProperty(propertyName);
            beanDefinitionBuilder.addPropertyValue(propertyName, autowiredProperties.get(propertyName));
        }
        // 依赖注入
        for (String dependOnBeanName : dependOns) {
            beanDefinitionBuilder.addDependsOn(dependOnBeanName);
        }
        BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
        BeanDefinitionRegistry beanFactory = (BeanDefinitionRegistry) applicationContext.getBeanFactory();
        beanFactory.registerBeanDefinition(name, beanDefinition);
        return applicationContext.getBean(name, clazz);
    }

}