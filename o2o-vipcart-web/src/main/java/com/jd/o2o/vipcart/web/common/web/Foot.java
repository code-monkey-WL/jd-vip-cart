package com.jd.o2o.vipcart.web.common.web;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Foot implements Serializable {
    private static final long serialVersionUID = 1L;
    private long opt;
    private String host;
    private static String LOCAL_HOST_NAME;

    public Foot() {
    }

    public long getOpt() {
        this.opt = System.currentTimeMillis();
        return this.opt;
    }

    public String getHost() {
        this.host = getLocalHostName();
        return this.host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public static String getLocalHostName() {
        if(null == LOCAL_HOST_NAME || "".equals(LOCAL_HOST_NAME)) {
            try {
                InetAddress e = InetAddress.getLocalHost();
                LOCAL_HOST_NAME = e.getHostName();
            } catch (UnknownHostException var1) {
                LOCAL_HOST_NAME = "localhost";
            }
        }

        return LOCAL_HOST_NAME;
    }
}
