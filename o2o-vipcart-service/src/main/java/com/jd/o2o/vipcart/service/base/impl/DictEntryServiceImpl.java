package com.jd.o2o.vipcart.service.base.impl;

import com.jd.o2o.vipcart.common.dao.BaseDao;
import com.jd.o2o.vipcart.common.service.AbstractBaseService;
import com.jd.o2o.vipcart.dao.DictEntryDao;
import com.jd.o2o.vipcart.domain.entity.DictEntryEntity;
import com.jd.o2o.vipcart.service.base.DictEntryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class DictEntryServiceImpl extends AbstractBaseService<DictEntryEntity,Long> implements DictEntryService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DictEntryServiceImpl.class);
    @Resource
    private DictEntryDao dictEntryDao;

    @Override
    protected BaseDao<DictEntryEntity, Long> getBaseDao() {
        return dictEntryDao;
    }
}
