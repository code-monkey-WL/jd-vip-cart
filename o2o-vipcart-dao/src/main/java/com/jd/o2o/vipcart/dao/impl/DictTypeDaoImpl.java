package com.jd.o2o.vipcart.dao.impl;
import com.jd.o2o.vipcart.common.dao.AbstractBaseDao;
import com.jd.o2o.vipcart.common.dao.TableDes;
import com.jd.o2o.vipcart.dao.DictTypeDao;
import com.jd.o2o.vipcart.domain.entity.DictTypeEntity;
import org.springframework.stereotype.Repository;

@TableDes(nameSpace = "dict_typeMapper", tableName = "DictType")
@Repository
public class DictTypeDaoImpl extends AbstractBaseDao<DictTypeEntity, Long> implements DictTypeDao {
	
}