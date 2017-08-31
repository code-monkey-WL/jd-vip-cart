package com.jd.o2o.vipcart.common.plugins.spider.parse;

import com.jd.o2o.vipcart.common.plugins.log.track.LoggerTrackFactory;
import com.jd.o2o.vipcart.common.plugins.spider.domain.ItemSourceEnum;
import com.jd.o2o.vipcart.common.plugins.spider.domain.ScanItemRule;
import com.jd.o2o.vipcart.common.plugins.spider.domain.ScanRuleInput;
import com.jd.o2o.vipcart.common.utils.BeanHelper;
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
    public <T> List<T> parse(ScanRuleInput<T> scanRuleInput) {
        List<T> resultList = new ArrayList<>();
        //获取html标签中的内容
        Document document = Jsoup.parse(scanRuleInput.getContent());
        List<ScanItemRule> scanItemRuleList = scanRuleInput.getScanItemRuleList();
        String[] scanExpressions = scanRuleInput.getScanExpressions();
        if (CollectionUtils.isEmpty(scanItemRuleList)) {
            LOGGER.warn("scanItemRuleList为空！param={}", JsonUtils.toJson(scanRuleInput));
            return resultList;
        }
        document.setBaseUri(scanRuleInput.getBaseUrl());
        Elements elements = selectElements(scanExpressions, document);
        if (elements == null) {
            LOGGER.warn("没有找对对应元素！param={}", JsonUtils.toJson(scanRuleInput));
            return resultList;
        }
        Map<String, String> resultMap = new HashMap<String, String>();
        for (Element element : elements) {
            for (ScanItemRule scanItemRule : scanItemRuleList) {
                String[] itemExpressions = scanItemRule.getItemExpressions();
                if (itemExpressions == null || itemExpressions.length < 1) {
                    resultMap.put(scanItemRule.getAliasName(), buildTargetItem(scanItemRule, element));
                    continue;
                }
                Elements subElements = selectElements(itemExpressions, element);
                if (CollectionUtils.isEmpty(subElements)) {
                    continue;
                }
                List<String> subList = new ArrayList<>();
                for (Element subElement : subElements) {
                    subList.add(buildTargetItem(scanItemRule, subElement));
                }
                resultMap.put(scanItemRule.getAliasName(), JsonUtils.toJson(subList));
            }
            resultList.add(BeanHelper.mapToModel(resultMap, scanRuleInput.getTargetClass()));
            resultMap.clear();
        }
        return resultList;
    }

    /**
     * 构建目标对象属性值
     *
     * @param scanItemRule
     * @param element
     * @return
     */
    private String buildTargetItem(ScanItemRule scanItemRule, Element element) {
        ItemSourceEnum itemSourceEnum = ItemSourceEnum.idOf(scanItemRule.getItemSource());
        if (itemSourceEnum == ItemSourceEnum.ATTR) {
            return element.attr(scanItemRule.getItemName());
        } else if (itemSourceEnum == ItemSourceEnum.TEXT) {
            return element.text();
        } else if (itemSourceEnum == ItemSourceEnum.IN_HTML) {
            return element.html();
        } else if (itemSourceEnum == ItemSourceEnum.OUT_HTML) {
            return element.outerHtml();
        } else {
            LOGGER.warn("itemSource=[{}]不支持的取值来源赋值！param={}", scanItemRule.getItemSource(), JsonUtils.toJson(scanItemRule));
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
        for (String scanExpression : scanExpressions) {
            elements = element.select(scanExpression);
        }
        return elements;
    }
}
