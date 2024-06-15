package com.zcode.zjw.log.trace.domain.core.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zcode.zjw.log.trace.domain.core.repository.po.TraceFileRecord;
import com.zcode.zjw.log.trace.domain.core.repository.mapper.TraceFileRecordMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 追踪文件信息仓储服务
 *
 * @author zhangjiwei
 * @since 2023/8/4
 */
@Repository
public class TraceFileInfoRepository {

    private final TraceFileRecordMapper traceFileRecordMapper;

    public TraceFileRecordMapper getTraceFileRecordMapper() {
        return traceFileRecordMapper;
    }

    public TraceFileInfoRepository(TraceFileRecordMapper traceFileRecordMapper) {
        this.traceFileRecordMapper = traceFileRecordMapper;
    }

    /**
     * 获取正在被追踪的文件记录
     *
     * @param sourceFilePath 源文件路径
     * @return
     */
    public List<TraceFileRecord> findTracingFileRecord(String sourceFilePath) {
        QueryWrapper<TraceFileRecord> queryWrapperByTraceFile = new QueryWrapper<>();
        queryWrapperByTraceFile.eq("source_path", sourceFilePath);
        queryWrapperByTraceFile.eq("status", 1);
        return traceFileRecordMapper.selectList(queryWrapperByTraceFile);
    }

    public int insert(TraceFileRecord traceFileRecord) {
        return traceFileRecordMapper.insert(traceFileRecord);
    }

    public int insert(String flowId, String sourceFilePath, String backupFilePath, String sourceClassPath, String methodName, int status) {
        TraceFileRecord traceFileRecord = new TraceFileRecord();
        traceFileRecord.setStatus(status);
        traceFileRecord.setBackupPath(backupFilePath);
        traceFileRecord.setSourcePath(sourceFilePath);
        traceFileRecord.setFlowId(flowId);
        traceFileRecord.setMethodName(methodName);
        traceFileRecord.setClassPath(sourceClassPath);
        return traceFileRecordMapper.insert(traceFileRecord);
    }


    public List<TraceFileRecord> findRecordByFlowId(String flowId) {
        QueryWrapper<TraceFileRecord> wrapperByFlow = new QueryWrapper<>();
        wrapperByFlow.eq("flow_id", flowId);
        return traceFileRecordMapper.selectList(wrapperByFlow);
    }

    public int updateStatus(String sourceFilePath, String backupFilePath) {
        TraceFileRecord traceFileRecord = new TraceFileRecord();
        traceFileRecord.setStatus(0); // 设置为未追踪状态
        QueryWrapper<TraceFileRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("backup_path", backupFilePath);
        queryWrapper.eq("source_path", sourceFilePath);
        return traceFileRecordMapper.update(traceFileRecord, queryWrapper);
    }


}
