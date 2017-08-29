import '../../../css/edit_activity.less';

import 'babel-polyfill';
import Vue from 'vue';
import store from '../../store';
import * as loader from '../../utils/loader';
import query from '../../utils/query';
import '../../filters';
import PageFooter from '../common/PageFooter.vue';
import * as utils from '../utils';
import djLogo from '../../images/dj_logo.jpg';
import redBg from '../../images/red-bg.png';

new Vue({
  el: "body",
  components:{
    PageFooter
  },
  data() {
    return {
      state: store.state,
      info: {
        activityName:'',
        beginTime:'',
        endTime:'',
        limitMax: null,
        limitMaxTimes: null,
        limitMaxTimesDay: null,
        // showLocation: [],
        adWords: '',
        ruleDesc: '',
        shareTitle: '',
        shareDetail: '',
        isNewPerson: 0,
        islimitFirstOrder: 0,
        imgUrl: '',
        plance: 0,
        grabMode: 0
      },
      imgUrl: {
        djLogo: djLogo,
        redBg: redBg
      },
      type: 1,
      purpose:null,
      activityCode: ''
    };
  },
  computed: {
    getAdWords() {
      if(this.info.adWords) {
        return this.info.adWords;
      }
      return "商超即可送到家";
    },
    getRuleDesc() {
      if(this.info.ruleDesc) {
        return this.info.ruleDesc;
      }
      return "1、每张优惠券同一设备、同一ID、同一用户每天仅限领取一次;2、每个订单仅限使用一张优惠券，优惠券金额不能兑换现金;";
    },
    getShareTitle() {
      if(this.info.shareTitle) {
        return this.info.shareTitle;
      }
      return "京东到家 - 1小时达！";
    },
    getShareDetail() {
      if(this.info.shareDetail) {
        return this.info.shareDetail;
      }
      return "超市、生鲜、外卖、医药，数万款附近商品，足不出户，1小时送上门！";
    },
    getShareImg() {
      if(this.info.imgUrl) {
        return this.info.imgUrl;
      }
      return '';
    }
    // ,
    // getPlances() {
    //   if(this.info.showLocation.length == 0) {
    //     $("input[name='grabMode']").eq(0).click();
    //     return true;
    //   }
    //   return false;
    // }
  },
  ready() {
    //获取地址栏参数 type:1 新增，2 修改，3 查看详情；
    this.type = query("type") || 1;
    this.activityCode = query('activityCode');
    this.purpose = query('purpose');
    if(this.type != 1) {
      $.ajax({
        url: '/activity/detail',
        dataType: 'json',
        data: {activityCode: this.activityCode},
        success: (result) => {
          if(result.code == '0') {
            //显示数据
            this.info = Object.assign(this.info, result.result);
            //限制规则radio落点
            this.info.limitMax = 0;
            if(this.info.limitMaxTimes == 0 && this.info.limitMaxTimesDay == 0) {
              this.info.limitMax = 1;
            }
            //限制新人和首单规则
            if(result.result.limitRules.indexOf("102") > -1){
              this.info.isNewPerson = 1;
            }
            if(result.result.limitRules.indexOf("101") > -1){
              this.info.islimitFirstOrder = 1;
            }
            this.info.imgUrl = result.result.shareImg;
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
    let $beginTime = $("input[name='beginTime']");
    let $endTime = $("input[name='endTime']");
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
  },
  methods: {
    onQuery() {
      this.$refs.table.setParams(this.query);
      this.$refs.table.resetTable();
      this.$refs.table.loadTable(() => {});
    },
    onCheck(event) {
      let $this = $(event.target);
      if(event.target.tagName == "INPUT") {
        $this.parents("span").prev().find("input").click();
      } else {
        $this.prev().find("input").click();
      }
    },
    onShow(type) {
      if(type == 'share') {
        $('.review_share_panel').show();
        $(".review_panel").hide();
      } else {
        $('.review_panel').show();
        $(".review_share_panel").hide();
      }
    },
    onHide(type) {
      if(type == 'share') {
        $(".review_share_panel").hide();
      } else {
        $(".review_panel").hide();
      }
    },
    onSelect() {
      let $shareImg = $("#shareImg");
      $shareImg.click();
      let self = this;
      $shareImg.unbind().change(function(){
        let r = new FileReader();
        let f = $shareImg[0].files[0];
        r.readAsDataURL(f);
        r.onload = function(e) {
          self.info.imgUrl=this.result;
        };
      });
    },
    onUpload() {
      var data = new FormData();
      data.append("imgFile", $('#shareImg')[0].files[0]);
      loader.show();
      $.ajax({
        url: '/activity/uploadIMG',
        type: 'POST',
        data: data,
        processData: false,
        contentType: false,
        cache: false,
        success: (result) => {
          loader.close();
          if(result.code == 0) {
            this.info.imgUrl = result.result;
            $("input[name='shareImg']").val(this.info.imgUrl);
          }else {
            alert(result.msg+":"+result.detail || '提示：上传图片失败！');
          }
        },
        error: () => {
          loader.close();
        }
      });
    },
    validate() {
      var $form = $("form");

      let fields = {
        activityName: {identifier : 'activityName', rules: [{type:'empty', prompt:'请输入活动名称'}]},
        beginTime: {identifier : ['beginTime', 'endTime'], rules: [{type:'empty', prompt:'请选择活动时间', parents: '.pfield'}]},
        limitMaxTimes: {identifier : ['limitMaxTimes', 'limitMaxTimesDay'], rules: [{type:'empty', prompt:'请输入领取次数', parents: '.pfield'},{type:'integer', prompt:'格式错误', parents: '.pfield'}]}
      };
      if(this.type == 2) {
        fields = {
          limitMaxTimes: {identifier : ['limitMaxTimes', 'limitMaxTimesDay'], rules: [{type:'empty', prompt:'请输入领取次数', parents: '.pfield'},{type:'integer', prompt:'格式错误', parents: '.pfield'}]}
        };
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
    onSubmit() {
      let data = this.validate();
      if(!data) {
        return;
      }
      let limitRules = [];
      if(data.isNewPerson == 1) {
        limitRules.push({limitType: 102, limitMode: 1, ruleValues: 0});
      }
      if(data.islimitRule == 1) {
        limitRules.push({limitType: 101, limitMode: 1, ruleValues: 0});
      }
      if(limitRules.length > 0) {
        data.limitRules = limitRules.map((item) => {return item.limitType;}).join(",");
        data.limitRuleValue = JSON.stringify(limitRules);
      }

      data.adWords = this.getAdWords;
      data.ruleDesc = this.getRuleDesc;
      data.shareTitle = this.getShareTitle;
      data.shareDetail = this.getShareDetail;
      data.shareImg = this.getShareImg;
      data.activityType = this.activityType;
      //let showLocation = "";
      //data.showLocation.forEach((v) => {
      //   if(v){showLocation += showLocation == '' ? v : (',' + v)}
      //});
      //data.showLocation = showLocation;
      data.purpose = this.purpose;
      loader.show();
      $.ajax({
        url: '/activity/save',
        type: 'post',
        dataType: "json",
        data: data,
        success: (result) => {
          loader.close();
          if(result.code == '0') {
            alert('提示：保存成功！');
            window.location = "edit_vipcart.html?type=1&pageType=index&activityCode=" + result.result;
          } else {
            alert(result.msg+":"+result.detail || '提示：操作失败！');
          }
        },
        error: () => {
          loader.close();
        }
      });
    },
    onClear() {
      $("form").form("clear");
    },
    onReview() {

    },
    onUpdate() {
      let data = this.validate();
      if(!data) {
        return;
      }
      data.activityCode = this.activityCode;
      data.shareTitle = this.getShareTitle;
      data.shareDetail = this.getShareDetail;
      data.shareImg = this.getShareImg;
      loader.show();
      $.ajax({
        url: '/activity/update',
        type: 'post',
        dataType: "json",
        data: data,
        success: (result) => {
          loader.close();
          if(result.code == '0') {
            alert('提示：更新成功！');
            window.history.go(-1);
          } else {
            alert(result.msg || '提示：操作失败！');
          }
        },
        error: () => {
          loader.close();
        }
      });
    },
    onBack() {
      history.go(-1);
    }
  }
});
