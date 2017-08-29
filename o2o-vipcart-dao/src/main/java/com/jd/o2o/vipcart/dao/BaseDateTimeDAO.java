package com.jd.o2o.vipcart.dao;

import com.jd.o2o.vipcart.common.dao.BaseDao;
import com.jd.o2o.vipcart.common.domain.criteria.Criteria;

import java.util.Date;

public interface BaseDateTimeDAO extends BaseDao<Criteria, Long> {
    public Date getCurrentDBTime();
}
