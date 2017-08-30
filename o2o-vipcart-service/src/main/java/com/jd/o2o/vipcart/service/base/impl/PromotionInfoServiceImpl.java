package com.jd.o2o.vipcart.service.base.impl;

import com.jd.o2o.vipcart.common.dao.BaseDao;
import com.jd.o2o.vipcart.common.service.AbstractBaseService;
import com.jd.o2o.vipcart.dao.PromotionInfoDao;
import com.jd.o2o.vipcart.domain.entity.PromotionInfoEntity;
import com.jd.o2o.vipcart.service.base.PromotionInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class PromotionInfoServiceImpl extends AbstractBaseService<PromotionInfoEntity,Long> implements PromotionInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PromotionInfoServiceImpl.class);
    @Resource
    private PromotionInfoDao promotionInfoDao;

    @Override
    protected BaseDao<PromotionInfoEntity, Long> getBaseDao() {
        return promotionInfoDao;
    }
}
