package com.jd.o2o.vipcart.service.busi.spider.worker;

import com.jd.o2o.vipcart.common.plugins.log.track.LoggerTrackFactory;
import com.jd.o2o.vipcart.domain.constant.SpiderStateEnum;
import com.jd.o2o.vipcart.domain.entity.SpiderConfigEntity;
import com.jd.o2o.vipcart.service.base.SpiderConfigService;
import com.jd.o2o.vipcart.service.busi.spider.SpiderWorker;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by liuhuiqing on 2017/9/5.
 */
@Service
public class AutoSpiderWorker implements SpiderWorker {
    private static final Logger LOGGER = LoggerTrackFactory.getLogger(AutoSpiderWorker.class);
    @Resource
    private SpiderConfigService spiderConfigServiceImpl;
    @Override
    public void spider() {
        SpiderConfigEntity query = new SpiderConfigEntity();
        query.setState(SpiderStateEnum.START.getCode());
        List<SpiderConfigEntity> spiderConfigEntityList = spiderConfigServiceImpl.findList(query);
        if(CollectionUtils.isEmpty(spiderConfigEntityList)){
            LOGGER.info("没有要爬取的任务记录！");
            return;
        }
        for (SpiderConfigEntity spiderConfigEntity:spiderConfigEntityList){
            Date lastSpiderTime = spiderConfigEntity.getLastSpiderTime();
            if(lastSpiderTime == null){
                lastSpiderTime = new Date();
            }
            CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(spiderConfigEntity.getScheduledCron());
            Date nextDate = cronSequenceGenerator.next(lastSpiderTime);
            
        }
    }

    /**
     * 任务间隔
     * @param cron
     * @return
     */
    private long intervalTime(String cron){
        Date currentTime = new Date();
        CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(cron);
        Date baseDate = cronSequenceGenerator.next(currentTime);
        Date nextDate = cronSequenceGenerator.next(baseDate);
        return (nextDate.getTime() - baseDate.getTime())/1000;
    }
}
