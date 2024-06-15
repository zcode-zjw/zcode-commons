SET NAMES utf8mb4;
SET
FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_user
-- ----------------------------
DROP TABLE IF EXISTS `t_user`;
CREATE TABLE `t_user`
(
    `id`          bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `name`        varchar(255) NOT NULL COMMENT '用户名',
    `password`    varchar(255) NOT NULL COMMENT '密码',
    `user_type`   tinyint(1) NOT NULL DEFAULT '4' COMMENT '用户类型 1-管理员 2-教师 3-学生 4-游客',
    `create_time` datetime DEFAULT CURRENT_TIMESTAMP,
    `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `deleted`     tinyint(1) DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of t_user
-- ----------------------------
BEGIN;
INSERT INTO `t_user`
VALUES (1, 'zjw', '123456', 1, '2021-02-13 20:01:44', '2021-02-15 10:29:46', 0);
INSERT INTO `t_user`
VALUES (2, 'lyq', '123456', 2, '2021-02-14 10:37:25', '2021-02-14 10:37:25', 0);
COMMIT;

SET
FOREIGN_KEY_CHECKS = 1;