package com.zcode.zjw.log.trace.domain.core.repository;

import com.zcode.zjw.log.trace.domain.core.repository.mapper.TraceLogInfoFlowMapper;
import com.zcode.zjw.log.trace.domain.core.repository.mapper.TraceLogInfoMapper;
import com.zcode.zjw.log.trace.domain.core.repository.mapper.TraceFileRecordMapper;
import org.springframework.stereotype.Repository;

/**
 * 追踪日志流程信息仓储服务
 *
 * @author zhangjiwei
 * @since 2023/8/4
 */
@Repository
public class TraceFlowInfoRepository {

    private final TraceLogInfoMapper traceLogInfoMapper;

    private final TraceFileRecordMapper traceFileRecordMapper;

    private final TraceLogInfoFlowMapper traceLogInfoFlowMapper;

    public TraceLogInfoFlowMapper getTraceLogInfoFlowMapper() {
        return traceLogInfoFlowMapper;
    }

    public TraceFlowInfoRepository(TraceLogInfoMapper traceLogInfoMapper, TraceFileRecordMapper traceFileRecordMapper, TraceLogInfoFlowMapper traceLogInfoFlowMapper) {
        this.traceLogInfoMapper = traceLogInfoMapper;
        this.traceFileRecordMapper = traceFileRecordMapper;
        this.traceLogInfoFlowMapper = traceLogInfoFlowMapper;
    }


}
