package com.jd.o2o.vipcart.common.service.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 代理对象注入
 * 需要再spring配置文件中配置：
 * <bean class="com.jd.o2o.vipcart.common.service.proxy.InjectProxyObjectProcessor"></bean>
 * Created by liuhuiqing on 2015/9/18.
 */
public class InjectProxyObjectProcessor implements BeanPostProcessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(InjectProxyObjectProcessor.class);

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof ProxyAware) {
            LOGGER.debug("inject proxy：" + bean.getClass());
            ProxyAware myBean = (ProxyAware) bean;
            myBean.setProxyObject(bean);
            return myBean;
        }
        return bean;
    }
}
