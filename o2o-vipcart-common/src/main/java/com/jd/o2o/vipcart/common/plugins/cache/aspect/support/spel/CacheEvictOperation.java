package com.jd.o2o.vipcart.common.plugins.cache.aspect.support.spel;

/**
 * 缓存清空操作
 * Created by liuhuiqing on 2015/10/30.
 */
public class CacheEvictOperation extends CacheOperation {

    /**
     * 是否清空所有缓存数据
     */
    private boolean allEntries = false;

    /**
     * 是否在方法成功调用后（默认）进行缓存清空操作
     */
    private boolean beforeInvocation = false;

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

    public boolean isAllEntries() {
        return allEntries;
    }

    public void setAllEntries(boolean allEntries) {
        this.allEntries = allEntries;
    }

    public boolean isBeforeInvocation() {
        return beforeInvocation;
    }

    public void setBeforeInvocation(boolean beforeInvocation) {
        this.beforeInvocation = beforeInvocation;
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

    @Override
    protected StringBuilder getOperationDescription() {
        StringBuilder sb = super.getOperationDescription();
        sb.append(",");
        sb.append(this.allEntries);
        sb.append(",");
        sb.append(this.beforeInvocation);
        sb.append(",");
        sb.append(this.sync);
        sb.append(",");
        sb.append(this.successMethod);
        return sb;
    }
}