package com.jd.o2o.vipcart.service.base.impl;

import com.jd.o2o.vipcart.common.dao.BaseDao;
import com.jd.o2o.vipcart.common.service.AbstractBaseService;
import com.jd.o2o.vipcart.dao.SpiderConfigDao;
import com.jd.o2o.vipcart.domain.entity.SpiderConfigEntity;
import com.jd.o2o.vipcart.service.base.SpiderConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class SpiderConfigServiceImpl extends AbstractBaseService<SpiderConfigEntity,Long> implements SpiderConfigService {
    private static final Logger LOGGER = LoggerFactory.getLogger(SpiderConfigServiceImpl.class);
    @Resource
    private SpiderConfigDao spiderConfigDao;

    @Override
    protected BaseDao<SpiderConfigEntity, Long> getBaseDao() {
        return spiderConfigDao;
    }
}
