/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 80018
 Source Host           : localhost:3306
 Source Schema         : sl_core

 Target Server Type    : MySQL
 Target Server Version : 80018
 File Encoding         : 65001

 Date: 13/11/2019 17:33:09
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sl_role_resource
-- ----------------------------
DROP TABLE IF EXISTS `sl_role_resource`;
CREATE TABLE `sl_role_resource`  (
  `role_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色编码',
  `resource_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资源编码',
  `status` smallint(2) NOT NULL DEFAULT 1 COMMENT '是否有效：1有效，0无效',
  UNIQUE INDEX `unique_code`(`role_code`, `resource_code`) USING BTREE COMMENT '联合主键'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sl_role_resource
-- ----------------------------
INSERT INTO `sl_role_resource` VALUES ('base_user', 'dictionary_get', 1);
INSERT INTO `sl_role_resource` VALUES ('sys_admin', 'user_add', 1);
INSERT INTO `sl_role_resource` VALUES ('sys_admin', 'user_update', 1);

-- ----------------------------
-- Table structure for sl_sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sl_sys_dict`;
create table sl_sys_dict
(
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dict_code`         varchar(32) NOT NULL COMMENT '字典编码',
  `dict_name`         varchar(64) NOT NULL COMMENT '字典名称',
  `status` smallint(2) NOT NULL DEFAULT 1 COMMENT '是否有效：1有效，0无效',
  `sort_by` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL COMMENT '更新时间',
  `create_id` int(11) NULL DEFAULT NULL,
  `update_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_code`(`dict_code`) USING BTREE COMMENT 'code唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 1;

create table NCMS_SYS_DICT_ITEM
(
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `item_code`    varchar(32) NOT NULL COMMENT '字典值编码',
  `parent_item_code`    varchar(32) NOT NULL COMMENT '字典值父级编码',
  `dict_code`    varchar(50) NOT NULL COMMENT '字典编码',
  `item_name`   varchar(64) NOT NULL COMMENT '字典值名称',
  `description`   varchar(128),
  `status` smallint(2) NOT NULL DEFAULT 1 COMMENT '是否有效：1有效，0无效',
  `sort_by` int(11) NULL DEFAULT NULL COMMENT '排序',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL COMMENT '更新时间',
  `create_id` int(11) NULL DEFAULT NULL,
  `update_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_code`(`item_code`) USING BTREE COMMENT 'code唯一'
)ENGINE = InnoDB AUTO_INCREMENT = 1;

-- ----------------------------
-- Table structure for sl_sys_resources
-- ----------------------------
DROP TABLE IF EXISTS `sl_sys_resources`;
CREATE TABLE `sl_sys_resources`  (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `resource_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '资源编码',
  `display` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '资源名称',
  `description` varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '资源描述',
  `parent_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '父资源id',
  `resource_type` smallint(2) NOT NULL COMMENT '资源类型^1菜单，2URL，3按钮',
  `url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '访问地址/按钮编码',
  `status` smallint(2) NOT NULL DEFAULT 1 COMMENT '是否启用^1启用0不启用',
  `sort_by` int(11) NULL DEFAULT NULL COMMENT '排序',
  `icon` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '图标',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL COMMENT '更新时间',
  `create_id` int(11) NULL DEFAULT NULL,
  `update_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_code`(`resource_code`) USING BTREE COMMENT 'code唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sl_sys_resources
-- ----------------------------
INSERT INTO `sl_sys_resources` VALUES (1, 'user_add', '增加用户', '增加用户', 'user_management', 3, '/user/addUser', 1, 10001, NULL, '2019-11-13 14:08:58', '2019-11-13 14:09:06', 1, 1);
INSERT INTO `sl_sys_resources` VALUES (2, 'user_update', '更改用户', '更改用户', 'user_management', 3, '/user/updateUser', 1, 10002, NULL, '2019-11-13 14:10:51', '2019-11-13 14:10:56', 1, 1);
INSERT INTO `sl_sys_resources` VALUES (3, 'dictionary_get', '字典查询', '字典查询', 'sys_management', 2, '/dic/getDictionary', 1, 20001, NULL, '2019-11-13 14:18:56', '2019-11-13 14:18:59', 1, 1);

-- ----------------------------
-- Table structure for sl_sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sl_sys_role`;
CREATE TABLE `sl_sys_role`  (
  `id` int(11) NOT NULL COMMENT '主键',
  `role_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色编码',
  `role_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名',
  `status` smallint(2) NOT NULL DEFAULT 1 COMMENT '是否有效：1有效，0无效',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL COMMENT '更新时间',
  `create_id` int(11) NULL DEFAULT NULL,
  `update_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_code`(`role_code`) USING BTREE COMMENT 'code唯一'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sl_sys_role
-- ----------------------------
INSERT INTO `sl_sys_role` VALUES (1, 'sys_admin', '系统管理员', 1, '2019-11-13 14:03:23', '2019-11-13 14:03:26', 1, 1);
INSERT INTO `sl_sys_role` VALUES (2, 'base_user', '基础用户', 1, '2019-11-13 14:03:52', '2019-11-13 14:03:55', 1, 1);

-- ----------------------------
-- Table structure for sl_sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sl_sys_user`;
CREATE TABLE `sl_sys_user`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
  `user_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
  `real_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '真实姓名',
  `id_card` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '省份证',
  `sex` smallint(2) NOT NULL DEFAULT 1 COMMENT '性别：1:男,0女',
  `mobile_phone` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '手机',
  `email` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '邮箱',
  `status` smallint(2) NOT NULL DEFAULT 1 COMMENT '是否有效：1有效，0无效',
  `create_time` timestamp(0) NOT NULL COMMENT '创建时间',
  `update_time` timestamp(0) NOT NULL COMMENT '更新时间',
  `create_id` int(11) NULL DEFAULT NULL,
  `update_id` int(11) NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_userName`(`user_name`) USING BTREE COMMENT '用户名唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sl_sys_user
-- ----------------------------
INSERT INTO `sl_sys_user` VALUES (1, 'luhai', '123456', '鲁海', '34088119921029063x', 1, '18155143649', '1219695198@qq.com', 1, '2019-11-13 14:01:53', '2019-11-13 14:01:57', 1, 1);

-- ----------------------------
-- Table structure for sl_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sl_user_role`;
CREATE TABLE `sl_user_role`  (
  `role_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色编码',
  `user_id` int(11) NOT NULL COMMENT '用户id',
  `status` smallint(2) NOT NULL DEFAULT 1 COMMENT '是否有效：1有效，0无效',
  UNIQUE INDEX `unique_code`(`role_code`, `user_id`) USING BTREE COMMENT '联合主键'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sl_user_role
-- ----------------------------
INSERT INTO `sl_user_role` VALUES ('base_user', 1, 1);
INSERT INTO `sl_user_role` VALUES ('sys_admin', 1, 1);

SET FOREIGN_KEY_CHECKS = 1;
