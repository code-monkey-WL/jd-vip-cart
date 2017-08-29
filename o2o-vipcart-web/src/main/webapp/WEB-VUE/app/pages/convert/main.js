import '../../../css/convert.less';

import 'babel-polyfill';
import Vue from 'vue';
import ConvertTable from './ConvertTable.vue';
import store from '../../store';
import * as loader from '../../utils/loader';

new Vue({
  el: "body",
  components:{
    ConvertTable
  },
  data() {
    return {
      state: store.state,
      query: {
        activityName: '',
        isFilterTestActivity: '',
        activityCode: '',
        codeBeginTime: '',
        codeEndTime: '',
        createTimeStart: '',
        createTimeEnd: '',
        createPin: '',
        cityCode: '-1',
        activityState: '-1'
      },
      listCity: []
    };
  },
  ready() {
    $('.checkbox').checkbox();
    $('.dropdown').dropdown();
    let $beginTime = $('#codeBeginTime');
    let $endTime = $('#codeEndTime');
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
    this.loadCity();
  },
  methods: {
    loadCity() {
      $.ajax({
        url: '/rpc/queryCityInfoList',
        dataType: 'json',
        success: (result) => {
          if(result.code == '0') {
            this.listCity = result.data;
          }
        }
      });
    },
    onQuery() {
      this.$refs.table.setParams(this.query);
      this.$refs.table.resetTable();
      this.$refs.table.loadTable(() => {});
    }
  }
});
