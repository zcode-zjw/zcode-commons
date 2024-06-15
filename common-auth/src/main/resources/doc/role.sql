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
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Records of t_role
-- ----------------------------
BEGIN;
INSERT INTO `t_role`
VALUES (1, 'admin', '管理员', '2021-02-13 20:01:44', '2021-02-15 10:29:46', 0);
INSERT INTO `t_role`
VALUES (2, 'develop', '开发用户', '2021-02-14 10:37:25', '2021-02-14 10:37:25', 0);
INSERT INTO `t_role`
VALUES (2, 'common', '普通用户', '2021-02-14 10:37:25', '2021-02-14 10:37:25', 0);
INSERT INTO `t_role`
VALUES (2, 'test', '测试用户', '2021-02-14 10:37:25', '2021-02-14 10:37:25', 0);
INSERT INTO `t_role`
VALUES (2, 'visitor', '游客', '2021-02-14 10:37:25', '2021-02-14 10:37:25', 0);
COMMIT;

SET
    FOREIGN_KEY_CHECKS = 1;