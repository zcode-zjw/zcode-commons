package com.zcode.zjw.log.trace.domain.core.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

/**
 * 追踪方法实体
 *
 * @author zhangjiwei
 * @since 2023/8/8
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class TraceMethodEntity {

    private String classpath;

    private String methodName;

    private String basePackage;

    private String backupFileName;

    private List<String> excludeMethod;

    private Boolean excludeCallMethods;

    private Boolean excludeCalledMethods;

}
