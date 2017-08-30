package com.jd.o2o.vipcart.dao.impl;
import com.jd.o2o.vipcart.common.dao.AbstractBaseDao;
import com.jd.o2o.vipcart.common.dao.TableDes;
import com.jd.o2o.vipcart.dao.BrandInfoDao;
import com.jd.o2o.vipcart.domain.entity.BrandInfoEntity;
import org.springframework.stereotype.Repository;

@TableDes(nameSpace = "brand_infoMapper", tableName = "BrandInfo")
@Repository
public class BrandInfoDaoImpl extends AbstractBaseDao<BrandInfoEntity, Long> implements BrandInfoDao {
	
}