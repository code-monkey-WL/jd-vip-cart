package com.jd.o2o.vipcart.dao.impl;
import com.jd.o2o.vipcart.common.dao.AbstractBaseDao;
import com.jd.o2o.vipcart.common.dao.TableDes;
import com.jd.o2o.vipcart.dao.PromotionInfoDao;
import com.jd.o2o.vipcart.domain.entity.PromotionInfoEntity;
import org.springframework.stereotype.Repository;

@TableDes(nameSpace = "promotion_infoMapper", tableName = "PromotionInfo")
@Repository
public class PromotionInfoDaoImpl extends AbstractBaseDao<PromotionInfoEntity, Long> implements PromotionInfoDao {
	
}