package com.jd.o2o.vipcart.common.plugins.cache.aspect.impl;

import com.jd.o2o.vipcart.common.plugins.cache.aspect.Cache;
import com.jd.o2o.vipcart.common.plugins.cache.aspect.support.DefaultKeyGenerator;
import com.jd.o2o.vipcart.common.plugins.cache.aspect.support.KeyGenerator;
import com.jd.o2o.vipcart.common.plugins.thread.ThreadPoolFactory;
import com.jd.o2o.vipcart.common.utils.json.JsonUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 缓存切面模板服务
 * Created by liuhuiqing on 2015/10/30.
 */
public abstract class CacheAspectSupport {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    protected KeyGenerator keyGenerator = new DefaultKeyGenerator();
    protected final Cache cache; // 缓存实例对象
    protected volatile boolean maskException = false; // 缓存解析操作出现异常是否继续执行主业务逻辑
    private static final Object NULL_HOLDER = new NullHolder();// 空值对象
    private final boolean allowNullValues;// 空值标记
    private transient ThreadPoolTaskExecutor threadPoolTaskExecutor;// 缓存异步处理线程池
    private final transient ScheduledExecutorService schedule = Executors.newSingleThreadScheduledExecutor(); // 缓存开关控制
    private volatile boolean openRead = true;// 是否从缓存中读取数据
    private volatile boolean openWrite = true;// 是否往缓存中写数据
    private final static String OPEN_READ_KEY = "openRead"; // 读缓存开关key
    private final static String OPEN_WRITE_KEY = "openWrite"; // 写缓存开关key

    protected CacheAspectSupport(boolean allowNullValues, Cache cache) {
        this.allowNullValues = allowNullValues;
        this.cache = cache;
        this.schedule.scheduleWithFixedDelay(
                new Runnable() {
                    @Override
                    public void run() {
                        clean();
                        Boolean isRead = syncCahceConfig(OPEN_READ_KEY);
                        if (isRead != null) {
                            openRead = isRead;
                        }
                        Boolean isWrite = syncCahceConfig(OPEN_WRITE_KEY);
                        if (isWrite != null) {
                            openWrite = isWrite;
                        }
                    }
                }
                ,
                1L, 60L, TimeUnit.SECONDS
        );
    }

    /**
     * 读缓存
     *
     * @param key
     * @return
     */
    protected Object readCache(Object key) {
        Object obj = null;
        if (!openRead) {
            return obj;
        }
        try {
            obj = cache.get(key);
        } catch (Exception e) {
            LOGGER.error(String.format("缓存中取值key=[%s]出现异常", key), e);
        }
        return obj;
    }

