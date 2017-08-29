package com.jd.o2o.vipcart.service.common;

import com.jd.o2o.vipcart.domain.constant.SequenceKeyEnum;
import com.jd.o2o.vipcart.service.base.busi.SpyBasicService;
import com.jd.o2o.vipcart.service.common.project.ProjectCommonService;
import com.jd.o2o.vipcart.service.common.project.ProjectContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ContextConfiguration({"classpath:spring-config-test.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class CommonServiceImplTest {

    @Resource
    private ProjectCommonService projectCommonServiceImpl;
    @Resource
    private SpyBasicService spyBasicServiceImpl;


    @Test
    public void testNextSequence() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        long startTime = System.currentTimeMillis();
        final CountDownLatch countDownLatch = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            executorService.submit(new Callable<Object>() {
                @Override
                public Object call() throws Exception {
//                    Thread.sleep(5000);
                    for (int j = 0; j < 1000; j++) {
                        System.err.println(SequenceKeyEnum.ACTIVITY_CODE.getCode() + "=" +
                                projectCommonServiceImpl.nextSequence(SequenceKeyEnum.ACTIVITY_CODE.getCode()));
//                        Thread.sleep(10);
                    }
                    countDownLatch.countDown();
                    return null;
                }
            });
        }
        countDownLatch.await();
        System.err.println("耗时：" + (System.currentTimeMillis() - startTime) + "ms");
    }

    @Test
    public void testTableColumns(){
        System.err.println(spyBasicServiceImpl.getColumnNameByTableName("activity_base_info"));
    }

}
