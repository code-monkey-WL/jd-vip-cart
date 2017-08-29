create database o2o_vipcart;
create table good_info
(
   id          bigint not null auto_increment,
   sku_id             varchar(64) not null comment '商品编号',
   out_sku_code             varchar(64) not null comment '第三方商品编码',
   sku_code             varchar(64) not null comment '商品编码，多个来源对应一个编码',
   sku_name        			varchar(256) not null comment '商品名称',
   sku_desc                  varchar(512) DEFAULT null comment '商品说明',
   sku_summary  varchar(512) DEFAULT null comment '商品摘要',
   sku_price           bigint not null COMMENT '价格',
   origin_price	bigint not null COMMENT '原价格',
   promotion_summary varchar(128) not null comment '促销摘要',
   sku_link varchar(256) not null comment '商品原链接',
   sku_img varchar(256) not null comment '商品图片',
   from_source int not null comment '商品来源 1:jd 2:tmall',
   category_code             varchar(64) not null comment '类目编码',
   full_category_code             varchar(256) not null comment '全路径类目编码逗号隔开',
   brand_code	   varchar(64) NOT NULL COMMENT '品牌编号',
   org_code varchar(64) not null comment '商家编号',
   stock_type tinyint(4) NOT NULL COMMENT '库存标记类型 0:数量 1:百分比',
   stock_num int not null default 0 comment '库存量',
   comment_num int not null comment '评论数量',
   grab_time datetime not NULL COMMENT '抓取时间',
   sku_status	tinyint(4) NOT NULL COMMENT '商品状态 0:上架 2:下架',
   ext                  varchar(1024) DEFAULT null comment '扩展字段',
   sort                 int not null default 0 comment '排序',
   create_time datetime DEFAULT NULL COMMENT '创建时间',
   create_pin varchar(50) DEFAULT NULL COMMENT '创建人',
   update_time datetime DEFAULT NULL COMMENT '更新时间',
   update_pin varchar(50) DEFAULT NULL COMMENT '更新人',
   sys_version int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
   yn tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0:有效 1:无效',
   ts timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
   primary key (id),
   UNIQUE KEY `uni_idx_sku_id` (`sku_id`),
   KEY `idx_sku_code` (`sku_code`)
) ENGINE=InnoDB AUTO_INCREMENT=20170401 DEFAULT CHARSET=utf8 COMMENT='商品表';


create table category_info
(
   id          bigint not null auto_increment,
   category_code             varchar(64) not null comment '类目编码',
   category_name        			varchar(256) not null comment '类目名称',
   category_desc                  varchar(256) DEFAULT null comment '类目说明',
   category_status	tinyint(4) NOT NULL COMMENT '目录状态 0:有效 2:无效',
   full_path        			int not null comment '目录全路径用逗号隔开',
   category_level          int not null default 0 comment '排序',
   sort                 int not null default 0 comment '排序',
   create_time datetime DEFAULT NULL COMMENT '创建时间',
   create_pin varchar(50) DEFAULT NULL COMMENT '创建人',
   update_time datetime DEFAULT NULL COMMENT '更新时间',
   update_pin varchar(50) DEFAULT NULL COMMENT '更新人',
   sys_version int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
   yn tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0:有效 1:无效',
   ts timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
   primary key (id),
   UNIQUE KEY `uni_idx_cat_cd` (`category_code`)
) ENGINE=InnoDB AUTO_INCREMENT=20170401 DEFAULT CHARSET=utf8 COMMENT='类目表';

