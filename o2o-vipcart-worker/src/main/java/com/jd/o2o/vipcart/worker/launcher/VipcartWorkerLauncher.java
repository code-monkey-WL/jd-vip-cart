package com.jd.o2o.vipcart.worker.launcher;

import com.jd.o2o.vipcart.common.plugins.log.track.LoggerTrackFactory;
import org.slf4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * worker启动类
 */
public class VipcartWorkerLauncher {
    /**日志打印控件**/
    private static final Logger LOGGER = LoggerTrackFactory.getLogger(VipcartWorkerLauncher.class);

    /**
     * 入口函数
     * @param args
     */
    public static void main(String[] args) {
        String paths[] = {"spring-config.xml"};
        new ClassPathXmlApplicationContext(paths);
        LOGGER.info("优惠vipcarton-worker）分布式调度服务启动成功...");
        // 启动本地服务，然后hold住本地服务
        synchronized(VipcartWorkerLauncher.class) {
            while (true) {
                try {
                    VipcartWorkerLauncher.class.wait();
                } catch (InterruptedException e) {
                    LOGGER.error("VipcartWorkerLauncher分布式调度服务异常终止:" + e.getMessage(), e);
                }
            }
        }
    }
}
