package com.jd.o2o.vipcart.service.busi.common.depend.impl;

import com.jd.o2o.vipcart.common.plugins.cache.aspect.LocalCached;
import com.jd.o2o.vipcart.domain.entity.DictEntryEntity;
import com.jd.o2o.vipcart.service.busi.common.depend.BasicDataDependService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 基础数据依赖服务
 * Created by liuhuiqing on 2017/5/18.
 */
@Service
public class BasicDataDependServiceImpl implements BasicDataDependService {
    @Override
    @LocalCached(lifetime = 5)
    public List<DictEntryEntity> findDictByDictTypeCode(String dictTypeCode) {
        return null;
    }

    @Override
    @LocalCached(lifetime = 5)
    public DictEntryEntity findDictByDictCode(String dictTypeCode, String dictCode) {
        return null;
    }

}
