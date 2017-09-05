package com.jd.o2o.vipcart.service.busi.spider.worker;

import com.jd.o2o.vipcart.common.plugins.log.track.LoggerTrackFactory;
import com.jd.o2o.vipcart.common.plugins.spider.domain.ScanRuleInput;
import com.jd.o2o.vipcart.common.plugins.spider.domain.SpiderInput;
import com.jd.o2o.vipcart.common.plugins.spider.domain.rule.ItemRule;
import com.jd.o2o.vipcart.common.plugins.spider.parse.HtmlSpider;
import com.jd.o2o.vipcart.common.plugins.spider.parse.Spider;
import com.jd.o2o.vipcart.common.plugins.thread.ThreadPoolFactory;
import com.jd.o2o.vipcart.common.service.BaseService;
import com.jd.o2o.vipcart.common.utils.BeanHelper;
import com.jd.o2o.vipcart.common.utils.json.JsonUtils;
import com.jd.o2o.vipcart.domain.constant.SpiderStateEnum;
import com.jd.o2o.vipcart.domain.entity.SpiderConfigEntity;
import com.jd.o2o.vipcart.service.base.SpiderConfigService;
import com.jd.o2o.vipcart.service.busi.spider.SpiderWorker;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by liuhuiqing on 2017/9/5.
 */
@Service
public class AutoSpiderWorker implements SpiderWorker, ApplicationContextAware {
    private static final Logger LOGGER = LoggerTrackFactory.getLogger(AutoSpiderWorker.class);
    @Resource
    private SpiderConfigService spiderConfigServiceImpl;
    private ThreadPoolFactory threadPoolFactory = ThreadPoolFactory.getInstance();
    private Spider spider = new HtmlSpider();
    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public void spider() {
        SpiderConfigEntity query = new SpiderConfigEntity();
        query.setState(SpiderStateEnum.START.getCode());
        List<SpiderConfigEntity> spiderConfigEntityList = spiderConfigServiceImpl.findList(query);
        if (CollectionUtils.isEmpty(spiderConfigEntityList)) {
            LOGGER.info("没有要爬取的任务记录！");
            return;
        }
        ThreadPoolTaskScheduler poolTaskScheduler = threadPoolFactory.getThreadPoolTaskScheduler("spider-worker");
        for (SpiderConfigEntity spiderConfigEntity : spiderConfigEntityList) {
            int spiderNum = spiderConfigEntity.getSpiderNum();
            if (spiderNum >= spiderConfigEntity.getDeepNum()) {
                continue;
            }
            Date lastSpiderTime = spiderConfigEntity.getLastSpiderTime();
            if (lastSpiderTime == null) {
                lastSpiderTime = new Date();
            }
            CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(spiderConfigEntity.getScheduledCron());
            Date nextDate = cronSequenceGenerator.next(lastSpiderTime);
            poolTaskScheduler.schedule(new Runnable() {
                @Override
                public void run() {
                    int deepNum = spiderConfigEntity.getDeepNum();
                    for (int i = spiderNum; i <= deepNum; i++) {
                        SpiderInput spiderInput = BeanHelper.copyTo(spiderConfigEntity, SpiderInput.class);
                        spiderInput.setUrl(String.format(spiderConfigEntity.getUrl(), i));
                        ScanRuleInput scanRuleInput = new ScanRuleInput();
                        scanRuleInput.setBaseUrl(spiderConfigEntity.getBaseUrl());
                        scanRuleInput.setScanExpressions(new String[]{spiderConfigEntity.getScanExpressions()});
                        scanRuleInput.setContent(spiderConfigEntity.getContent());
                        scanRuleInput.setItemRuleList(JsonUtils.fromJsonArray(spiderConfigEntity.getItemRules(), ItemRule.class));
                        List<Map<String, String>> listMap = spider.analyse(spiderInput);
                        String tableName = spiderConfigEntity.getOutTableName();
                        String targetOut = "";
                        if (StringUtils.isNotEmpty(tableName) && CollectionUtils.isNotEmpty(listMap)) {
                            targetOut = JsonUtils.toJson(listMap);
                            BaseService baseService = (BaseService) applicationContext.getBean(tableName + "ServiceImpl");
                            List objectList = new ArrayList();
                            for (Map<String, String> map : listMap) {
                                try {
                                    Object bean = BeanHelper.mapToModel(map,
                                            Class.forName("com.jd.o2o.vipcart.domain.entity." + tableName + "Entity"));
                                    objectList.add(bean);
                                } catch (ClassNotFoundException e) {
                                    LOGGER.error("实体类型转换异常！", e);
                                }
                            }
                            baseService.saveAll(objectList);
                        }
                        SpiderConfigEntity updateEntity = new SpiderConfigEntity();
                        updateEntity.setId(spiderConfigEntity.getId());
                        int maxLength = 7168;
                        updateEntity.setTargetOut(targetOut.length() > maxLength ? targetOut.substring(0, maxLength) : targetOut);
                        updateEntity.setSpiderNum(spiderNum + 1);
                        updateEntity.setLastSpiderTime(new Date());
                        spiderConfigServiceImpl.update(updateEntity);
                    }

                }
            }, nextDate);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator("0/30 * * * * *");
        for (int i = 0; i < 10; i++) {
            System.err.println(cronSequenceGenerator.next(new Date()));
            Thread.sleep(6000);
        }
    }

}
