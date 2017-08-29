import '../../../css/edit_orient_push.less';

import 'babel-polyfill';
import Vue from 'vue';
import PageFooter from '../common/PageFooter.vue';
import store from '../../store';
import * as loader from '../../utils/loader';
import query from '../../utils/query';
import SelectUserPin from '../common/SelectUserPin.vue';
import ListItem from '../common/ListItem.vue';
import * as utils from '../utils';

new Vue({
  el: "body",
  components:{
    PageFooter,
    SelectUserPin,
    ListItem
  },
  data() {
    return {
      state: store.state,
      type: 1,
      pushTimeType: '',
      userPinList: [],
      pushChanel: [],
      cost: 0
    };
  },
  ready() {
      //绑定日期控件
      let $pushTime = $("input[name='pushTime']");
      $pushTime.datetimepicker({
        dateFormat: "yy-mm-dd"
      }).datepicker("option", 'zh-CN');

      $(".checkbox").checkbox();
  },
  methods: {
    showUserPanel(event) {
      this.$refs.selectuserpin.show(this.userPinList, (userPinList) => {
        this.$watch("userPinList", (n,v) => {
          $("input[name='bpiId']").val(this.userPinList.map((item) => {return item.id}).join(',')).blur();
          $("input[name='batchsNo']").val(this.userPinList.map((item) => {return item.name}).join(',')).blur();
        });
        this.userPinList = userPinList;
        if(userPinList.length > 0) {
          this.cost = userPinList[0].notPushUserNum * 0.1;
        }
        // $(event.target).parent().removeClass("error");
        // $(event.target).parent().parents('.field').removeClass("error");
      });
    },
    onCheck(event) {
      let $this = $(event.target);
      if(event.target.tagName == "INPUT") {
        $this.parents("span").prev().find("input").click();
      } else {
        $this.prev().find("input").click();
      }
    },
    validate() {
      var $form = $(this.$el).find(".form");

      let fields = {
        activityName: {identifier : 'activityName', rules: [{type:'empty', prompt:'请输入活动名称'}]},
        bpiId: {identifier : 'bpiId', rules: [{type:'empty', prompt:'请选择用户批次'}]},
        'pushChanel[]': {identifier : 'pushChanel[]', rules: [{type:'checked'}]},
        pushTimeType: {identifier : 'pushTimeType', rules: [{type:'checked'}]}
      };

      if(this.pushTimeType == 2) {
        fields.pushTime = {identifier : 'pushTime', rules: [{type:'empty', prompt:'请输入推送时间'}]};
      }
      if(this.pushChanel == 1) {
        fields.smsInfo = {identifier : 'smsInfo', rules: [{type:'empty', prompt:'请输入短信内容'}]};
      }
      if(this.pushChanel == 2) {
        fields.appInfo = {identifier : 'appInfo', rules: [{type:'empty', prompt:'请输入APP提醒内容'}]};
      }
      if(Object.prototype.toString.apply(this.pushChanel) == '[object Array]') {
        this.pushChanel.forEach((v) => {
          if(v == 1) {
            fields.smsInfo = {identifier : 'smsInfo', rules: [{type:'empty', prompt:'请输入短信内容'}]};
          }

          if(v == 2) {
            fields.appInfo = {identifier : 'appInfo', rules: [{type:'empty', prompt:'请输入APP提醒内容'}]};
          }
        });
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
    onSave() {
      let data = this.validate();
      if(!data) {
        return;
      }
      if(Object.prototype.toString.apply(this.pushChanel) == '[object Array]') {
        let pushChanel = [];
        this.pushChanel.map((v) => {
          if(v !== false) {
            pushChanel.push(v);
          }
        }).join(",");
        data.pushChanel = pushChanel.join(",");
      }
      loader.show();
      $.ajax({
        url: '/activity/saveDirectionPushActivity',
        dataType: 'json',
        data: data,
        success: (result) => {
          loader.close();
          if(result.code == '0') {
            alert("提示：创建成功！");
            window.location = "edit_vipcart.html?type=1&pageType=orient_push&activityCode=" + result.result;
          }else{
            alert(result.msg+":"+result.detail || '提示：操作失败！');
          }
        },
        error: () => {
          loader.close();
        }
      });
    }
  }
});
