import '../../../css/city_push.less';

import 'babel-polyfill';
import Vue from 'vue';
import PageFooter from '../common/PageFooter.vue';
import store from '../../store';
import * as loader from '../../utils/loader';
import query from '../../utils/query';
import CityItem from './CityItem.vue';

new Vue({
  el: "body",
  components:{
    PageFooter,
    CityItem
  },
  data() {
    return {
      state: store.state,
      cityPushList: []
    };
  },
  ready() {
    $('.dimmer').dimmer({
          closable: false
      }).dimmer('show');

    this.loadCityPush();
  },
  methods: {
    loadCityPush() {
      $.ajax({
        url: '/activity/queryDTActivity',
        dataType: 'json',
        success: (result) => {
          if(result.code == '0') {
            this.cityPushList = result.result;
          }
        }
      });
    }
  }
});
