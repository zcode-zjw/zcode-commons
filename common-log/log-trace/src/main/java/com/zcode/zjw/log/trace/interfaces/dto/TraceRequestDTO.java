package com.zcode.zjw.log.trace.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 追踪请求DTO
 *
 * @author zhangjiwei
 * @since 2023/8/4
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TraceRequestDTO implements Serializable {

    @NotNull
    private String classpath;

    private String methodName;

    @NotNull
    private String basePackage;

    private String backupFileName;

    private List<String> excludeMethod;

    private Boolean excludeCallMethods;

    private Boolean excludeCalledMethods;

}
