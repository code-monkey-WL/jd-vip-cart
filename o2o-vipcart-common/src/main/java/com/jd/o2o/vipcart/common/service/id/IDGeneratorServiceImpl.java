package com.jd.o2o.vipcart.common.service.id;

import com.jd.o2o.vipcart.common.dao.common.id.IDGeneratorDao;
import com.jd.o2o.vipcart.common.domain.exception.BaseMsgException;
import com.jd.o2o.vipcart.common.domain.id.SequenceRange;
import org.apache.commons.lang.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 内存Id生成器
 * Created by liuhuiqing on 2017/4/20.
 */
public class IDGeneratorServiceImpl implements IDGeneratorService {
    private IDGeneratorDao idGeneratorDaoImpl;
    private final Map<String, SequenceRange> generatorMap = new ConcurrentHashMap<String, SequenceRange>();

    @Override
    public synchronized long nextId(String primaryKey) {
        SequenceRange range = generatorMap.get(primaryKey);
        if (range == null || (range.getCursorNo() > range.getEndNo())) {
            if (StringUtils.isEmpty(primaryKey)) {
                throw new BaseMsgException("primaryKey属性不能为空");
            }
            range = idGeneratorDaoImpl.nextMaxSequenceNo(primaryKey);
            generatorMap.put(primaryKey, range);
        }
        long sequence = range.getCursorNo();
        range.setCursorNo(sequence + 1);
        return sequence;
    }

    public void setIdGeneratorDaoImpl(IDGeneratorDao idGeneratorDaoImpl) {
        this.idGeneratorDaoImpl = idGeneratorDaoImpl;
    }
}
