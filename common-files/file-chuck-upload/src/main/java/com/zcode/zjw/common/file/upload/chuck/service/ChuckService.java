package com.zcode.zjw.common.file.upload.chuck.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zcode.zjw.common.file.upload.chuck.entity.Chuck;
import com.zcode.zjw.common.file.upload.chuck.mapper.ChuckMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author zhangjiwei
 * @description 分块服务
 * @date 2022年11月18日 下午 11:08
 */
@Slf4j
@Service
public class ChuckService extends ServiceImpl<ChuckMapper, Chuck> {

    /**
     * 保存分块信息
     *
     * @param chuck
     */
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public Chuck saveChuck(Chuck chuck) {
        final int insert = baseMapper.insert(chuck);
        return 1 == insert ? chuck : null;
    }

    /**
     * 判断该分块是否上传过
     *
     * @param chuck
     * @return
     */
    public boolean checkHasChucked(Chuck chuck) {
        final List<Chuck> chucks = lambdaQuery()
                .eq(Chuck::getIdentifier, chuck.getIdentifier())
                .eq(Chuck::getChunkNumber, chuck.getChunkNumber())
                .list();
        return CollectionUtils.isNotEmpty(chucks);
    }
}