package com.jd.o2o.vipcart.service.base.impl;

import com.jd.o2o.vipcart.common.dao.BaseDao;
import com.jd.o2o.vipcart.common.service.AbstractBaseService;
import com.jd.o2o.vipcart.dao.GoodInfoDao;
import com.jd.o2o.vipcart.domain.entity.GoodInfoEntity;
import com.jd.o2o.vipcart.service.base.GoodInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class GoodInfoServiceImpl extends AbstractBaseService<GoodInfoEntity,Long> implements GoodInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(GoodInfoServiceImpl.class);
    @Resource
    private GoodInfoDao goodInfoDao;

    @Override
    protected BaseDao<GoodInfoEntity, Long> getBaseDao() {
        return goodInfoDao;
    }
}
