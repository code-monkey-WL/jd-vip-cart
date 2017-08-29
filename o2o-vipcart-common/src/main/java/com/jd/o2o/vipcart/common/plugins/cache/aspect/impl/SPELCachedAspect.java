package com.jd.o2o.vipcart.common.plugins.cache.aspect.impl;

import com.jd.o2o.vipcart.common.domain.exception.BaseMsgException;
import com.jd.o2o.vipcart.common.plugins.cache.aspect.Cache;
import com.jd.o2o.vipcart.common.plugins.cache.aspect.support.spel.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.expression.EvaluationContext;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 支持SPEL表达式缓存实现
 * Created by liuhuiqing on 2015/8/13.
 */
@Aspect
public final class SPELCachedAspect extends CacheAspectSupport{
    private static final Logger LOGGER = LoggerFactory.getLogger(SPELCachedAspect.class);
    private static final String CACHEABLE = "cacheable", UPDATE = "cacheupdate", EVICT = "cacheevict";
    private final ExpressionEvaluator evaluator = new ExpressionEvaluator(); // SPEL表达式解析对象
    private CacheOperationSource cacheOperationSource = new AnnotationCacheOperationSource(); // 缓存操作来源解析对象

    protected SPELCachedAspect(boolean allowNullValues, Cache cache) {
        super(allowNullValues, cache);
    }

    /**
     * 调用服务方法或直接冲缓存中获取数据
     * 不要修改方法签名，否则可能返回值可能不兼容
     *
     * @param point Joint point
     * @return The result of call
     * @throws Throwable
     */
    @Around("execution(@(com.jd.o2o.vipcart.common.plugins.cache.aspect.Cached || com.jd.o2o.vipcart.common.plugins.cache.aspect.CacheEvict || com.jd.o2o.vipcart.common.plugins.cache.aspect.CachePut || com.jd.o2o.vipcart.common.plugins.cache.aspect.CacheGroup) * *(..))")
    public Object cache(final ProceedingJoinPoint point) throws Throwable {
        Map<String, Collection<CacheOperationContext>> opsMap = buildCacheOperationContext(point);
        if(opsMap == null || opsMap.size() == 0){
            return point.proceed();
        }
        Map<CacheOperationContext, Object> updates = null;
        try{
            inspectCacheEvicts(opsMap.get(EVICT),true);
            CacheStatus status = inspectCacheables(opsMap.get(CACHEABLE));
            updates = inspectCacheUpdates(opsMap.get(UPDATE));
            if (status != null) {
                if (status.updateRequired) {
                    updates.putAll(status.cUpdates);
                }else {
                    return status.retVal;
                }
            }
        }catch (Exception e){
            if(!maskException){
                throw e;
            }
        }
        Object retVal = point.proceed();
        try{
            inspectCacheEvicts(opsMap.get(EVICT),false);
            if (updates!=null && !updates.isEmpty()) {
                writeCache(updates, retVal);
            }
        }catch (Exception e){
            if(!maskException){
                throw e;
            }
        }
        return retVal;
    }

    /**
     * 根据配置生成缓存操作上下文对象
     * @param point
     * @return
     */
    private Map<String, Collection<CacheOperationContext>> buildCacheOperationContext(ProceedingJoinPoint point){
        try{
            Object target = point.getThis();
            Class<?> targetClass = AopProxyUtils.ultimateTargetClass(target);
            if (targetClass == null && target != null) {
                targetClass = target.getClass();
            }
            Method method = MethodSignature.class.cast(point.getSignature()).getMethod();
            Collection<CacheOperation> cacheOp = getCacheOperationSource().getCacheOperations(method, targetClass);
            return createOperationContext(cacheOp, method, point.getArgs(), target, targetClass);
        }catch (Exception e){
            LOGGER.error("构建CacheOperationContext出现异常："+point,e);
            return new LinkedHashMap<String, Collection<CacheOperationContext>>(0);
        }
    }

