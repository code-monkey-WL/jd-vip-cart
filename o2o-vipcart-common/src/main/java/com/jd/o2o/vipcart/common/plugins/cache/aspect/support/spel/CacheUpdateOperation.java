package com.jd.o2o.vipcart.common.plugins.cache.aspect.support.spel;

import java.util.concurrent.TimeUnit;

/**
 * 通用写缓存操作（常规+强制写）
 * Created by liuhuiqing on 2015/10/30.
 */
public class CacheUpdateOperation extends CacheOperation {
    /**
     * 缓存生效时间
     */
    private int lifetime = 1;

    /**
     * 指定的缓存生效时间的基础上延长范围幅度阀值，用来削峰填谷
     * 例如：
     *  lifetime=5 unit=TimeUnit.MINUTES step=1
     *  则缓存的生效时间计算公式为：（lifetime+step*random）*unit 其中random为0到1的随机数
     *  即五分钟到六分钟之间的随机值
     * @return
     */
    private int step = 0;

    /**
     * 缓存生效时间单位
     */
    private TimeUnit unit = TimeUnit.MINUTES;

    /**
     * 缓存永不失效
     */
    private boolean forever = false;

    /**
     * 缓存与业务是否同步处理
     * @return
     */
    private boolean sync = true;
    /**
     * 业务方法执行结果返回值对象里的判断业务方法调用结果成功与否的方法名称
     * 如果值为空，则将结果进行缓存（抛出异常除外）
     * 如果值不为空，则只有返回true时进行结果缓存
     * For example:
     * 服务返回结果类定义
     * <pre> class ServiceResponse {
     *   private java.lang.String code;
     *   private java.lang.String msg;
     *   private java.lang.String detail;
     *   private T result;
     *   public boolean isSuccess() {
     *      return "0".equels("code");
     *   }
     * }</pre>
     * @return
     */
    private String successMethod = "";

    public int getLifetime() {
        return lifetime;
    }

    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public TimeUnit getUnit() {
        return unit;
    }

    public void setUnit(TimeUnit unit) {
        this.unit = unit;
    }

    public boolean isForever() {
        return forever;
    }

    public void setForever(boolean forever) {
        this.forever = forever;
    }

    public boolean isSync() {
        return sync;
    }

    public void setSync(boolean sync) {
        this.sync = sync;
    }

    public String getSuccessMethod() {
        return successMethod;
    }

    public void setSuccessMethod(String successMethod) {
        this.successMethod = successMethod;
    }
}
