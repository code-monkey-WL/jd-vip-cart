package com.jd.o2o.vipcart.common.plugins.thread;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.util.ErrorHandler;

import java.util.Map;
import java.util.concurrent.*;

/**
 * 线程池工厂类
 * Created by liuhuiqing on 2015/8/27.
 */
public class ThreadPoolFactory implements DisposableBean {
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPoolFactory.class);
    private static final Map<String,ThreadPoolTaskExecutor> threadPoolMap = new ConcurrentHashMap<String,ThreadPoolTaskExecutor>();
    private static final Map<String,ThreadPoolTaskScheduler> threadPoolSchedulerMap = new ConcurrentHashMap<String,ThreadPoolTaskScheduler>();
    private static final ThreadPoolFactory threadPoolFactory = new ThreadPoolFactory();

    /**
     * 保证单例
     */
    private ThreadPoolFactory() {
    }

    /**
     * 获得线程池工厂实例
     * @return
     */
    public static ThreadPoolFactory getInstance(){
        return threadPoolFactory;
    }

    /**
     * 获得线程池
     * @param groupName 任务组名
     * @return
     */
    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor(String groupName){
        return getThreadPoolTaskExecutor(groupName,0);
    }

    /**
     * 获得线程池
     * @param groupName 任务组名
     * @param queueCapacity 队列大小
     * @return
     */
    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor(String groupName,int queueCapacity){
        final Map<String,ThreadPoolTaskExecutor> threadPoolMap = threadPoolFactory.getThreadPoolMap();
        ThreadPoolTaskExecutor threadPoolTaskExecutor = threadPoolMap.get(groupName);
        if(threadPoolTaskExecutor != null){
            return threadPoolTaskExecutor;
        }
        synchronized (ThreadPoolFactory.class){
            if(threadPoolTaskExecutor == null){
                queueCapacity = queueCapacity < 1?99999:queueCapacity;
                int availableProcessors = Runtime.getRuntime().availableProcessors();
                threadPoolTaskExecutor = buildThreadPoolTaskExecutor(groupName,availableProcessors*2,
                        availableProcessors * 4,false,30,queueCapacity,false,0,new ThreadPoolExecutor.CallerRunsPolicy());
                threadPoolMap.put(groupName,threadPoolTaskExecutor);
            }
        }
        return threadPoolTaskExecutor;
    }

    /**
     * 获得线程池
     * @param groupName 任务组名
     * @return
     */
    public ThreadPoolTaskScheduler getThreadPoolTaskScheduler(String groupName){
        final Map<String,ThreadPoolTaskScheduler> threadPoolSchedulerMap = threadPoolFactory.getThreadPoolSchedulerMap();
        ThreadPoolTaskScheduler threadPoolTaskScheduler = threadPoolSchedulerMap.get(groupName);
        if(threadPoolTaskScheduler != null){
            return threadPoolTaskScheduler;
        }
        synchronized (ThreadPoolFactory.class){
            if(threadPoolTaskScheduler == null){
                int availableProcessors = Runtime.getRuntime().availableProcessors();
                threadPoolTaskScheduler = buildThreadPoolTaskScheduler(groupName, availableProcessors * 2, Thread.NORM_PRIORITY,
                        false, 0,null, new ThreadPoolExecutor.CallerRunsPolicy());
                threadPoolSchedulerMap.put(groupName,threadPoolTaskScheduler);
            }
        }
        return threadPoolTaskScheduler;
    }

    /**
     * 构建默认线程池对象
     * @param groupName 任务组名
     * @param corePoolSize 核心线程数
     * @param maxPoolSize 最大线程数
     * @param allowCoreThreadTimeOut 核心线程是否允许回收
     * @param keepAliveSeconds 空闲线程存活时间
     * @param queueCapacity 任务队列最大长度
     * @param waitForJobsToCompleteOnShutdown 应用关闭时是否要等到队列中的任务执行完毕
     * @param awaitTerminationSeconds 应用关闭时，等待任务队列中的任务执行完毕的时间
     * @param rejectedExecutionHandler 任务队列超过最大阀值时，对于的任务的处理策略
     * @return
     */
    public ThreadPoolTaskExecutor getThreadPoolTaskExecutor(String groupName,int corePoolSize,int maxPoolSize,
                                                            boolean allowCoreThreadTimeOut, int keepAliveSeconds,
                                                            int queueCapacity,boolean waitForJobsToCompleteOnShutdown,
                                                            int awaitTerminationSeconds,RejectedExecutionHandler rejectedExecutionHandler){
        final Map<String,ThreadPoolTaskExecutor> threadPoolMap = threadPoolFactory.getThreadPoolMap();
        ThreadPoolTaskExecutor threadPoolTaskExecutor = threadPoolMap.get(groupName);
        if(threadPoolTaskExecutor != null){
            return threadPoolTaskExecutor;
        }
        // 此处需锁定class对象
        synchronized (ThreadPoolFactory.class){
            if(threadPoolTaskExecutor == null){
                threadPoolTaskExecutor = buildThreadPoolTaskExecutor(groupName,corePoolSize,maxPoolSize,
                        allowCoreThreadTimeOut,keepAliveSeconds,queueCapacity,waitForJobsToCompleteOnShutdown,
                        awaitTerminationSeconds,rejectedExecutionHandler);
                threadPoolMap.put(groupName,threadPoolTaskExecutor);
            }
        }
        return threadPoolTaskExecutor;
    }

    /**
     * 构建默认线程池对象
     * @param groupName 任务组名
     * @param corePoolSize 核心线程数
     * @param maxPoolSize 最大线程数
     * @param allowCoreThreadTimeOut 核心线程是否允许回收
     * @param keepAliveSeconds 空闲线程存活时间
     * @param queueCapacity 任务队列最大长度
     * @param waitForJobsToCompleteOnShutdown 应用关闭时是否要等到队列中的任务执行完毕
     * @param awaitTerminationSeconds 应用关闭时，等待任务队列中的任务执行完毕的时间
     * @param rejectedExecutionHandler 任务队列超过最大阀值时，对于的任务的处理策略
     * @return
     */
    private ThreadPoolTaskExecutor buildThreadPoolTaskExecutor(String groupName,int corePoolSize,int maxPoolSize,
                                                               boolean allowCoreThreadTimeOut, int keepAliveSeconds,
                                                               int queueCapacity,boolean waitForJobsToCompleteOnShutdown,
                                                               int awaitTerminationSeconds,RejectedExecutionHandler rejectedExecutionHandler){

        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.setCorePoolSize(corePoolSize);
        threadPoolTaskExecutor.setKeepAliveSeconds(keepAliveSeconds);
        threadPoolTaskExecutor.setMaxPoolSize(maxPoolSize);
        threadPoolTaskExecutor.setQueueCapacity(queueCapacity);
        if(rejectedExecutionHandler == null){
            rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
        }
        threadPoolTaskExecutor.setRejectedExecutionHandler(rejectedExecutionHandler);
        threadPoolTaskExecutor.setThreadGroupName(groupName);
        threadPoolTaskExecutor.setBeanName(groupName);
        threadPoolTaskExecutor.setAwaitTerminationSeconds(awaitTerminationSeconds);
        threadPoolTaskExecutor.setAllowCoreThreadTimeOut(allowCoreThreadTimeOut);
        threadPoolTaskExecutor.setWaitForTasksToCompleteOnShutdown(waitForJobsToCompleteOnShutdown);
        threadPoolTaskExecutor.afterPropertiesSet();// 初始化线程池
        return threadPoolTaskExecutor;
    }

    /**
     * 构建默认线程池对象
     * @param groupName 任务组名
     * @param poolSize 线程数
     * @param threadPriority 线程优先级
     * @param waitForJobsToCompleteOnShutdown 应用关闭时是否要等到队列中的任务执行完毕
     * @param awaitTerminationSeconds 应用关闭时，等待任务队列中的任务执行完毕的时间
     * @param errorHandler 异常处理逻辑
     * @param rejectedExecutionHandler 任务队列超过最大阀值时，对于的任务的处理策略
     * @return
     */
    private ThreadPoolTaskScheduler buildThreadPoolTaskScheduler(String groupName,int poolSize,int threadPriority,
                                                              boolean waitForJobsToCompleteOnShutdown,
                                                              int awaitTerminationSeconds,ErrorHandler errorHandler,
                                                              RejectedExecutionHandler rejectedExecutionHandler){
        ThreadPoolTaskScheduler threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
        threadPoolTaskScheduler.setWaitForTasksToCompleteOnShutdown(waitForJobsToCompleteOnShutdown);
        threadPoolTaskScheduler.setAwaitTerminationSeconds(awaitTerminationSeconds);
        threadPoolTaskScheduler.setBeanName(groupName);
        if(rejectedExecutionHandler == null){
            rejectedExecutionHandler = new ThreadPoolExecutor.CallerRunsPolicy();
        }
        threadPoolTaskScheduler.setRejectedExecutionHandler(rejectedExecutionHandler);
        if(errorHandler == null){
            errorHandler = new ErrorHandler() {
                @Override
                public void handleError(Throwable t) {
                    if (LOGGER.isErrorEnabled()) {
                        LOGGER.error("Unexpected error occurred in scheduled task.", t);
                    }
                }
            };
        }
        threadPoolTaskScheduler.setErrorHandler(errorHandler);
        threadPoolTaskScheduler.setPoolSize(poolSize);
        threadPoolTaskScheduler.setThreadGroupName(groupName);
        threadPoolTaskScheduler.setThreadPriority(threadPriority);
        threadPoolTaskScheduler.afterPropertiesSet();// 初始化线程池
        return threadPoolTaskScheduler;
    }

    public Map<String, ThreadPoolTaskExecutor> getThreadPoolMap() {
        return threadPoolMap;
    }

    public Map<String, ThreadPoolTaskScheduler> getThreadPoolSchedulerMap() {
        return threadPoolSchedulerMap;
    }

    @Override
    public void destroy() throws Exception {
        final Map<String,ThreadPoolTaskExecutor> threadPoolMap = threadPoolFactory.getThreadPoolMap();
        final Map<String,ThreadPoolTaskScheduler> threadPoolSchedulerMap = threadPoolFactory.getThreadPoolSchedulerMap();
        if(threadPoolMap!=null && !threadPoolMap.isEmpty()){
            for(Map.Entry<String,ThreadPoolTaskExecutor> entry:threadPoolMap.entrySet()){
                entry.getValue().destroy();// 处理线程池队列中剩下的任务
            }
        }
        if(threadPoolSchedulerMap!=null && !threadPoolSchedulerMap.isEmpty()){
            for(Map.Entry<String,ThreadPoolTaskScheduler> entry:threadPoolSchedulerMap.entrySet()){
                entry.getValue().destroy();// 处理线程池队列中剩下的任务
            }
        }
    }
}
