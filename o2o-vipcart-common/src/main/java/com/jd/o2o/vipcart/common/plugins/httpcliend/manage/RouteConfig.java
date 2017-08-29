package com.jd.o2o.vipcart.common.plugins.httpcliend.manage;

import com.jd.o2o.vipcart.common.domain.BaseBean;
import org.apache.commons.lang.StringUtils;

/**
 * Class Name: RouteConfig.java
 *
 * @author liuhuiqing DateTime 2014-6-19 上午8:51:24
 * @version 1.0
 */
public class RouteConfig extends BaseBean {
    private static final long serialVersionUID = 1944397320954404147L;
    private String hostName;
    private int port;
    private int maxPerRoute;

    public RouteConfig(String hostName, int port, int maxPerRoute) {
        super();
        this.hostName = hostName;
        this.port = port;
        this.maxPerRoute = maxPerRoute;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getMaxPerRoute() {
        return maxPerRoute;
    }

    public void setMaxPerRoute(int maxPerRoute) {
        this.maxPerRoute = maxPerRoute;
    }

    @Override
    public int hashCode() {
        int result = 17;
        result += 31 * result
                + (StringUtils.isEmpty(hostName) ? 0 : hostName.hashCode());
        result += 31 * result + port;
        result += 31 * result + maxPerRoute;

        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RouteConfig)) {
            return false;
        }
        RouteConfig model = (RouteConfig) obj;
        if (StringUtils.equals(hostName, model.getHostName())
                && port == model.getPort()
                && maxPerRoute == model.getMaxPerRoute()) {
            return true;
        }
        return false;
    }
}
