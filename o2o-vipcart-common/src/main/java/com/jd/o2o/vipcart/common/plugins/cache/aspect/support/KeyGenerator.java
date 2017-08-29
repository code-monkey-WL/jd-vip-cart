package com.jd.o2o.vipcart.common.plugins.cache.aspect.support;

import java.lang.reflect.Method;

/**
 * 缓存key生成器
 * Created by liuhuiqing on 2015/10/29.
 */
public interface KeyGenerator {
    Object generate(Object target, Method method, Object... params);
}
