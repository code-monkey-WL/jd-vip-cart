package com.jd.o2o.vipcart.common.plugins.log.support;

import org.springframework.util.Assert;

import java.io.Serializable;

/**
 * 日志操作
 * Created by liuhuiqing on 2015/10/30.
 */
public class LogOperation implements Serializable{
    /**
     * SPEL表达式，操作关键字
     */
    private String operationKey = "";
    /**
     * SPEL表达式，操作备选关键字
     */
    private String[] secondaryKeys;
    /**
     * 是否屏蔽保存日志服务异常
     */
    private boolean maskException;

    public LogOperation() {
    }

    public LogOperation(String operationKey, String[] secondaryKeys, boolean maskException) {
        this.operationKey = operationKey;
        this.secondaryKeys = secondaryKeys;
        this.maskException = maskException;
    }

    public String getOperationKey() {
        return operationKey;
    }

    public void setOperationKey(String operationKey) {
        Assert.notNull(operationKey);
        this.operationKey = operationKey;
    }

    public String[] getSecondaryKeys() {
        return secondaryKeys;
    }

    public void setSecondaryKeys(String[] secondaryKeys) {
        Assert.noNullElements(secondaryKeys);
        this.secondaryKeys = secondaryKeys;
    }

    public boolean getMaskException() {
        return maskException;
    }

    public void setMaskException(boolean maskException) {
        this.maskException = maskException;
    }

    /**
     * This implementation compares the {@code toString()} results.
     * @see #toString()
     */
    @Override
    public boolean equals(Object other) {
        return (other instanceof LogOperation && toString().equals(other.toString()));
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
        result.append("[ operationKey=");
        result.append(this.operationKey);
        result.append("] [secondaryKeys=");
        if(secondaryKeys!=null){
            result.append(secondaryKeys[0]);
            for(int i=1;i < secondaryKeys.length;i++){
                result.append(",").append(secondaryKeys[i]);
            }
        }
        result.append("]");
        return result;
    }
}
