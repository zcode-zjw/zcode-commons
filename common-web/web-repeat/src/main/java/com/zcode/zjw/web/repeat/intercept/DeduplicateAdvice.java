package com.zcode.zjw.web.repeat.intercept;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdviceAdapter;

import java.lang.reflect.Type;

/**
 * @author zhangjiwei
 * @description 标记非重复标识
 * @date 2023/2/9 下午2:08
 */
@ControllerAdvice
@Order(999)
public class DeduplicateAdvice extends RequestBodyAdviceAdapter {

    @Value("${zcode.repeat.open:false}")
    private Boolean open;


    @Override
    public boolean supports(@NotNull MethodParameter methodParameter, @NotNull Type type,
                            @NotNull Class<? extends HttpMessageConverter<?>> aClass) {
        return open != null && open;
    }

    @Override
    public Object afterBodyRead(@NotNull Object body, @NotNull HttpInputMessage inputMessage, @NotNull MethodParameter parameter,
                                @NotNull Type targetType, @NotNull Class<? extends HttpMessageConverter<?>> converterType) {
        return super.afterBodyRead(body, inputMessage, parameter, targetType, converterType);
    }

}
