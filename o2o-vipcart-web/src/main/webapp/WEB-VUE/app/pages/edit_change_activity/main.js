import '../../../css/edit_change_activity.less';

import 'babel-polyfill';
import Vue from 'vue';
import store from '../../store';
import * as loader from '../../utils/loader';
import query from '../../utils/query';
import '../../filters';
import PageFooter from '../common/PageFooter.vue';
import * as utils from '../utils';

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
      limitMaxTimes: null,
      shareTitle: '',
      shareDetail: '',
      imgUrl: '',
      plance: 0,
      grabMode: 0
    },
    type: 1,
    activityCode: ''
  };
},
computed: {
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
  },
  getPlances() {
    if(this.info.plance != 1) {
      $("input[name='grabMode']").eq(0).click();
      return true;
    }
    return false;
  }
},
ready() {
  //获取地址栏参数 type:1 新增，2 修改，3 查看详情；
  this.type = query("type") || 1;
  this.activityCode = query('activityCode');
  if(this.type != 1) {
    $.ajax({
      url: '/activity/detail',
      dataType: 'json',
      data: {activityCode: this.activityCode},
      success: (result) => {
      if(result.code == '0') {
      //显示数据
      this.info = Object.assign(this.info, result.data);
    } else {
      alert(result.errorMsg || '提示：加载错误,返回上级页面！');
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
$beginTime.datepicker({
  dateFormat: "yy-mm-dd",
  onSelect: function() {
    var date = $(this).datepicker('getDate');
    $endTime.datepicker("option", "minDate", date);
  }
}).datepicker("option", 'zh-CN');
$endTime.datepicker({
  dateFormat: "yy-mm-dd",
  onSelect: function() {
    var date = $(this).datepicker('getDate');
    $beginTime.datepicker("option", "maxDate", date);
  }
}).datepicker("option", 'zh-CN');
},
methods: {
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
    data.append("file", $('#shareImg')[0].files[0]);
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
        if(result.errorCode == 0) {
          this.info.imgUrl = result.data;
          $("input[name='shareImg']").val(this.info.imgUrl);
        } else {
          alert(result.msg || '提示：操作失败！');
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
    limitMaxTimes: {identifier : 'limitMaxTimes', rules: [{type:'empty', prompt:'请输入领取次数'},{type:'integer', prompt:'格式错误'}]}
    // beginTime: {identifier : ['orderMoneyMin', 'endTime'], rules: [{type:'empty', prompt:'请选择活动时间', parents: '.pfield'}]},
  };

  //type 1：新增，2：修改，3:查看详情
  if(this.type == 2) {
    fields = {
      limitMaxTimes: {identifier : 'limitMaxTimes', rules: [{type:'empty', prompt:'请输入领取次数'},{type:'integer', prompt:'格式错误'}]}
    }
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
  if(data.islimitRule == 1) {
    limitRules.push({limitType: 101, limitMode: 1, ruleValues: 0});
  }
  if(limitRules.length > 0) {
    data.limitRules = limitRules.map((item) => {return item.limitType;}).join(",");
    data.limitRuleValue = JSON.stringify(limitRules);
  }

  data.shareTitle = this.getShareTitle;
  data.shareDetail = this.getShareDetail;
  data.shareImg = this.getShareImg;
  data.activityType = this.type;
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
      } else {
        alert(result.msg || '提示：操作失败！');
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
