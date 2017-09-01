package com.jd.o2o.vipcart.common.plugins.spider.parse;

import com.jd.o2o.vipcart.common.plugins.log.track.LoggerTrackFactory;
import com.jd.o2o.vipcart.common.plugins.spider.domain.ScanRuleInput;
import com.jd.o2o.vipcart.common.plugins.spider.domain.constant.ItemSourceEnum;
import com.jd.o2o.vipcart.common.plugins.spider.domain.rule.AttrItemRule;
import com.jd.o2o.vipcart.common.plugins.spider.domain.rule.BaseItemRule;
import com.jd.o2o.vipcart.common.plugins.spider.domain.rule.ItemRule;
import com.jd.o2o.vipcart.common.utils.json.JsonUtils;
import org.apache.commons.collections.CollectionUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Jsoup文本解析器
 * Created by liuhuiqing on 2017/8/31.
 */
public class JsoupParser implements Parser {
    private static final Logger LOGGER = LoggerTrackFactory.getLogger(JsoupParser.class);

    @Override
    public List<Map<String,String>> parse(ScanRuleInput scanRuleInput) {
        List<Map<String,String>> resultList = new ArrayList<>();
        //获取html标签中的内容
        Document document = Jsoup.parse(scanRuleInput.getContent());
        List<BaseItemRule> baseItemRuleList = scanRuleInput.getItemRuleList();
        String[] scanExpressions = scanRuleInput.getScanExpressions();
        if (CollectionUtils.isEmpty(baseItemRuleList)) {
            LOGGER.warn("itemRuleList为空！param={}", JsonUtils.toJson(scanRuleInput));
            return resultList;
        }
        document.setBaseUri(scanRuleInput.getBaseUrl());
        Elements elements = selectElements(scanExpressions, document);
        if (elements == null) {
            LOGGER.warn("没有找对对应元素！param={}", JsonUtils.toJson(scanRuleInput));
            return resultList;
        }
        for (Element element : elements) {
            element.setBaseUri(scanRuleInput.getBaseUrl());
            Map<String, String> resultMap = new HashMap<String, String>();
            for (BaseItemRule baseItemRule : baseItemRuleList) {
                String[] itemExpressions = baseItemRule.getItemExpressions();
                if (itemExpressions == null || itemExpressions.length < 1) {
                    resultMap.put(baseItemRule.getAliasName(), buildTargetItem(baseItemRule, element));
                    continue;
                }
                Elements subElements = selectElements(itemExpressions, element);
                if (CollectionUtils.isEmpty(subElements)) {
                    continue;
                }
                List<String> subList = new ArrayList<>();
                for (Element subElement : subElements) {
                    subList.add(buildTargetItem(baseItemRule, subElement));
                }
                resultMap.put(baseItemRule.getAliasName(), JsonUtils.toJson(subList));
            }
            resultList.add(resultMap);
        }
        return resultList;
    }

    /**
     * 构建目标对象属性值
     *
     * @param baseItemRule
     * @param element
     * @return
     */
    private String buildTargetItem(BaseItemRule baseItemRule, Element element) {
        if(baseItemRule instanceof ItemRule){
            ItemRule itemRule = (ItemRule) baseItemRule;
            ItemSourceEnum itemSourceEnum = ItemSourceEnum.idOf(itemRule.getItemSource());
            if (itemSourceEnum == ItemSourceEnum.TEXT) {
                return element.text();
            } else if (itemSourceEnum == ItemSourceEnum.IN_HTML) {
                return element.html();
            } else if (itemSourceEnum == ItemSourceEnum.OUT_HTML) {
                return element.outerHtml();
            } else {
                LOGGER.warn("itemSource=[{}]不支持的取值来源赋值！param={}", itemRule.getItemSource(), JsonUtils.toJson(itemRule));
            }
        }else if(baseItemRule instanceof AttrItemRule){
            AttrItemRule attrItemRule = (AttrItemRule) baseItemRule;
            return element.attr(attrItemRule.getAttrName());
        }else {
            LOGGER.warn("不支持的扫描规则定义！param={}", JsonUtils.toJson(baseItemRule));
        }
        return null;
    }

    /**
     * 过滤元素
     *
     * @param scanExpressions
     * @param element
     * @return
     */
    private Elements selectElements(String[] scanExpressions, Element element) {
        Elements elements = null;
        if (scanExpressions == null || scanExpressions.length < 1) {
            return elements;
        }
        int i =0;
        for (String scanExpression : scanExpressions) {
            if(i < 1){
                elements = element.select(scanExpression);
            }else{
                elements = elements.select(scanExpression);
            }
            if(elements.isEmpty()){
                return elements;
            }
            i++;
        }
        return elements;
    }
}
