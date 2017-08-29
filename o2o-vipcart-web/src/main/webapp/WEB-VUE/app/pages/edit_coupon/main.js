import '../../../css/edit_vipcart.less';

import 'babel-polyfill';
import Vue from 'vue';
import store from '../../store';
import * as loader from '../../utils/loader';
import query from '../../utils/query';
import '../../filters';
import PageFooter from '../common/PageFooter.vue';
import SelectCity from '../common/SelectCity.vue';
import ListItem from '../common/ListItem.vue';
import ModelSelect from '../common/ModelSelect.vue';
import ModelPageSelect from '../common/ModelPageSelect.vue';
import confirm from '../../utils/confirm.js';
import * as utils from '../utils';

new Vue({
  el: "body",
  components:{
    PageFooter,
    SelectCity,
    ListItem,
    ModelSelect,
    ModelPageSelect
  },
  data() {
    return {
      state: store.state,
      info: {
        vipcartType: 0,
        my: 0,
        expiry: 0,
        limitOrgType: 0,
        limitSkuType: 0,
        platShareProportion: 0,
        limitOrgShows: [],
        limitStationShows: [],
        limitSkuShows: []
      },
      type: 1,
      activityCode: '',
      vipcartId: '',
      pageId: 1,
      selectCity: [],
      venderList: [],
      storeList: [],
      industryList: [],
      brandList: [],
      whiteList: [],
      pageType: 'index'
    };
  },
  computed: {
    getVenderId() {
      if(this.venderList.length > 0) {
        return this.venderList[0].id;
      }
      return '';
    },
    getReoportion() {
      if(this.info.platShareProportion % 1 == 0 && this.info.platShareProportion <= 100) {
        return 100 - this.info.platShareProportion;
      }
      return 0;
    }
  },
  ready() {
    //获取地址栏参数 type:1 新增，2 修改，3 查看详情；
    this.type = query("type") || 1;
    this.activityCode = query('activityCode');
    this.vipcartId = query('vipcartId');
    this.pageType = query('pageType') || 'index';
    if(this.type != 1) {
      $.ajax({
        url: '/vipcart/detail',
        dataType: 'json',
        data: {vipcartId: this.vipcartId},
        success: (result) => {
          if(result.code == '0') {
            this.info = result.result;
          } else {
            alert(result.msg || '提示：加载错误,返回上级页面！');
            window.history.go(-1);
          }
        }
      });
    }

    //radio checkbox 初始化
    $('.checkbox').checkbox();

    //绑定日期控件
    let $beginTime = $("input[name='grabBeginTime']");
    let $endTime = $("input[name='grabEndTime']");
    $beginTime.datetimepicker({
      dateFormat: "yy-mm-dd",
      onSelect: function() {
        var date = $(this).datetimepicker('getDate');
        $endTime.datetimepicker("option", "minDate", date);
        $endTime.datetimepicker("option", "hourMin", date.getHours());
        $endTime.datetimepicker("option", "minuteMin", date.getMinutes());
      }
    }).datepicker("option", 'zh-CN');
    $endTime.datetimepicker({
      dateFormat: "yy-mm-dd",
      onSelect: function() {
        var date = $(this).datetimepicker('getDate');
        $beginTime.datetimepicker("option", "maxDate", date);
        $beginTime.datetimepicker("option", "hourMax", date.getHours());
        $beginTime.datetimepicker("option", "minuteMax", date.getMinutes());
      }
    }).datepicker("option", 'zh-CN');

    //绑定日期控件
    let $beginTime1 = $("input[name='vipcartBeginTime']");
    let $endTime1 = $("input[name='vipcartEndTime']");
    $beginTime1.datetimepicker({
      dateFormat: "yy-mm-dd",
      onSelect: function() {
        var date = $(this).datetimepicker('getDate');
        $endTime1.datetimepicker("option", "minDate", date);
        $endTime1.datetimepicker("option", "hourMin", date.getHours());
        $endTime1.datetimepicker("option", "minuteMin", date.getMinutes());
      }
    }).datepicker("option", 'zh-CN');
    $endTime1.datetimepicker({
      dateFormat: "yy-mm-dd",
      onSelect: function() {
        var date = $(this).datetimepicker('getDate');
        $beginTime1.datetimepicker("option", "maxDate", date);
        $beginTime1.datetimepicker("option", "hourMax", date.getHours());
        $beginTime1.datetimepicker("option", "minuteMax", date.getMinutes());
      }
    }).datepicker("option", 'zh-CN');
  },
  methods: {
    onQuery() {
      this.$refs.table.setParams(this.query);
      this.$refs.table.resetTable();
      this.$refs.table.loadTable(() => {});
    },
    showCityPanel(event) {
      this.$refs.selectcity.show(this.selectCity, (selectCity) => {
        this.$watch("selectCity", (n,v) => {
          $("input[name='limitCity']").val(this.selectCity.map((item) => {return item.id}).join(',')).blur();
        });
        this.selectCity = selectCity;
        // $("input[name='limitCity']").val(this.selectCity.map((item) => {return item.id}).join(',')).blur();
        // $(event.target).parent().removeClass("error");
        // $(event.target).parent().parents('.field').removeClass("error");
      });
    },
    checkOne() {
      var $form = $("#form1");
      $("input[name='limitCity']").val(this.selectCity.map((item) => {return item.id}).join(','));

      let fields = {
        vipcartType: {identifier : 'vipcartType', rules: [{type:'checked', prompt:'请选择优惠券类型'}]},
        limitCity: {identifier : 'limitCity', rules: [{type:'empty', prompt:'请选择城市'}]},
        vipcartName: {identifier : 'vipcartName', rules: [{type:'empty', prompt:'请输入优惠券名称'}]}
      };

      $form.form({
        on: "blur",
        inline: true,
        fields: fields
      });
      if(!$form.form('validate form')) {
        return false;
      }
      return true;
    },
    next() {
      if(this.checkOne()) {
        this.pageId = 2;
        this.$nextTick(() => {
          $('.checkbox').checkbox();
        });
      }
    },
    onCheck(event) {
      let $this = $(event.target);
      if(event.target.tagName == "INPUT") {
        $this.parents("span").prev().find("input").click();
      } else {
        $this.prev().find("input").click();
      }
    },
    prev() {
      this.pageId = 1;
    },
    selectPanel(type) {
      if(type == 'vender') {
        this.$refs.vender.show(this.venderList, (venderList) => {
          this.$watch("venderList", (n,v) => {
            $("input[name='limitOrgValue']").val(this.venderList.map((item) => {return item.id}).join(',')).blur();
          });
          this.venderList = venderList;
        });
      } else if(type == "store") {
        this.$refs.store.show(this.storeList, (storeList) => {
          this.$watch("storeList", (n,v) => {
            $("input[name='limitStation']").val(this.storeList.map((item) => {return item.id}).join(',')).blur();
          });
          this.storeList = storeList;
        });
      } else if(type == "industry") {
        this.$refs.industry.show(this.industryList, (industryList) => {
          this.$watch("industryList", (n,v) => {
            $("input[name='limitOrgValue']").val(this.industryList.map((item) => {return item.id}).join(',')).blur();
          });
          this.industryList = industryList;
        });
      } else if(type == "brand") {
        this.$refs.brand.show(this.brandList, (brandList) => {
          this.$watch("brandList", (n,v) => {
            $("input[name='limitSkuValue']").val(this.brandList.map((item) => {return item.id}).join(',')).blur();
          });
          this.brandList = brandList;
        });
      } else if(type == "white") {
        this.$refs.brand.show(this.whiteList, (whiteList) => {
          this.$watch("whiteList", (n,v) => {
            $("input[name='blackOrderValue']").val(this.whiteList.map((item) => {return item.id}).join(',')).blur();
          });
          this.whiteList = whiteList;
        });
      }
    },
    validate() {
      var $form = $("form");

      let fields = {
        totalNum: {identifier : 'totalNum', rules: [{type:'empty', prompt:'请输入发券总量'},{type:'integer', prompt:'格式错误'}]},
        grabBeginTime: {identifier : ['grabBeginTime', 'grabEndTime'], rules: [{type:'empty', prompt:'请选择领券时间', parents: '.pfield'}]},
        minOrderAmount: {identifier : ['minOrderAmount', 'quota', 'maxOrderAmount'], rules: [{type:'empty', prompt:'请输入面值', parents: '.pfield'},{type:'number', prompt:'格式错误', parents: '.pfield'}]},
        'limitChannel[]': {identifier : 'limitChannel[]', rules: [{type:'checked', prompt:'请选择适用平台'}]},
        limitOrgType: {identifier : 'limitOrgType', rules: [{type:'checked', prompt:'请选择商家适用范围'}]},
        limitSkuType: {identifier : 'limitSkuType', rules: [{type:'checked', prompt:'请选择商品适用范围'}]},
        platShareProportion: {identifier : 'platShareProportion', rules: [{type:'empty', prompt:'请输入承担比例'},{type:'integer', prompt:'格式错误'}]},
        showName: {identifier : 'showName', rules: [{type:'empty', prompt:'请输入优惠券标题'}]},
        showText: {identifier : 'showText', rules: [{type:'empty', prompt:'请输入优惠券说明'}]},
        phoneNumbers: {identifier : 'phoneNumbers', rules: [{type:'empty', prompt:'请输入负责人电话'}, {type: 'regExp[/^\([ \t]|1(3|4|5|7|8)[0-9]{9})*$/]', prompt:'格式错误'}]}
      };
      if(this.info.expiry == '1') {
        fields['expiryEndDays'] = {identifier : 'expiryEndDays', rules: [{type:'empty', prompt:'请输入用券时间'},{type:'integer', prompt:'格式错误'}]};
      } else {
        fields['vipcartBeginTime'] = {identifier : ['vipcartBeginTime', 'vipcartEndTime'], rules: [{type:'empty', prompt:'请选择领券时间', parents: '.pfield'}]}
      }
      if(this.info.limitOrgType == '200') {
        fields['limitOrgValue'] = {identifier : 'limitOrgValue', rules: [{type:'empty', prompt:'请选择商家'}]};
      }
      if(this.info.limitOrgType == '220') {
        fields['limitOrgValue'] = {identifier : 'limitOrgValue', rules: [{type:'empty', prompt:'请选择行业'}]};
      }
      if(this.info.limitSkuType == '270') {
        fields['limitSkuValue'] = {identifier : 'limitSkuValue', rules: [{type:'empty', prompt:'请选择品牌'}]};
      }
      if(!utils.form($form, {
        on: "blur",
        inline: true,
        fields: fields
      })) {
        return false;
      }
      return $form.form('get values');
    },
    clearOrgList(type) {
      if(type == 0) {
        this.venderList = [];
        this.storeList = [];
        this.industryList = [];
      } else if(type == 200) {
        this.industryList = [];
      } else if(type == 220) {
        this.venderList = [];
        this.storeList = [];
      }
    },
    clearSkuList(type) {
      if(type == 0) {
        this.brandList = [];
      }
    },
    onRiskCheck(){
      let data = this.validate();
      if(!data) {
        return;
      }
      data.activityCode = this.activityCode;
      if(data.limitChannel){
        let limitChannel = [];
        data.limitChannel.map((v) => {
          if(v !== false) {
            limitChannel.push(v);
          }
        }).join(",");
        data.limitChannel = limitChannel.join(",");
      }else{
        data.limitChannel = 0;
      }
      if(data.expiry == 1){
        data.expiryBeginDays =0;
      }
      data.minOrderAmount = parseInt(data.minOrderAmount * 100);
      data.quota = parseInt(data.quota * 100);
      data.maxOrderAmount = parseInt(data.maxOrderAmount * 100);
      //免N元运费券转换
      if(data.vipcartType == 3 && data.quota > 0){
        data.vipcartType = 4;
      }
      loader.show();
      $.ajax({
        url: '/vipcart/checkvipcartPromotionRisk',
        type: 'post',
        dataType: "json",
        data: data,
        success: (result) => {
          if(result.code == 'warning') {
            confirm(result.msg, () => {
              loader.close();
              this.onSubmit(data);
            });
          }
          else if(result.code == 'success'){
            this.onSubmit(data);
          }
          else {
            loader.close();
            alert(result.msg || '提示：操作失败！');
            return;
          }
        },
        error: () => {
          loader.close();
        }
      });
    },
    onSubmit(data) {
        loader.show();
        $.ajax({
          url: '/vipcart/save',
          type: 'post',
          dataType: "json",
          data: data,
          success: (result) => {
            loader.close();
            if(result.code == '0') {
              alert('提示：保存成功！');
              this.onBackAndRefresh(result);
            } else {
              alert(result.msg+":"+result.detail || '提示：操作失败！');
            }
          },
          error: () => {
            loader.close();
          }
        });
    },
    onBackAndRefresh(result) {
      window.location = "vipcart.html?pageType="+this.pageType+"&activityCode="+result.result.activityCode+"&activityName="+result.result.activityName+"&activityState="+result.result.activityState;
    },
    onBack() {
      history.go(-1);
    }
  }
});
