package com.jd.o2o.vipcart.common.plugins.spider.parse;

import com.jd.o2o.vipcart.common.domain.response.ServiceResponse;
import com.jd.o2o.vipcart.common.plugins.httpcliend.HttpClientExecutor;
import com.jd.o2o.vipcart.common.plugins.log.track.LoggerTrackFactory;
import com.jd.o2o.vipcart.common.plugins.spider.domain.ScanRuleInput;
import com.jd.o2o.vipcart.common.plugins.spider.domain.SpiderInput;
import com.jd.o2o.vipcart.common.utils.BeanHelper;
import com.jd.o2o.vipcart.common.utils.json.JsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Html内容解析
 * Created by liuhuiqing on 2017/8/31.
 */
public class HtmlSpider implements Spider {
    private static final Logger LOGGER = LoggerTrackFactory.getLogger(JsoupParser.class);
    private Parser parser = new JsoupParser();
    HttpClientExecutor httpClientExecutor = HttpClientExecutor.newInstance();
    @Override
    public List analyse(SpiderInput spiderInput) {
        ServiceResponse<String> response = httpClientExecutor.executeGet(spiderInput.getUrl());
        if(!response.isSuccess()){
            LOGGER.error("获取给定url资源出现异常！param={},result={}", JsonUtils.toJson(spiderInput),JsonUtils.toJson(response));
            return null;
        }
        ScanRuleInput scanRuleInput = spiderInput.getScanRuleInput();
        scanRuleInput.setContent(response.getResult());
        List<Map<String,String>> resultMapList = parser.parse(scanRuleInput);
        if(CollectionUtils.isEmpty(resultMapList)){
            return Collections.EMPTY_LIST;
        }
        Class clazz = spiderInput.getTargetClass();
        if(clazz == null){
            return resultMapList;
        }
        List resultList = new ArrayList<>();
        for(Map<String,String> map:resultMapList){
            resultList.add(BeanHelper.mapToModel(map, spiderInput.getTargetClass()));
        }
        return resultList;
    }
}
