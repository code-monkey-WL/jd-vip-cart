package com.jd.o2o.vipcart.common.plugins.httpcliend.manage;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.TimeUnit;

/**
 * http连接池管理类：连接失效策略 Class Name: HttpClientProxy.java
 *
 * @author liuhuiqing DateTime 2014-6-19 上午9:03:16
 * @version 1.0
 */
//@Component
public class HttpClientProxy implements DisposableBean, Runnable {
    private static final Logger logger = LoggerFactory.getLogger(HttpClientProxy.class);
    private static List<HttpClientManagerBuilder> httpClientManagerBuilderList = new Vector<HttpClientManagerBuilder>();
    private volatile boolean shutdown = false;
    private Thread thread;
    private static HttpClientProxy httpClientProxy;

//    @PostConstruct
    public void startListening() {
        thread = new Thread(this, "HttpClientProxy-ExpiredConnectionsManager");
        thread.start();
    }

    private HttpClientProxy() {
        startListening();
    }

    public synchronized static HttpClientProxy getInstance(){
        if(httpClientProxy == null){
            httpClientProxy = new HttpClientProxy();
        }
        return httpClientProxy;
    }

    public void registerHttpClientManagerBuilder(
            HttpClientManagerBuilder httpClientManagerBuilder) {
        httpClientManagerBuilderList.add(httpClientManagerBuilder);
    }

    public static void addHttpClientManagerBuilder(
            HttpClientManagerBuilder httpClientManagerBuilder) {
        httpClientManagerBuilderList.add(httpClientManagerBuilder);
    }

    public void closeIdleConnections() {
        for (Iterator<HttpClientManagerBuilder> iterator = httpClientManagerBuilderList
                .iterator(); iterator.hasNext(); ) {
            HttpClientManagerBuilder builder = iterator.next();
            HttpClientConnectionManager connMgr = builder.getConManager();
            if (connMgr != null) {
                // 关闭失效的连接
                connMgr.closeExpiredConnections();
                // 可选的, 关闭30秒内不活动的连接
                connMgr.closeIdleConnections(30, TimeUnit.SECONDS);
            }
        }
    }

    @Override
    public void run() {
        try {
            logger.info("HttpClientProxy start....");
            while (!shutdown) {
                synchronized (this) {
                    wait(5000);
                    closeIdleConnections();
                }
            }
        } catch (InterruptedException ex) {
            logger.info("httpClient连接池管理线程已中断!");
        }

    }

    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }

    @Override
    public void destroy() {
        int count = 0;
        shutdown();
        for (Iterator<HttpClientManagerBuilder> iterator = httpClientManagerBuilderList
                .iterator(); iterator.hasNext(); ) {
            HttpClientManagerBuilder builder = iterator.next();
            CloseableHttpClient httpClient = builder.getHttpClient();
            if (httpClient != null) {
                try {
                    httpClient.close();
                    count++;
                } catch (IOException e) {
                    logger.error("销毁httpclient出现异常！", e);
                }
            }
        }
        logger.info("HttpClientProxy destroy httpClient:count={}", count);
    }

}
