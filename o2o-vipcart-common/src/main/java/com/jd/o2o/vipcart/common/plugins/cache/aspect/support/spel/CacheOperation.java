package com.jd.o2o.vipcart.common.plugins.cache.aspect.support.spel;

import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * 缓存操作
 * Created by liuhuiqing on 2015/10/30.
 */
public abstract class CacheOperation implements Serializable{
    /**
     * SPEL表达式，用来判断缓存操作是否有效
     */
    private String condition = "";
    /**
     * 缓存key的SPEL表达式
     */
    private String key = "";
    /**
     * 注解信息
     */
    private String name = "";

    public String getCondition() {
        return condition;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public void setCondition(String condition) {
        Assert.notNull(condition);
        this.condition = condition;
    }

    public void setKey(String key) {
        Assert.notNull(key);
        this.key = key;
    }

    public void setName(String name) {
        Assert.hasText(name);
        this.name = name;
    }

    /**
     * This implementation compares the {@code toString()} results.
     * @see #toString()
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof CacheOperation && toString().equals(other.toString()));
    }

    /**
     * This implementation returns {@code toString()}'s hash code.
     * @see #toString()
     */
    @Override
    public int hashCode() {
        return toString().hashCode();
    }

    /**
     * Return an identifying description for this cache operation.
     * <p>Has to be overridden in subclasses for correct {@code equals}
     * and {@code hashCode} behavior. Alternatively, {@link #equals}
     * and {@link #hashCode} can be overridden themselves.
     */
    @Override
    public String toString() {
        return getOperationDescription().toString();
    }

    /**
     * Return an identifying description for this caching operation.
     * <p>Available to subclasses, for inclusion in their {@code toString()} result.
     */
    protected StringBuilder getOperationDescription() {
        StringBuilder result = new StringBuilder();
        result.append(getClass().getSimpleName());
        result.append("[");
        result.append(this.name);
        result.append("] condition='");
        result.append(this.condition);
        result.append("' | key='");
        result.append(this.key);
        result.append("'");
        return result;
    }
}
