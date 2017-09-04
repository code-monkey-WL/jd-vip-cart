package com.jd.o2o.vipcart.service.busi.spider.worker;

import com.jd.o2o.vipcart.common.domain.PageBean;
import com.jd.o2o.vipcart.common.domain.enums.YNEnum;
import com.jd.o2o.vipcart.common.domain.response.ServiceResponse;
import com.jd.o2o.vipcart.common.plugins.httpcliend.HttpClientExecutor;
import com.jd.o2o.vipcart.common.plugins.log.track.LoggerTrackFactory;
import com.jd.o2o.vipcart.common.plugins.spider.domain.ScanRuleInput;
import com.jd.o2o.vipcart.common.plugins.spider.domain.SpiderInput;
import com.jd.o2o.vipcart.common.plugins.spider.domain.constant.ItemSourceEnum;
import com.jd.o2o.vipcart.common.plugins.spider.domain.rule.ItemRule;
import com.jd.o2o.vipcart.common.plugins.spider.parse.HtmlSpider;
import com.jd.o2o.vipcart.common.utils.NumUtils;
import com.jd.o2o.vipcart.common.utils.json.JsonUtils;
import com.jd.o2o.vipcart.domain.entity.GoodInfoEntity;
import com.jd.o2o.vipcart.domain.spider.SpiderParam;
import com.jd.o2o.vipcart.domain.spider.miaoshao.MiaoJDSha;
import com.jd.o2o.vipcart.domain.spider.miaoshao.MiaoShaJDGoodInfo;
import com.jd.o2o.vipcart.service.base.GoodInfoService;
import com.jd.o2o.vipcart.service.busi.common.id.IDGenerator;
import com.jd.o2o.vipcart.service.busi.spider.SpiderService;
import com.jd.o2o.vipcart.service.busi.spider.SpiderWorker;
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
public class MiaoShaJDSpiderWorker implements SpiderWorker, SpiderService {
    private static final Logger LOGGER = LoggerTrackFactory.getLogger(MiaoShaJDSpiderWorker.class);
    private static final String miaoshaUrl = "https://ai.jd.com/index_new?app=Seckill&action=pcMiaoShaAreaList&callback=pcMiaoShaAreaList&gid=30&_=1504258871484";
    @Resource
    private GoodInfoService goodInfoServiceImpl;
    private static HttpClientExecutor httpClientExecutor = HttpClientExecutor.newInstance();

    @Override
    public void spider() {
        LOGGER.info("MiaoShaSpiderWorker启动...");
        List<GoodInfoEntity> goodInfoEntityList = buildGoodInfoEntityList();
        int num = goodInfoServiceImpl.saveAll(goodInfoEntityList);
        LOGGER.info("MiaoShaSpiderWorker结束...导入[{}]条", num);


    }


    @Override
    public PageBean spider(SpiderParam spiderParam) {
        List<GoodInfoEntity> goodInfoEntityList = buildGoodInfoEntityList();
        return buildPageBean(new PageBean(spiderParam.getPageNo(),spiderParam.getPageSize()), goodInfoEntityList);
    }

    private PageBean buildPageBean(PageBean pageBean, List resultList) {
        int startIndex = (pageBean.getPageNo() - 1) * pageBean.getPageSize();
        int endIndex = pageBean.getPageNo() * pageBean.getPageSize();
        int realSize = resultList.size();
        pageBean.setTotalCount(realSize);
        if (realSize < startIndex) {
            return pageBean;
        }
        if (realSize < endIndex) {
            pageBean.setResultList(new ArrayList(resultList.subList(startIndex, realSize)));
            return pageBean;
        }
        pageBean.setResultList(new ArrayList(resultList.subList(startIndex, endIndex)));
        return pageBean;
    }


