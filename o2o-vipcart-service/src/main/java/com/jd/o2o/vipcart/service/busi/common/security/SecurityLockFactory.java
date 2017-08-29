package com.jd.o2o.vipcart.service.busi.common.security;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库访问
 * Created by liuhuiqing on 2017/8/10.
 */
public class SecurityLockFactory {
    private static final Map<String, SecurityLockQueue> securityLockQueueMap = new HashMap<String, SecurityLockQueue>();

    private SecurityLockFactory() {
    }

    public static synchronized SecurityLockQueue getInstance(String name) {
        SecurityLockQueue securityLockQueue = securityLockQueueMap.get(name);
        if (securityLockQueue == null) {
            securityLockQueue = new SecurityLockQueue(name,1024);
            securityLockQueueMap.put(name, securityLockQueue);
        }
        return securityLockQueue;
    }
}
