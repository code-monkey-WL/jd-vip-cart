package com.jd.o2o.vipcart.common.dao.common;

import com.jd.o2o.vipcart.common.domain.id.SqlID;

import java.util.Date;

/**
 * 通用功能实现
 * Created by liuhuiqing on 2016/11/9.
 */
public interface CommonBaseDao {
    /**
     * 获得数据库系统时间
     * @return
     */
    public Date getCurrentDBTime();

    /**
      CREATE TABLE `sequence_task` (
     `id` bigint(20) NOT NULL AUTO_INCREMENT,
     PRIMARY KEY (`id`)
     ) ENGINE=MyISAM AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8 COMMENT='任务编号生成器';
     从高可用角度考虑，要解决单点故障问题：可启用了两台数据库服务器来生成ID，通过区分auto_increment的起始值和步长来生成奇偶数的ID
     例如数据库1的起始值为100 步长为2，那么数据库2的起始值为101，步长为2
     */
    /**
     * 生成最新序列号
     * @param tableName
     * @return
     */
    public Long nextSequence(String tableName);

    /**
     * 生成最新序列号(可跳跃)
     * @param sqlID
     * @return
     */
    public Long nextSequence(SqlID sqlID);

    /**
     * 清空已生成的序列号
     * @param sqlID
     * @return
     */
    public Integer clearSequence(SqlID sqlID);

    /**
     * 获得最近一次生成的序列号
     * @param tableName
     * @return
     */
    public Long selectCurrentSequence(String tableName);
}
