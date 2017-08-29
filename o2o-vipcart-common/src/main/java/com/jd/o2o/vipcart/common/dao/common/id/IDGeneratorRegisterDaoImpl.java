package com.jd.o2o.vipcart.common.dao.common.id;

import com.jd.o2o.vipcart.common.dao.AbstractBaseDao;
import com.jd.o2o.vipcart.common.dao.TableDes;
import com.jd.o2o.vipcart.common.domain.enums.YNEnum;
import com.jd.o2o.vipcart.common.domain.exception.BaseMsgException;
import com.jd.o2o.vipcart.common.domain.id.IDGeneratorRegisterEntity;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@TableDes(nameSpace = "ID_generator_registerMapper", tableName = "IDGeneratorRegister")
public class IDGeneratorRegisterDaoImpl extends AbstractBaseDao<IDGeneratorRegisterEntity, Long> implements IDGeneratorRegisterDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(IDGeneratorRegisterDaoImpl.class);

    @Override
    public Integer getRegisterWorkNo(String primaryKey, String registerKey) {
        if (StringUtils.isEmpty(primaryKey)) {
            throw new BaseMsgException("获得机器注册编号primaryKey属性不能为空");
        }
        if (StringUtils.isEmpty(registerKey)) {
            throw new BaseMsgException("获得机器注册编号registerKey属性不能为空");
        }
        int loop = 1;
        Integer workNo = 0;
        while (workNo < 1) {
            try {
                if (loop++ % 4 == 0) {
                    Thread.sleep(500);
                }
                workNo = getWorkerNo(primaryKey, registerKey);
            } catch (Exception e) {
                LOGGER.error(String.format("获得机器注册编号primaryKey=[%s],registerKey=[%s]出现异常", primaryKey, registerKey), e);
                throw new RuntimeException(e.getMessage(), e);
            }
        }
        return workNo;
    }

    private Integer getWorkerNo(String primaryKey, String registerKey) {
        IDGeneratorRegisterEntity query = new IDGeneratorRegisterEntity();
        query.setPrimaryKey(primaryKey);
        query.setYn(YNEnum.Y.getCode());
        List<IDGeneratorRegisterEntity> list = super.findList(query);
        Integer maxWorkNo = 0;
        if (CollectionUtils.isNotEmpty(list)) {
            for (IDGeneratorRegisterEntity entity : list) {
                if (registerKey.equals(entity.getRegisterKey())) {
                    return entity.getWorkerNo();
                }
                if (entity.getWorkerNo() > maxWorkNo) {
                    maxWorkNo = entity.getWorkerNo();
                }
            }
        }
        Integer workNo = maxWorkNo + 1;
        IDGeneratorRegisterEntity entity = new IDGeneratorRegisterEntity();
        entity.setPrimaryKey(primaryKey);
        entity.setRegisterKey(registerKey);
        entity.setWorkerNo(workNo);
        entity.setYn(YNEnum.Y.getCode());
        int r = super.save(entity);
        return r > 0 ? workNo : 0;
    }
}