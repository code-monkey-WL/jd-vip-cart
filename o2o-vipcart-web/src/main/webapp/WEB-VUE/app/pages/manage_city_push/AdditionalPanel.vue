<template>
<div class="ui modal mini">
  <div class="header">
	  <span>追加地推码</span>
  </div>
  <div  class="content">
    <ul class="ui form">
      <li>
        <label class="empty">生成码数量</label>
        <div class="ui input field"><input name="totalNum" type="text"></div>
      </li>
      <li>
        <label class="empty">码有效期</label>
        <div class="ui input icon time field">
          <i class="calendar icon"></i>
          <input name="codeBeginTime" type="text">
        </div>
        -
        <div class="ui input icon time field">
          <i class="calendar icon"></i>
          <input name="codeEndTime" type="text">
        </div>
      </li>
      <li>
        <label class="empty">单码可用次数</label>
        <div class="ui input field"><input name="canUseTimes" type="text"></div>
      </li>
      <li>
        <label></label>
        <a class="ui secondary button" @click="onClickOk()">确定</a>
        <div class="spacing"></div>
        <a href="javascript:void(0);" @click="onCancel()">取消</a>
      </li>
    </ul>
  </div>
  <!-- <div class="actions">
  	<div class="ui ok button green" @click="onClickOk()">确定</div>
  	<div class="ui cancel button" @click="onCancel()">取消</div>
  </div> -->
</div>
</template>

<script>
import '../../filters';
import query from '../../utils/query';

export default {
	data() {
      return {
        activityCode: ''
      }
	},
	ready() {
      this.activityCode = query('activityCode');
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
	},
	methods: {
		show() {
			$(this.$el).modal("show");
		},
    validate() {
      var $form = $(this.$el).find(".form");

      let fields = {
        numbers: ['empty','integer'],
        beginTime: 'empty',
        endTime: 'empty',
        singelNumbers: ['empty','integer']
      };

      $form.form({
        on: "blur",
        fields: fields
      });
      if(!$form.form('validate form')) {
        return false;
      }
      return $form.form('get values');
    },
    onClickOk() {
      let data = this.validate();
      if(!data) {
        return false;
      }
        data.activityCode = this.activityCode;
      $.ajax({
         url: '/vipcartCode/appendvipcartCodeBatch',
         dataType: 'json',
         data: data,
         success: (result) => {
           if(result.code == '0') {
             alert("提示：创建成功！");
             this.onCancel();
             this.$parent.onQuery();
           }else{
             alert(result.msg+":"+result.detail || '提示：操作失败！');
           }
         }
      });
      this.onCancel();
    },
		onCancel() {
			$(this.$el).modal("hide");
		}
	}
}
</script>
<style scoped lang="less">
</style>
