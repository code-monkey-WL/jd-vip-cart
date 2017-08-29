create database o2o_vipcart;
create table good_info
(
   id          bigint not null auto_increment,
   sku_id             varchar(64) not null comment '��Ʒ���',
   out_sku_code             varchar(64) not null comment '��������Ʒ����',
   sku_code             varchar(64) not null comment '��Ʒ���룬�����Դ��Ӧһ������',
   sku_name        			varchar(256) not null comment '��Ʒ����',
   sku_desc                  varchar(512) DEFAULT null comment '��Ʒ˵��',
   sku_summary  varchar(512) DEFAULT null comment '��ƷժҪ',
   sku_price           bigint not null COMMENT '�۸�',
   origin_price	bigint not null COMMENT 'ԭ�۸�',
   promotion_summary varchar(128) not null comment '����ժҪ',
   sku_link varchar(256) not null comment '��Ʒԭ����',
   sku_img varchar(256) not null comment '��ƷͼƬ',
   from_source int not null comment '��Ʒ��Դ 1:jd 2:tmall',
   category_code             varchar(64) not null comment '��Ŀ����',
   full_category_code             varchar(256) not null comment 'ȫ·����Ŀ���붺�Ÿ���',
   brand_code	   varchar(64) NOT NULL COMMENT 'Ʒ�Ʊ��',
   org_code varchar(64) not null comment '�̼ұ��',
   stock_type tinyint(4) NOT NULL COMMENT '��������� 0:���� 1:�ٷֱ�',
   stock_num int not null default 0 comment '�����',
   comment_num int not null comment '��������',
   grab_time datetime not NULL COMMENT 'ץȡʱ��',
   sku_status	tinyint(4) NOT NULL COMMENT '��Ʒ״̬ 0:�ϼ� 2:�¼�',
   ext                  varchar(1024) DEFAULT null comment '��չ�ֶ�',
   sort                 int not null default 0 comment '����',
   create_time datetime DEFAULT NULL COMMENT '����ʱ��',
   create_pin varchar(50) DEFAULT NULL COMMENT '������',
   update_time datetime DEFAULT NULL COMMENT '����ʱ��',
   update_pin varchar(50) DEFAULT NULL COMMENT '������',
   sys_version int(11) NOT NULL DEFAULT '1' COMMENT '�汾��',
   yn tinyint(4) NOT NULL DEFAULT '0' COMMENT 'ɾ����ʶ 0:��Ч 1:��Ч',
   ts timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'ʱ���',
   primary key (id),
   UNIQUE KEY `uni_idx_sku_id` (`sku_id`),
   KEY `idx_sku_code` (`sku_code`)
) ENGINE=InnoDB AUTO_INCREMENT=20170401 DEFAULT CHARSET=utf8 COMMENT='��Ʒ��';


create table category_info
(
   id          bigint not null auto_increment,
   category_code             varchar(64) not null comment '��Ŀ����',
   category_name        			varchar(256) not null comment '��Ŀ����',
   category_desc                  varchar(256) DEFAULT null comment '��Ŀ˵��',
   category_status	tinyint(4) NOT NULL COMMENT 'Ŀ¼״̬ 0:��Ч 2:��Ч',
   full_path        			int not null comment 'Ŀ¼ȫ·���ö��Ÿ���',
   category_level          int not null default 0 comment '����',
   sort                 int not null default 0 comment '����',
   create_time datetime DEFAULT NULL COMMENT '����ʱ��',
   create_pin varchar(50) DEFAULT NULL COMMENT '������',
   update_time datetime DEFAULT NULL COMMENT '����ʱ��',
   update_pin varchar(50) DEFAULT NULL COMMENT '������',
   sys_version int(11) NOT NULL DEFAULT '1' COMMENT '�汾��',
   yn tinyint(4) NOT NULL DEFAULT '0' COMMENT 'ɾ����ʶ 0:��Ч 1:��Ч',
   ts timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'ʱ���',
   primary key (id),
   UNIQUE KEY `uni_idx_cat_cd` (`category_code`)
) ENGINE=InnoDB AUTO_INCREMENT=20170401 DEFAULT CHARSET=utf8 COMMENT='��Ŀ��';

create table good_flow
(
   id          bigint not null auto_increment,
   sku_id             varchar(64) not null comment '��Ʒ���',
   out_sku_code             varchar(64) not null comment '��������Ʒ����',
   sku_code             varchar(64) not null comment '��Ʒ���룬�����Դ��Ӧһ������',
   sku_name        			varchar(256) not null comment '��Ʒ����',
   sku_price           bigint not null COMMENT '�۸�',
   origin_price	bigint not null COMMENT 'ԭ�۸�',
   from_source int not null comment '��Ʒ��Դ 1:jd 2:tmall',
   org_code varchar(64) not null comment '�̼ұ��',
   grab_time datetime not NULL COMMENT 'ץȡʱ��',
   ext                  varchar(1024) DEFAULT null comment '��չ�ֶ�',
   sort                 int not null default 0 comment '����',
   create_time datetime DEFAULT NULL COMMENT '����ʱ��',
   create_pin varchar(50) DEFAULT NULL COMMENT '������',
   update_time datetime DEFAULT NULL COMMENT '����ʱ��',
   update_pin varchar(50) DEFAULT NULL COMMENT '������',
   sys_version int(11) NOT NULL DEFAULT '1' COMMENT '�汾��',
   yn tinyint(4) NOT NULL DEFAULT '0' COMMENT 'ɾ����ʶ 0:��Ч 1:��Ч',
   ts timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'ʱ���',
   primary key (id),
   KEY `idx_sku_id` (`sku_id`),
   KEY `idx_sku_code` (`sku_code`)
) ENGINE=InnoDB AUTO_INCREMENT=20170401 DEFAULT CHARSET=utf8 COMMENT='��Ʒ��ˮ��';

