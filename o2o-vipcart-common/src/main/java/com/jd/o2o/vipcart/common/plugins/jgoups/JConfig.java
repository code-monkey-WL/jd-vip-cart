package com.jd.o2o.vipcart.common.plugins.jgoups;

import com.jd.o2o.vipcart.common.domain.BaseBean;

/**
 * 配置参数
 * Created by liuhuiqing on 2016/5/27.
 */
public class JConfig extends BaseBean {
    // JGroups通讯组名
    private String clusterName;
    // 是否不接受自己发送的消息
    private Boolean discardOwnMessages;
    // JGroups初始成员列表localhost1[7800],localhost2[7800]
    private String jGroupsInitHosts;
    // 配置文件名称
    private String xmlFileName;

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public Boolean getDiscardOwnMessages() {
        return discardOwnMessages;
    }

    public void setDiscardOwnMessages(Boolean discardOwnMessages) {
        this.discardOwnMessages = discardOwnMessages;
    }

    public String getjGroupsInitHosts() {
        return jGroupsInitHosts;
    }

    public void setjGroupsInitHosts(String jGroupsInitHosts) {
        this.jGroupsInitHosts = jGroupsInitHosts;
    }

    public String getXmlFileName() {
        return xmlFileName;
    }

    public void setXmlFileName(String xmlFileName) {
        this.xmlFileName = xmlFileName;
    }
}
