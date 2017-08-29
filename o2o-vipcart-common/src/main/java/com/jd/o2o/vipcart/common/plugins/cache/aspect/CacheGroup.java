package com.jd.o2o.vipcart.common.plugins.cache.aspect;

import java.lang.annotation.*;

/**
 * 多个相同或不同的缓存注解的集合
 * Created by liuhuiqing on 2015/11/2.
 */
@Target({ ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface CacheGroup {

    Cached[] cacheable() default {};

    CachePut[] put() default {};

    CacheEvict[] evict() default {};
}