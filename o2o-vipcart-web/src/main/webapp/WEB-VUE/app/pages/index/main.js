import '../../../css/index.less';

import 'babel-polyfill';
import Vue from 'vue';
import IndexTable from './IndexTable.vue';
import store from '../../store';
import * as loader from '../../utils/loader';
import query from '../../utils/query';
import ShowLocation from './ShowLocation.vue';

new Vue({
  el: "body",
  components:{
    IndexTable,
    ShowLocation
  },
  data() {
    return {
      state: store.state,
      query: {
        activityName: '',
        isFilterTestActivity: '',
        activityCode: '',
        beginTime: '',
        endTime: '',
        createTimeStart: '',
        createTimeEnd: '',
        createPin: '',
        activityState: '-1',
        activityType: null,
        purpose:null
      }
    };
  },
  ready() {
    this.query.activityType = query("activityType") == null ? 1 : query("activityType");
    this.query.purpose = query("purpose") == null ? 1 : query("purpose");
    $('.checkbox').checkbox();
    $('.dropdown').dropdown();
    let $beginTime = $('#beginTime');
    let $endTime = $('#endTime');
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

    let $createTimeStart = $('#createTimeStart');
    let $createTimeEnd = $('#createTimeEnd');
    $createTimeStart.datetimepicker({
      dateFormat: "yy-mm-dd",
      onSelect: function() {
        var date = $(this).datetimepicker('getDate');
        $createTimeEnd.datetimepicker("option", "minDate", date);
        $createTimeEnd.datetimepicker("option", "hourMin", date.getHours());
        $createTimeEnd.datetimepicker("option", "minuteMin", date.getHours());
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
