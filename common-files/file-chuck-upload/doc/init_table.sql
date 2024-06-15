-- 创建文件分块表
DROP TABLE IF EXISTS chuck_file;
CREATE TABLE `chuck_file`
(
    `ID`         int NOT NULL AUTO_INCREMENT,
    `FILENAME`   varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `IDENTIFIER` varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL,
    `TOTAL_SIZE` bigint                                  DEFAULT NULL,
    `TYPE`       varchar(24) COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `LOCATION`   varchar(128) COLLATE utf8mb4_general_ci DEFAULT NULL,
    PRIMARY KEY (`ID`)
) COMMENT '文件分块表' ENGINE = InnoDB
                       AUTO_INCREMENT = 9
                       DEFAULT CHARSET = utf8mb4
                       COLLATE = utf8mb4_general_ci;

-- 创建分块表
DROP TABLE IF EXISTS chuck;
CREATE TABLE `chuck`
(
    `ID`                 int NOT NULL AUTO_INCREMENT,
    `FILENAME`           varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `IDENTIFIER`         varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `TOTAL_SIZE`         bigint                                                        DEFAULT NULL,
    `TYPE`               varchar(24) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  DEFAULT NULL,
    `LOCATION`           varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL,
    `CHUNK_NUMBER`       int                                                           DEFAULT NULL,
    `CHUNK_SIZE`         int                                                           DEFAULT NULL,
    `CURRENT_CHUNK_SIZE` int                                                           DEFAULT NULL,
    `RELATIVE_PATH`      varchar(128) COLLATE utf8mb4_general_ci                       DEFAULT NULL,
    `TOTAL_CHUNKS`       int                                                           DEFAULT NULL,
    PRIMARY KEY (`ID`)
) COMMENT '分块表' ENGINE = InnoDB
                   AUTO_INCREMENT = 1734
                   DEFAULT CHARSET = utf8mb4
                   COLLATE = utf8mb4_general_ci;