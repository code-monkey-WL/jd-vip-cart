package com.jd.o2o.vipcart.common.plugins.cache.aspect.support.spel;

import org.springframework.util.Assert;

import java.io.Serializable;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.*;

/**
 * 注解缓存配置解析
 * Created by liuhuiqing on 2015/10/30.
 */
public class AnnotationCacheOperationSource extends AbstractCacheOperationSource implements Serializable {

    private final boolean publicMethodsOnly; // 是否只有公共方法进行缓存操作

    private final Set<CacheAnnotationParser> annotationParsers; // 注解信息解析器


    /**
     * 默认公共方法支持 {@code Cacheable} and {@code CacheEvict} 注解.
     */
    public AnnotationCacheOperationSource() {
        this(true);
    }

    /**
     * 创建默认 {@code AnnotationCacheOperationSource}, 支持公共方法
     *  {@code Cacheable} and {@code CacheEvict} 注解
     *
     * @param publicMethodsOnly proxy-based AOP支持公共方法，AspectJ类织入则支持protected/private方法
     */
    public AnnotationCacheOperationSource(boolean publicMethodsOnly) {
        this.publicMethodsOnly = publicMethodsOnly;
        this.annotationParsers = new LinkedHashSet<CacheAnnotationParser>(1);
        this.annotationParsers.add(new AspectCacheAnnotationParser());
    }

    /**
     *  注入注解解析实现类
     * @param annotationParsers CacheAnnotationParser使用
     */
    public AnnotationCacheOperationSource(CacheAnnotationParser... annotationParsers) {
        this.publicMethodsOnly = true;
        Assert.notEmpty(annotationParsers, "At least one CacheAnnotationParser needs to be specified");
        Set<CacheAnnotationParser> parsers = new LinkedHashSet<CacheAnnotationParser>(annotationParsers.length);
        Collections.addAll(parsers, annotationParsers);
        this.annotationParsers = parsers;
    }


    @Override
    protected Collection<CacheOperation> findCacheOperations(Class<?> clazz) {
        return determineCacheOperations(clazz);
    }

    @Override
    protected Collection<CacheOperation> findCacheOperations(Method method) {
        return determineCacheOperations(method);
    }

    /**
     *
     * 解析给定方法或类的缓存操作信息
     * @param ae 注解标记的方法或类
     * @return 缓存操作配置信息, or {@code null} 如果找不到
     */
    protected Collection<CacheOperation> determineCacheOperations(AnnotatedElement ae) {
        Collection<CacheOperation> ops = null;

        for (CacheAnnotationParser annotationParser : this.annotationParsers) {
            Collection<CacheOperation> annOps = annotationParser.parseCacheAnnotations(ae);
            if (annOps != null) {
                if (ops == null) {
                    ops = new ArrayList<CacheOperation>();
                }
                ops.addAll(annOps);
            }
        }
        return ops;
    }

    /**
     * 默认只有public方法才执行缓存操作
     */
    @Override
    protected boolean allowPublicMethodsOnly() {
        return this.publicMethodsOnly;
    }
}