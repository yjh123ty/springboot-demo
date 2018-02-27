/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : springboot_demo

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2018-02-12 18:05:55
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for order_item
-- ----------------------------
DROP TABLE IF EXISTS `order_item`;
CREATE TABLE `order_item` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) DEFAULT NULL,
  `product_id` int(11) DEFAULT NULL,
  `number` int(11) DEFAULT NULL COMMENT '数量',
  `amount` int(11) DEFAULT NULL COMMENT '金额',
  `note` varchar(255) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order_item
-- ----------------------------
INSERT INTO `order_item` VALUES ('1', '1', '1', '1', '1', '1');

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` int(11) NOT NULL,
  `name` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `quantity` int(11) DEFAULT NULL,
  `price` decimal(10,0) DEFAULT NULL,
  `bargain_flag` tinyint(1) DEFAULT NULL,
  `image` varchar(255) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `is_delete` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES ('1', '苹果', 'x', '10', '1', '0', 'http://localhost:8090/images/product/e66d8f9a74f3a08ef8c40ef136f54996.jpg', null, '\0');
INSERT INTO `product` VALUES ('2', '梨子', '', '0', '2', '0', 'http://localhost:8090/images/product/79be0a0fcb4143afa40b9770c1b72eac.png', null, '\0');
INSERT INTO `product` VALUES ('3', '香蕉', 'e', '40', '3', '0', null, null, '\0');
INSERT INTO `product` VALUES ('4', '火龙果', 'e', '30', '2', '0', null, null, '\0');
INSERT INTO `product` VALUES ('5', '西瓜', 'r', '20', '1', '0', null, null, '\0');
INSERT INTO `product` VALUES ('6', '草莓', 'we', '60', '2', '0', null, null, '\0');
INSERT INTO `product` VALUES ('7', '柚子', 'sdf', '90', '3', '1', null, null, '\0');
INSERT INTO `product` VALUES ('8', '葡萄', 'ds', '100', '5', '0', null, null, '\0');
INSERT INTO `product` VALUES ('9', 'xxx', null, '1', '1', null, null, '2018-01-25 15:30:31', null);

-- ----------------------------
-- Table structure for product_img
-- ----------------------------
DROP TABLE IF EXISTS `product_img`;
CREATE TABLE `product_img` (
  `id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `url` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of product_img
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(32) DEFAULT NULL,
  `url` varchar(200) DEFAULT NULL,
  `url_icon` varchar(128) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `order` int(2) DEFAULT NULL COMMENT '排序',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('0', '首页', '/main/index', 'lnr lnr-home', null, null);
INSERT INTO `sys_menu` VALUES ('1', '系统设置', null, 'lnr lnr-cog', null, null);
INSERT INTO `sys_menu` VALUES ('2', '角色管理', '/sysRole/index', null, '1', '1');
INSERT INTO `sys_menu` VALUES ('3', '权限管理', '/sysPerm/index', null, '1', '2');
INSERT INTO `sys_menu` VALUES ('4', '菜单管理', '/sysMenu/index', null, '1', '3');
INSERT INTO `sys_menu` VALUES ('5', '会员管理', '/user/index', 'lnr lnr-user', null, null);
INSERT INTO `sys_menu` VALUES ('6', '地图管理', null, 'lnr lnr-map', null, null);
INSERT INTO `sys_menu` VALUES ('7', '基础地图', '/map/index', null, '6', '1');
INSERT INTO `sys_menu` VALUES ('8', '距离地图', '/map/distance', null, '6', '2');
INSERT INTO `sys_menu` VALUES ('9', '商品管理', '/product/index', 'lnr lnr-cart', null, null);

-- ----------------------------
-- Table structure for sys_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_permission`;
CREATE TABLE `sys_permission` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `available` bit(1) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `parent_id` int(11) DEFAULT NULL,
  `parent_ids` varchar(255) DEFAULT NULL,
  `permission` varchar(255) DEFAULT NULL,
  `resource_type` varchar(255) DEFAULT NULL,
  `url` varchar(255) DEFAULT NULL,
  `last_update_user_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_permission
