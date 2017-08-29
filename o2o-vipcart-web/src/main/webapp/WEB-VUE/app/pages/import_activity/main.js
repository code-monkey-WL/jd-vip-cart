import '../../../css/import_activity.less';

import 'babel-polyfill';
import Vue from 'vue';
import * as loader from '../../utils/loader';
import PageFooter from '../common/PageFooter.vue';

new Vue({
  el: "body",
  components:{
    PageFooter
  },
  data() {
    return {
      fileName: ''
    };
  },
  computed: {
    getFileName() {
      return this.fileName || '未选择文件';
    }
  },
  ready() {
  },
  methods: {
    onSelect() {
      let $file = $("#file");
      $file.click();
      let self = this;
      $file.unbind().change(function(){
        let file = $file.val();
        let fileExt = file.replace(/.+\./,"");   //正则表达式获取后缀
        self.fileName = file.replace(/^.+?\\([^\\]+?)(\.[^\.\\]*?)?$/gi,"$1") + '.' + fileExt;
      });
    },
    onChange() {

    },
    onSubmit() {
      var data = new FormData();
      data.append("file", $('#file')[0].files[0]);
      loader.show();
      $.ajax({
        url: '/activity/excelBatchCreateActivity',
        type: 'POST',
        data: data,
        processData: false,
        contentType: false,
        cache: false,
        success: (result) => {
          loader.close();
          if(result.code == 0) {
            alert("提示：导入成功！");
          }else{
            alert(result.msg+":"+result.detail || '提示：操作失败！');
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
