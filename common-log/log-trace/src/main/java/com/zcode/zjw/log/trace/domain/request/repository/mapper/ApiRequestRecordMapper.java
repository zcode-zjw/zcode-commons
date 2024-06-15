package com.zcode.zjw.log.trace.domain.request.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zcode.zjw.log.trace.domain.request.repository.po.ApiRequestRecord;
import org.apache.ibatis.annotations.Mapper;

/**
 * 数据访问层
 *
 * @author zhangjiwei
 * @since 2023/8/4
 */
@Mapper
public interface ApiRequestRecordMapper extends BaseMapper<ApiRequestRecord> {


}
