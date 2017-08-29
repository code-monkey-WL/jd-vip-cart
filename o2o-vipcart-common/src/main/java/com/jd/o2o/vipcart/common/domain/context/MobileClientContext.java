package com.jd.o2o.vipcart.common.domain.context;

/**
 * 移动客户端上下文
 * User: wuqingming
 * Date: 14-1-7
 * Time: 上午10:43
 * To change this template use File | Settings | File Templates.
 */
public class MobileClientContext extends ClientContext {

    /**
     * 客户端版本（例如：2.7.0）
     */
    private String clientVersion;
    /**
     * 联网方式（例如：wifi）
     */
    private String networkType;
    /**
     * 客户端版本（例如：4fe67543185f766fedb4b5c8212214165f4828b9）
     */
    private String openudid;
    /**
     * 用户id
     */
    private String pin;
    /**
     * 地址（例如：1-72-2799-0）
     */
    private String area;
    /**
     * 客户端（例如：apple）
     */
    private String client;
    /**
     * 操作系统版本（例如：7.0）
     */
    private String osVersion;

    private String uuid;
    /**
     * 屏幕大小（例如：640*1136）
     */
    private String screen;

    private String adid;
    /**
     * 合作伙伴（例如：apple）
     */
    private String partner;

    public String getClientVersion() {
        return clientVersion;
    }

    public void setClientVersion(String clientVersion) {
        this.clientVersion = clientVersion;
    }

    public String getNetworkType() {
        return networkType;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    public String getOpenudid() {
        return openudid;
    }

    public void setOpenudid(String openudid) {
        this.openudid = openudid;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public void setOsVersion(String osVersion) {
        this.osVersion = osVersion;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getScreen() {
        return screen;
    }

    public void setScreen(String screen) {
        this.screen = screen;
    }

    public String getAdid() {
        return adid;
    }

    public void setAdid(String adid) {
        this.adid = adid;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }
}
