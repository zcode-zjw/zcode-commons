package com.zcode.zjw.timejob.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangjiwei
 * @description 任务操作类型
 * @date 2022/11/4 下午10:19
 */
public enum JobOperateTypeEnum implements IJobOperateType {

    /**
     * 任务开始
     */
    START(),

    /**
     * 停止任务
     */
    STOP();

    JobOperateTypeEnum() {
    }

    @Override
    public List<IJobOperateType> getTypeList() {
        ArrayList<IJobOperateType> typeList = new ArrayList<>();
        typeList.add(START);
        typeList.add(STOP);
        return typeList;
    }
}
