package com.jd.o2o.vipcart.common.plugins.cache.aspect.support;

import com.jd.o2o.vipcart.common.utils.json.JsonUtils;

import java.lang.reflect.Method;

/**
 * 默认缓存key生成器
 * Created by liuhuiqing on 2015/10/29.
 */
public class DefaultKeyGenerator implements KeyGenerator{
//    public static final int NULL_PARAM_KEY = 53;

    public Object generate(Object target, Method method, Object... params) {
        return new StringBuilder(method.getDeclaringClass().getName()).append(".").append(method.getName()).append("(").append(JsonUtils.toJson(params)).append(")").toString();
    }

//    @Override
//    public String toString() {
//        return new StringBuilder(this.method.getDeclaringClass().getName()).append(".").append(this.method.getName()).append("(").append(this.argsString).append(")").toString();
//    }

//    @Override
//    public int hashCode() {
//        int mHashCode = method.hashCode();
//        if (params == null || params.length == 0) {
//            return String.valueOf(mHashCode);
//        }
//        long hashCode = 17;
//        for (Object object : params) {
//            hashCode = 31 * hashCode + (object == null ? NULL_PARAM_KEY : object.hashCode());
//        }
//        return new StringBuilder().append(mHashCode).append(hashCode).toString();
//    }
//
//    @Override
//    public boolean equals(final Object obj) {
//        final boolean equals;
//        if (this == obj) {
//            equals = true;
//        } else if (obj instanceof CachedAspect.Key) {
//            final CachedAspect.Key key = CachedAspect.Key.class.cast(obj);
//            equals = key.method.equals(this.method)
//                    && JsonUtils.toJson(key.argsString).equals(JsonUtils.toJson(this.argsString));
//        } else {
//            equals = false;
//        }
//        return equals;
//    }
}
