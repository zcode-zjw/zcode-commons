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
 * 链路追踪文件记录实体
 *
 * @author zhangjiwei
 * @since 2023/8/4
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@TableName("zcode_trace_file_record")
public class TraceFileRecord implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 流程ID
     */
    private String flowId;

    /**
     * 原始文件路径
     */
    private String sourcePath;

    /**
     * 备份文件路径
     */
    private String backupPath;

    /**
     * 类路径
     */
    private String classPath;

    /**
     * 追踪的方法名称
     */
    private String methodName;

    /**
     * 状态（0未追踪、1正在追踪）
     */
    private Integer status;

    private String createDatetime = DateUtils.getCurrentDateStringFormat();

    private String updateDatetime = DateUtils.getCurrentDateStringFormat();


}
