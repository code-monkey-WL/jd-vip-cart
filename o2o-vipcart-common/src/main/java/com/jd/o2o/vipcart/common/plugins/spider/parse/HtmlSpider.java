package com.jd.o2o.vipcart.common.plugins.spider.parse;

import com.jd.o2o.vipcart.common.domain.BaseBean;
import com.jd.o2o.vipcart.common.domain.response.ServiceResponse;
import com.jd.o2o.vipcart.common.plugins.httpcliend.HttpClientExecutor;
import com.jd.o2o.vipcart.common.plugins.log.track.LoggerTrackFactory;
import com.jd.o2o.vipcart.common.plugins.spider.domain.*;
import com.jd.o2o.vipcart.common.utils.json.JsonUtils;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Html内容解析
 * Created by liuhuiqing on 2017/8/31.
 */
public class HtmlSpider implements Spider {
    private static final Logger LOGGER = LoggerTrackFactory.getLogger(JsoupParser.class);
    private Parser parser = new JsoupParser();
    HttpClientExecutor httpClientExecutor = HttpClientExecutor.newInstance();
    @Override
    public <T> List<T> analyse(SpiderInput<T> spiderInput) {
        ServiceResponse<String> response = httpClientExecutor.executeGet(spiderInput.getUrl());
        if(!response.isSuccess()){
            LOGGER.error("获取url资源出现异常！param={},result={}", JsonUtils.toJson(spiderInput),JsonUtils.toJson(response));
            return null;
        }
        ScanRuleInput<T> scanRuleInput = spiderInput.getScanRuleInput();
        scanRuleInput.setContent(response.getResult());
        return parser.parse(scanRuleInput);
    }

    public static void main(String[] args){
        String url = "https://www.jd.com/";
        SpiderInput<Category> spiderInput = new SpiderInput();
        ScanRuleInput<Category> scanRuleInput = new ScanRuleInput();
        List<ScanItemRule> scanItemRuleList = new ArrayList<>();
        spiderInput.setUrl(url);
        spiderInput.setScanRuleInput(scanRuleInput);
        scanRuleInput.setBaseUrl(url);
        scanRuleInput.setScanExpressions(new String[]{"li.cate_menu_item a"});
        scanRuleInput.setScanRuleType(ScanRuleTypeEnum.JSOUP.getCode());
        scanRuleInput.setTargetClass(Category.class);
        scanRuleInput.setScanItemRuleList(scanItemRuleList);

        ScanItemRule scanItemRule = new ScanItemRule();
        scanItemRule.setAliasName("name");
        scanItemRule.setItemName("");
        scanItemRule.setItemExpressions(new String[]{});
        scanItemRule.setItemSource(ItemSourceEnum.TEXT.getCode());
        scanItemRuleList.add(scanItemRule);

        scanItemRule = new ScanItemRule();
        scanItemRule.setAliasName("link");
        scanItemRule.setItemName("href");
        scanItemRule.setItemExpressions(new String[]{});
        scanItemRule.setItemSource(ItemSourceEnum.ATTR.getCode());
        scanItemRuleList.add(scanItemRule);
        System.err.println(JsonUtils.toJson(new HtmlSpider().analyse(spiderInput)));
    }
}
