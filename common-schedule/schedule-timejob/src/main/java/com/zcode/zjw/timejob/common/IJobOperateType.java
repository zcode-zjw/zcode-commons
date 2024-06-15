package com.zcode.zjw.timejob.common;

import java.util.List;

/**
 * @author zhangjiwei
 * @description 任务操作类型接口
 * @date 2022/11/5 上午11:34
 */
public interface IJobOperateType {

    /**
     * 获取操作类型
     *
     * @return 操作类型
     */
    List<IJobOperateType> getTypeList();

}
