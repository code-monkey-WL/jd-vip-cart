package com.jd.o2o.vipcart.common.plugins.httpcliend.manage;

import org.apache.http.concurrent.FutureCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * http请求回调处理类 Class Name: HttpClientFutureCallback.java
 *
 * @author liuhuiqing DateTime 2014-6-24 下午2:04:50
 * @version 1.0
 */
public class HttpClientFutureCallback<T> implements FutureCallback<T> {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientFutureCallback.class);
    private String message;
    private long startTime;

    public HttpClientFutureCallback(String message, long startTime) {
        super();
        this.message = message;
        this.startTime = startTime;
    }

    @Override
    public void completed(T result) {
        logger.info("Request completed[cost:{}ms] :{}",
                new Object[]{(System.currentTimeMillis() - startTime),
                        message});
    }

    @Override
    public void failed(Exception e) {
        logger.error("Request failed[[cost:{}ms] :{}",
                new Object[]{(System.currentTimeMillis() - startTime),
                        message}, e);
    }

    @Override
    public void cancelled() {
        logger.warn("Request cancelled[cost:{}ms] :{}",
                new Object[]{(System.currentTimeMillis() - startTime),
                        message});
    }

}
