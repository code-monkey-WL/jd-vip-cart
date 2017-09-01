package com.jd.o2o.vipcart.service.busi.worker;

import com.jd.o2o.vipcart.common.domain.enums.YNEnum;
import com.jd.o2o.vipcart.common.domain.response.ServiceResponse;
import com.jd.o2o.vipcart.common.plugins.httpcliend.HttpClientExecutor;
import com.jd.o2o.vipcart.common.plugins.log.track.LoggerTrackFactory;
import com.jd.o2o.vipcart.common.plugins.spider.domain.ScanRuleInput;
import com.jd.o2o.vipcart.common.plugins.spider.domain.SpiderInput;
import com.jd.o2o.vipcart.common.plugins.spider.domain.constant.ItemSourceEnum;
import com.jd.o2o.vipcart.common.plugins.spider.domain.rule.AttrItemRule;
import com.jd.o2o.vipcart.common.plugins.spider.domain.rule.BaseItemRule;
import com.jd.o2o.vipcart.common.plugins.spider.domain.rule.ItemRule;
import com.jd.o2o.vipcart.common.plugins.spider.parse.HtmlSpider;
import com.jd.o2o.vipcart.common.utils.NumUtils;
import com.jd.o2o.vipcart.common.utils.json.JsonUtils;
import com.jd.o2o.vipcart.domain.entity.GoodInfoEntity;
import com.jd.o2o.vipcart.domain.spider.miaoshao.JDMiaoSha;
import com.jd.o2o.vipcart.domain.spider.miaoshao.JDMiaoShaGoodInfo;
import com.jd.o2o.vipcart.service.base.GoodInfoService;
import com.jd.o2o.vipcart.service.busi.common.id.IDGenerator;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * jd:https://ai.jd.com/index_new?app=Seckill&action=pcMiaoShaAreaList&callback=pcMiaoShaAreaList&gid=30&_=1504258871484
 * Created by liuhuiqing on 2017/9/1.
 */
@Service
public class MiaoShaSpiderWorker implements Work {
    private static final Logger LOGGER = LoggerTrackFactory.getLogger(MiaoShaSpiderWorker.class);
    private static final String miaoshaUrl = "https://ai.jd.com/index_new?app=Seckill&action=pcMiaoShaAreaList&callback=pcMiaoShaAreaList&gid=30&_=1504258871484";
    @Resource
    private GoodInfoService goodInfoServiceImpl;
    private static HttpClientExecutor httpClientExecutor = HttpClientExecutor.newInstance();

    @Override
    public void doWorker() {
        LOGGER.info("MiaoShaSpiderWorker启动...");
        ServiceResponse<String> response = httpClientExecutor.executeGet(miaoshaUrl);
        String content = response.getResult();
        content = content.substring(content.indexOf("{"), content.lastIndexOf("}") + 1);
        JDMiaoSha jdMiaoSha = JsonUtils.fromJson(content, JDMiaoSha.class);
        List<GoodInfoEntity> goodInfoEntityList = buildGoodInfoEntityList(jdMiaoSha.getMiaoShaList());
        int num = goodInfoServiceImpl.saveAll(goodInfoEntityList);
        LOGGER.info("MiaoShaSpiderWorker结束...导入[{}]条", num);


    }

