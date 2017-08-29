package com.jd.o2o.vipcart.common.dao.common.id;

/**
 * snowflake主键生成器
 *
     create table ID_generator_register(
     id          bigint not null auto_increment,
     primary_key             varchar(64) not null comment '业务主键标识',
     register_key             varchar(64) not null comment '注册主键标识',
     worker_no             int(11) not null comment '注册机标识',
     create_time datetime DEFAULT NULL COMMENT '创建时间',
     create_pin varchar(50) DEFAULT NULL COMMENT '创建人',
     update_time datetime DEFAULT NULL COMMENT '更新时间',
     update_pin varchar(50) DEFAULT NULL COMMENT '更新人',
     sys_version int(11) NOT NULL DEFAULT '1' COMMENT '版本号',
     yn tinyint(4) NOT NULL DEFAULT '0' COMMENT '删除标识 0:有效 1:无效',
     ts timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '时间戳',
     primary key (id),
     UNIQUE KEY `uni_idx_prikey_regkey` (`primary_key`,`register_key`),
     UNIQUE KEY `uni_idx_prikey_workno` (`primary_key`,`worker_no`)
     ) ENGINE=InnoDB AUTO_INCREMENT=20170401 DEFAULT CHARSET=utf8 COMMENT='snowflake主键生成器注册表';
 */
public interface IDGeneratorRegisterDao {
    /**
     * 获得对应业务的机器注册编号
     * @param primaryKey
     * @param registerKey
     * @return
     */
    public Integer getRegisterWorkNo(String primaryKey, String registerKey);

}