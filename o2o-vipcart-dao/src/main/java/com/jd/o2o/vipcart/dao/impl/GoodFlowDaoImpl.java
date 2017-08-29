package com.jd.o2o.vipcart.dao.impl;
import com.jd.o2o.vipcart.common.dao.AbstractBaseDao;
import com.jd.o2o.vipcart.common.dao.TableDes;
import com.jd.o2o.vipcart.dao.GoodFlowDao;
import com.jd.o2o.vipcart.domain.entity.GoodFlowEntity;
import org.springframework.stereotype.Repository;

@TableDes(nameSpace = "good_flowMapper", tableName = "GoodFlow")
@Repository
public class GoodFlowDaoImpl extends AbstractBaseDao<GoodFlowEntity, Long> implements GoodFlowDao {
	
}