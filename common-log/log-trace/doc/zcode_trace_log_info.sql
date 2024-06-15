-- 创建链路追踪日志信息表
DROP TABLE IF EXISTS zcode_trace_log_info;
CREATE TABLE zcode_trace_log_info
(
    `id`              BIGINT(20) UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    `flow_id`         VARCHAR(255) NOT NULL COMMENT '流程ID',
    `var_name`        VARCHAR(255) COMMENT '变量名称',
    `var_type`        VARCHAR(255) COMMENT '变量类型',
    `var_value`       VARCHAR(255) COMMENT '变量值',
    `create_datetime` VARCHAR(255) COMMENT '创建时间'
) COMMENT '链路追踪日志信息表' ENGINE = INNODB
                               DEFAULT CHARSET = utf8mb4;