    private List<GoodInfoEntity> buildGoodInfoEntityList(List<JDMiaoShaGoodInfo> miaoShaList) {
        List<GoodInfoEntity> goodInfoEntityList = new ArrayList<>();
        if (CollectionUtils.isEmpty(miaoShaList)) {
            return goodInfoEntityList;
        }
        Date date = new Date();
        for (JDMiaoShaGoodInfo goodInfo : miaoShaList) {
            GoodInfoEntity goodInfoEntity = new GoodInfoEntity();
            goodInfoEntity.setSkuId(IDGenerator.getSkuId());
            goodInfoEntity.setOutSkuCode(goodInfo.getWareId());
            goodInfoEntity.setSkuCode(IDGenerator.getSkuCode());
            goodInfoEntity.setSkuName(goodInfo.getWname());
            goodInfoEntity.setSkuDesc(goodInfo.getTagText());
            goodInfoEntity.setSkuSummary(goodInfo.getOperateWord());
            goodInfoEntity.setSkuPrice(Double.valueOf(NumUtils.mul(goodInfo.getMiaoShaPrice(), 100)).longValue());
            goodInfoEntity.setOriginPrice(Double.valueOf(NumUtils.mul(goodInfo.getJdPrice(), 100)).longValue());
            goodInfoEntity.setPromotionSummary(StringUtils.isEmpty(goodInfo.getTagText())?goodInfo.getPromotionId():goodInfo.getTagText());
            goodInfoEntity.setSkuLink(StringUtils.isEmpty(goodInfo.getPcJumpUrl())?"https://miaosha.jd.com/":goodInfo.getPcJumpUrl());
            String img = goodInfo.getImageurl();
            if (!img.startsWith("http")) {
                img = "https:" + img;
            }
            goodInfoEntity.setSkuImg(img);
            goodInfoEntity.setFromSource(1);
            goodInfoEntity.setCategoryCode("0");
            goodInfoEntity.setFullCategoryCode("0");
            goodInfoEntity.setBrandCode("0");
            goodInfoEntity.setOrgCode("0");
            goodInfoEntity.setStockType(1);
            int stockNum = 0;
            if(StringUtils.isNotEmpty(goodInfo.getSoldRate())){
                stockNum = Integer.valueOf(goodInfo.getSoldRate());
            }
            goodInfoEntity.setStockNum(stockNum);
            goodInfoEntity.setCommentNum(0);
            goodInfoEntity.setGrabTime(date);
            goodInfoEntity.setSkuStatus(0);
            goodInfoEntity.setYn(YNEnum.Y.getCode());
            goodInfoEntity.setExt(JsonUtils.toJson(goodInfo));
            goodInfoEntity.setSort(0);
            goodInfoEntityList.add(goodInfoEntity);
        }
        return goodInfoEntityList;
    }

    public static void main(String[] args) {
    }


    public static void spiderJDMiaoSha() {
        String url = "https://miaosha.jd.com/";
        SpiderInput<GoodInfoEntity> spiderInput = new SpiderInput();
        ScanRuleInput scanRuleInput = new ScanRuleInput();
        List<BaseItemRule> scanItemRuleList = new ArrayList<BaseItemRule>();
        spiderInput.setUrl(url);
        spiderInput.setScanRuleInput(scanRuleInput);
        spiderInput.setTargetClass(GoodInfoEntity.class);
        scanRuleInput.setBaseUrl(url);
        scanRuleInput.setScanExpressions(new String[]{"li.seckill_mod_goods"});
        scanRuleInput.setItemRuleList(scanItemRuleList);

        ItemRule itemRule = new ItemRule();
        itemRule.setAliasName("skuName");
        itemRule.setItemExpressions(new String[]{"a"});
        itemRule.setItemSource(ItemSourceEnum.TEXT.getCode());
        scanItemRuleList.add(itemRule);

        itemRule = new ItemRule();
        itemRule.setAliasName("skuDesc");
        itemRule.setItemExpressions(new String[]{"span"});
        itemRule.setItemSource(ItemSourceEnum.TEXT.getCode());
        scanItemRuleList.add(itemRule);

        itemRule = new ItemRule();
        itemRule.setAliasName("skuPrice");
        itemRule.setItemExpressions(new String[]{".seckill_mod_goods_price"});
        itemRule.setItemSource(ItemSourceEnum.TEXT.getCode());
        scanItemRuleList.add(itemRule);

        itemRule = new ItemRule();
        itemRule.setAliasName("originPrice");
        itemRule.setItemExpressions(new String[]{".seckill_mod_goods_price_pre"});
        itemRule.setItemSource(ItemSourceEnum.TEXT.getCode());
        scanItemRuleList.add(itemRule);

        AttrItemRule scanItemRule = new AttrItemRule();
        scanItemRule.setAliasName("skuLink");
        scanItemRule.setAttrName("href");
        scanItemRule.setItemExpressions(new String[]{"a"});
        scanItemRuleList.add(scanItemRule);

        scanItemRule = new AttrItemRule();
        scanItemRule.setAliasName("skuImg");
        scanItemRule.setAttrName("src");
        scanItemRule.setItemExpressions(new String[]{".seckill_mod_goods_link_img"});
        scanItemRuleList.add(scanItemRule);

        itemRule = new ItemRule();
        itemRule.setAliasName("stockNum");
        itemRule.setItemExpressions(new String[]{".seckill_mod_goods_progress_txt"});
        itemRule.setItemSource(ItemSourceEnum.TEXT.getCode());
        scanItemRuleList.add(itemRule);

        System.err.println(JsonUtils.toJson(new HtmlSpider().analyse(spiderInput)));
    }

}
