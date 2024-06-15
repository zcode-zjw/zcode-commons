package com.zcode.zjw.log.trace.domain.core.repository.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.zcode.zjw.common.utils.common.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 链路追踪日志信息实体
 *
 * @author zhangjiwei
 * @since 2023/8/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("zcode_trace_log_info")
public class TraceLogInfo implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String flowId;

    /**
     * 所属方法
     */
    private String methodName;

    /**
     * 变量作用域
     */
    private String varScope;

    /**
     * 变量名称
     */
    private String varName;

    /**
     * 变量类型
     */
    private String varType;

    /**
     * 变量值
     */
    private Object varValue;

    private String createDatetime = DateUtils.getCurrentDateStringFormat();


}
