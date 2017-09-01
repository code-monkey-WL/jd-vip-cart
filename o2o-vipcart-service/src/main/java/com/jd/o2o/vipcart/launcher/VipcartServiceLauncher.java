package com.jd.o2o.vipcart.launcher;

import com.jd.o2o.vipcart.common.domain.PageBean;
import com.jd.o2o.vipcart.common.plugins.log.track.LoggerTrackFactory;
import com.jd.o2o.vipcart.common.utils.json.JsonUtils;
import com.jd.o2o.vipcart.service.busi.spider.SpiderService;
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
        LOGGER.warn("****************************vipcart服务已启动****************************");
        SpiderService spiderService = (SpiderService) context.getBean("miaoShaJDSpiderWorker");
        System.err.println(JsonUtils.toJson(spiderService.spider(new PageBean())));
        System.err.println(JsonUtils.toJson(spiderService.spider(new PageBean(2,20))));
        // 启动本地服务，然后hold住本地服务
        synchronized (VipcartServiceLauncher.class) {
            while (true) {
                try {
                    VipcartServiceLauncher.class.wait();
                } catch (InterruptedException e) {
                    LOGGER.error("vipcart服务异常终止:" + e.getMessage(), e);
                }
            }
        }
    }
}
