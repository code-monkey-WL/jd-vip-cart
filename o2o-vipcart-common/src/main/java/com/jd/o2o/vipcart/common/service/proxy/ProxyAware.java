package com.jd.o2o.vipcart.common.service.proxy;

/**
 * 代理对象处理：解决AOP方法内部调用失效的问题
 * Created by liuhuiqing on 2015/9/18.
 */
public interface ProxyAware<T> {
    /**
     * 设置代理对象
     * @param proxyObject
     */
    public void setProxyObject(T proxyObject);

    /**
     * 获得代理对象
     * @return
     */
    public T getProxyObject();
}
