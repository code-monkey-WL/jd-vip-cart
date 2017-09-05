CREATE DATABASE `o2o_vipcart` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
/*
Navicat MySQL Data Transfer

Source Server         : 192.168.202.97
Source Server Version : 50166
Source Host           : 192.168.202.97:3306
Source Database       : o2o_vipcart

Target Server Type    : MYSQL
Target Server Version : 50166
File Encoding         : 65001

Date: 2017-08-30 15:15:11
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for category_info
-- ----------------------------
DROP TABLE IF EXISTS `category_info`;
CREATE TABLE `category_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category_code` varchar(64) NOT NULL COMMENT '类目编码',
  `category_name` varchar(256) NOT NULL COMMENT '类目名称',
  `category_desc` varchar(256) DEFAULT NULL COMMENT '类目说明',
  `brand_img` varchar(256) NOT NULL COMMENT '目录图标',
  `category_status` tinyint(4) NOT NULL COMMENT '目录状态 0:有效 2:无效',
  `full_path` int(11) NOT NULL COMMENT '目录全路径用逗号隔开',
  `category_level` int(11) NOT NULL DEFAULT '0' COMMENT '目录级别',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_pin` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_pin` varchar(50) DEFAULT NULL COMMENT '更新人',
  `sys_version` int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
  `yn` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0:有效 1:无效',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_idx_cat_cd` (`category_code`)
) ENGINE=InnoDB AUTO_INCREMENT=20170401 DEFAULT CHARSET=utf8 COMMENT='类目表';

-- ----------------------------
-- Table structure for dict_entry
-- ----------------------------
DROP TABLE IF EXISTS `dict_entry`;
CREATE TABLE `dict_entry` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `dict_type_id` bigint(20) NOT NULL COMMENT '字典类型编号',
  `dict_code` varchar(256) NOT NULL COMMENT '字典编号',
  `dict_name` varchar(1024) DEFAULT NULL,
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_pin` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_pin` varchar(50) DEFAULT NULL COMMENT '更新人',
  `sys_version` int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
  `yn` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0:有效 1:无效',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`),
  KEY `idx_dti_dc` (`dict_type_id`,`dict_code`(255))
) ENGINE=InnoDB AUTO_INCREMENT=1264 DEFAULT CHARSET=utf8 COMMENT='字典项';

-- ----------------------------
-- Table structure for dict_type
-- ----------------------------
DROP TABLE IF EXISTS `dict_type`;
CREATE TABLE `dict_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `dict_type_id` bigint(20) NOT NULL COMMENT '类型编号',
  `app_code` varchar(64) DEFAULT NULL COMMENT '所属应用',
  `dict_type_code` varchar(128) NOT NULL COMMENT '类型编码',
  `dict_type_name` varchar(128) NOT NULL COMMENT '类型名称',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_pin` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_pin` varchar(50) DEFAULT NULL COMMENT '更新人',
  `sys_version` int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
  `yn` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0:有效 1:无效',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_idx_dti` (`dict_type_id`),
  KEY `idx_dtc` (`dict_type_code`)
) ENGINE=InnoDB AUTO_INCREMENT=237 DEFAULT CHARSET=utf8 COMMENT='字典类型';

-- ----------------------------
-- Table structure for good_flow
-- ----------------------------
DROP TABLE IF EXISTS `good_flow`;
CREATE TABLE `good_flow` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sku_id` varchar(64) NOT NULL COMMENT '商品编号',
  `out_sku_code` varchar(64) NOT NULL COMMENT '第三方商品编码',
  `sku_code` varchar(64) NOT NULL COMMENT '商品编码，多个来源对应一个编码',
  `sku_name` varchar(256) NOT NULL COMMENT '商品名称',
  `sku_price` bigint(20) NOT NULL COMMENT '价格',
  `origin_price` bigint(20) NOT NULL COMMENT '原价格',
  `from_source` int(11) NOT NULL COMMENT '商品来源 1:jd 2:tmall',
  `org_code` varchar(64) NOT NULL COMMENT '商家编号',
  `grab_time` datetime NOT NULL COMMENT '抓取时间',
  `ext` varchar(1024) DEFAULT NULL COMMENT '扩展字段',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_pin` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_pin` varchar(50) DEFAULT NULL COMMENT '更新人',
  `sys_version` int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
  `yn` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0:有效 1:无效',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`),
  KEY `idx_sku_id` (`sku_id`),
  KEY `idx_sku_code` (`sku_code`)
) ENGINE=InnoDB AUTO_INCREMENT=20170401 DEFAULT CHARSET=utf8 COMMENT='商品流水表';

-- ----------------------------
-- Table structure for good_info
-- ----------------------------
DROP TABLE IF EXISTS `good_info`;
CREATE TABLE `good_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `sku_id` varchar(64) NOT NULL COMMENT '商品编号',
  `out_sku_code` varchar(64) NOT NULL COMMENT '第三方商品编码',
  `sku_code` varchar(64) NOT NULL COMMENT '商品编码，多个来源对应一个编码',
  `sku_name` varchar(256) NOT NULL COMMENT '商品名称',
  `sku_desc` varchar(512) DEFAULT NULL COMMENT '商品说明',
  `sku_summary` varchar(512) DEFAULT NULL COMMENT '商品摘要',
  `sku_price` bigint(20) NOT NULL COMMENT '价格',
  `origin_price` bigint(20) NOT NULL COMMENT '原价格',
  `promotion_summary` varchar(128) NOT NULL COMMENT '促销摘要',
  `sku_link` varchar(256) NOT NULL COMMENT '商品原链接',
  `sku_img` varchar(256) NOT NULL COMMENT '商品图片',
  `from_source` int(11) NOT NULL COMMENT '商品来源 1:jd 2:tmall',
  `category_code` varchar(64) NOT NULL COMMENT '类目编码',
  `full_category_code` varchar(256) NOT NULL COMMENT '全路径类目编码逗号隔开',
  `brand_code` varchar(64) NOT NULL COMMENT '品牌编号',
  `org_code` varchar(64) NOT NULL COMMENT '商家编号',
  `stock_type` tinyint(4) NOT NULL COMMENT '库存标记类型 0:数量 1:百分比',
  `stock_num` int(11) NOT NULL DEFAULT '0' COMMENT '库存量',
  `comment_num` int(11) NOT NULL COMMENT '评论数量',
  `grab_time` datetime NOT NULL COMMENT '抓取时间',
  `sku_status` tinyint(4) NOT NULL COMMENT '商品状态 0:上架 2:下架',
  `ext` varchar(8192) DEFAULT NULL COMMENT '扩展字段',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_pin` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_pin` varchar(50) DEFAULT NULL COMMENT '更新人',
  `sys_version` int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
  `yn` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0:有效 1:无效',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_idx_sku_id` (`sku_id`),
  KEY `idx_sku_code` (`sku_code`)
) ENGINE=InnoDB AUTO_INCREMENT=20170403 DEFAULT CHARSET=utf8 COMMENT='商品表';

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `login_name` varchar(50) NOT NULL COMMENT '登录名',
  `login_password` varchar(100) NOT NULL COMMENT '登录凭证',
  `salt_code` int(11) NOT NULL COMMENT '登录加盐编码',
  `cn` varchar(32) DEFAULT NULL COMMENT '中文名',
  `phone` varchar(32) DEFAULT NULL COMMENT '联系电话',
  `email` varchar(64) DEFAULT NULL COMMENT '邮箱',
  `wei_xin_id` varchar(64) DEFAULT NULL COMMENT '微信id',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态，0-启用、1-禁用',
  `last_login_time` datetime DEFAULT NULL COMMENT '上次登录时间',
  `login_fail_count` int(11) DEFAULT '0' COMMENT '连续登录失败次数',
  `remark` varchar(300) DEFAULT NULL COMMENT '备注',
  `register_source` varchar(50) DEFAULT NULL COMMENT '注册来源',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_pin` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_pin` varchar(50) DEFAULT NULL COMMENT '更新人',
  `sys_version` int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
  `yn` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_idx_user_id` (`user_id`),
  KEY `idx_log_name` (`login_name`)
) ENGINE=InnoDB AUTO_INCREMENT=714 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `brand_info`;
CREATE TABLE `brand_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `brand_code` varchar(64) NOT NULL COMMENT '品牌编码',
  `brand_name` varchar(256) NOT NULL COMMENT '品牌名称',
  `brand_desc` varchar(256) DEFAULT NULL COMMENT '品牌说明',
  `brand_img` varchar(256) NOT NULL COMMENT '品牌图标',
  `brand_status` tinyint(4) NOT NULL COMMENT '品牌状态 0:有效 2:无效',
  `brand_level` int(11) NOT NULL DEFAULT '0' COMMENT '品牌级别',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_pin` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_pin` varchar(50) DEFAULT NULL COMMENT '更新人',
  `sys_version` int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
  `yn` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0:有效 1:无效',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_idx_brd_cd` (`brand_code`)
) ENGINE=InnoDB AUTO_INCREMENT=20170401 DEFAULT CHARSET=utf8 COMMENT='品牌表';

DROP TABLE IF EXISTS `promotion_info`;
CREATE TABLE `promotion_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `promotion_code` varchar(64) NOT NULL COMMENT '促销编码',
  `promotion_type` int(11) NOT NULL DEFAULT '0' COMMENT '促销类型',
  `sku_id` varchar(64) NOT NULL COMMENT '促销商品编号',
  `promotion_desc` varchar(256) DEFAULT NULL COMMENT '促销说明',
  `promotion_img` varchar(256) DEFAULT NULL COMMENT '促销图标',
  `start_time` datetime NOT NULL COMMENT '促销开始时间',
  `end_time` datetime NOT NULL COMMENT '促销结束时间',
  `promotion_status` tinyint(4) NOT NULL COMMENT '促销状态 0:有效 2:无效',
  `from_source` int(11) NOT NULL COMMENT '商品来源 1:jd 2:tmall',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_pin` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_pin` varchar(50) DEFAULT NULL COMMENT '更新人',
  `sys_version` int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
  `yn` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0:有效 1:无效',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_idx_pro_cd` (`promotion_code`)
) ENGINE=InnoDB AUTO_INCREMENT=20170401 DEFAULT CHARSET=utf8 COMMENT='促销表';

DROP TABLE IF EXISTS `ID_generator`;
CREATE TABLE `ID_generator` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `primary_key` varchar(64) NOT NULL COMMENT '业务主键标识',
  `step` int(11) NOT NULL COMMENT '更新步长',
  `sequence_no` bigint(20) NOT NULL COMMENT '主键编号',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_pin` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_pin` varchar(50) DEFAULT NULL COMMENT '更新人',
  `sys_version` int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
  `yn` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0:有效 1:无效',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_idx_key` (`primary_key`)
) ENGINE=InnoDB AUTO_INCREMENT=20170405 DEFAULT CHARSET=utf8 COMMENT='业务主键生成器';

DROP TABLE IF EXISTS `ID_generator_register`;
CREATE TABLE `ID_generator_register` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `primary_key` varchar(64) NOT NULL COMMENT '业务主键标识',
  `register_key` varchar(64) NOT NULL COMMENT '注册主键标识',
  `worker_no` int(11) NOT NULL COMMENT '注册机标识',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_pin` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_pin` varchar(50) DEFAULT NULL COMMENT '更新人',
  `sys_version` int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
  `yn` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0:有效 1:无效',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_idx_prikey_regkey` (`primary_key`,`register_key`),
  UNIQUE KEY `uni_idx_prikey_workno` (`primary_key`,`worker_no`)
) ENGINE=InnoDB AUTO_INCREMENT=20170401 DEFAULT CHARSET=utf8 COMMENT='snowflake主键生成器注册表';

INSERT INTO `ID_generator`(primary_key,step,sequence_no,create_pin,sys_version,yn,create_time,ts) VALUES ('sku_id', 500, 20170901,  'liuhuiqing', '1', '0',now(),now());
INSERT INTO `ID_generator`(primary_key,step,sequence_no,create_pin,sys_version,yn,create_time,ts) VALUES ('sku_code', 500, 20180901, 'liuhuiqing', '1', '0',now(),now());

DROP TABLE IF EXISTS `spider_config`;
CREATE TABLE `spider_config` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `spider_name` varchar(128) not NULL COMMENT '爬取名称',
  `spider_type` tinyint(4) NOT NULL COMMENT '爬取类型 1:URL 2:content',
  `url` varchar(512) DEFAULT NULL COMMENT '爬取url',
  `request_param` varchar(512) DEFAULT NULL COMMENT '请求入参',
  `content` varchar(8192) DEFAULT NULL COMMENT '解析内容',
  `rule_engine` tinyint(4) NOT NULL COMMENT ' 解析规则类型',
  `target_out` varchar(7168) DEFAULT NULL COMMENT '解析返回值输出:json字符串对象格式',
  `out_table_name` varchar(64) DEFAULT NULL COMMENT '输出数据库表名称',
  `base_url` varchar(256) DEFAULT NULL COMMENT '基础url',
  `scan_expressions` varchar(512) DEFAULT NULL COMMENT '全局扫描表达式',
  `item_rules` varchar(3072) DEFAULT NULL COMMENT '爬取项目表达式:json字符串对象格式',
  `scheduled_cron` varchar(32) NOT NULL COMMENT '爬取时间规则表达式',
  `deep_num` int(11) NOT NULL COMMENT '爬取深度（分页）',
  `state` tinyint(4) NOT NULL COMMENT '爬取状态 1:开启 2:运行中 3:暂停 4:废弃',
  `spider_num` int(11) NOT NULL COMMENT '爬取次数',
  `last_spider_time` datetime DEFAULT NULL COMMENT '上次爬取时间',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `create_pin` varchar(50) DEFAULT NULL COMMENT '创建人',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  `update_pin` varchar(50) DEFAULT NULL COMMENT '更新人',
  `sys_version` int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
  `yn` tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0:有效 1:无效',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`),
  KEY `idx_sp_name` (`spider_name`)
) ENGINE=InnoDB AUTO_INCREMENT=237 DEFAULT CHARSET=utf8 COMMENT='爬取配置表';

