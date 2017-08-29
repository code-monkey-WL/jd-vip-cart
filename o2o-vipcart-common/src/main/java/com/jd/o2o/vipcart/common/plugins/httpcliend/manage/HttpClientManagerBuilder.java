package com.jd.o2o.vipcart.common.plugins.httpcliend.manage;

import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;

import java.util.HashSet;
import java.util.Set;


/**
 * 设置http连接池封装处理类
 * Class Name: HttpClientManagerBuilder.java
 *
 * @author liuhuiqing DateTime 2014-6-19 上午8:54:09
 * @version 1.0
 */
public class HttpClientManagerBuilder {
    // http连接池
    private PoolingHttpClientConnectionManager conManager;
    // http上下文
    private HttpContext httpContext;
    private CloseableHttpClient httpClient;
    // 设置指定host连接池属性：例如，最多连接数
    private Set<RouteConfig> routeConfigSet = new HashSet<RouteConfig>();
    // 连接池所有host默认最多连接数
    private int maxTotal = 200;
    // 指定host默认最多连接数
    private int maxPerRoute = 100;

    private HttpClientManagerBuilder() {
        super();
    }

    private HttpClientManagerBuilder(RouteConfig routeConfig) {
        super();
        routeConfigSet.add(routeConfig);
    }

    public static HttpClientManagerBuilder create() {
        return new HttpClientManagerBuilder();
    }

    public static HttpClientManagerBuilder create(
            RouteConfig routeConfig) {
        return new HttpClientManagerBuilder(routeConfig);
    }

    /**
     * 配置指定host信息的连接池，可在构造时或构造完成后调用
     *
     * @param routeConfig
     * @return
     * @author liuhuiqing  DateTime 2014-6-19 上午8:55:51
     */
    public HttpClientManagerBuilder addHost(RouteConfig routeConfig) {
        routeConfigSet.add(routeConfig);
        if (httpClient != null) {
            setRouteConfig(routeConfig);
        }
        return this;
    }

    public HttpClientManagerBuilder setPoolingHttpClientConnectionManager(
            PoolingHttpClientConnectionManager conManager) {
        this.conManager = conManager;
        return this;
    }

    public HttpClientManagerBuilder setHttpContext(HttpContext httpContext) {
        this.httpContext = httpContext;
        return this;
    }

    public CloseableHttpClient getHttpClient() {
        return httpClient;
    }

    public HttpContext getHttpContext() {
        return httpContext;
    }

    public HttpClientManagerBuilder setMaxTotal(int maxTotal) {
        this.maxTotal = maxTotal;
        return this;
    }

    public Set<RouteConfig> getRouteConfigSet() {
        return routeConfigSet;
    }

    public HttpClientManagerBuilder setMaxPerRoute(int maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
        return this;
    }

    public PoolingHttpClientConnectionManager getConManager() {
        return conManager;
    }

    public CloseableHttpClient builder() {
        if (conManager == null) {
            conManager = createPoolingHttpClientConnectionManager();
        }
        setRouteConfig();
        if (httpContext == null) {
            httpContext = createHttpContext();
        }
        CloseableHttpClient httpClient = createHttpClient();
        this.httpClient = httpClient;
        return httpClient;
    }

    /**
     * 增添连接失效策略
     *
     * @param httpClientProxy
     * @return
     * @author liuhuiqing  DateTime 2014-6-19 上午10:48:33
     */
    public HttpClientManagerBuilder setHttpClientProxy(HttpClientProxy httpClientProxy) {
        httpClientProxy.registerHttpClientManagerBuilder(this);
        return this;
    }

    private PoolingHttpClientConnectionManager createPoolingHttpClientConnectionManager() {
        PoolingHttpClientConnectionManager conManager = new PoolingHttpClientConnectionManager();
        // 所有路由的最大连接数
        conManager.setMaxTotal(maxTotal);
        // 每个路由的最大连接数
        conManager.setDefaultMaxPerRoute(maxPerRoute);
        return conManager;
    }

    private HttpContext createHttpContext() {
        return HttpClientContext.create();
    }

    private CloseableHttpClient createHttpClient() {
        return HttpClients.custom().setConnectionManager(conManager)
                .setKeepAliveStrategy(new HttpConnectionKeepAliveStrategy()).build();
    }

    private HttpClientManagerBuilder setRouteConfig() {
        if (CollectionUtils.isNotEmpty(routeConfigSet)) {
            for (RouteConfig route : routeConfigSet) {
                setRouteConfig(route);
            }
        }
        return this;
    }

    private HttpClientManagerBuilder setRouteConfig(RouteConfig route) {
        HttpHost httpHost = new HttpHost(route.getHostName(), route.getPort());
        conManager.setMaxPerRoute(new HttpRoute(httpHost),
                route.getMaxPerRoute());
        return this;
    }

}
