CREATE TABLE `sl_sys_dict` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `dict_code` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `dict_name` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `status` smallint(2) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_code` (`dict_code`) USING BTREE COMMENT '唯一code'
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;


CREATE TABLE `sl_sys_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(32) COLLATE utf8mb4_bin NOT NULL,
  `email` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL,
  `status` smallint(2) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;