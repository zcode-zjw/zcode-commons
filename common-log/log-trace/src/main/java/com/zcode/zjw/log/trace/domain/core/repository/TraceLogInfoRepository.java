package com.zcode.zjw.log.trace.domain.core.repository;

import com.zcode.zjw.log.trace.domain.core.repository.po.TraceLogInfo;
import com.zcode.zjw.log.trace.domain.core.repository.mapper.TraceLogInfoMapper;
import org.springframework.stereotype.Repository;

/**
 * 追踪日志信息仓储服务
 *
 * @author zhangjiwei
 * @since 2023/8/4
 */
@Repository
public class TraceLogInfoRepository {

    private final TraceLogInfoMapper traceLogInfoMapper;

    public TraceLogInfoMapper getTraceLogInfoMapper() {
        return traceLogInfoMapper;
    }

    public TraceLogInfoRepository(TraceLogInfoMapper traceLogInfoMapper) {
        this.traceLogInfoMapper = traceLogInfoMapper;
    }

    public int insert(TraceLogInfo traceLogInfo) {
        return traceLogInfoMapper.insert(traceLogInfo);
    }

}
