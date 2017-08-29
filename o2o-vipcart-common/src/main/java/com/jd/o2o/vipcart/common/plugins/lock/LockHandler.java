package com.jd.o2o.vipcart.common.plugins.lock;

import com.jd.o2o.vipcart.common.plugins.lock.domain.Locked;

import java.util.List;


public interface LockHandler<K> {
    /**
     * 不需等待，获取锁成功 返回true， 否则返回false
     * @author liuhuiqing
     * @date 2015-11-1 下午4:51:45
     * @param key
     * @return boolean
     */
	public boolean tryLock(K key);

	/**
	 * 锁在给定的等待时间内空闲，则获取锁成功 返回true， 否则返回false
	* @author liuhuiqing
	* @date 2015-11-1 下午4:51:45
	* @param key
	* @param millWaitTime
	* @return boolean
	 */
	public boolean tryLock(K key, long millWaitTime);

    /**
     * 锁在给定的等待时间内空闲，则获取锁成功，同时指定锁的持续时间 返回true， 否则返回false
     * @author liuhuiqing
     * @date 2015-11-1 下午4:51:45
     * @param key
     * @param millWaitTime 单位：毫秒
     * @param millLockTime 锁持续（超时）定时间
     * @return boolean
     */
    public boolean tryLock(K key, long millWaitTime, long millLockTime);

	/**
	 * 如果锁空闲立即返回   获取失败 一直等待
	* @author liuhuiqing
	* @date 2015-11-1 下午4:51:37
	* @param key void
	 */
	public void lock(K key);


	/**
	 * 批量获取锁  如果全部获取   立即返回true, 部分获取失败 返回false
	* @author liuhuiqing
	* @date 2015-11-1 下午4:52:44
	* @param keyList
	* @return boolean
	 */
	public boolean tryLock(List<K> keyList);
	
	/**
	 * 锁在给定的等待时间内空闲，则获取锁成功 返回true， 否则返回false
	* @author liuhuiqing
	* @date 2015-11-1 下午4:53:15
	* @param keyList
	* @param millWaitTime 单位：毫秒
	* @return boolean
	 */
	public boolean tryLock(List<K> keyList, long millWaitTime);

    /**
     * 锁在给定的等待时间内空闲，则获取锁成功 返回true， 否则返回false
     * @author liuhuiqing
     * @date 2015-11-1 下午4:53:15
     * @param keyList
     * @param millWaitTime 单位：毫秒
     * @param millLockTime 锁持续（超时）定时间 单位：毫秒
     * @return boolean
     */
    public boolean tryLock(List<K> keyList, long millWaitTime, long millLockTime);

    /**
     * 批量获得并设置个性化锁
     * @param waitLockedList
     * @param millWaitTime 锁在给定的等待时间内空闲，则获取锁成功
     * @return
     */
    public boolean lockByCondition(List<Locked> waitLockedList, Long millWaitTime);
	
	/**
	 * 释放锁
	* @author liuhuiqing
	* @date 2015-11-1 下午4:52:08
	* @param key void
	 */
	public void unLock(K key);

	/**
	 * 批量释放锁
	* @author liuhuiqing
	* @date 2015-11-1 下午4:53:39
	* @param keyList void
	 */
	public void unLock(List<K> keyList);

    /**
     * 批量释放锁
     * @param lockedList
     * @return
     */
    public void unLockByCondition(List<Locked> lockedList);
}
