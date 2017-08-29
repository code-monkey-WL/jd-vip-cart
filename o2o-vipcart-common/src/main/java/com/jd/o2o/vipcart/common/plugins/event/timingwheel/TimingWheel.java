package com.jd.o2o.vipcart.common.plugins.event.timingwheel;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *  时间轮盘算法：
 *  Class Name: TimingWheel.java   
 *  @author liuhuiqing DateTime 2014-8-27 下午3:27:50    
 *  @version 1.0 
 *  @param <E>
 */
public class TimingWheel<E> implements Serializable{
	private static final long serialVersionUID = -1704936976912879290L;
	
	private final long tickDuration;// 一个tick的持续时间
	private final int ticksPerWheel;// 一轮的tick数，取值为2的幂
	private final long roundDuration;// 一轮的总时间
	private volatile int currentTickIndex = 0;// 轮盘指针
	// 事件监听集合
	private final CopyOnWriteArrayList<ExpirationListener<E>> expirationListeners = new CopyOnWriteArrayList<ExpirationListener<E>>();
	final Set<HashedWheelTimeout<E>>[] wheel;// 时间轮盘对象
	final ReusableIterator<HashedWheelTimeout<E>>[] iterators;// 轮盘集合元素指针
	final int mask;// 取值为ticksPerWheel-1，用于轮盘闭环处理
	private final int maxTimerCapacity;// 轮盘存放元素的最大容量
	private final AtomicInteger size = new AtomicInteger(0);// 轮盘当前容量
	private final AtomicBoolean shutdown = new AtomicBoolean(false);// 轮盘解体标志
	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private Thread workerThread;// 轮盘启动线程

	/**
	 * Construct a timing wheel.
	 * 
	 * @param tickDuration
	 *            一个tick的持续时间
	 * @param ticksPerWheel
	 *            一轮的tick数
	 * @param timeUnit
	 *            时间单位
	 * @param maxTimerCapacity
	 *            时间轮最大元素个数
	 */
	public TimingWheel(long tickDuration, int ticksPerWheel, TimeUnit timeUnit,
			int maxTimerCapacity) {
		if (timeUnit == null) {
			throw new NullPointerException("unit");
		}
		if (tickDuration <= 0) {
			throw new IllegalArgumentException(
					"tickDuration must be greater than 0: " + tickDuration);
		}
		if (ticksPerWheel <= 0) {
			throw new IllegalArgumentException(
					"ticksPerWheel must be greater than 0: " + ticksPerWheel);
		}
		if (maxTimerCapacity <= 0) {
			throw new IllegalArgumentException(
					"maxTimerCapacity must be greater than 0: "
							+ maxTimerCapacity);
		}
		this.ticksPerWheel = ticksPerWheel = normalizeTicksPerWheel(ticksPerWheel);
		this.wheel = createWheel(this.ticksPerWheel);
		this.iterators = createIterators(this.wheel);
		this.mask = ticksPerWheel - 1;
		this.tickDuration = tickDuration = timeUnit.toMillis(tickDuration);
		if (tickDuration == Long.MAX_VALUE
				|| tickDuration >= Long.MAX_VALUE / ticksPerWheel) {
			throw new IllegalArgumentException("tickDuration is too long: "
					+ tickDuration + ' ' + timeUnit);
		}

		this.roundDuration = tickDuration * ticksPerWheel;
		this.maxTimerCapacity = maxTimerCapacity;
		workerThread = new Thread(new TickWorker(), "Timing-Wheel");
	}

	/**
	 * 轮盘生成
	 * 
	 * @param ticksPerWheel
	 * @return
	 * @author liuhuiqing DateTime 2014-8-27 下午2:31:32
	 */
	private Set<HashedWheelTimeout<E>>[] createWheel(int ticksPerWheel) {
		if (ticksPerWheel <= 0) {
			throw new IllegalArgumentException(
					"ticksPerWheel must be greater than 0: " + ticksPerWheel);
		}
		if (ticksPerWheel > 1073741824) {
			throw new IllegalArgumentException(
					"ticksPerWheel may not be greater than 2^30: "
							+ ticksPerWheel);
		}

		Set<HashedWheelTimeout<E>>[] wheel = new Set[ticksPerWheel];
		for (int i = 0; i < wheel.length; i++) {
			wheel[i] = new MapBackedSet<HashedWheelTimeout<E>>(
					new ConcurrentIdentityHashMap<HashedWheelTimeout<E>, Boolean>(
							16, 0.95f, 4));
		}
		return wheel;
	}

	/**
	 * 轮盘tick数目校正，必须是 2的幂
	 * 
	 * @param ticksPerWheel
	 * @return
	 * @author liuhuiqing DateTime 2014-8-27 下午2:32:09
	 */
	private int normalizeTicksPerWheel(int ticksPerWheel) {
		int normalizedTicksPerWheel = 1;
		while (normalizedTicksPerWheel < ticksPerWheel) {
			normalizedTicksPerWheel <<= 1;
		}
		return normalizedTicksPerWheel;
	}

