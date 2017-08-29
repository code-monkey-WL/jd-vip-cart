package com.jd.o2o.vipcart.common.plugins.cache.aspect.support.spel;

import java.lang.reflect.AnnotatedElement;
import java.util.Collection;

/**
 * 缓存注解解析器
 * Created by liuhuiqing on 2015/10/30.
 */
public interface CacheAnnotationParser {
    Collection<CacheOperation> parseCacheAnnotations(AnnotatedElement ae);
}
