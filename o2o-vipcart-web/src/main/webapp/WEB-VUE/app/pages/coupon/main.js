import '../../../css/vipcart.less';

import 'babel-polyfill';
import Vue from 'vue';
import EffectiveTable from './EffectiveTable.vue';
import NotEffectiveTable from './NotEffectiveTable.vue';
import PageFooter from '../common/PageFooter.vue';
import store from '../../store';
import * as loader from '../../utils/loader';
import query from '../../utils/query';

new Vue({
  el: "body",
  components:{
    EffectiveTable,
    NotEffectiveTable,
    PageFooter
  },
  data() {
    return {
      state: store.state,
      effectivevipcart: 0,
      notEffectivevipcart: 0,
      activityCode: '',
      activityName: '',
      activityState: '',
      pageType: 'index'
    };
  },
  ready() {
    this.activityCode = query("activityCode");
    this.activityName = query("activityName");
    this.activityState = query("activityState");
    this.pageType = query("pageType") || 'index';
    this.initTabs();
    this.onQuery();
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
    onQuery() {
      this.$refs.effectivetable.resetTable();
      this.$refs.effectivetable.loadTable(() => {
        this.effectivevipcart = this.$refs.effectivetable.pagination.page.totalCount;
      });
      this.$refs.noteffectivetable.resetTable();
      this.$refs.noteffectivetable.loadTable(() => {
        this.notEffectivevipcart = this.$refs.noteffectivetable.pagination.page.totalCount;
      });
    },
    toAddvipcart() {
      $.ajax({
        url: '/vipcart/validateAddvipcart',
        dataType: 'json',
        data: {activityCode: this.activityCode},
        success: (result) => {
          if(result.code == '0') {
            window.location = 'edit_vipcart.html?activityCode='+this.activityCode+"&pageType="+this.pageType;
          } else {
            alert(result.msg+":"+result.detail || '提示：操作失败！');
          }
        }
      });

    }
  }
});