    private List<GoodInfoEntity> buildGoodInfoEntityList() {
        ServiceResponse<String> response = httpClientExecutor.executeGet(miaoshaUrl);
        String content = response.getResult();
        content = content.substring(content.indexOf("{"), content.lastIndexOf("}") + 1);
        MiaoJDSha jdMiaoSha = JsonUtils.fromJson(content, MiaoJDSha.class);
        List<MiaoShaJDGoodInfo> miaoShaList = jdMiaoSha.getMiaoShaList();
        List<GoodInfoEntity> goodInfoEntityList = new ArrayList<>();
        if (CollectionUtils.isEmpty(miaoShaList)) {
            return goodInfoEntityList;
        }
        Date date = new Date();
        for (MiaoShaJDGoodInfo goodInfo : miaoShaList) {
            GoodInfoEntity goodInfoEntity = new GoodInfoEntity();
            goodInfoEntity.setSkuId(IDGenerator.getSkuId());
            goodInfoEntity.setOutSkuCode(goodInfo.getWareId());
            goodInfoEntity.setSkuCode(IDGenerator.getSkuCode());
            goodInfoEntity.setSkuName(goodInfo.getWname());
            goodInfoEntity.setSkuDesc(goodInfo.getTagText());
            goodInfoEntity.setSkuSummary(goodInfo.getOperateWord());
            goodInfoEntity.setSkuPrice(Double.valueOf(NumUtils.mul(goodInfo.getMiaoShaPrice(), 100)).longValue());
            goodInfoEntity.setOriginPrice(Double.valueOf(NumUtils.mul(goodInfo.getJdPrice(), 100)).longValue());
            goodInfoEntity.setPromotionSummary(StringUtils.isEmpty(goodInfo.getTagText()) ? goodInfo.getPromotionId() : goodInfo.getTagText());
            goodInfoEntity.setSkuLink(StringUtils.isEmpty(goodInfo.getPcJumpUrl()) ? "https://miaosha.jd.com/" : goodInfo.getPcJumpUrl());
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
            if (StringUtils.isNotEmpty(goodInfo.getSoldRate())) {
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


    public static void spiderJDMiaoSha() {
        String url = "https://miaosha.jd.com/";
        SpiderInput<GoodInfoEntity> spiderInput = new SpiderInput();
        ScanRuleInput scanRuleInput = new ScanRuleInput();
        List<ItemRule> itemRuleList = new ArrayList<ItemRule>();
        spiderInput.setUrl(url);
        spiderInput.setScanRuleInput(scanRuleInput);
        spiderInput.setTargetClass(GoodInfoEntity.class);
        scanRuleInput.setBaseUrl(url);
        scanRuleInput.setScanExpressions(new String[]{"li.seckill_mod_goods"});
        scanRuleInput.setItemRuleList(itemRuleList);

        ItemRule itemRule = new ItemRule();
        itemRule.setAliasName("skuName");
        itemRule.setItemExpressions(new String[]{"a"});
        itemRule.setItemSource(ItemSourceEnum.TEXT.getCode());
        itemRuleList.add(itemRule);

        itemRule = new ItemRule();
        itemRule.setAliasName("skuDesc");
        itemRule.setItemExpressions(new String[]{"span"});
        itemRule.setItemSource(ItemSourceEnum.TEXT.getCode());
        itemRuleList.add(itemRule);

        itemRule = new ItemRule();
        itemRule.setAliasName("skuPrice");
        itemRule.setItemExpressions(new String[]{".seckill_mod_goods_price"});
        itemRule.setItemSource(ItemSourceEnum.TEXT.getCode());
        itemRuleList.add(itemRule);

        itemRule = new ItemRule();
        itemRule.setAliasName("originPrice");
        itemRule.setItemExpressions(new String[]{".seckill_mod_goods_price_pre"});
        itemRule.setItemSource(ItemSourceEnum.TEXT.getCode());
        itemRuleList.add(itemRule);

        itemRule = new ItemRule();
        itemRule.setAliasName("skuLink");
        itemRule.setAttrName("href");
        itemRule.setItemExpressions(new String[]{"a"});
        itemRuleList.add(itemRule);

        itemRule = new ItemRule();
        itemRule.setAliasName("skuImg");
        itemRule.setAttrName("src");
        itemRule.setItemExpressions(new String[]{".seckill_mod_goods_link_img"});
        itemRuleList.add(itemRule);

        itemRule = new ItemRule();
        itemRule.setAliasName("stockNum");
        itemRule.setItemExpressions(new String[]{".seckill_mod_goods_progress_txt"});
        itemRule.setItemSource(ItemSourceEnum.TEXT.getCode());
        itemRuleList.add(itemRule);

        System.err.println(JsonUtils.toJson(new HtmlSpider().analyse(spiderInput)));
    }

}