    /**
     * 写缓存操作
     * @param updates
     * @param retVal
     */
    private void writeCache(Map<CacheOperationContext, Object> updates, Object retVal) {
        for (Map.Entry<CacheOperationContext, Object> entry : updates.entrySet()) {
            CacheOperationContext cacheOperationContext = entry.getKey();
            Object key = entry.getValue();
            try {
                if(cacheOperationContext.operation instanceof CacheUpdateOperation){
                    CacheUpdateOperation cacheUpdateOperation = (CacheUpdateOperation) cacheOperationContext.operation;
                    if (isUseCache(cacheUpdateOperation.getSuccessMethod(), retVal)) {
                        // 计算缓存时间
                        int lifeTime = getLifeTime(cacheUpdateOperation.isForever(),cacheUpdateOperation.getLifetime(),
                                cacheUpdateOperation.getUnit(),cacheUpdateOperation.getStep());
                        putCache(key,retVal,lifeTime,cacheUpdateOperation.isSync());
                    }
                }
            } catch (Exception e) {
                LOGGER.error(String.format("写缓存key=[%s]出现异常", key), e);
            }
        }

    }

    /**
     * 操作更新缓存对象转换
     * @param updates
     * @return
     */
    private Map<CacheOperationContext, Object> inspectCacheUpdates(Collection<CacheOperationContext> updates) {
        Map<CacheOperationContext, Object> cUpdates = new LinkedHashMap<CacheOperationContext, Object>(updates.size());
        if(CollectionUtils.isEmpty(updates)){
            return cUpdates;
        }
        for (CacheOperationContext context : updates) {
            if (context.isConditionPassing()) {
                Object key = context.generateKey();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("CachePut key=[%s] for operation [%s]",key,context.operation));
                }
                if(key == null){
                    continue;
                }
                cUpdates.put(context, key);
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("CachePut condition failed on method [%s] for operation [%s]",context.method,context.operation));
                }
            }
        }
        return cUpdates;
    }

    /**
     * 是否执行缓存操作判断（缓存中取数据）
     * @param cacheables
     * @return
     */
    private CacheStatus inspectCacheables(Collection<CacheOperationContext> cacheables) {
        if(CollectionUtils.isEmpty(cacheables)){
            return null;
        }
        Map<CacheOperationContext, Object> cUpdates = new LinkedHashMap<CacheOperationContext, Object>(cacheables.size());
        boolean updateRequire = false;
        Object retVal = null;
        boolean atLeastOnePassed = false;
        for (CacheOperationContext context : cacheables) {
            if (context.isConditionPassing()) {
                atLeastOnePassed = true;
                Object key = context.generateKey();
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("Cached key=[%s] for operation [%s]",key,context.operation));
                }
                if(key == null){
                    continue;
                }
                // add op/key (in case an update is discovered later on)
                cUpdates.put(context, key);
                boolean localCacheHit = false;
                // check whether the cache needs to be inspected or not (the method will be invoked anyway)
                if (!updateRequire) {
                    Object obj = readCache(key);
                    if (obj != null) {
                        retVal = fromStoreValue(obj);
                        localCacheHit = true;
                    }
                }
                if (!localCacheHit) {
                    updateRequire = true;
                }
            } else {
                if (LOGGER.isDebugEnabled()) {
                    LOGGER.debug(String.format("Cached condition failed on method [%s] for operation [%s]",context.method,context.operation));
                }
            }
        }
        // return a status only if at least on cacheable matched
        if (atLeastOnePassed) {
            return new CacheStatus(cUpdates, updateRequire, retVal);
        }
        return null;
    }

    /**
     * 清空缓存数据
     * @param evictions
     * @param beforeInvocation 是否执行业务操作之前清除
     */
    private void inspectCacheEvicts(Collection<CacheOperationContext> evictions, boolean beforeInvocation) {
        if(CollectionUtils.isEmpty(evictions)){
            return;
        }
        for (CacheOperationContext context : evictions) {
            CacheEvictOperation evictOp = (CacheEvictOperation) context.operation;
            if (beforeInvocation == evictOp.isBeforeInvocation()) {
                if (context.isConditionPassing()) {
                    Object key = context.generateKey();
                    if(key == null){
                        continue;
                    }
                    clearCache(key,evictOp.isAllEntries(),evictOp.isSync());
                } else {
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug(String.format("CacheEvict condition failed on method [%s] for operation [%s]",context.method,context.operation));
                    }
                }
            }
        }
    }

    /**
     * 根据注解对象信息生成缓存SPEL上下文对象
     * @param cacheOp
     * @param method
     * @param args
     * @param target
     * @param targetClass
     * @return
     */
    private Map<String, Collection<CacheOperationContext>> createOperationContext(Collection<CacheOperation> cacheOp,
                                                                                  Method method, Object[] args, Object target, Class<?> targetClass) {
        Map<String, Collection<CacheOperationContext>> map = new LinkedHashMap<String, Collection<CacheOperationContext>>(3);
        if(CollectionUtils.isEmpty(cacheOp)){
            return map;
        }
        Collection<CacheOperationContext> cacheables = new ArrayList<CacheOperationContext>();
        Collection<CacheOperationContext> evicts = new ArrayList<CacheOperationContext>();
        Collection<CacheOperationContext> updates = new ArrayList<CacheOperationContext>();
        for (CacheOperation cacheOperation : cacheOp) {
            CacheOperationContext opContext = new CacheOperationContext(cacheOperation, method, args, target, targetClass);
            if (cacheOperation instanceof CacheableOperation) {
                cacheables.add(opContext);
            }
            if (cacheOperation instanceof CacheEvictOperation) {
                evicts.add(opContext);
            }
            if (cacheOperation instanceof CachePutOperation) {
                updates.add(opContext);
            }
        }
        map.put(CACHEABLE, cacheables);
        map.put(EVICT, evicts);
        map.put(UPDATE, updates);
        return map;
    }

    public ExpressionEvaluator getEvaluator() {
        return evaluator;
    }

    public CacheOperationSource getCacheOperationSource() {
        return cacheOperationSource;
    }

    public void setCacheOperationSource(CacheOperationSource cacheOperationSource) {
        this.cacheOperationSource = cacheOperationSource;
    }

    /**
     * 缓存SPEL上下文
     * @param <T>
     */
    private class CacheOperationContext<T extends CacheOperation> {
        private final T operation;
        private final Cache cache;
        private final Object target;
        private final Method method;
        private final Object[] args;
        private final EvaluationContext evalContext;

        public CacheOperationContext(T operation, Method method, Object[] args, Object target, Class<?> targetClass) {
            this.operation = operation;
            this.cache = SPELCachedAspect.this.cache;
            this.target = target;
            this.method = method;
            this.args = args;
            this.evalContext = evaluator.createEvaluationContext(this.cache, method, args, target, targetClass);
        }
        protected boolean isConditionPassing() {
            try{
                if (!StringUtils.isEmpty(this.operation.getCondition())) {
                    return evaluator.condition(this.operation.getCondition(), this.method, this.evalContext);
                }
            }catch (Exception e){
                LOGGER.error("缓存判断条件表达式解析出错:"+this.operation,e);
                if(!maskException){
                    throw new BaseMsgException("缓存判断条件表达式解析出错："+this.operation);
                }
            }
            return true;
        }
        /**
         * 生成缓存key
         */
        protected Object generateKey() {
            Object key = null;
            try{
                if (!StringUtils.isEmpty(this.operation.getKey())) {
                    key = evaluator.key(this.operation.getKey(), this.method, this.evalContext);
                }
                if(key == null){
                    key = keyGenerator.generate(this.target, this.method, this.args);
                }
            }catch (Exception e){
                LOGGER.error("生成缓存key出现异常，表达式="+this.operation,e);
                if(!maskException){
                    throw new BaseMsgException("生成缓存key出现异常:"+e.getMessage());
                }
            }
            return key;
        }

        public Cache getCache() {
            return cache;
        }
    }

    /**
     * 缓存状态
     */
    private static class CacheStatus {
        final Map<CacheOperationContext, Object> cUpdates;
        final boolean updateRequired;
        final Object retVal;
        CacheStatus(Map<CacheOperationContext, Object> cUpdates, boolean updateRequired, Object retVal) {
            this.cUpdates = cUpdates;
            this.updateRequired = updateRequired;
            this.retVal = retVal;
        }
    }
}
