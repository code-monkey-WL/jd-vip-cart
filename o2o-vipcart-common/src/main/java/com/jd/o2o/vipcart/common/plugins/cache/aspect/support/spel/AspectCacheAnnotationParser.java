package com.jd.o2o.vipcart.common.plugins.cache.aspect.support.spel;

import com.jd.o2o.vipcart.common.plugins.cache.aspect.CacheEvict;
import com.jd.o2o.vipcart.common.plugins.cache.aspect.CacheGroup;
import com.jd.o2o.vipcart.common.plugins.cache.aspect.CachePut;
import com.jd.o2o.vipcart.common.plugins.cache.aspect.Cached;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 类方法注解解析处理
 * Created by liuhuiqing on 2015/10/30.
 */
public class AspectCacheAnnotationParser implements CacheAnnotationParser, Serializable {

    public Collection<CacheOperation> parseCacheAnnotations(AnnotatedElement ae) {
        Collection<CacheOperation> ops = null;
        Collection<Cached> cacheables = getAnnotations(ae, Cached.class);
        if (cacheables != null) {
            ops = lazyInit(ops);
            for (Cached cacheable : cacheables) {
                ops.add(parseCacheableAnnotation(ae, cacheable));
            }
        }
        Collection<CacheEvict> evicts = getAnnotations(ae, CacheEvict.class);
        if (evicts != null) {
            ops = lazyInit(ops);
            for (CacheEvict e : evicts) {
                ops.add(parseEvictAnnotation(ae, e));
            }
        }
        Collection<CachePut> updates = getAnnotations(ae, CachePut.class);
        if (updates != null) {
            ops = lazyInit(ops);
            for (CachePut p : updates) {
                ops.add(parseUpdateAnnotation(ae, p));
            }
        }
        Collection<CacheGroup> cacheGroups = getAnnotations(ae, CacheGroup.class);
        if (cacheGroups != null) {
            ops = lazyInit(ops);
            for (CacheGroup c : cacheGroups) {
                ops.addAll(parseCachingAnnotation(ae, c));
            }
        }
        return ops;
    }

    private <T extends Annotation> Collection<CacheOperation> lazyInit(Collection<CacheOperation> ops) {
        return (ops != null ? ops : new ArrayList<CacheOperation>(1));
    }

    CacheableOperation parseCacheableAnnotation(AnnotatedElement ae, Cached caching) {
        CacheableOperation cuo = new CacheableOperation();
        cuo.setCondition(caching.condition());
        cuo.setKey(caching.key());
        cuo.setName(ae.toString());
        cuo.setForever(caching.forever());
        cuo.setLifetime(caching.lifetime());
        cuo.setStep(caching.step());
        cuo.setSuccessMethod(caching.successMethod());
        cuo.setSync(caching.sync());
        cuo.setUnit(caching.unit());
        return cuo;
    }

    CacheEvictOperation parseEvictAnnotation(AnnotatedElement ae, CacheEvict caching) {
        CacheEvictOperation ceo = new CacheEvictOperation();
        ceo.setCondition(caching.condition());
        ceo.setKey(caching.key());
        ceo.setAllEntries(caching.allEntries());
        ceo.setBeforeInvocation(caching.beforeInvocation());
        ceo.setName(ae.toString());
        ceo.setSync(caching.sync());
        ceo.setSuccessMethod(caching.successMethod());
        return ceo;
    }

    CacheOperation parseUpdateAnnotation(AnnotatedElement ae, CachePut caching) {
        CachePutOperation cuo = new CachePutOperation();
        cuo.setCondition(caching.condition());
        cuo.setKey(caching.key());
        cuo.setName(ae.toString());
        cuo.setForever(caching.forever());
        cuo.setLifetime(caching.lifetime());
        cuo.setStep(caching.step());
        cuo.setSuccessMethod(caching.successMethod());
        cuo.setSync(caching.sync());
        cuo.setUnit(caching.unit());
        return cuo;
    }

    Collection<CacheOperation> parseCachingAnnotation(AnnotatedElement ae, CacheGroup cacheGroup) {
        Collection<CacheOperation> ops = null;
        Cached[] cacheables = cacheGroup.cacheable();
        if (!ObjectUtils.isEmpty(cacheables)) {
            ops = lazyInit(ops);
            for (Cached cacheable : cacheables) {
                ops.add(parseCacheableAnnotation(ae, cacheable));
            }
        }
        CacheEvict[] evicts = cacheGroup.evict();
        if (!ObjectUtils.isEmpty(evicts)) {
            ops = lazyInit(ops);
            for (CacheEvict evict : evicts) {
                ops.add(parseEvictAnnotation(ae, evict));
            }
        }
        CachePut[] updates = cacheGroup.put();
        if (!ObjectUtils.isEmpty(updates)) {
            ops = lazyInit(ops);
            for (CachePut update : updates) {
                ops.add(parseUpdateAnnotation(ae, update));
            }
        }
        return ops;
    }

    private static <T extends Annotation> Collection<T> getAnnotations(AnnotatedElement ae, Class<T> annotationType) {
        Collection<T> anns = new ArrayList<T>(2);

        // look at raw annotation
        T ann = ae.getAnnotation(annotationType);
        if (ann != null) {
            anns.add(ann);
        }
        // scan meta-annotations
        for (Annotation metaAnn : ae.getAnnotations()) {
            ann = metaAnn.annotationType().getAnnotation(annotationType);
            if (ann != null) {
                anns.add(ann);
            }
        }
        return (anns.isEmpty() ? null : anns);
    }
}