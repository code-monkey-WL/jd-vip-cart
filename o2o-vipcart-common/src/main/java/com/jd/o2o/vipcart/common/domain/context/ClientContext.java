package com.jd.o2o.vipcart.common.domain.context;

import java.io.Serializable;

/**
 * 客户端上下文
 * User: wuqingming
 * Date: 14-1-7
 * Time: 上午10:43
 * To change this template use File | Settings | File Templates.
 */
public class ClientContext implements Serializable {
    //客户端ip
    private String ip;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

}
