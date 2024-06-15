package com.zcode.zjw.web.encrypt.annotation;



import com.zcode.zjw.web.encrypt.common.EncryptAlgorithmEnum;

import java.lang.annotation.*;

/**
 * 限流注解
 *
 * @author zhangjiwei
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiDecrypt {

    EncryptAlgorithmEnum algorithm() default EncryptAlgorithmEnum.RSA;

}
