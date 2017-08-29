package com.jd.o2o.vipcart.service.common.project;

import com.jd.o2o.vipcart.common.plugins.cache.aspect.JCache;
import com.jd.o2o.vipcart.common.utils.BeanHelper;
import com.jd.o2o.vipcart.common.utils.json.JsonUtils;
import com.jd.o2o.vipcart.domain.inside.common.Dictionary;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.List;

/**
 * 系统配置工具类
 * Created by liuhuiqing on 2017/4/25.
 */
@Component
public class ProjectContext implements ApplicationContextAware {
    private static ApplicationContext applicationContext;
    private static ProjectCommonService projectCommonServiceImpl;
    private static JCache redisCache;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        ProjectContext.applicationContext = applicationContext;
        projectCommonServiceImpl = applicationContext.getBean(ProjectCommonService.class);
        redisCache = projectCommonServiceImpl.getRedisCache();
    }

    /**
     * 获得数据库系统时间
     *
     * @return
     */
    public static Date getDBSystemTime() {
        return projectCommonServiceImpl.getSystemTime();
    }

    /**
     * 获得字典配置
     *
     * @param dictTypeCode
     * @param dictCode
     * @return
     */
    public static String getDictValue(String dictTypeCode, String dictCode) {
        return projectCommonServiceImpl.getDictValue(dictTypeCode, dictCode);
    }

    /**
     * 获得指定类型字典项
     *
     * @param dictTypeCode
     * @param dictCode
     * @param defaultValue
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T getDictValue(String dictTypeCode, String dictCode, String defaultValue, Class<T> target) {
        String value = projectCommonServiceImpl.getDictValue(dictTypeCode, dictCode);
        if (StringUtils.isEmpty(value)) {
            value = defaultValue;
        }
        if (!target.isArray()) {
            try {
                return BeanHelper.primitiveToObject(value, target);
            } catch (Exception e) {
                return JsonUtils.fromJson(value, target);
            }
        } else {
            String[] ss = value.split(",");
            int size = ss.length;
            Class<?> clazz = target.getComponentType();
            Object array = Array.newInstance(clazz, ss.length);
            for (int i = 0; i < size; i++) {
                Object obj = null;
                try {
                    obj = BeanHelper.primitiveToObject(ss[i], clazz);
                } catch (Exception e) {
                    obj = JsonUtils.fromJson(ss[i], clazz);
                }
                Array.set(array, i, obj);
            }
            return (T) array;
        }
    }

    /**
     * 根据字典类型查询字典项
     *
     * @param dictTypeCode
     * @return
     */
    public static List<Dictionary> getDictValue(String dictTypeCode) {
        return projectCommonServiceImpl.getDictValue(dictTypeCode);
    }

    /**
     * 获得缓存对象
     * @return
     */
    public static JCache getCache(){
        return redisCache;
    }

    /**
     * 业务序列生成
     * @param sequenceKey
     * @return
     */
    public static long nextSequence(String sequenceKey){
        return projectCommonServiceImpl.nextSequence(sequenceKey);
    }

    /**
     * 生成长序列号
     * @param sequenceKey
     * @return
     */
    public static long nextLongSequence(String sequenceKey){
        return projectCommonServiceImpl.nextLongSequence(sequenceKey);
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}
