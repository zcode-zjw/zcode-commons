-- 创建配置参数表
DROP TABLE IF EXISTS `zcode_config_params`;
CREATE TABLE `zcode_config_params`
(
    `id`          BIGINT(20) UNSIGNED PRIMARY KEY AUTO_INCREMENT COMMENT 'ID', -- ID
    `key`         VARCHAR(255) COMMENT '配置key',
    `value`       VARCHAR(255) COMMENT '参数值',
    `description` VARCHAR(255) COMMENT '描述',
    `level`       int(11) COMMENT '参数等级（值越大等级越低）' DEFAULT 3
) ENGINE = INNODB
  DEFAULT CHARSET = 'utf8mb4';

-- init data
INSERT INTO zcode_config_params(`key`, `value`, `description`) VALUES ('print_sql', 'true', '是否打印运行时sql语句');
INSERT INTO zcode_config_params(`key`, `value`, `description`) VALUES ('api_white_list', null, 'api白名单');
INSERT INTO zcode_config_params(`key`, `value`, `description`) VALUES ('web_ignored', null, '静态资源放行名单');
INSERT INTO zcode_config_params(`key`, `value`, `description`) VALUES ('own_site_dir', null, '项目所在目录');
INSERT INTO zcode_config_params(`key`, `value`, `description`) VALUES ('node_type', 'local', '节点类型');
INSERT INTO zcode_config_params(`key`, `value`, `description`) VALUES ('swagger', '{title: Demo, version: 1.0.0, description: Demo}', 'swagger配置');
INSERT INTO zcode_config_params(`key`, `value`, `description`) VALUES ('ftp', '{host: localhost, port: 21, password: admin, username: admin}', 'ftp配置');
INSERT INTO zcode_config_params(`key`, `value`, `description`) VALUES ('ssh', '{host: localhost, port: 22, password: null, username: mac}', 'ssh配置');