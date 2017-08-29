package com.jd.o2o.vipcart.common.plugins.event.handle;

import com.jd.o2o.vipcart.common.plugins.event.O2OEvent;
import com.jd.o2o.vipcart.common.plugins.event.model.EventResult;
import com.jd.o2o.vipcart.common.plugins.event.model.TestModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * 测试用，事件处理
 *  Class Name: TestEventHandleImpl.java   
 *  @author liuhuiqing DateTime 2014-9-25 下午4:08:57    
 *  @version 1.0
 */
@Service
public class TestEventHandleImpl implements O2OEventHandle {
   private final static Logger logger = LoggerFactory.getLogger(TestEventHandleImpl.class);
    @Override
    public EventResult handleEvent(O2OEvent event) {
    	EventResult eventResult = buildEventResult();
        try {
            if(event == null || !(event.getData() instanceof TestModel)){
                logger.warn("event is not instanceof TestModel [{}]",event);
                eventResult.setRet(false);
                eventResult.setRetMsg("event is not instanceof TestModel");
                return eventResult;
            }
            TestModel message = (TestModel) event.getData();
            logger.info("get data:{}",message);
            eventResult.setRet(true);
        }catch(Exception e){
            eventResult.setRet(false);
            eventResult.setRetMsg(e.getMessage());
            logger.error("TestEventHandleImpl has error!",e);
        }
        return eventResult;
    }
    /**
     *  注册事件处理回调逻辑
     *  @return
     *  @author liuhuiqing  DateTime 2014-9-25 下午7:04:50
     */
    private EventResult buildEventResult(){
    	EventResult eventResult = new EventResult();
        eventResult.setEventCallBack(new EventCallBack(){
			@Override
			public Object callBackFail(O2OEvent event) {
				logger.warn("invoke callBackFail service...{}",event);
				return null;
			}

			@Override
			public Object callBackSuccess(O2OEvent event) {
				logger.info("invoke callBackSuccess service...{}",event);
				return null;
			}
        });
        return eventResult;
    }
    
}
