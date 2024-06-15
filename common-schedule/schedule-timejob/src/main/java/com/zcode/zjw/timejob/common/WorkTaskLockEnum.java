package com.zcode.zjw.timejob.common;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangjiwei
 * @description 工作任务锁类型
 * @date 2022/11/7 下午9:47
 */
public enum WorkTaskLockEnum {

    MUTEX_LOCK("互斥锁"),
    OPTIMISTIC_LOCK("乐观锁");
    
    WorkTaskLockEnum(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return desc;
    }

    private String desc;

    public static WorkTaskLockEnum selectType(String desc) {
        for (WorkTaskLockEnum value : WorkTaskLockEnum.values()) {
            if (desc.equals(value.getDesc())) {
                return value;
            }
        }
        return null;
    }

    public static List<WorkTaskLockEnum> getAllLockType() {
        return new ArrayList<WorkTaskLockEnum>() {
            {
                add(MUTEX_LOCK);
                add(OPTIMISTIC_LOCK);
            }
        };
    }

}
