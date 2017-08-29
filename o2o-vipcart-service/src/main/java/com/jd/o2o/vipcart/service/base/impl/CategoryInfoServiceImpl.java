package com.jd.o2o.vipcart.service.base.impl;

import com.jd.o2o.vipcart.common.dao.BaseDao;
import com.jd.o2o.vipcart.common.service.AbstractBaseService;
import com.jd.o2o.vipcart.dao.CategoryInfoDao;
import com.jd.o2o.vipcart.domain.entity.CategoryInfoEntity;
import com.jd.o2o.vipcart.service.base.CategoryInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

@Service
public class CategoryInfoServiceImpl extends AbstractBaseService<CategoryInfoEntity,Long> implements CategoryInfoService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryInfoServiceImpl.class);
    @Resource
    private CategoryInfoDao categoryInfoDao;

    @Override
    protected BaseDao<CategoryInfoEntity, Long> getBaseDao() {
        return categoryInfoDao;
    }
}
