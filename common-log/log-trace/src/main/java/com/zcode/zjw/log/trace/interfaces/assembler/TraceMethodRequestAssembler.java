package com.zcode.zjw.log.trace.interfaces.assembler;

import com.zcode.zjw.log.trace.domain.core.entity.TraceMethodEntity;
import com.zcode.zjw.log.trace.interfaces.dto.TraceRequestDTO;
import org.springframework.beans.BeanUtils;

/**
 * 追踪方法请求汇编者
 *
 * @author zhangjiwei
 * @since 2023/8/8
 */
public class TraceMethodRequestAssembler {

    public TraceMethodEntity toDo(TraceRequestDTO traceRequestDTO) {
        TraceMethodEntity traceMethodEntity = new TraceMethodEntity();
        BeanUtils.copyProperties(traceRequestDTO, traceMethodEntity);
        return traceMethodEntity;
    }

    public TraceRequestDTO toDto(TraceMethodEntity traceMethodEntity) {
        TraceRequestDTO traceRequestDTO = new TraceRequestDTO();
        BeanUtils.copyProperties(traceMethodEntity, traceRequestDTO);
        return traceRequestDTO;
    }

}
