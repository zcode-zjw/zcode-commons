package com.zcode.zjw.configs.common;

/**
 * @author zhangjiwei
 * @description 配置文件类型枚举
 * @date 2022/12/2 下午10:08
 */
public enum ConfigPropertiesTypeEnum {

    APPLICATION("application") {
        @Override
        protected boolean supports(String fileName) {
            return fileName.equals("application");
        }
    },
    HOST_APP("oamp") {
        @Override
        protected boolean supports(String fileName) {
            return fileName.equals("oamp");
        }
    },
    SSH("oamp-ssh") {
        @Override
        protected boolean supports(String fileName) {
            return fileName.equals("oamp-ssh");
        }
    },
    CONNECT_POOL("oamp-pool") {
        @Override
        protected boolean supports(String fileName) {
            return fileName.equals("oamp-pool");
        }
    },
    TASK_TASK("oamp-timetask") {
        @Override
        protected boolean supports(String fileName) {
            return fileName.equals("oamp-timetask");
        }
    },
    FILE_MONITOR("oamp-monitor") {
        @Override
        protected boolean supports(String fileName) {
            return fileName.equals("oamp-monitor");
        }
    };

    ConfigPropertiesTypeEnum(String desc) {
        this.fileName = desc;
    }

    public String getFileName() {
        return fileName;
    }

    private String fileName;

    /**
     * 是否找的是自己
     *
     * @param fileName
     * @return
     */
    protected abstract boolean supports(String fileName);

    /**
     * 选择类型
     *
     * @param fileName 文件名称
     * @return
     */
    public static ConfigPropertiesTypeEnum selectType(String fileName) {
        for (ConfigPropertiesTypeEnum type : ConfigPropertiesTypeEnum.values()) {
            if (type.supports(fileName)) {
                return type;
            }
        }
        return null;
    }

}
