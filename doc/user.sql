SET NAMES utf8mb4;
SET
    FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`
(
    `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`         varchar(255)        NOT NULL COMMENT '用户名',
    `password`     varchar(255)        NOT NULL COMMENT '密码',
    `user_type`    tinyint(1)          NOT NULL DEFAULT '4' COMMENT '用户类型',
    `create_time`  datetime                     DEFAULT CURRENT_TIMESTAMP,
    `update_time`  datetime                     DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`      tinyint(1)                   DEFAULT '0',
    `nickname`     varchar(255) COMMENT '昵称',
    `email`        varchar(255) COMMENT '邮箱',
    `phone`        varchar(255) COMMENT '手机号',
    `city`         varchar(255) COMMENT '城市',
    `address`      varchar(255) COMMENT '详细地址',
    `province`     varchar(255) COMMENT '省',
    `id_card`      varchar(255) COMMENT '身份证号',
    `origin_place` varchar(255) COMMENT '来源地（籍贯）',
    `edu_bg`       varchar(255) COMMENT '学历',
    `company_addr` varchar(255) COMMENT '工作地点',
    `property1`    varchar(255),
    `property2`    varchar(255),
    `property3`    varchar(255),
    `property4`    varchar(255),
    `property5`    varchar(255),
    `property6`    varchar(255),
    `property7`    varchar(255),
    `property8`    varchar(255),
    `property9`    varchar(255),
    `property10`   varchar(255),
    `property11`   varchar(255),
    `property12`   varchar(255),
    `property13`   varchar(255),
    `property14`   varchar(255),
    `property15`   varchar(255),
    `property16`   varchar(255),
    `property17`   varchar(255),
    `property18`   varchar(255),
    `property19`   varchar(255),
    `property20`   varchar(255),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Records of t_user
-- ----------------------------
BEGIN;
INSERT INTO `t_user`(`id`, `name`,`password`, user_type, create_time, update_time, deleted)
VALUES (1, 'zjw', '123456', 1, '2021-02-13 20:01:44', '2021-02-15 10:29:46', 0);
INSERT INTO `t_user`(`id`, `name`,`password`, user_type, create_time, update_time, deleted)
VALUES (2, 'lyq', '123456', 2, '2021-02-14 10:37:25', '2021-02-14 10:37:25', 0);
COMMIT;

SET
    FOREIGN_KEY_CHECKS = 1;