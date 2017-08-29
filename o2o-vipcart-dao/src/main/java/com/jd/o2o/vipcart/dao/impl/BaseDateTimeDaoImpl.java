package com.jd.o2o.vipcart.dao.impl;

import com.jd.o2o.vipcart.common.dao.AbstractBaseDao;
import com.jd.o2o.vipcart.common.dao.TableDes;
import com.jd.o2o.vipcart.common.domain.criteria.Criteria;
import com.jd.o2o.vipcart.dao.BaseDateTimeDAO;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * 获得数据库时间
 */
@Repository
@TableDes(nameSpace = "commonRoadMapper",tableName = "commonRoadMapper")
public class BaseDateTimeDaoImpl extends AbstractBaseDao<Criteria, Long> implements BaseDateTimeDAO {
    public Date getCurrentDBTime(){
        return (Date)super.getSqlSession().selectOne("commonRoadMapper.selectCurrentDateTime");
    }
}
