package com.jd.o2o.vipcart.common.service.proxy;

/**
 * 代理对象注入模板方法
 * Created by liuhuiqing on 2015/9/18.
 */
public class AbstractProxyAware<T> implements ProxyAware<T> {
    protected T proxyObject;
    @Override
    public void setProxyObject(T proxyObject) {
        this.proxyObject = proxyObject;
    }

    @Override
    public T getProxyObject() {
        return this.proxyObject;
    }
}
