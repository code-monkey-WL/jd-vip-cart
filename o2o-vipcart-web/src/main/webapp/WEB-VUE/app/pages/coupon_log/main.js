import '../../../css/vipcart_log.less';

import 'babel-polyfill';
import Vue from 'vue';
import LogTable from './LogTable.vue';
import PageFooter from '../common/PageFooter.vue';
import store from '../../store';
import * as loader from '../../utils/loader';
import query from '../../utils/query';

new Vue({
  el: "body",
  components:{
    LogTable,
    PageFooter
  },
  data() {
    return {
      state: store.state,
      query: {
        userPin: '',
        phone: '',
        orderNo: '',
        stationNo: '',
        state: '-1'
      },
      counts: {
        count: 0,
        received: 0,
        userReceived: 0,
        useCount: 0
      },
      vipcartId: '',
      activityCode: ''
    };
  },
  ready() {
    this.vipcartId = query("vipcartId");
    this.activityCode = query("activityCode");
    $(".dropdown").dropdown();
    this.loadCounts();
    // this.onQuery();
  },
  methods: {
    loadCounts() {
      $.ajax({
        url: '/vipcart/tovipcartLogPage',
        dataType: 'json',
        data: {vipcartId: this.vipcartId},
        success: (result) => {
          if(result.code == '0') {
            this.counts.count = result.result.totalNum;
            this.counts.received = result.result.currentNum;
            this.counts.userReceived = result.result.currentNum - result.result.unRegisterGrabvipcartNum;
            this.counts.useCount = result.result.usedNum;
          }
        }
      });
    },
    onQuery() {
      this.$refs.table.setParams(this.query);
      this.$refs.table.resetTable();
      this.$refs.table.loadTable(() => { });
    }
  }
});
