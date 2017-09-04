package com.jd.o2o.vipcart.common.plugins.spider.demo;

import com.jd.o2o.vipcart.common.plugins.spider.domain.ScanRuleInput;
import com.jd.o2o.vipcart.common.plugins.spider.domain.SpiderInput;
import com.jd.o2o.vipcart.common.plugins.spider.domain.constant.ItemSourceEnum;
import com.jd.o2o.vipcart.common.plugins.spider.domain.rule.ItemRule;
import com.jd.o2o.vipcart.common.plugins.spider.parse.HtmlSpider;
import com.jd.o2o.vipcart.common.utils.json.JsonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liuhuiqing on 2017/9/1.
 */
public class TestSpider {

    public static void spiderJDCategory(){
        String url = "https://www.jd.com/";
        SpiderInput<Category> spiderInput = new SpiderInput();
        ScanRuleInput scanRuleInput = new ScanRuleInput();
        List<ItemRule> itemRuleList = new ArrayList<ItemRule>();
        spiderInput.setUrl(url);
        spiderInput.setScanRuleInput(scanRuleInput);
        spiderInput.setTargetClass(Category.class);
        scanRuleInput.setBaseUrl(url);
        scanRuleInput.setScanExpressions(new String[]{"li.cate_menu_item a"});
        scanRuleInput.setItemRuleList(itemRuleList);

        ItemRule itemRule = new ItemRule();
        itemRule.setAliasName("name");
        itemRule.setItemExpressions(new String[]{});
        itemRule.setItemSource(ItemSourceEnum.TEXT.getCode());
        itemRuleList.add(itemRule);

        itemRule = new ItemRule();
        itemRule.setAliasName("link");
        itemRule.setAttrName("href");
        itemRule.setItemExpressions(new String[]{});
        itemRuleList.add(itemRule);
        System.err.println(JsonUtils.toJson(new HtmlSpider().analyse(spiderInput)));
    }

    public static void main(String[] args) {
        spiderJDCategory();
    }
}
