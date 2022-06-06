/*
Navicat MySQL Data Transfer

Source Server         : MySQL57
Source Server Version : 50731
Source Host           : localhost:3306
Source Database       : hego

Target Server Type    : MYSQL
Target Server Version : 50731
File Encoding         : 65001

Date: 2022-05-21 21:52:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(30) DEFAULT NULL COMMENT '商品名称',
  `price` int(11) DEFAULT '0' COMMENT '价格',
  `version` int(11) DEFAULT '0' COMMENT '乐观锁版本号',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES ('1', '笔记本', '120', '5');

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `uid` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `name` varchar(30) DEFAULT NULL COMMENT '姓名',
  `age` int(11) DEFAULT NULL COMMENT '年龄',
  `email` varchar(50) DEFAULT NULL COMMENT '邮箱',
  `data_time` datetime DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `is_deleted` tinyint(1) DEFAULT NULL,
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'Jone', '50', 'test1@baomidou.com', null, null, '1');
INSERT INTO `user` VALUES ('2', 'Jack', '20', 'test2@baomidou.com', null, null, '0');
INSERT INTO `user` VALUES ('3', 'Tom', '28', 'test3@baomidou.com', null, null, '0');
INSERT INTO `user` VALUES ('4', 'Sandy', '21', 'test4@baomidou.com', null, null, '0');
INSERT INTO `user` VALUES ('5', 'Billie', '24', 'test5@baomidou.com', null, null, '0');
INSERT INTO `user` VALUES ('6', '花花0', '18', 'user@atguigu.com', '2021-05-16 17:17:06', null, '0');
INSERT INTO `user` VALUES ('7', '花花1', '18', 'user@atguigu.com', '2021-05-16 17:17:06', null, '0');
INSERT INTO `user` VALUES ('8', '花花2', '18', 'user@atguigu.com', '2021-05-16 17:17:06', null, '0');
INSERT INTO `user` VALUES ('9', '花花3', '23', null, null, null, '1');
INSERT INTO `user` VALUES ('10', '花花4', '24', null, null, null, '1');
INSERT INTO `user` VALUES ('11', '建国xxx', '75', 'jiangguxxxo@qq.com', null, null, '0');
INSERT INTO `user` VALUES ('12', '建国xxx', '75', 'jiangguxxxo@qq.com', '2021-05-05 12:04:28', '2021-05-05 12:04:28', '0');
INSERT INTO `user` VALUES ('13', '建国100', '75', 'jiangguxxxo@qq.com', '2021-05-05 13:39:12', '2021-05-05 13:39:12', '0');
INSERT INTO `user` VALUES ('14', '建国200', '75', 'jiangguxxxo@qq.com', '2021-05-07 20:56:45', '2021-05-07 20:56:45', '0');
INSERT INTO `user` VALUES ('15', '建国300', '50', 'jiangguxxxo@qq.com', null, '2021-05-07 20:58:38', '0');
INSERT INTO `user` VALUES ('16', '建国400', '75', 'jiangguxxxo@qq.com', null, '2021-05-07 21:00:50', '0');
INSERT INTO `user` VALUES ('17', '建国400', '75', 'jiangguxxxo@qq.com', null, '2021-05-07 21:01:58', '0');
INSERT INTO `user` VALUES ('18', '建国500', '50', 'jiangguxxxo@qq.com', '2021-05-07 21:05:37', '2021-05-07 21:04:15', '0');
