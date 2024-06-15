SET NAMES utf8mb4;
SET
    FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `t_role_permission`;
CREATE TABLE `t_role_permission`
(
    `id`            bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT 'ID',
    `role_id`       bigint(20) unsigned COMMENT '角色ID',
    `permission_id` bigint(20) unsigned COMMENT '权限ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4;

-- ----------------------------
-- Records of t_role_permission
-- ----------------------------
BEGIN;
INSERT INTO `t_role_permission`
VALUES (1, 1, 1);
INSERT INTO `t_role_permission`
VALUES (2, 2, 2);
COMMIT;

SET
    FOREIGN_KEY_CHECKS = 1;