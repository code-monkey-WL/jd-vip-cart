package com.jd.o2o.vipcart.common.plugins.lock.impl;

import com.jd.o2o.vipcart.common.domain.exception.BaseMsgException;
import com.jd.o2o.vipcart.common.plugins.lock.LockHandler;
import com.jd.o2o.vipcart.common.plugins.lock.domain.Locked;
import com.jd.o2o.vipcart.common.plugins.serializer.DefaultObjectSerializer;
import com.jd.o2o.vipcart.common.plugins.serializer.Serializer;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * 缓存模板实现类
 * Created by liuhuiqing on 2016/3/22.
 */
public abstract class AbstractLockHandler implements LockHandler<Object> {
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractLockHandler.class);
    public static final Long DEFAULT_EXPIRETIME = 10 * 60L;// 默认十分钟过期
    protected Long expireTime = DEFAULT_EXPIRETIME; // 单位为秒
    protected Serializer<Object> serializer = new DefaultObjectSerializer();
    public static final int TICK_SLEEP = 100; // 获取锁休眠一次时间

    @Override
    public boolean tryLock(Object key) {
        return tryLock(key, 0);
    }

    @Override
    public boolean tryLock(Object key, long millWaitTime) {
        return tryLock(key, millWaitTime, expireTime);
    }

    @Override
    public boolean tryLock(Object key, long millWaitTime, long millLockTime) {
        try {
            long milli = System.currentTimeMillis();
            do {
                if (locked(buildLocked(key, millLockTime))) {
                    return true;
                }
                if (millWaitTime <= 0) {
                    break;
                }
                Thread.sleep(TICK_SLEEP);
            } while ((System.currentTimeMillis() - milli) < millWaitTime);
            return false;
        } catch (Exception e) {
            LOGGER.error("get lock[" + String.valueOf(key) + "] exception :" + e.getMessage(), e);
        }
        return false;
    }

    @Override
    public void lock(Object key) {
        try {
            do {
                if (locked(buildLocked(key, expireTime * 1000))) {
                    return;
                }
                Thread.sleep(TICK_SLEEP);
            } while (true);
        } catch (Exception e) {
            LOGGER.error("get lock[" + String.valueOf(key) + "] exception :" + e.getMessage(), e);
        }
    }

    @Override
    public boolean tryLock(List<Object> keyList) {
        return tryLock(keyList, 0);
    }

    @Override
    public boolean tryLock(final List<Object> keyList, long millWaitTime) {
        return tryLock(keyList, millWaitTime, expireTime);
    }

    @Override
    public boolean tryLock(List<Object> keyList, long millWaitTime, long millLockTime) {
        if (CollectionUtils.isEmpty(keyList)) {
            return false;
        }
        List<Locked> waitLockedList = new ArrayList<Locked>(keyList.size());
        for (Object key : keyList) {
            waitLockedList.add(buildLocked(key, millLockTime));
        }
        return lockByCondition(waitLockedList, millWaitTime);
    }


    @Override
    public boolean lockByCondition(List<Locked> waitLockedList, Long millWaitTime) {
        if (CollectionUtils.isEmpty(waitLockedList)) {
            return false;
        }
        long milli = System.currentTimeMillis();
        int size = waitLockedList.size();
        List<Locked> allKeyList = new ArrayList<Locked>(waitLockedList);
        List<Locked> lockedList = new ArrayList<Locked>(0);
        try {
            do {
                Iterator<Locked> iterator = allKeyList.iterator();
                while (iterator.hasNext()) {
                    Locked locked = iterator.next();
                    if (locked(locked)) {
                        lockedList.add(locked);
                        iterator.remove();
                    }
                }
                if (millWaitTime <= 0 || lockedList.size() == size) {
                    break;
                }
                Thread.sleep(TICK_SLEEP);
            } while ((System.currentTimeMillis() - milli) < millWaitTime);
        } catch (Exception e) {
            LOGGER.error("get tryLock list exception :" + e.getMessage(), e);
        }
        if (lockedList.size() == size) {
            return true;
        }
        unLockByCondition(lockedList);
        return false;
    }

    @Override
    public void unLock(Object key) {
        try {
            unLocked(buildLocked(key));
        } catch (Exception e) {
            LOGGER.error("release lock[" + String.valueOf(key) + "] exception :" + e.getMessage(), e);
        }
    }

    @Override
    public void unLock(List<Object> keyList) {
        if (CollectionUtils.isEmpty(keyList)) {
            return;
        }
        for (Object key : keyList) {
            try {
                unLocked(buildLocked(key));
            } catch (Exception e) {
                LOGGER.error("release lock[" + key + "] exception :" + e.getMessage(), e);
            }
        }
    }

    @Override
    public void unLockByCondition(List<Locked> lockedList) {
        if (CollectionUtils.isEmpty(lockedList)) {
            return;
        }
        for (Locked locked : lockedList) {
            try {
                unLocked(locked);
            } catch (Exception e) {
                LOGGER.error("release lock[" + locked.getKey() + "] exception :" + e.getMessage(), e);
            }
        }
    }

    public byte[] serializeKeyValue(Object key) {
        if (key == null) {
            throw new BaseMsgException("non null key required");
        }
        if (key instanceof byte[]) {
            return (byte[]) key;
        }
        return serializer.serialize(key);
    }

    public Object deserializeKeyValue(byte[] value) {
        if (value == null) {
            throw new BaseMsgException("non null value required");
        }
        return serializer.deserialize(value);
    }

    protected abstract boolean locked(Locked locked);

    protected abstract boolean unLocked(Locked locked);

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public void setSerializer(Serializer<Object> serializer) {
        this.serializer = serializer;
    }

    /**
     * 构建锁对象
     *
     * @param key
     * @param millLockTime
     * @return
     */
    private Locked buildLocked(Object key, Long millLockTime) {
        Locked locked = new Locked();
        locked.setKey(key);
        locked.setValue(1);
        locked.setMillLockTime(millLockTime);
        return locked;
    }

    /**
     * 构建锁对象
     *
     * @param key
     * @return
     */
    private Locked buildLocked(Object key) {
        return buildLocked(key, null);
    }

}
