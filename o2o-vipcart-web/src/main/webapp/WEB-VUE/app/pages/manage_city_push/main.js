import '../../../css/manage_city_push.less';

import 'babel-polyfill';
import Vue from 'vue';
import PageFooter from '../common/PageFooter.vue';
import EffectiveTable from './EffectiveTable.vue';
import NotEffectiveTable from './NotEffectiveTable.vue';
import AdditionalPanel from './AdditionalPanel.vue';
import store from '../../store';
import * as loader from '../../utils/loader';
import query from '../../utils/query';

new Vue({
  el: "body",
  components:{
    EffectiveTable,
    NotEffectiveTable,
    AdditionalPanel,
    PageFooter
  },
  data() {
    return {
      state: store.state,
      activityName: '',
      effectiveCityPush: 0,
      notEffectiveCityPush: 0,
      query: {
        vipcartCode: '',
        reflectionCode: '',
        channelSource:'-1'
      }
    };
  },
  ready() {
    this.activityName = query("activityName");
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
    onQuery() {
      this.$refs.effectivetable.setParams(this.query);
      this.$refs.effectivetable.resetTable();
      this.$refs.effectivetable.loadTable(() => {
        this.effectiveCityPush = this.$refs.effectivetable.pagination.page.totalCount;
      });
      this.$refs.noteffectivetable.setParams(this.query);
      this.$refs.noteffectivetable.resetTable();
      this.$refs.noteffectivetable.loadTable(() => {
        this.notEffectiveCityPush = this.$refs.noteffectivetable.pagination.page.totalCount;
      });
    },
    showAdditional() {
      this.$refs.additional.show();
    }
  }
});
