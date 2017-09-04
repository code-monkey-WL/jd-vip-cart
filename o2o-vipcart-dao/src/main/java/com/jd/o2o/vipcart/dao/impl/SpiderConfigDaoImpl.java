package com.jd.o2o.vipcart.dao.impl;
import com.jd.o2o.vipcart.common.dao.AbstractBaseDao;
import com.jd.o2o.vipcart.common.dao.TableDes;
import com.jd.o2o.vipcart.dao.SpiderConfigDao;
import com.jd.o2o.vipcart.domain.entity.SpiderConfigEntity;
import org.springframework.stereotype.Repository;

@TableDes(nameSpace = "spider_configMapper", tableName = "SpiderConfig")
@Repository
public class SpiderConfigDaoImpl extends AbstractBaseDao<SpiderConfigEntity, Long> implements SpiderConfigDao {
	
}