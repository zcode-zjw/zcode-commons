CREATE TABLE `t_permission`
(
    `id`            bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `permission_id` bigint(20),
    `method`        varchar(255)        NOT NULL,
    `name`          varchar(255)        NOT NULL,
    `module`        text                NOT NULL,
    `create_time`   datetime   DEFAULT CURRENT_TIMESTAMP,
    `update_time`   datetime   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`       tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;