package com.jd.o2o.vipcart.launcher;

import com.jd.o2o.vipcart.common.plugins.log.track.LoggerTrackFactory;
import org.slf4j.Logger;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 服务加载器
 */
public class VipcartServiceLauncher {
    private static final Logger LOGGER = LoggerTrackFactory.getLogger(VipcartServiceLauncher.class);

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"spring-config.xml"});
        LOGGER.warn("****************************优惠券开放（coupon-open-service）服务已启动****************************");
        // 启动本地服务，然后hold住本地服务
        synchronized (VipcartServiceLauncher.class) {
            while (true) {
                try {
                    VipcartServiceLauncher.class.wait();
                } catch (InterruptedException e) {
                    LOGGER.error("优惠券开放（coupon-open-service）服务异常终止:" + e.getMessage(), e);
                }
            }
        }
    }
}
