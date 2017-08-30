package com.jd.o2o.vipcart.service.base.impl;

import com.jd.o2o.vipcart.common.dao.BaseDao;
import com.jd.o2o.vipcart.common.service.AbstractBaseService;
import com.jd.o2o.vipcart.dao.BrandInfoDao;
import com.jd.o2o.vipcart.domain.entity.BrandInfoEntity;
import com.jd.o2o.vipcart.service.base.BrandInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class BrandInfoServiceImpl extends AbstractBaseService<BrandInfoEntity,Long> implements BrandInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(BrandInfoServiceImpl.class);
    @Resource
    private BrandInfoDao brandInfoDao;

    @Override
    protected BaseDao<BrandInfoEntity, Long> getBaseDao() {
        return brandInfoDao;
    }
}
