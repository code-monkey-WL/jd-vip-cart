package com.jd.o2o.vipcart.common.plugins.jgoups;

import org.jgroups.Address;
import org.jgroups.View;

/**
 * 成员变更处理（禁止使用能够引起通道阻塞的处理逻辑，例如发送消息等，以免引起死锁）
 * Created by liuhuiqing on 2016/5/30.
 */
public interface Notification {
    void viewChange(View view, java.util.List<Address> mbrs_joined, java.util.List<Address> mbrs_left);
}
