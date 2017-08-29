import '../../../css/vipcart_query.less';

import 'babel-polyfill';
import Vue from 'vue';
import vipcartTable from './vipcartTable.vue';
import LimitTable from './LimitTable.vue';
import BindTable from './BindTable.vue';
import UsevipcartTable from './UsevipcartTable.vue';
import UseCodeTable from './UseCodeTable.vue';
import PageFooter from '../common/PageFooter.vue';

new Vue({
  el: "body",
  components:{
    vipcartTable,
    LimitTable,
    BindTable,
    UsevipcartTable,
    UseCodeTable,
    PageFooter
  },
  data() {
    return {
    };
  },
  ready() {
    this.initTabs();
    $("select").dropdown();
    $('.checkbox').checkbox();
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
    }
  }
});
