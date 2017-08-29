package com.jd.o2o.vipcart.common.service.id;

import com.jd.o2o.vipcart.common.dao.common.id.IDGeneratorRegisterDao;
import com.jd.o2o.vipcart.common.domain.exception.BaseMsgException;
import com.jd.o2o.vipcart.common.utils.NetUtils;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * snowflake主键生成器
 * Created by liuhuiqing on 2017/4/24.
 */
public class IDGeneratorRegisterServiceImpl implements IDGeneratorService {
    private IDGeneratorRegisterDao idGeneratorRegisterDaoImpl;
    private final Map<String, SnowFlakeGenerator> generatorMap = new HashMap<String, SnowFlakeGenerator>();

    @Override
    public synchronized long nextId(String primaryKey) {
        SnowFlakeGenerator generator = generatorMap.get(primaryKey);
        if (generator == null) {
            if (StringUtils.isEmpty(primaryKey)) {
                throw new BaseMsgException("primaryKey属性不能为空");
            }
            String registerKey = NetUtils.getIP();
            Integer workId = idGeneratorRegisterDaoImpl.getRegisterWorkNo(primaryKey, registerKey);
            generator = new SnowFlakeGenerator(workId);
            generatorMap.put(primaryKey, generator);
        }
        return generator.nextId();
    }
    public void setIdGeneratorRegisterDaoImpl(IDGeneratorRegisterDao idGeneratorRegisterDaoImpl) {
        this.idGeneratorRegisterDaoImpl = idGeneratorRegisterDaoImpl;
    }

}