CREATE TABLE `dict_type` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '����ID',
   dict_type_id bigint(20) NOT NULL COMMENT '���ͱ��',
  `app_code` varchar(64) DEFAULT NULL COMMENT '����Ӧ��',
  `dict_type_code` varchar(128) NOT NULL COMMENT '���ͱ��',
  `dict_type_name` varchar(128) NOT NULL COMMENT '��������',
  `remark` varchar(512) DEFAULT NULL COMMENT '��ע',
   sort                 int not null default 0 comment '����',
   create_time datetime DEFAULT NULL COMMENT '����ʱ��',
   create_pin varchar(50) DEFAULT NULL COMMENT '������',
   update_time datetime DEFAULT NULL COMMENT '����ʱ��',
   update_pin varchar(50) DEFAULT NULL COMMENT '������',
   sys_version int(11) NOT NULL DEFAULT '1' COMMENT '�汾��',
   yn tinyint(4) NOT NULL DEFAULT '0' COMMENT 'ɾ����ʶ 0:��Ч 1:��Ч',
   ts timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'ʱ���',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_idx_dti` (`dict_type_id`),
  KEY `idx_dtc` (`dict_type_code`)
) ENGINE=InnoDB AUTO_INCREMENT=237 DEFAULT CHARSET=utf8 COMMENT='�ֵ�����';

CREATE TABLE `dict_entry` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '����ID',
  `dict_type_id` bigint(20) NOT NULL COMMENT '�ֵ����ͱ��',
  `dict_code` varchar(256) NOT NULL COMMENT '�ֵ���',
  `dict_name` varchar(1024) DEFAULT NULL,
  `remark` varchar(1024) DEFAULT NULL COMMENT '��ע',
   sort                 int not null default 0 comment '����',
   create_time datetime DEFAULT NULL COMMENT '����ʱ��',
   create_pin varchar(50) DEFAULT NULL COMMENT '������',
   update_time datetime DEFAULT NULL COMMENT '����ʱ��',
   update_pin varchar(50) DEFAULT NULL COMMENT '������',
   sys_version int(11) NOT NULL DEFAULT '1' COMMENT '�汾��',
   yn tinyint(4) NOT NULL DEFAULT '0' COMMENT 'ɾ����ʶ 0:��Ч 1:��Ч',
   ts timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'ʱ���',
  PRIMARY KEY (`id`),
  KEY `idx_dti_dc` (`dict_type_id`,`dict_code`)
) ENGINE=InnoDB AUTO_INCREMENT=1264 DEFAULT CHARSET=utf8 COMMENT='�ֵ���';

CREATE TABLE `user_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '����ID',
  `user_id` bigint(20) NOT NULL COMMENT '�û�ID',
  `login_name` varchar(50) NOT NULL COMMENT '��¼��',
  `login_password` varchar(100) NOT NULL COMMENT '��¼ƾ֤',
   salt_code int not null comment '��¼���α���',
  `cn` varchar(32) DEFAULT NULL COMMENT '������',
  `phone` varchar(32) DEFAULT NULL COMMENT '��ϵ�绰',
  `email` varchar(64) DEFAULT NULL COMMENT '����',
  `wei_xin_id` varchar(64) DEFAULT NULL COMMENT '΢��id',
  `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '״̬��0-���á�1-����',
  `last_login_time` datetime DEFAULT NULL COMMENT '�ϴε�¼ʱ��',
  `login_fail_count` int(11) DEFAULT '0' COMMENT '������¼ʧ�ܴ���',
  `remark` varchar(300) DEFAULT NULL COMMENT '��ע',
  `register_source` varchar(50) DEFAULT NULL COMMENT 'ע����Դ',
  `create_time` datetime DEFAULT NULL COMMENT '����ʱ��',
  `create_pin` varchar(50) DEFAULT NULL COMMENT '������',
  `update_time` datetime DEFAULT NULL COMMENT '����ʱ��',
  `update_pin` varchar(50) DEFAULT NULL COMMENT '������',
  `sys_version` int(11) NOT NULL DEFAULT '1' COMMENT '�汾��',
  `yn` tinyint(4) NOT NULL DEFAULT '0' COMMENT 'ɾ����ʶ',
  `ts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'ʱ���',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_idx_user_id` (`user_id`),
  KEY `idx_log_name` (`login_name`)
) ENGINE=InnoDB AUTO_INCREMENT=714 DEFAULT CHARSET=utf8;



/**
create table promotion_info
(
   id          bigint not null auto_increment,
   promotion_code varchar(32) not null comment '�������',
   ext                  varchar(1024) DEFAULT null comment '��չ�ֶ�',
   sort                 int not null default 0 comment '����',
   create_time datetime DEFAULT NULL COMMENT '����ʱ��',
   create_pin varchar(50) DEFAULT NULL COMMENT '������',
   update_time datetime DEFAULT NULL COMMENT '����ʱ��',
   update_pin varchar(50) DEFAULT NULL COMMENT '������',
   sys_version int(11) NOT NULL DEFAULT '1' COMMENT '�汾��',
   yn tinyint(4) NOT NULL DEFAULT '0' COMMENT 'ɾ����ʶ 0:��Ч 1:��Ч',
   ts timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'ʱ���',
   primary key (id),
   KEY `idx_sku_code` (`sku_code`)
) ENGINE=InnoDB AUTO_INCREMENT=20170401 DEFAULT CHARSET=utf8 COMMENT='������Ϣ��';
**/

