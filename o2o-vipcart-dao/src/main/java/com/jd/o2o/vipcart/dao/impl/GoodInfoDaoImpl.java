package com.jd.o2o.vipcart.dao.impl;
import com.jd.o2o.vipcart.common.dao.AbstractBaseDao;
import com.jd.o2o.vipcart.common.dao.TableDes;
import com.jd.o2o.vipcart.dao.GoodInfoDao;
import com.jd.o2o.vipcart.domain.entity.GoodInfoEntity;
import org.springframework.stereotype.Repository;

@TableDes(nameSpace = "good_infoMapper", tableName = "GoodInfo")
@Repository
public class GoodInfoDaoImpl extends AbstractBaseDao<GoodInfoEntity, Long> implements GoodInfoDao {
	
}