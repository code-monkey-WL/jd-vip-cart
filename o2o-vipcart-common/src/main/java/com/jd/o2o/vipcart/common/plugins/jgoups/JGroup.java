package com.jd.o2o.vipcart.common.plugins.jgoups;

import com.jd.o2o.vipcart.common.domain.exception.O2OException;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.jgroups.*;
import org.jgroups.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * jgroups接收器
 * Created by liuhuiqing on 2016/5/27.
 */
public class JGroup extends ReceiverAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(JGroup.class);
    private static final String DEFAULT_CLUSTER_NAME = "default_cluster_name";
    private JConfig jConfig;
    // JGroups通讯通道
    private JChannel jChannel;
    // 记录所有成员
    private final List<Address> members=new ArrayList<Address>();
    // 成员变更触发回调服务列表
    private Set<Notification> notifications=new CopyOnWriteArraySet<Notification>();

    public void start() {
        try {
            if(CollectionUtils.isEmpty(notifications)){
                notifications.add(new DefaultNotification());
            }
            jChannel = buildJChannel();
            jChannel.setDiscardOwnMessages(jConfig.getDiscardOwnMessages());
            jChannel.setReceiver(this);
            String clusterName = jConfig.getClusterName();
            if (StringUtils.isBlank(clusterName)) {
                clusterName = DEFAULT_CLUSTER_NAME;
                LOGGER.warn("没有获取到JGroups组名，使用默认组名：{}", clusterName);
            }
            jChannel.connect(clusterName);
            jChannel.getState(null, 50000);
            LOGGER.info("连接到JGroups组：{}详细信息:{}", new Object[]{clusterName, jChannel.toString(true)});
        } catch (Exception e) {
            LOGGER.error("JGroups启动节点异常!", e);
            throw new O2OException("JGroups启动节点异常!", e);
        }
    }

    public void stop() {
        Util.close(jChannel);
    }

    @Override
    public void receive(Message msg) {
        super.receive(msg);
    }

    @Override
    public void getState(OutputStream output) throws Exception {
        super.getState(output);
    }

    @Override
    public void setState(InputStream input) throws Exception {
        super.setState(input);
    }

    @Override
    public void viewAccepted(View view) {
        List<Address> new_mbrs=view.getMembers();
        if(new_mbrs != null) {
            sendViewChangeNotifications(view, new_mbrs, new ArrayList<Address>(members)); // notifies observers (joined, left)
            members.clear();
            members.addAll(new_mbrs);
        }
        super.viewAccepted(view);
    }

    @Override
    public void suspect(Address mbr) {
        super.suspect(mbr);
    }

    @Override
    public void block() {
        super.block();
    }

    @Override
    public void unblock() {
        super.unblock();
    }

    private JChannel buildJChannel() {
        String xmlFileName = jConfig.getXmlFileName();
        String jGroupsInitHosts = jConfig.getjGroupsInitHosts();
        if (org.apache.commons.lang.StringUtils.isEmpty(xmlFileName)) {
            if (org.apache.commons.lang.StringUtils.isEmpty(jGroupsInitHosts)) {
                return JChannelBuilder.buildUDPChannel();
            }
            return JChannelBuilder.buildTCPChannel(jGroupsInitHosts);
        }
        return JChannelBuilder.buildChannelByFile(xmlFileName);
    }

    private void sendViewChangeNotifications(View view, List<Address> new_mbrs, List<Address> old_mbrs) {
        List<Address> joined, left;
        if((notifications.isEmpty()) || (old_mbrs == null) || (new_mbrs == null)){
            return;
        }
        // 1. 新成员加入
        joined=new ArrayList<Address>();
        for(Address mbr: new_mbrs) {
            if(!old_mbrs.contains(mbr))
                joined.add(mbr);
        }

        // 2. 老成员退出
        left=new ArrayList<Address>();
        for(Address mbr: old_mbrs) {
            if(!new_mbrs.contains(mbr)) {
                left.add(mbr);
            }
        }

        for(Notification notif: notifications) {
            notif.viewChange(view, joined, left);
        }
    }

    public JChannel getjChannel() {
        return jChannel;
    }

    public JConfig getjConfig() {
        return jConfig;
    }

    public void setjConfig(JConfig jConfig) {
        this.jConfig = jConfig;
    }

    public Set<Notification> getNotifications() {
        return notifications;
    }

    public void setNotifications(Set<Notification> notifications) {
        this.notifications = notifications;
    }
}
