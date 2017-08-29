package com.jd.o2o.vipcart.service.busi.common.depend;

import com.jd.o2o.vipcart.domain.entity.DictEntryEntity;

import java.util.List;

/**
 * 基础数据依赖服务
 * Created by liuhuiqing on 2017/5/18.
 */
public interface BasicDataDependService {
    /**
     * 根据字典类型编号获得所有字典项
     * @param dictTypeCode
     * @return
     */
    public List<DictEntryEntity> findDictByDictTypeCode(String dictTypeCode);

    /**
     * 根据字典类型编号和字典项编号获得字典项
     * @param dictTypeCode
     * @param dictCode
     * @return
     */
    public DictEntryEntity findDictByDictCode(String dictTypeCode,String dictCode);
}
