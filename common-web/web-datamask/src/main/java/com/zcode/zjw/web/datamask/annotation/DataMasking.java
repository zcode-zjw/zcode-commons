package com.zcode.zjw.web.datamask.annotation;

import com.zcode.zjw.web.datamask.common.DataMaskingFunc;

import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataMasking {

    DataMaskingFunc maskFunc() default DataMaskingFunc.NO_MASK;

}