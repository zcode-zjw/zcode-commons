package com.zcode.zjw.log.trace.domain.core.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcode.zjw.log.trace.domain.core.repository.po.TraceLogFlowInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据访问层
 *
 * @author zhangjiwei
 * @since 2023/8/4
 */
@Mapper
public interface TraceLogInfoFlowMapper extends BaseMapper<TraceLogFlowInfo> {


}
