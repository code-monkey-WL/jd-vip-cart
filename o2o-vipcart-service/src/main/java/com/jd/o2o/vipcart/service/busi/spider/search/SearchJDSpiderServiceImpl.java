package com.jd.o2o.vipcart.service.busi.spider.search;

import com.jd.o2o.vipcart.common.domain.PageBean;
import com.jd.o2o.vipcart.common.domain.response.ServiceResponse;
import com.jd.o2o.vipcart.common.plugins.httpcliend.HttpClientExecutor;
import com.jd.o2o.vipcart.common.plugins.log.track.LoggerTrackFactory;
import com.jd.o2o.vipcart.common.plugins.spider.demo.Category;
import com.jd.o2o.vipcart.common.plugins.spider.domain.ScanRuleInput;
import com.jd.o2o.vipcart.common.plugins.spider.domain.SpiderInput;
import com.jd.o2o.vipcart.common.plugins.spider.domain.constant.ItemSourceEnum;
import com.jd.o2o.vipcart.common.plugins.spider.domain.rule.AttrItemRule;
import com.jd.o2o.vipcart.common.plugins.spider.domain.rule.BaseItemRule;
import com.jd.o2o.vipcart.common.plugins.spider.domain.rule.ItemRule;
import com.jd.o2o.vipcart.common.plugins.spider.parse.HtmlSpider;
import com.jd.o2o.vipcart.common.utils.json.JsonUtils;
import com.jd.o2o.vipcart.domain.entity.GoodInfoEntity;
import com.jd.o2o.vipcart.domain.spider.SpiderParam;
import com.jd.o2o.vipcart.service.busi.spider.SpiderService;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhuiqing on 2017/9/4.
 */
public class SearchJDSpiderServiceImpl implements SpiderService {
    private static final Logger LOGGER = LoggerTrackFactory.getLogger(SearchJDSpiderServiceImpl.class);
    private static final String searchUrl = "https://search.jd.com/Search?keyword=%s&enc=utf-8&page=%s";
    private static HttpClientExecutor httpClientExecutor = HttpClientExecutor.newInstance();
    @Override
    public PageBean spider(SpiderParam spiderParam) {
        spiderJDCategory(spiderParam);
        return null;
    }

    public void spiderJDCategory(SpiderParam spiderParam){
        String baseUrl = "https://www.jd.com/";
        SpiderInput<GoodInfoEntity> spiderInput = new SpiderInput();
        ScanRuleInput scanRuleInput = new ScanRuleInput();
        List<BaseItemRule> scanItemRuleList = new ArrayList<BaseItemRule>();
        spiderInput.setUrl(String.format(searchUrl,"Mac",spiderParam.getPageNo()));
        spiderInput.setScanRuleInput(scanRuleInput);
//        spiderInput.setTargetClass(GoodInfoEntity.class);
        scanRuleInput.setBaseUrl(baseUrl);
        scanRuleInput.setScanExpressions(new String[]{"div.J-goods-list li"});
        scanRuleInput.setItemRuleList(scanItemRuleList);

        ItemRule itemRule = new ItemRule();
        itemRule.setAliasName("name");
        itemRule.setItemExpressions(new String[]{});
        itemRule.setItemSource(ItemSourceEnum.TEXT.getCode());
        scanItemRuleList.add(itemRule);

        AttrItemRule scanItemRule = new AttrItemRule();
        scanItemRule.setAliasName("outSkuCode");
        scanItemRule.setAttrName("data-sku");
        scanItemRule.setItemExpressions(new String[]{});
        scanItemRuleList.add(scanItemRule);
        System.err.println(JsonUtils.toJson(new HtmlSpider().analyse(spiderInput)));
    }

    public static void main(String[] args) {
        SpiderParam spiderParam = new SpiderParam();
        spiderParam.setInputParam("Mac");
        new SearchJDSpiderServiceImpl().spider(spiderParam);
    }
}
