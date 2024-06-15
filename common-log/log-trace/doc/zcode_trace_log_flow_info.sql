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