	/**
	 * 轮盘元素指针，用于遍历每个轮盘里的元素集合
	 * 
	 * @param wheel
	 * @return
	 * @author liuhuiqing DateTime 2014-8-27 下午2:32:43
	 */
	private ReusableIterator<HashedWheelTimeout<E>>[] createIterators(
			Set<HashedWheelTimeout<E>>[] wheel) {
		ReusableIterator<HashedWheelTimeout<E>>[] iterators = new ReusableIterator[wheel.length];
		for (int i = 0; i < wheel.length; i++) {
			iterators[i] = (ReusableIterator<HashedWheelTimeout<E>>) wheel[i]
					.iterator();
		}
		return iterators;
	}

	public synchronized void start() {
		if (shutdown.get()) {
            return;
//			throw new IllegalStateException("Cannot be started once stopped");
		}

		if (!workerThread.isAlive()) {
			workerThread.start();
		}
	}

	public synchronized Set<HashedWheelTimeout<E>> stop() {
		if (!this.shutdown.compareAndSet(false, true)) {
			return Collections.emptySet();
		}

		boolean interrupted = false;
		while (this.workerThread.isAlive()) {
			this.workerThread.interrupt();
			try {
				this.workerThread.join(100);
			} catch (InterruptedException e) {
				interrupted = true;
			}
		}

		if (interrupted) {
			Thread.currentThread().interrupt();
		}

		Set<HashedWheelTimeout<E>> unprocessedTimeouts = new HashSet<HashedWheelTimeout<E>>();
		for (Set<HashedWheelTimeout<E>> bucket : this.wheel) {
			unprocessedTimeouts.addAll(bucket);
			bucket.clear();
		}

		return Collections.unmodifiableSet(unprocessedTimeouts);
	}

	/**
	 *  添加元素监听事件
	 *  @param listener
	 *  @return
	 *  @author liuhuiqing  DateTime 2014-8-27 下午3:11:06
	 */
	public TimingWheel<E> addExpirationListener(ExpirationListener<E> listener) {
		expirationListeners.add(listener);
		return this;
	}

	/**
	 *  移除元素监听
	 *  @param listener
	 *  @return
	 *  @author liuhuiqing  DateTime 2014-8-27 下午3:11:27
	 */
	public TimingWheel<E> removeExpirationListener(ExpirationListener<E> listener) {
		expirationListeners.remove(listener);
		return this;
	}

	/**
	 * 添加元素
	 * 
	 * @param data
	 *            目标元素对象
	 * @param delay
	 *            过期时间，即调用对应监听事件处理的延时时长
	 * @param timeUnit
	 *            时间单位
	 * @return
	 * @author liuhuiqing DateTime 2014-8-27 下午2:34:41
	 */
	public HashedWheelTimeout<E> add(E data, long delay, TimeUnit timeUnit) {
		final long currentTime = System.currentTimeMillis();
		checkAdd(data, timeUnit);

		delay = timeUnit.toMillis(delay);
		if (delay < this.tickDuration) {
			delay = this.tickDuration;
		}
		// Prepare the required parameters to create the timeout object.
		final long lastRoundDelay = delay % this.roundDuration;
		final long lastTickDelay = delay % this.tickDuration;
		final long relativeIndex = lastRoundDelay / this.tickDuration
				+ (lastTickDelay != 0 ? 1 : 0);
		final long deadline = currentTime + delay;
		final long remainingRounds = delay / this.roundDuration
				- (lastRoundDelay == 0 ? 1 : 0);
		this.lock.readLock().lock();
		HashedWheelTimeout<E> timeout;
		try {
			timeout = new HashedWheelTimeout<E>(data, deadline,
					(int) (this.currentTickIndex + relativeIndex & this.mask),
					remainingRounds);

			this.wheel[timeout.stopIndex].add(timeout);
		} finally {
			this.lock.readLock().unlock();
		}
		this.size.incrementAndGet();

		return timeout;
	}

	/**
	 * 移除元素
	 * 
	 * @param timeout
	 * @return
	 * @author liuhuiqing DateTime 2014-8-27 下午2:36:24
	 */
	public boolean remove(HashedWheelTimeout<E> timeout) {
		if (TimingWheel.this.wheel[timeout.stopIndex].remove(timeout)) {
			TimingWheel.this.size.decrementAndGet();
			return true;
		}
		return false;
	}

