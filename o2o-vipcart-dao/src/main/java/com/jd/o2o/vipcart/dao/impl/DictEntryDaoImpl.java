package com.jd.o2o.vipcart.dao.impl;
import com.jd.o2o.vipcart.common.dao.AbstractBaseDao;
import com.jd.o2o.vipcart.common.dao.TableDes;
import com.jd.o2o.vipcart.dao.DictEntryDao;
import com.jd.o2o.vipcart.domain.entity.DictEntryEntity;
import org.springframework.stereotype.Repository;

@TableDes(nameSpace = "dict_entryMapper", tableName = "DictEntry")
@Repository
public class DictEntryDaoImpl extends AbstractBaseDao<DictEntryEntity, Long> implements DictEntryDao {
	
}