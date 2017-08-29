package com.jd.o2o.vipcart.service.base.impl;

import com.jd.o2o.vipcart.common.dao.BaseDao;
import com.jd.o2o.vipcart.common.service.AbstractBaseService;
import com.jd.o2o.vipcart.dao.DictTypeDao;
import com.jd.o2o.vipcart.domain.entity.DictTypeEntity;
import com.jd.o2o.vipcart.service.base.DictTypeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class DictTypeServiceImpl extends AbstractBaseService<DictTypeEntity,Long> implements DictTypeService {
    private static final Logger LOGGER = LoggerFactory.getLogger(DictTypeServiceImpl.class);
    @Resource
    private DictTypeDao dictTypeDao;

    @Override
    protected BaseDao<DictTypeEntity, Long> getBaseDao() {
        return dictTypeDao;
    }
}
