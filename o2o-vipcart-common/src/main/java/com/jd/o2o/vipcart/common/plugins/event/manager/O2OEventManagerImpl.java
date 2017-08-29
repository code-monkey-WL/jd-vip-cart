package com.jd.o2o.vipcart.common.plugins.event.manager;

import com.jd.o2o.vipcart.common.plugins.event.O2OEvent;
import com.jd.o2o.vipcart.common.plugins.event.common.RetryTypeEnum;
import com.jd.o2o.vipcart.common.plugins.event.handle.EventCallBack;
import com.jd.o2o.vipcart.common.plugins.event.handle.O2OEventHandle;
import com.jd.o2o.vipcart.common.plugins.event.model.EventResult;
import com.jd.o2o.vipcart.common.plugins.event.strategy.RetryStrategy;
import com.jd.o2o.vipcart.common.plugins.event.timingwheel.ExpirationListener;
import com.jd.o2o.vipcart.common.plugins.event.timingwheel.TimingWheel;
import com.jd.o2o.vipcart.common.plugins.thread.ThreadPoolFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * 事件处理 Class Name: O2OEventManagerImpl.java
 * 
 * @author liuhuiqing DateTime 2014年7月25日 下午1:33:34
 * @version 1.0
 */
public class O2OEventManagerImpl implements O2OEventManager {
	private final static Logger logger = LoggerFactory
			.getLogger(O2OEventManagerImpl.class);

	// 无边界链表队列
	private static ConcurrentLinkedQueue<O2OEvent> eventQueue = new ConcurrentLinkedQueue<O2OEvent>();
	// 注册事件处理
	private Map<String, O2OEventHandle> handleMap = new HashMap<String, O2OEventHandle>();// 业务处理
	private Map<String, RetryStrategy> retryStrategyMap = new HashMap<String, RetryStrategy>();// 重试机制处理
	// 开启队列监听
	private final EventMonitor eventMonitor = new EventMonitor();
	// 队列监听等待时间
	private long waitTime;
	// 线程池管理
	private ThreadPoolTaskExecutor threadPoolTaskExecutor;
	//时间轮盘
	TimingWheel<O2OEvent> timingWheel = new TimingWheel<O2OEvent>(50, 512, TimeUnit.MILLISECONDS,999999);

	/**
	 * 监听启动
	 */
	public void init() {
        if(threadPoolTaskExecutor == null){
            threadPoolTaskExecutor = ThreadPoolFactory.getInstance().getThreadPoolTaskExecutor("o2oEventManager");
        }
		eventMonitor.start();
		logger.info("o2o事件监听已启动！" + registerEventManager());
		timingWheel.addExpirationListener(new ExpirationListener<O2OEvent>() {
			@Override
			public void expired(O2OEvent event) {
				eventQueue.add(event);
			}

		});
		timingWheel.start();
	}

	/**
	 * 添加新的事件到队列中
	 */
	@Override
	public void addEvent(O2OEvent event) {
		eventQueue.add(event);
	}

	/**
	 *
	 * Class Name: O2OEventManagerImpl.java
	 *
	 * @author liuhuiqing DateTime 2014-7-25 上午9:49:56
	 * @version 1.0
	 */
	class EventMonitor implements Runnable {

		public void start() {

			new Thread(this).start();
		}

		public void run() {

			while (true) {
				try {
					CountDownLatch latch = new CountDownLatch(1);
					latch.await(waitTime, TimeUnit.MILLISECONDS);
					O2OEvent event = eventQueue.poll();// 线程安全
					if (event == null) {
						continue;
					}
					threadPoolTaskExecutor.getThreadPoolExecutor().submit(
							new EventWorker(event));// 提交任务执行
				} catch (Throwable e) {
					try {
						logger.error("任务处理出现异常：", e);
					} catch (Throwable ex) {
					}
				}
			}
		}
	}

	/**
	 * 消息处理消费者 Class Name: O2OEventManagerImpl.java
	 *
	 * @author liuhuiqing DateTime 2014-7-25 上午9:56:52
	 * @version 1.0
	 */
	class EventWorker implements Runnable {

		private O2OEvent event;

		public EventWorker(O2OEvent event) {
			this.event = event;
		}

		public void run() {
			String eventCode = event.getEventCode();
			O2OEventHandle eventHandle = handleMap.get(eventCode);
			if (eventHandle == null) {
				logger.warn("未找到任务对应的事件处理器！任务没有正常执行.{}", event);
				return;
			}
			EventResult eventResult = eventHandle.handleEvent(event);// 事件处理
			EventCallBack eventCallBack = eventResult.getEventCallBack();
			if (!eventResult.isRet()) {
				int tryCount = event.getTryCount();
				if (tryCount > 0) {
					long expiredTime = getExpiredTime(event);
					event.setTryCount(--tryCount);
					timingWheel.add(event, expiredTime, TimeUnit.SECONDS);
				} else {
					if(eventCallBack != null){
						Object obj = eventCallBack.callBackFail(event);
						logger.info("invoke EventCallBack.callBackFail service result={},event={}",obj,event);
					}else{
						logger.warn("event has no EventCallBack service...{}",event);
					}
				}

			}else{
				if(eventCallBack != null){
					Object obj = eventCallBack.callBackSuccess(event);
					logger.info("invoke EventCallBack.callBackSuccess service result={},event={}",obj,event);
				}
			}
		}
	}

	private long getExpiredTime(O2OEvent event){
		long expiredTime = event.getTryFixed();
		int tryStrategy = event.getTryStrategy();
		RetryStrategy rs =retryStrategyMap.get(tryStrategy);
		if(rs == null){
			rs = retryStrategyMap.get(String.valueOf(RetryTypeEnum.idOf(tryStrategy).getCode()));
		}
		if(rs != null){
			expiredTime = rs.expiredTime(event.getTryCount(), event.getTryFixed());
		}
		expiredTime = expiredTime<0?0:expiredTime;
		return expiredTime;
	}

	public Map<String, O2OEventHandle> getHandleMap() {
		return handleMap;
	}

	public void setHandleMap(Map<String, O2OEventHandle> handleMap) {
		this.handleMap = handleMap;
	}

	public Map<String, RetryStrategy> getRetryStrategyMap() {
		return retryStrategyMap;
	}

	public void setRetryStrategyMap(Map<String, RetryStrategy> retryStrategyMap) {
		this.retryStrategyMap = retryStrategyMap;
	}

	public ThreadPoolTaskExecutor getThreadPoolTaskExecutor() {
		return threadPoolTaskExecutor;
	}

	public void setThreadPoolTaskExecutor(
			ThreadPoolTaskExecutor threadPoolTaskExecutor) {
		this.threadPoolTaskExecutor = threadPoolTaskExecutor;
	}

	public long getWaitTime() {
		return waitTime;
	}

	public void setWaitTime(long waitTime) {
		this.waitTime = waitTime;
	}

	/**
	 *  类加载时，打印查看事件处理类的注册信息
	 *  @return
	 *  @author liuhuiqing  DateTime 2014-8-20 下午6:12:20
	 */
	private String registerEventManager() {
		StringBuilder sb = new StringBuilder();
		if (handleMap != null && !handleMap.isEmpty()) {
			for (Map.Entry<String, O2OEventHandle> entry : handleMap.entrySet()) {
				sb.append(entry.getKey()).append(":").append(entry.getValue())
						.append("|");
			}
		}
		return sb.toString();
	}
}
