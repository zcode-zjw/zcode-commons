SET NAMES utf8mb4;
SET
    FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_role
-- ----------------------------
DROP TABLE IF EXISTS `t_role`;
CREATE TABLE `t_role`
(
    `id`          bigint(20) unsigned NOT NULL AUTO_INCREMENT,
    `role`        varchar(255)        NOT NULL,
    `name`        varchar(255)        NOT NULL,
    `create_time` datetime   DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime   DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`     tinyint(1) DEFAULT '0',
    `property1`   varchar(255),
    `property2`   varchar(255),
    `property3`   varchar(255),
    `property4`   varchar(255),
    `property5`   varchar(255),
    `property6`   varchar(255),
    `property7`   varchar(255),
    `property8`   varchar(255),
    `property9`   varchar(255),
    `property10`  varchar(255),
    `property11`  varchar(255),
    `property12`  varchar(255),
    `property13`  varchar(255),
    `property14`  varchar(255),
    `property15`  varchar(255),
    `property16`  varchar(255),
    `property17`  varchar(255),
    `property18`  varchar(255),
    `property19`  varchar(255),
    `property20`  varchar(255),
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Records of t_role
-- ----------------------------
BEGIN;
INSERT INTO `t_role`(id, role, name, create_time, update_time, deleted)
VALUES (1, 'admin', '管理员', '2021-02-13 20:01:44', '2021-02-15 10:29:46', 0);
INSERT INTO `t_role`(id, role, name, create_time, update_time, deleted)
VALUES (2, 'develop', '开发用户', '2021-02-14 10:37:25', '2021-02-14 10:37:25', 0);
INSERT INTO `t_role`(id, role, name, create_time, update_time, deleted)
VALUES (2, 'common', '普通用户', '2021-02-14 10:37:25', '2021-02-14 10:37:25', 0);
INSERT INTO `t_role`(id, role, name, create_time, update_time, deleted)
VALUES (2, 'test', '测试用户', '2021-02-14 10:37:25', '2021-02-14 10:37:25', 0);
INSERT INTO `t_role`(id, role, name, create_time, update_time, deleted)
VALUES (2, 'visitor', '游客', '2021-02-14 10:37:25', '2021-02-14 10:37:25', 0);
COMMIT;

SET
    FOREIGN_KEY_CHECKS = 1;