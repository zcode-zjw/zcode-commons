-- 创建链路追踪文件记录表
DROP TABLE IF EXISTS zcode_trace_file_record;
CREATE TABLE zcode_trace_file_record
(
    `id`              BIGINT(20) UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID',
    `flow_id`         VARCHAR(255) NOT NULL COMMENT '流程ID',
    `source_path`     VARCHAR(255) NOT NULL COMMENT '原始文件路径',
    `backup_path`     VARCHAR(255) NOT NULL COMMENT '备份文件路径',
    `method_name`     VARCHAR(255) NOT NULL COMMENT '追踪方法名称',
    `status`          INT          NOT NULL COMMENT '状态（0未追踪、1正在追踪）',
    `create_datetime` VARCHAR(255) COMMENT '创建时间',
    `update_datetime` VARCHAR(255) COMMENT '修改时间'
) COMMENT '链路追踪文件记录表' ENGINE = INNODB
                               DEFAULT CHARSET = utf8mb4;