	/**
	 * 添加元素前的有效性校验
	 * 
	 * @param data
	 * @param timeUnit
	 * @author liuhuiqing DateTime 2014-8-27 下午2:36:56
	 */
	private void checkAdd(E data, TimeUnit timeUnit) {
		if (data == null) {
			throw new NullPointerException("data");
		}
		if (timeUnit == null) {
			throw new NullPointerException("unit");
		}

		if (this.size.get() >= this.maxTimerCapacity) {
			throw new RejectedExecutionException("Timer size " + this.size
					+ " is great than maxTimerCapacity "
					+ this.maxTimerCapacity);
		}
		if (!this.workerThread.isAlive()) {
			this.start();
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	/**
	 * 轮盘监听处理任务线程 Class Name: TimingWheel.java
	 * 
	 * @author liuhuiqing DateTime 2014-8-27 下午2:37:25
	 * @version 1.0
	 */
	private class TickWorker implements Runnable {

		private long startTime;
		private long tick;

		@Override
		public void run() {
			List<HashedWheelTimeout<E>> expiredTimeouts = new ArrayList<HashedWheelTimeout<E>>();
			startTime = System.currentTimeMillis();
			tick = 1;
			while (!shutdown.get()) {
				this.waitForNextTick();
				this.fetchExpiredTimeouts(expiredTimeouts);
				this.notifyExpiredTimeouts(expiredTimeouts);
			}
		}

		/**
		 * 等待一个tick时长
		 * 
		 * @author liuhuiqing DateTime 2014-8-27 下午2:39:09
		 */
		private void waitForNextTick() {
			for (;;) {
				final long currentTime = System.currentTimeMillis();
				final long sleepTime = TimingWheel.this.tickDuration
						* this.tick - (currentTime - this.startTime);

				if (sleepTime <= 0) {
					break;
				}

				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					if (TimingWheel.this.shutdown.get()) {
						return;
					}
				}
			}

			// Reset the tick if overflow is expected.
			if (TimingWheel.this.tickDuration * this.tick > Long.MAX_VALUE
					- TimingWheel.this.tickDuration) {
				this.startTime = System.currentTimeMillis();
				this.tick = 1;
			} else {
				// Increase the tick if overflow is not likely to happen.
				this.tick++;
			}
		}

		/**
		 * 获得当前tick到期的元素集合
		 * 
		 * @param expiredTimeouts
		 * @author liuhuiqing DateTime 2014-8-27 下午2:39:58
		 */
		private void fetchExpiredTimeouts(
				List<HashedWheelTimeout<E>> expiredTimeouts) {

			// Find the expired timeouts and decrease the round counter
			// if necessary. Note that we don't send the notification
			// immediately to make sure the listeners are called without
			// an exclusive lock.
			TimingWheel.this.lock.writeLock().lock();
			try {
				int oldBucketHead = TimingWheel.this.currentTickIndex;
				TimingWheel.this.currentTickIndex = oldBucketHead + 1
						& TimingWheel.this.mask;
				ReusableIterator<HashedWheelTimeout<E>> i = TimingWheel.this.iterators[oldBucketHead];
				this.fetchExpiredTimeouts(expiredTimeouts, i);
			} finally {
				TimingWheel.this.lock.writeLock().unlock();
			}
		}

		private void fetchExpiredTimeouts(
				List<HashedWheelTimeout<E>> expiredTimeouts,
				ReusableIterator<HashedWheelTimeout<E>> i) {

			long currentDeadline = System.currentTimeMillis()
					+ TimingWheel.this.tickDuration;
			i.rewind();
			while (i.hasNext()) {
				HashedWheelTimeout<E> timeout = i.next();
				if (timeout.remainingRounds <= 0) {
					if (timeout.deadline < currentDeadline) {
						i.remove();
						expiredTimeouts.add(timeout);
					} else {
						// A rare case where a timeout is put for the next
						// round: just wait for the next round.
					}
				} else {
					timeout.remainingRounds--;
				}
			}
		}

		/**
		 * 元素事件处理
		 * 
		 * @param expiredTimeouts
		 * @author liuhuiqing DateTime 2014-8-27 下午2:41:11
		 */
		private void notifyExpiredTimeouts(
				List<HashedWheelTimeout<E>> expiredTimeouts) {
			// Notify the expired timeouts.
			for (int i = expiredTimeouts.size() - 1; i >= 0; i--) {
				for (ExpirationListener<E> listener : expirationListeners) {
					listener.expired(expiredTimeouts.get(i).getData());
				}
				TimingWheel.this.size.decrementAndGet();
			}
			// Clean up the temporary list.
			expiredTimeouts.clear();
		}
	}

	static class A {
		private String a;

		public A(String a) {
			this.a = a;
		}

		public String getA() {
			return a;
		}
	}

	public static void main(String[] args) throws Exception {
		final TimingWheel<A> tw = new TimingWheel<A>(1, 60, TimeUnit.SECONDS,
				1000).addExpirationListener(new ExpirationListener<A>() {

			@Override
			public void expired(A expiredObject) {
				System.out.println(expiredObject.getA());
			}

		});
		tw.start();
		for (int a = 0; a < 5; a++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					for (int i = 0; i < 200; i++) {
						HashedWheelTimeout<A> ht = tw.add(new A(Thread
								.currentThread().getName() + "#" + i), i,
								TimeUnit.SECONDS);
						if (i % 2 == 0) {
							tw.remove(ht);
						}
					}

				}

			}).start();
		}
		System.out.println(tw);
	}

}