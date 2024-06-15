package com.zcode.zjw.common.utils.bean;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;

/**
 * Spring工具类
 *
 * @author zhangjiwei
 * @since 2023/8/6
 */
public class SpringUtils {

    /**
     * 是否为SpringBean
     *
     * @param classify 包名 + 类名
     * @return
     */
    public static boolean isSpringBean(String classify) throws ClassNotFoundException {
        try {
            SpringContextUtils.getBean(Class.forName(classify));
            return true;
        } catch (NoSuchBeanDefinitionException e) {
            return false;
        }
    }

}
