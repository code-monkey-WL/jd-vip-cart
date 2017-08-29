import '../../../css/orient_push.less';

import 'babel-polyfill';
import Vue from 'vue';
import VueTable from './VueTable.vue';
import store from '../../store';
import * as loader from '../../utils/loader';

new Vue({
  el: "body",
  components:{
    VueTable
  },
  data() {
    return {
      state: store.state,
      query: {
        activityName: '',
        isFilterTestActivity: '',
        activityCode: '',
        createTimeStart: '',
        createTimeEnd: '',
        createPin: '',
        pushState: '-1'
      }
    };
  },
  ready() {
    $('.checkbox').checkbox();
    $('.dropdown').dropdown();

    let $createTimeStart = $('#createTimeStart');
    let $createTimeEnd = $('#createTimeEnd');
    $createTimeStart.datetimepicker({
      dateFormat: "yy-mm-dd",
      onSelect: function() {
        var date = $(this).datetimepicker('getDate');
        $createTimeEnd.datetimepicker("option", "minDate", date);
        $createTimeEnd.datetimepicker("option", "hourMin", date.getHours());
        $createTimeEnd.datetimepicker("option", "minuteMin", date.getMinutes());
      }
    }).datepicker("option", 'zh-CN');
    $createTimeEnd.datetimepicker({
      dateFormat: "yy-mm-dd",
      onSelect: function() {
        var date = $(this).datetimepicker('getDate');
        $createTimeStart.datetimepicker("option", "maxDate", date);
        $createTimeStart.datetimepicker("option", "hourMax", date.getHours());
        $createTimeStart.datetimepicker("option", "minuteMax", date.getMinutes());
      }
    }).datepicker("option", 'zh-CN');
  },
  methods: {
    onQuery() {
      this.$refs.table.setParams(this.query);
      this.$refs.table.resetTable();
      this.$refs.table.loadTable(() => {});
    }
  }
});
