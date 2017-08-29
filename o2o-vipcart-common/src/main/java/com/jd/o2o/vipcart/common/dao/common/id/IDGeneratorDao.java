package com.jd.o2o.vipcart.common.dao.common.id;

import com.jd.o2o.vipcart.common.domain.id.SequenceRange;

/**
 * 按照号段管理模式，生成业务主键
 *
 * 通过每次请求将获得指定步长的主键号段，然后在应用内存中使用时，只需将颁发号段中的主键号自增即可，当用完后，再次请求颁发新的号段
 *
     create table ID_generator(
     id          bigint not null auto_increment,
     primary_key             varchar(64) not null comment '业务主键标识',
     step             int(11) not null comment '更新步长',
     sequence_no             bigint not null comment '主键编号',
     create_time datetime DEFAULT NULL COMMENT '创建时间',
     create_pin varchar(50) DEFAULT NULL COMMENT '创建人',
     update_time datetime DEFAULT NULL COMMENT '更新时间',
     update_pin varchar(50) DEFAULT NULL COMMENT '更新人',
     sys_version int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
     yn tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0:有效 1:无效',
     ts timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
     primary key (id),
     UNIQUE KEY `uni_idx_key` (`primary_key`)
     ) ENGINE=InnoDB AUTO_INCREMENT=20170401 DEFAULT CHARSET=utf8 COMMENT='业务主键生成器';
 *
 */
public interface IDGeneratorDao {
    /**
     * 获得下一批序列号段的最大值
     * @param primaryKey
     * @return
     */
    public SequenceRange nextMaxSequenceNo(String primaryKey);
}