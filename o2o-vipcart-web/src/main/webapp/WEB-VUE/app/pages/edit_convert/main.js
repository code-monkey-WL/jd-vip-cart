import '../../../css/edit_convert.less';

import 'babel-polyfill';
import Vue from 'vue';
import PageFooter from '../common/PageFooter.vue';
import store from '../../store';
import * as loader from '../../utils/loader';
import query from '../../utils/query';
import ModelSelect from '../common/ModelSelect.vue';
import ListItem from '../common/ListItem.vue';
import * as utils from '../utils';

new Vue({
  el: "body",
  components:{
    PageFooter,
    ModelSelect,
    ListItem
  },
  data() {
    return {
      state: store.state,
      type: 1,
      selectCity: []
    };
  },
  ready() {
      //绑定日期控件
      let $beginTime = $("input[name='codeBeginTime']");
      let $endTime = $("input[name='codeEndTime']");
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

      $(".checkbox").checkbox();
  },
  methods: {
    showCityPanel(event) {
      this.$refs.selectcity.show(this.selectCity, (selectCity) => {
        this.$watch("selectCity", (n,v) => {
          $("input[name='areaCode']").val(this.selectCity.map((item) => {return item.id}).join(',')).blur();
        });
        this.selectCity = selectCity;
        // $(event.target).parent().removeClass("error");
        // $(event.target).parent().parents('.field').removeClass("error");
      });
    },
    validate() {
      var $form = $(this.$el).find(".form");

      let fields = {
        activityName: {identifier : 'activityName', rules: [{type:'empty', prompt:'请输入活动名称'}]},
        //areaCode: {identifier : 'areaCode', rules: [{type:'empty', prompt:'请选择城市'}]},
        totalNum: {identifier : 'totalNum', rules: [{type:'empty', prompt:'请输入生成码数量'},{type:'integer', prompt:'格式错误'}]},
        codeBeginTime: {identifier : ['codeBeginTime', 'codeEndTime'], rules: [{type:'empty', prompt:'请选择码有效期', parents: '.pfield'}]}
      };

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
      loader.show();
      $.ajax({
        url: '/activity/savevipcartCodeActivity',
        dataType: 'json',
        data: data,
        success: (result) => {
          loader.close();
          if(result.code == '0') {
            alert("提示：创建成功！");
            window.location = "edit_vipcart.html?type=1&pageType=convert&activityCode=" + result.result;
          } else {
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
