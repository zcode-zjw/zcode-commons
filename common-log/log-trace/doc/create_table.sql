-- 创建链路追踪文件记录表
DROP TABLE IF EXISTS zcode_trace_file_record;
CREATE TABLE zcode_trace_file_record
(
    `id`              BIGINT(20) UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    `flow_id`         VARCHAR(255) NOT NULL COMMENT '流程ID',
    `source_path`     VARCHAR(255) NOT NULL COMMENT '原始文件路径',
    `backup_path`     VARCHAR(255) NOT NULL COMMENT '备份文件路径',
    `class_path`      VARCHAR(255) NOT NULL COMMENT '追踪类路径',
    `method_name`     VARCHAR(255) NOT NULL COMMENT '追踪方法名称',
    `status`          INT          NOT NULL COMMENT '状态（0未追踪、1正在追踪）',
    `create_datetime` VARCHAR(255) COMMENT '创建时间',
    `update_datetime` VARCHAR(255) COMMENT '修改时间'
) COMMENT '链路追踪文件记录表' ENGINE = INNODB
                               DEFAULT CHARSET = utf8mb4;

-- 创建链路追踪日志信息表
DROP TABLE IF EXISTS zcode_trace_log_info;
CREATE TABLE zcode_trace_log_info
(
    `id`              BIGINT(20) UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    `flow_id`         VARCHAR(255) NOT NULL COMMENT '流程ID',
    `method_name`     VARCHAR(255) COMMENT '所属方法',
    `var_scope`       VARCHAR(255) COMMENT '变量作用域',
    `var_name`        VARCHAR(255) COMMENT '变量名称',
    `var_type`        VARCHAR(255) COMMENT '变量类型',
    `var_value`       VARCHAR(255) COMMENT '变量值',
    `create_datetime` VARCHAR(255) COMMENT '创建时间'
) COMMENT '链路追踪日志信息表' ENGINE = INNODB
                               DEFAULT CHARSET = utf8mb4;

-- 创建链路追踪日志流程信息表
DROP TABLE IF EXISTS zcode_trace_log_flow_info;
CREATE TABLE zcode_trace_log_flow_info
(
    `id`              BIGINT(20) UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    `flow_id`         VARCHAR(255) NOT NULL COMMENT '流程ID',
    `start_method`    VARCHAR(255) NOT NULL COMMENT '初始方法',
    `status`          INT DEFAULT 1 COMMENT '流程状态（0：失效，1：有效）',
    `create_datetime` VARCHAR(255) COMMENT '创建时间'
) COMMENT '链路追踪日志流程信息表' ENGINE = INNODB
                                   DEFAULT CHARSET = utf8mb4;