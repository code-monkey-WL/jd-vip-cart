package com.jd.o2o.vipcart.service.common.project.impl;

import com.jd.o2o.vipcart.common.domain.SoftHashMap;
import com.jd.o2o.vipcart.common.plugins.cache.aspect.JCache;
import com.jd.o2o.vipcart.common.plugins.log.track.LoggerTrackFactory;
import com.jd.o2o.vipcart.common.service.id.IDGeneratorService;
import com.jd.o2o.vipcart.service.common.busi.depend.BasicDataDependService;
import com.jd.o2o.vipcart.service.common.project.ProjectCommonService;
import com.jd.o2o.vipcart.dao.BaseDateTimeDAO;
import com.jd.o2o.vipcart.domain.constant.DictTypeCodeEnum;
import com.jd.o2o.vipcart.domain.inside.common.Dictionary;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 基础公共服务实现
 * Created by liuhuiqing on 2017/04/25.
 */
@Service
public class ProjectCommonServiceImpl implements ProjectCommonService {
    private static final Logger LOGGER = LoggerTrackFactory.getLogger(ProjectCommonServiceImpl.class);
    @Resource
    private BaseDateTimeDAO baseDateTimeDAOImpl;
    @Resource
    private BasicDataDependService basicDataDependServiceImpl;
    @Resource
    private IDGeneratorService idGenerator;
    @Resource
    private IDGeneratorService registerGenerator;
    @Resource
    private JCache redisCache;
    // 缓存容器
    private SoftHashMap<String, Object> localCache = new SoftHashMap<String, Object>();

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        // 字典降级
        for (DictTypeCodeEnum dictTypeCodeEnum : DictTypeCodeEnum.values()) {
            List<Dictionary> dictionaryList = getDictValue(dictTypeCodeEnum.getCode());
            if (dictionaryList == null) {
                continue;
            }
            String key = getDictCacheKeyByDictTypeCode(dictTypeCodeEnum.getCode());
            localCache.put(key, dictionaryList);
//            redisCache.put(key, dictionaryList, 6 * 30 * 24 * 60 * 60);
            for (Dictionary dictionary : dictionaryList) {
                String dictKey = getDictCacheKeyByDictCode(dictTypeCodeEnum.getCode(), dictionary.getDictCode());
                localCache.put(dictKey, dictionary.getDictName());
//                redisCache.put(dictKey, dictionary.getDictName(), 6 * 30 * 24 * 60 * 60);
            }
        }
    }

    @Override
    public List<Dictionary> getDictValue(String dictTypeCode) {
        try {
            return basicDataDependServiceImpl.findDictByDictTypeCode(dictTypeCode);
        } catch (Exception e) {
            LOGGER.error(String.format("查询字典类型[%s]对应的字典项值时出现异常，将启用降级方案", dictTypeCode), e);
            return getDictValueByDictTypeCodeFromCache(dictTypeCode);
        }
    }


    @Override
    public String getDictValue(String dictTypeCode, String dictCode) {
        String value = null;
        try {
            Dictionary dictionary = basicDataDependServiceImpl.findDictByDictCode(dictTypeCode, dictCode);
            if (dictionary == null || StringUtils.isEmpty(dictionary.getDictName())) {
                LOGGER.warn("字典类型{}中，没有配置{}对应的字典项值", dictTypeCode, dictCode);
            } else {
                value = dictionary.getDictName();
            }
        } catch (Exception e) {
            LOGGER.error(String.format("查询字典类型[%s]字典编码[%s]对应的字典项值时出现异常，将启用降级方案", dictTypeCode, dictCode), e);
            value = getDictValueByDictCodeFromCache(dictTypeCode, dictCode);
        }
        return value;
    }

    @Override
    public Date getSystemTime() {
        try {
            return baseDateTimeDAOImpl.getCurrentDBTime();
        } catch (Exception e) {
            LOGGER.error("查询数据库时间出现异常，将使用应用服务器时间", e);
        }
        return new Date();
    }

    @Override
    public JCache getRedisCache() {
        return redisCache;
    }

    @Override
    public long nextSequence(String sequenceKey) {
        return idGenerator.nextId(sequenceKey);
    }

    @Override
    public long nextLongSequence(String sequenceKey) {
        return registerGenerator.nextId(sequenceKey);
    }


    /**
     * 字典类型缓存降级实现
     *
     * @param dictTypeCode
     * @return
     */
    private List<Dictionary> getDictValueByDictTypeCodeFromCache(String dictTypeCode) {
        String key = getDictCacheKeyByDictTypeCode(dictTypeCode);
        List<Dictionary> dictionaryList = null;
        try {
            dictionaryList = (List<Dictionary>) localCache.get(key);
            if (dictionaryList == null) {
                dictionaryList = (List<Dictionary>) redisCache.list(key, Dictionary.class);
            }
        } catch (Exception e) {
            LOGGER.error("字典类型" + key + "缓存降级失败！", e);
        }
        return dictionaryList;
    }

    /**
     * 字典项缓存降级
     *
     * @param dictTypeCode
     * @param dictCode
     * @return
     */
    private String getDictValueByDictCodeFromCache(String dictTypeCode, String dictCode) {
        String key = getDictCacheKeyByDictCode(dictTypeCode, dictCode);
        String value = null;
        try {
            value = (String) localCache.get(key);
            if (value == null) {
                value = (String) redisCache.get(key, String.class);
            }
        } catch (Exception e) {
            LOGGER.error("字典项" + key + "缓存降级失败！", e);
        }
        return value;
    }

    /**
     * 字典类型缓存key
     *
     * @param dictTypeCode
     * @return
     */
    private String getDictCacheKeyByDictTypeCode(String dictTypeCode) {
        return new StringBuilder().append("dictTypeCode:").append(dictTypeCode).toString();
    }

    /**
     * 字典项缓存key
     *
     * @param dictTypeCode
     * @param dictCode
     * @return
     */
    private String getDictCacheKeyByDictCode(String dictTypeCode, String dictCode) {
        return new StringBuilder().append("dictCode:").append(dictTypeCode).append("#").append(dictCode).toString();
    }
}
