import '../../../css/import_user_pin.less';

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
      fileName1: '',
      fileName2: ''
    };
  },
  computed: {
    getFileName1() {
      return this.fileName1 || '未选择文件';
    },
    getFileName2() {
      return this.fileName2 || '未选择文件';
    }
  },
  ready() {
    this.initTabs();
  },
  methods: {
    initTabs() {
      let $tabs = $(".tabs").find("> ul > li");
      if($tabs.length <= 0) {
        return;
      }
      let $panels = $(".tabs").find("> div");
      $tabs.each(function(index) {
        let $this = $(this);
        $this.click(() => {
          $tabs.removeClass("active");
          $this.addClass("active");
          $panels.hide();
          $panels.eq(index).show();
        });
      });
      $tabs.eq(0).click();
    },
    onSelect(type) {
      let $file = $("#file"+type);
      $file.click();
      let self = this;
      $file.unbind().change(function(){
        let file = $file.val();
        let fileExt = file.replace(/.+\./,"");   //正则表达式获取后缀
        if(type == 1) {
          self.fileName1 = file.replace(/^.+?\\([^\\]+?)(\.[^\.\\]*?)?$/gi,"$1") + '.' + fileExt;
        } else {
          self.fileName2 = file.replace(/^.+?\\([^\\]+?)(\.[^\.\\]*?)?$/gi,"$1") + '.' + fileExt;
        }
      });
    },
    onChange() {

    },
    onSubmit(type) {
      var data = new FormData();
      data.append("file", $('#file' + type)[0].files[0]);
      data.append("remark", $('#msg' + type).val());
      data.append("batchType", type);
      loader.show();
      $.ajax({
        url: '/activity/uploadUserBatch',
        type: 'POST',
        data: data,
        processData: false,
        contentType: false,
        cache: false,
        success: (result) => {
          loader.close();
          if(result.code == 0) {
            window.location = "orient_push.html";
            alert("提示：导入成功！");
          }else{
            alert("提示：创建失败！"+result.msg);
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
