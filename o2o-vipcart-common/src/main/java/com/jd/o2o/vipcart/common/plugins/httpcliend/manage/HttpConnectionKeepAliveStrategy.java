package com.jd.o2o.vipcart.common.plugins.httpcliend.manage;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

/**
 * 连接超时策略 Class Name: HttpConnectionKeepAliveStrategy.java
 *
 * @author liuhuiqing DateTime 2014-6-19 下午4:15:48
 * @version 1.0
 */
public class HttpConnectionKeepAliveStrategy implements
        ConnectionKeepAliveStrategy {
    private long keepLive = 30000;

    public HttpConnectionKeepAliveStrategy() {
        super();
    }

    public HttpConnectionKeepAliveStrategy(long keepLive) {
        super();
        this.keepLive = keepLive;
    }

    @Override
    public long getKeepAliveDuration(HttpResponse response, HttpContext context) {
        // Honor 'keep-alive' header
        HeaderElementIterator it = new BasicHeaderElementIterator(
                response.headerIterator(HTTP.CONN_KEEP_ALIVE));
        while (it.hasNext()) {
            HeaderElement he = it.nextElement();
            String param = he.getName();
            String value = he.getValue();
            if (value != null && param.equalsIgnoreCase("timeout")) {
                try {
                    return Long.parseLong(value) * 1000;
                } catch (NumberFormatException ignore) {
                }
            }
        }
        HttpHost target = (HttpHost) context
                .getAttribute(HttpClientContext.HTTP_TARGET_HOST);
        if (target != null
                && "www.xyz.com".equalsIgnoreCase(target.getHostName())) {
            // Keep alive for 5 seconds only
            return 5 * 1000;
        } else {
            // otherwise keep alive for 30 seconds
            return keepLive;
        }
    }

}
