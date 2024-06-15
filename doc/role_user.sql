SET NAMES utf8mb4;
SET
    FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_user_role
-- ----------------------------
DROP TABLE IF EXISTS `t_user_role`;
CREATE TABLE `t_user_role`
(
    `id`            bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `user_id`       bigint(20) unsigned COMMENT '用户ID',
    `role_id` bigint(20) unsigned COMMENT '角色ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Records of t_user_role
-- ----------------------------
BEGIN;
INSERT INTO `t_user_role`
VALUES (1, 1, 1);
INSERT INTO `t_user_role`
VALUES (2, 2, 2);
COMMIT;

SET
    FOREIGN_KEY_CHECKS = 1;