create table good_flow
(
   id          bigint not null auto_increment,
   sku_id             varchar(64) not null comment '商品编号',
   out_sku_code             varchar(64) not null comment '第三方商品编码',
   sku_code             varchar(64) not null comment '商品编码，多个来源对应一个编码',
   sku_name        			varchar(256) not null comment '商品名称',
   sku_price           bigint not null COMMENT '价格',
   origin_price	bigint not null COMMENT '原价格',
   from_source int not null comment '商品来源 1:jd 2:tmall',
   org_code varchar(64) not null comment '商家编号',
   grab_time datetime not NULL COMMENT '抓取时间',
   ext                  varchar(1024) DEFAULT null comment '扩展字段',
   sort                 int not null default 0 comment '排序',
   create_time datetime DEFAULT NULL COMMENT '创建时间',
   create_pin varchar(50) DEFAULT NULL COMMENT '创建人',
   update_time datetime DEFAULT NULL COMMENT '更新时间',
   update_pin varchar(50) DEFAULT NULL COMMENT '更新人',
   sys_version int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
   yn tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0:有效 1:无效',
   ts timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
   primary key (id),
   KEY `idx_sku_id` (`sku_id`),
   KEY `idx_sku_code` (`sku_code`)
) ENGINE=InnoDB AUTO_INCREMENT=20170401 DEFAULT CHARSET=utf8 COMMENT='商品流水表';

CREATE TABLE `dict_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
   dict_type_id bigint(20) NOT NULL COMMENT '类型编号',
  `app_code` varchar(64) DEFAULT NULL COMMENT '所属应用',
  `dict_type_code` varchar(128) NOT NULL COMMENT '类型编号',
  `dict_type_name` varchar(128) NOT NULL COMMENT '类型名称',
  `remark` varchar(512) DEFAULT NULL COMMENT '备注',
   sort                 int not null default 0 comment '排序',
   create_time datetime DEFAULT NULL COMMENT '创建时间',
   create_pin varchar(50) DEFAULT NULL COMMENT '创建人',
   update_time datetime DEFAULT NULL COMMENT '更新时间',
   update_pin varchar(50) DEFAULT NULL COMMENT '更新人',
   sys_version int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
   yn tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0:有效 1:无效',
   ts timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_idx_dti` (`dict_type_id`),
  KEY `idx_dtc` (`dict_type_code`)
) ENGINE=InnoDB AUTO_INCREMENT=237 DEFAULT CHARSET=utf8 COMMENT='字典类型';

CREATE TABLE `dict_entry` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `dict_type_id` bigint(20) NOT NULL COMMENT '字典类型编号',
  `dict_code` varchar(256) NOT NULL COMMENT '字典编号',
  `dict_name` varchar(1024) DEFAULT NULL,
  `remark` varchar(1024) DEFAULT NULL COMMENT '备注',
   sort                 int not null default 0 comment '排序',
   create_time datetime DEFAULT NULL COMMENT '创建时间',
   create_pin varchar(50) DEFAULT NULL COMMENT '创建人',
   update_time datetime DEFAULT NULL COMMENT '更新时间',
   update_pin varchar(50) DEFAULT NULL COMMENT '更新人',
   sys_version int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
   yn tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0:有效 1:无效',
   ts timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
  PRIMARY KEY (`id`),
  KEY `idx_dti_dc` (`dict_type_id`,`dict_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1264 DEFAULT CHARSET=utf8 COMMENT='字典项';

CREATE TABLE `user_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '自增ID',
  `user_id` bigint(20) NOT NULL COMMENT '用户ID',
  `login_name` varchar(50) NOT NULL COMMENT '登录名',
  `login_password` varchar(100) NOT NULL COMMENT '登录凭证',
   salt_code int not null comment '登录加盐编码',
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



/**
create table promotion_info
(
   id          bigint not null auto_increment,
   promotion_code varchar(32) not null comment '促销编号',
   ext                  varchar(1024) DEFAULT null comment '扩展字段',
   sort                 int not null default 0 comment '排序',
   create_time datetime DEFAULT NULL COMMENT '创建时间',
   create_pin varchar(50) DEFAULT NULL COMMENT '创建人',
   update_time datetime DEFAULT NULL COMMENT '更新时间',
   update_pin varchar(50) DEFAULT NULL COMMENT '更新人',
   sys_version int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
   yn tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0:有效 1:无效',
   ts timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
   primary key (id),
   KEY `idx_sku_code` (`sku_code`)
) ENGINE=InnoDB AUTO_INCREMENT=20170401 DEFAULT CHARSET=utf8 COMMENT='促销信息表';
**/

