package com.jd.o2o.vipcart.dao.impl;
import com.jd.o2o.vipcart.common.dao.AbstractBaseDao;
import com.jd.o2o.vipcart.common.dao.TableDes;
import com.jd.o2o.vipcart.dao.UserInfoDao;
import com.jd.o2o.vipcart.domain.entity.UserInfoEntity;
import org.springframework.stereotype.Repository;

@TableDes(nameSpace = "user_infoMapper", tableName = "UserInfo")
@Repository
public class UserInfoDaoImpl extends AbstractBaseDao<UserInfoEntity, Long> implements UserInfoDao {
	
}