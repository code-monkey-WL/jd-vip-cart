package com.jd.o2o.vipcart.service.common.project;

import com.jd.o2o.vipcart.common.plugins.cache.aspect.JCache;
import com.jd.o2o.vipcart.domain.entity.DictEntryEntity;

import java.util.Date;
import java.util.List;

/**
 * 系统公共服务
 * Created by liuhuiqing on 2017/04/25.
 */
public interface ProjectCommonService {

    /**
     * 根据字典类型查询字典项
     *
     * @param dictTypeCode
     * @return
     */
    public List<DictEntryEntity> getDictValue(String dictTypeCode);

    /**
     * 获得字典配置项对应值
     *
     * @param dictTypeCode
     * @param dictCode
     * @return
     */
    public String getDictValue(String dictTypeCode, String dictCode);

    /**
     * 获得数据库系统时间
     *
     * @return
     */
    public Date getSystemTime();

    /**
     * 获得缓存对象
     *
     * @return
     */
    public JCache getRedisCache();

    /**
     * 业务序列号生成器
     *
     * @param sequenceKey
     * @return
     */
    public long nextSequence(String sequenceKey);

    /**
     * 长序列号（18位）
     *
     * @param sequenceKey
     * @return
     */
    public long nextLongSequence(String sequenceKey);

}
