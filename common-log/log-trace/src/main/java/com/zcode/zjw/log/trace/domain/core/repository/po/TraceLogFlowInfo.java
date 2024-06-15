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
 * 链路追踪流信息实体
 *
 * @author zhangjiwei
 * @since 2023/8/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("zcode_trace_log_flow_info")
public class TraceLogFlowInfo implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 流程ID
     */
    private String flowId;

    /**
     * 初始方法
     */
    private String startMethod;

    /**
     * 流程状态（0：失效，1：有效）
     */
    private Integer status;


    private String createDatetime = DateUtils.getCurrentDateStringFormat();


}
