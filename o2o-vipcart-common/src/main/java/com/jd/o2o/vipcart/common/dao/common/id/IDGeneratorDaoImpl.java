package com.jd.o2o.vipcart.common.dao.common.id;

import com.jd.o2o.vipcart.common.dao.AbstractBaseDao;
import com.jd.o2o.vipcart.common.dao.TableDes;
import com.jd.o2o.vipcart.common.domain.enums.YNEnum;
import com.jd.o2o.vipcart.common.domain.exception.BaseMsgException;
import com.jd.o2o.vipcart.common.domain.id.IDGeneratorEntity;
import com.jd.o2o.vipcart.common.domain.id.SequenceRange;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@TableDes(nameSpace = "ID_generatorMapper", tableName = "IDGenerator")
public class IDGeneratorDaoImpl extends AbstractBaseDao<IDGeneratorEntity, Long> implements IDGeneratorDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(IDGeneratorRegisterDaoImpl.class);

    @Override
    public SequenceRange nextMaxSequenceNo(String primaryKey) {
        if (StringUtils.isEmpty(primaryKey)) {
            throw new BaseMsgException("获取业务主键编号的primaryKey属性不能为空");
        }
        SequenceRange range = null;
        int loop = 1;
        while (range == null) {
            try {
                if (loop++ % 4 == 0) {
                    Thread.sleep(500);
                }
                range = next(primaryKey);
            } catch (Exception e) {
                LOGGER.error(String.format("获得业务主键编号primaryKey=[%s]出现异常", primaryKey), e);
                throw new RuntimeException(e.getMessage(),e);
            }
        }
        return range;
    }

    private SequenceRange next(String primaryKey) {
        IDGeneratorEntity idGeneratorEntity = getIDGenerator(primaryKey);
        if (idGeneratorEntity == null) {
            throw new BaseMsgException("没有找到primaryKey=" + primaryKey + "的主键编号生成规则！");
        }
        Long sequenceNo = idGeneratorEntity.getSequenceNo();
        Long nextMaxSequenceNo = sequenceNo + idGeneratorEntity.getStep();
        IDGeneratorEntity updateEntity = new IDGeneratorEntity();
        updateEntity.setId(idGeneratorEntity.getId());
        updateEntity.setSequenceNo(nextMaxSequenceNo);
        updateEntity.setSysVersion(idGeneratorEntity.getSysVersion());
        int r = super.update(updateEntity);
        SequenceRange range = null;
        if (r > 0) {
            range = new SequenceRange();
            range.setStartNo(sequenceNo+1);
            range.setEndNo(nextMaxSequenceNo);
            range.setCursorNo(range.getStartNo());
        }
        return range;
    }

    private IDGeneratorEntity getIDGenerator(String primaryKey) {
        IDGeneratorEntity query = new IDGeneratorEntity();
        query.setPrimaryKey(primaryKey);
        query.setYn(YNEnum.Y.getCode());
        List<IDGeneratorEntity> list = super.findList(query);
        if (CollectionUtils.isEmpty(list)) {
            return null;
        } else {
            return list.get(0);
        }
    }

}