    /**
     * 执行写缓存操作
     * @param key
     * @param value
     * @param lifeTime
     * @param isSync
     */
    protected void putCache(final Object key,final Object value, final int lifeTime,boolean isSync) {
        if(!openWrite){
            return;
        }
        if (isSync) {
            putCache(key,value,lifeTime);
        } else {
            getThreadPoolTaskExecutor().submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    putCache(key, value, lifeTime);
                    return null;
                }
            });
        }
    }

    /**
     * 清空缓存操作
     * @param key 缓存操作key
     * @param isClearAll 是否全部清空
     * @param isSync 是否同步进行缓存清空操作
     */
    protected void clearCache(final Object key, final boolean isClearAll, boolean isSync){
        if(!openWrite){
            return;
        }
        if (isSync) {
            clearCache(key,isClearAll);
        } else {
            getThreadPoolTaskExecutor().submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
                    clearCache(key, isClearAll);
                    return null;
                }
            });
        }
    }

    /**
     * 读缓存取值空数据转换
     *
     * @param storeValue
     * @return
     */
    protected Object fromStoreValue(Object storeValue) {
        if (this.allowNullValues && NULL_HOLDER.equals(storeValue)) {
            return null;
        }
        return storeValue;
    }

    /**
     * 写缓存空值数据转换
     *
     * @param userValue
     * @return
     */
    protected Object toStoreValue(Object userValue) {
        if (this.allowNullValues && userValue == null) {
            return NULL_HOLDER;
        }
        return userValue;
    }

    /**
     * 计算缓存生命周期
     * @param isForever 是否永久生效
     * @param lifeTime 缓存时间数
     * @param units 缓存时间单位
     * @param step 缓存时间上调阀值，防缓存大量瞬时失效，用于削峰填谷
     * @return
     */
    protected int getLifeTime(boolean isForever,int lifeTime, TimeUnit units,int step) {
        if (isForever) {
            lifeTime = Integer.MAX_VALUE;
        } else if (lifeTime == 0) {
            lifeTime = 0;
        } else {
            long seconds = units.toSeconds((long)lifeTime);
            if (step != 0) {
                seconds = seconds + (int) (Math.random() * units.toSeconds((long) step));
            }
            if (Integer.MAX_VALUE < seconds) {
                lifeTime = Integer.MAX_VALUE;
            } else {
                lifeTime = (int) seconds;
            }
        }
        return lifeTime;
    }

    /**
     * 是否将缓存结果添加到缓存中去
     *
     * @param methodName
     * @param result
     * @return
     */
    protected boolean isUseCache(String methodName, Object result) {
        boolean isSuccess = true;
        if (StringUtils.isNotEmpty(methodName) && result != null) {
            try {
                Method m = result.getClass().getMethod(methodName);
                isSuccess = Boolean.class.cast(m.invoke(result, null));
            } catch (Exception e) {
                LOGGER.error(String.format("执行业务返回对象结果判断方法[%s](结果数据是否需要缓存)出现错误", methodName), e);
            }
        }
        return isSuccess;
    }

    /**
     * 获得线程池
     *
     * @return
     */
    protected ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
        if (this.threadPoolTaskExecutor == null) {
            // 如果没有个性化制定，则使用工厂类生成线程池，要保证工厂类受spring管理（否则部分功能会失效）
            this.threadPoolTaskExecutor = ThreadPoolFactory.getInstance().getThreadPoolTaskExecutor("defaultCachedAspect");
        }
        return this.threadPoolTaskExecutor;
    }

    /**
     * 清空缓存
     */
    protected void clean() {
        LOGGER.info("CacheAspectSupport.clean()");
    }

    public KeyGenerator getKeyGenerator() {
        return keyGenerator;
    }

    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    public boolean isOpenRead() {
        return openRead;
    }

    public void setOpenRead(boolean openRead) {
        this.openRead = openRead;
        if (!setCahceConfig(OPEN_READ_KEY, openRead)) {
            throw new RuntimeException(new StringBuilder(openRead ? "打开" : "关闭").append("读缓存开关失败").toString());
        }
    }

    public boolean isOpenWrite() {
        return openWrite;
    }

    public void setOpenWrite(boolean openWrite) {
        this.openWrite = openWrite;
        if (!setCahceConfig(OPEN_WRITE_KEY, openWrite)) {
            throw new RuntimeException(new StringBuilder(openWrite ? "打开" : "关闭").append("写缓存开关失败").toString());
        }
    }

    public boolean isMaskException() {
        return maskException;
    }

    public void setMaskException(boolean maskException) {
        this.maskException = maskException;
    }

    /**
     * 配置信息key：主要针对openRead和openWrite
     *
     * @param key
     * @return
     */
    private String buildCacheConfigKey(String key) {
        return new StringBuilder().append(this.cache.getName()).append(":").append(key).toString();
    }

    /**
     * 从缓存中获取缓存配置信息：主要针对openRead和openWrite
     *
     * @param key
     * @return
     */
    private Boolean syncCahceConfig(String key) {
        Boolean value = null;
        try {
            key = buildCacheConfigKey(key);
            Object obj = this.cache.get(key);
            if (obj instanceof Boolean) {
                value = (Boolean) obj;
            }
        } catch (Exception e) {
            LOGGER.error(String.format("同步缓存key[%s]为[%s]出现异常", new Object[]{key, value}), e);
        }
        return value;
    }

    /**
     * 设置缓存配置信息：主要针对openRead和openWrite
     *
     * @param key
     * @param value
     * @return
     */
    private Boolean setCahceConfig(String key, Object value) {
        if (value == null) {
            value = NULL_HOLDER;
        }
        try {
            key = buildCacheConfigKey(key);
            this.cache.put(key, value, Integer.MAX_VALUE);
            return true;
        } catch (Exception e) {
            LOGGER.error(String.format("设置缓存key[%s]为[%s]出现异常", new Object[]{key, JsonUtils.toJson(value)}), e);
        }
        return false;
    }

    /**
     * 缓存清空操作
     * @param key
     * @param isClearAll
     */
    private void clearCache(Object key,boolean isClearAll){
        try{
            if (isClearAll) {
                cache.clear();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("清空所有缓存key=" + key);
                }
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug("清空缓存key=" + key);
                }
                cache.evict(key);
            }
        }catch (Exception e) {
            LOGGER.error(String.format("清空缓存值key=[%s]出现异常", key), e);
        }
    }

    /**
     * 写缓存操作
     *
     * @param key
     * @param value
     * @param lifeTime
     */
    private void putCache(Object key, Object value, int lifeTime) {
        try {
            cache.put(key, toStoreValue(value), lifeTime);
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(new StringBuilder(key.toString())
                        .append("=")
                        .append(String.format(": %s cached in [%s]s",
                                new Object[]{JsonUtils.toJson(value), lifeTime})).toString());
            }
        } catch (Exception e) {
            LOGGER.error(String.format("缓存中存值key=[%s]出现异常", key), e);
        }
    }

    /**
     * 空值对象
     */
    protected static class NullHolder implements Serializable {

        @Override
        public boolean equals(Object obj) {
            if(obj==null){
                return false;
            }
            if(this==obj){
                return true;
            }
            if(obj instanceof NullHolder){
                return true;
            }
            return false;
        }
    }
}