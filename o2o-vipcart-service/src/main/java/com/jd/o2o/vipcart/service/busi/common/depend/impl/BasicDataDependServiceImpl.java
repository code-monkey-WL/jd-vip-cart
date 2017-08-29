package com.jd.o2o.vipcart.service.busi.common.depend.impl;

import com.jd.o2o.vipcart.common.plugins.cache.aspect.LocalCached;
import com.jd.o2o.vipcart.service.busi.common.depend.BasicDataDependService;
import com.jd.o2o.vipcart.domain.inside.common.Dictionary;
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
    public List<Dictionary> findDictByDictTypeCode(String dictTypeCode) {
        return null;
    }

    @Override
    @LocalCached(lifetime = 5)
    public Dictionary findDictByDictCode(String dictTypeCode, String dictCode) {
        return null;
    }

//    @Override
//    @Cached(lifetime = 60*24)
//    public StationOutput findStationInfoByNo(String stationNo) {
//        return storeQueryServiceImpl.findStationByStationNo(stationNo);
//    }
}
