package com.jd.o2o.vipcart.common.domain.context;

/**
 * 应用上下文，用于标识接口访问来源
 * User: wuqingming
 * Date: 14-11-7
 * Time: 下午4:35
 * To change this template use File | Settings | File Templates.
 */
public class AppContext extends ClientContext {
    private String source;
    private String accessToken;

    public AppContext() {
    }

    public AppContext(String source) {
        this.source = source;
    }

    public AppContext(String source, String accessToken) {
        this.source = source;
        this.accessToken = accessToken;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