-- ----------------------------
INSERT INTO `sys_permission` VALUES ('1', '', '用户编辑', null, '', 'userInfo:edit', 'button', '/user/saveOrUpdate', null);
INSERT INTO `sys_permission` VALUES ('2', '', '用户删除', null, '', 'userInfo:del', 'button', '/user/delete', null);
INSERT INTO `sys_permission` VALUES ('3', '', 'xxxx', null, null, 'xx', null, '/xx', null);
INSERT INTO `sys_permission` VALUES ('4', '', '菜单编辑', null, null, 'menu:edit', null, '/sysMenu/saveOrUpdate', '1');
INSERT INTO `sys_permission` VALUES ('5', '', '菜单删除', null, null, 'menu:del', null, '/sysMenu/delete', '1');
INSERT INTO `sys_permission` VALUES ('6', '\0', '', null, null, '', null, '', '1');
INSERT INTO `sys_permission` VALUES ('7', '\0', '', null, null, '', null, '', '1');
INSERT INTO `sys_permission` VALUES ('8', '\0', '', null, null, '', null, '', '1');

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `role` varchar(255) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `available` bit(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', 'admin', '超级系统管理员', '');
INSERT INTO `sys_role` VALUES ('2', 'vip', '用户', '');
INSERT INTO `sys_role` VALUES ('3', 'N/A', '游客1', '');
INSERT INTO `sys_role` VALUES ('5', 'sys-manager', '系统管理员', '');

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `role_id` int(11) NOT NULL,
  `menu_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('4', '31');
INSERT INTO `sys_role_menu` VALUES ('17', '1');
INSERT INTO `sys_role_menu` VALUES ('3', '0');
INSERT INTO `sys_role_menu` VALUES ('2', '0');
INSERT INTO `sys_role_menu` VALUES ('2', '6');
INSERT INTO `sys_role_menu` VALUES ('2', '7');
INSERT INTO `sys_role_menu` VALUES ('2', '8');
INSERT INTO `sys_role_menu` VALUES ('5', '1');
INSERT INTO `sys_role_menu` VALUES ('5', '4');
INSERT INTO `sys_role_menu` VALUES ('5', '5');
INSERT INTO `sys_role_menu` VALUES ('1', '0');
INSERT INTO `sys_role_menu` VALUES ('1', '1');
INSERT INTO `sys_role_menu` VALUES ('1', '2');
INSERT INTO `sys_role_menu` VALUES ('1', '3');
INSERT INTO `sys_role_menu` VALUES ('1', '4');
INSERT INTO `sys_role_menu` VALUES ('1', '5');
INSERT INTO `sys_role_menu` VALUES ('1', '6');
INSERT INTO `sys_role_menu` VALUES ('1', '7');
INSERT INTO `sys_role_menu` VALUES ('1', '8');
INSERT INTO `sys_role_menu` VALUES ('1', '9');

-- ----------------------------
-- Table structure for sys_role_permission
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_permission`;
CREATE TABLE `sys_role_permission` (
  `permission_id` int(11) DEFAULT NULL,
  `role_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_role_permission
-- ----------------------------
INSERT INTO `sys_role_permission` VALUES ('4', '5');
INSERT INTO `sys_role_permission` VALUES ('1', '1');
INSERT INTO `sys_role_permission` VALUES ('2', '1');
INSERT INTO `sys_role_permission` VALUES ('3', '1');
INSERT INTO `sys_role_permission` VALUES ('5', '1');
INSERT INTO `sys_role_permission` VALUES ('4', '1');

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `role_id` int(11) DEFAULT NULL,
  `uid` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '1');
INSERT INTO `sys_user_role` VALUES ('2', '10');
INSERT INTO `sys_user_role` VALUES ('3', '20');
INSERT INTO `sys_user_role` VALUES ('3', '5');
INSERT INTO `sys_user_role` VALUES ('3', '3');
INSERT INTO `sys_user_role` VALUES ('5', '2');
INSERT INTO `sys_user_role` VALUES ('3', '21');

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `uid` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(255) DEFAULT NULL COMMENT '用户名',
  `name` varchar(255) DEFAULT NULL COMMENT '昵称',
  `password` varchar(255) DEFAULT NULL,
  `mobile` varchar(32) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `id_no` varchar(32) DEFAULT NULL,
  `salt` varchar(255) DEFAULT NULL,
  `state` tinyint(1) DEFAULT '1' COMMENT '1已注销，2未认证，3已认证',
  `head_icon` varchar(255) DEFAULT NULL,
  `is_delete` bit(1) DEFAULT b'0',
  `create_time` datetime DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES ('1', 'admin', '管理员-余子酱', '1', '181', 'yujh4@allinpay.com', '510', null, '3', 'http://localhost:8090/images/headIcon/1/ff3a2869f6684d01aa478cac2aec97d5.jpg', '\0', '2017-11-20 13:42:24');
INSERT INTO `user_info` VALUES ('2', 'yu', '管理员-yu', '1', '2', null, '2', null, '3', 'http://localhost:8090/images/headIcon/2/82c1b3ed9f97491faaaa9378643454f9.png', '\0', '2017-11-08 16:31:42');
INSERT INTO `user_info` VALUES ('3', 'tom', '王玄屁', '123', '181', null, '1', null, '3', 'http://localhost:8090/images/headIcon/3/35d01eba16364d9ab2d52dbfb2bd82b5.jpg', '\0', '2017-11-08 16:31:42');
INSERT INTO `user_info` VALUES ('4', 'aaa', '朱可可', '123', null, null, '330722197103186268', null, '2', null, '\0', '2017-11-08 16:31:42');
INSERT INTO `user_info` VALUES ('5', 'sss', '王玄屁', '123', null, null, '330623196112147279', null, '2', null, '\0', '2017-11-08 16:31:42');
INSERT INTO `user_info` VALUES ('6', 'asd', '楼侠平', '1', null, null, '330623196305170010', null, '2', null, '\0', '2017-11-08 16:31:42');
INSERT INTO `user_info` VALUES ('7', 'zj', '周杰', '123', null, null, '330722197103186269', null, '1', null, '\0', '2017-11-08 16:31:42');
INSERT INTO `user_info` VALUES ('8', 'xzl', '徐自力', '1', null, null, '330722197103186261', null, '1', null, '\0', '2017-11-08 16:31:42');
INSERT INTO `user_info` VALUES ('9', 'tmx', '汤敏雄', '1', null, null, '330722197103186262', null, '2', null, '\0', '2017-11-08 16:31:42');
INSERT INTO `user_info` VALUES ('10', 'yy', '余洋', '1', null, null, '330722197103186233', null, '2', null, '\0', '2017-11-08 16:31:42');
INSERT INTO `user_info` VALUES ('11', 'gq', '郭强', '1', null, null, '330722197103186264', null, '1', null, '\0', '2017-11-08 16:31:42');
INSERT INTO `user_info` VALUES ('12', 'wch', '王才华', '1', null, null, '330722197103186265', null, '1', null, '\0', '2017-11-08 16:31:42');
INSERT INTO `user_info` VALUES ('17', '1', 'test', '1', '1', null, '1', null, '3', 'http://localhost:8090/images/headIcon/17/0eefcbc4cebf43f1b11106e46b97eb04.png', '\0', '2017-11-08 16:31:42');
INSERT INTO `user_info` VALUES ('19', '12', '12', '1', '1', null, '1', null, '3', 'http://localhost:8090/images/headIcon/19/480f9d41da8b49cb8d442ad3ff4a05a7.png', '\0', '2017-11-20 14:41:51');
INSERT INTO `user_info` VALUES ('20', '123', 'asdf', null, null, null, null, null, '2', null, '\0', '2017-11-30 15:36:05');
INSERT INTO `user_info` VALUES ('21', '1814802566723', '3134', null, null, null, null, null, '2', null, '', '2018-01-23 17:35:13');
