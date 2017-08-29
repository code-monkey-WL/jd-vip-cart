package com.jd.o2o.vipcart.dao.impl;
import com.jd.o2o.vipcart.common.dao.AbstractBaseDao;
import com.jd.o2o.vipcart.common.dao.TableDes;
import com.jd.o2o.vipcart.dao.CategoryInfoDao;
import com.jd.o2o.vipcart.domain.entity.CategoryInfoEntity;
import org.springframework.stereotype.Repository;

@TableDes(nameSpace = "category_infoMapper", tableName = "CategoryInfo")
@Repository
public class CategoryInfoDaoImpl extends AbstractBaseDao<CategoryInfoEntity, Long> implements CategoryInfoDao {
	
}