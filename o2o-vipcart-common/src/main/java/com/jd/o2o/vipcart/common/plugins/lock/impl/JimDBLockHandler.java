//package com.jd.o2o.vipcart.common.plugins.lock.impl;
//
//import com.jd.o2o.vipcart.common.plugins.lock.domain.Locked;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.concurrent.TimeUnit;
//
///**
// * @author liuhuiqing
// * @ClassName: JimDBLockHandler
// * @Description: TODO
// * @date 2015-11-12 下午4:23:57
// */
//public class JimDBLockHandler extends AbstractLockHandler {
//    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLockHandler.class);
//    private Cluster client;
//
//    private JimDBLockHandler() {
//        super();
//    }
//
//    private JimDBLockHandler(Long expireTime) {
//        super();
//        if (expireTime == null) {
//            this.expireTime = DEFAULT_EXPIRETIME;
//        } else {
//            expireTime = expireTime.longValue() > Integer.MAX_VALUE ? Integer.MAX_VALUE : expireTime;
//            this.expireTime = expireTime;
//        }
//    }
//
//
//    @Override
//    protected boolean locked(Locked locked) {
//        Object key = locked.getKey();
//        Object value = locked.getValue();
//        long millLockTime = locked.getMillLockTime();
//        int expireTime = (int) (millLockTime < 1000 ? this.expireTime : millLockTime / 1000);
//        StringBuilder builder = new StringBuilder();
//        builder.append("try lock key: " + key).append("\n");
//        byte[] keyBytes = serializeKeyValue(key);
////        boolean isAbsent = client.setNX(keyBytes, serializeKeyValue(value==null?1:value));
//        boolean isAbsent = client.set(keyBytes,serializeKeyValue(value==null?1:value),expireTime, TimeUnit.SECONDS,false);
//        if (isAbsent) {
////            client.expire(keyBytes, expireTime, TimeUnit.SECONDS);
//            if (LOGGER.isDebugEnabled()) {
//                builder.append("get lock, key: ").append(key).append(" , expire in ").append(expireTime).append(" seconds.\n");
//                LOGGER.debug(builder.toString());
//            }
//            return true;
//        } else { // 存在锁
//            if (LOGGER.isDebugEnabled()) {
//                Object desc = deserializeKeyValue(client.get(keyBytes));
//                builder.append("key: ").append(key).append(" locked by another business：").append(desc).append("\n");
//                LOGGER.debug(builder.toString());
//            }
//        }
//        return false;
//    }
//
//    @Override
//    protected boolean unLocked(Locked locked) {
//        Boolean activeRelease = locked.getActiveRelease();
//        if(activeRelease == null || activeRelease){
//            Object key = locked.getKey();
//            if (LOGGER.isDebugEnabled()) {
//                LOGGER.debug("start release lock, key :" + key);
//            }
//            client.del(serializeKeyValue(key));
//            return true;
//        }
//        return false;
//    }
//
//    public Cluster getClient() {
//        return client;
//    }
//
//    public void setClient(Cluster client) {
//        this.client = client;
//    }
//}