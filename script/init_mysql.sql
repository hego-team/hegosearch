/*
Navicat MySQL Data Transfer

Source Server         : MySQL57
Source Server Version : 50731
Source Host           : localhost:3306
Source Database       : hego

Target Server Type    : MYSQL
Target Server Version : 50731
File Encoding         : 65001

*/
CREATE DATABASE IF NOT EXISTS HEGO;
SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for user
-- ----------------------------

use HEGO;
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
                        `id` int(11) NOT NULL AUTO_INCREMENT,
                        `name` varchar(255) NOT NULL,
                        `password` varchar(255) NOT NULL,
                        `role` int(11) NOT NULL DEFAULT '0',
                        `email` varchar(255) DEFAULT NULL,

                        `lastLoginTime` datetime DEFAULT NULL,
                        PRIMARY KEY (`id`)
) ;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'Default', 'Default', '1', '1111111111\@qq.com', '2000-1-1 00:00:00');
DROP TABLE IF EXISTS `tags`;
CREATE TABLE `tags` (
                        `id` int(11) NOT NULL AUTO_INCREMENT,
                        `owner` varchar(255) NOT NULL,
                        `name` varchar(255) NOT NULL,
                        `docid`  int(11) NOT NULL,
                        PRIMARY KEY (`id`)
) ;
INSERT INTO `tags` VALUES ('1','Default','百度一下，也不知道','34');
DROP TABLE IF EXISTS `his`;
CREATE TABLE `his` (
                       `id` int(11) NOT NULL AUTO_INCREMENT,
                       `owner` varchar(255) NOT NULL,
                       `content` varchar(255) NOT NULL,
                       `times` int(11) NOT NULL ,
                       PRIMARY KEY (`id`)
) ;
SET FOREIGN_KEY_CHECKS=0;

