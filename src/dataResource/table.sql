CREATE TABLE `sl_sys_dict` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `dict_code` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `dict_name` varchar(64) COLLATE utf8mb4_bin NOT NULL,
  `status` smallint(2) NOT NULL DEFAULT 1,
   `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `create_id` int(11) DEFAULT NULL,
  `update_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_code` (`dict_code`) USING BTREE COMMENT '唯一code'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4;


CREATE TABLE `sl_sys_user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(32) NOT NULL COMMENT '用户名',
  `password` varchar(16) NOT NULL  COMMENT '密码',
  `real_name` varchar(32) NOT NULL COMMENT '真实姓名',
  `id_card` varchar(32) NOT NULL COMMENT '省份证',
  `sex` smallint(2) NOT NULL DEFAULT '1' COMMENT '性别：1:男,0女',
  `mobile_phone` varchar(16) DEFAULT NULL COMMENT '手机',
  `email` varchar(32) NOT NULL COMMENT '邮箱',
  `status` smallint(2) NOT NULL DEFAULT '1' COMMENT '是否有效：1有效，0无效',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `create_id` int(11) DEFAULT NULL,
  `update_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_userName` (`user_name`) USING BTREE COMMENT '用户名唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;



CREATE TABLE `sl_sys_resources` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `resource_code` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '资源编码',
  `display` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '资源名称',
  `description` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '资源描述',
  `parent_code` varchar(32) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '父资源id',
  `resource_type` smallint(2) COLLATE utf8mb4_bin NOT NULL COMMENT '资源类型^1菜单，2URL，3按钮',
  `url` varchar(500) COLLATE utf8mb4_bin NOT NULL COMMENT '访问地址/按钮编码',
  `status` smallint(2) NOT NULL DEFAULT 1 COMMENT '是否启用^1启用0不启用',
  `sort_by` int(11) DEFAULT NULL COMMENT '排序',
  `icon` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '图标',
   `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `create_id` int(11) DEFAULT NULL,
  `update_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_code` (`resource_code`) USING BTREE COMMENT 'code唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

create table sl_sys_role
(
  id        int(11) not null COMMENT '主键',
  role_code      varchar(32) not null COMMENT '角色编码',
  role_name      varchar(64) not null COMMENT '角色名',
  `status` smallint(2) NOT NULL DEFAULT 1 COMMENT '是否有效：1有效，0无效',
   `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `create_id` int(11) DEFAULT NULL,
  `update_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_code` (`role_code`) USING BTREE COMMENT 'code唯一'
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

create table sl_role_resource
(
  role_code      varchar(32) not null COMMENT '角色编码',
  resource_code      varchar(32) not null COMMENT '资源编码',
  `status` smallint(2) NOT NULL DEFAULT '1' COMMENT '是否有效：1有效，0无效',
  UNIQUE KEY `unique_code` (`role_code`,`resource_code`) USING BTREE COMMENT '联合主键'
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

create table sl_user_role
(
  role_code      varchar(32) not null COMMENT '角色编码',
  user_id      int(11) not null COMMENT '用户id',
  `status` smallint(2) NOT NULL DEFAULT 1 COMMENT '是否有效：1有效，0无效',
  UNIQUE KEY `unique_code` (`role_code`,`user_id`) USING BTREE COMMENT '联合主键'
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

CREATE TABLE `sl_sys_config` (
  `id` int(11) NOT NULL,
  `config_code` varchar(32) COLLATE utf8mb4_bin NOT NULL COMMENT '配置编码',
  `config_name` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '配置名称',
  `config_value` varchar(128) COLLATE utf8mb4_bin NOT NULL COMMENT '配置值',
  `description` varchar(512) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '配置描述',
  `status` smallint(1) DEFAULT '1' COMMENT '是否有效：1有效，0无效',
  `create_time` timestamp NOT NULL COMMENT '创建时间',
  `update_time` timestamp NOT NULL COMMENT '更新时间',
  `create_id` int(11) DEFAULT NULL,
  `update_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `unique_code` (`config_code`) USING BTREE COMMENT 'code唯一'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;