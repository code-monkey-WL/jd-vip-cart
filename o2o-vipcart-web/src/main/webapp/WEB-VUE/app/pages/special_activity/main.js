import '../../../css/special_activity.less';

import 'babel-polyfill';
import Vue from 'vue';
import PageFooter from '../common/PageFooter.vue';
import store from '../../store';
import * as loader from '../../utils/loader';
import query from '../../utils/query';
import SpecialItem from './SpecialItem.vue';

new Vue({
  el: "body",
  components:{
    PageFooter,
    SpecialItem
  },
  data() {
    return {
      state: store.state,
      specialItemList: []
    };
  },
  ready() {
    $('.dimmer').dimmer({
          closable: false
      }).dimmer('show');

    this.loadSpecialItem();
  },
  methods: {
    loadSpecialItem() {
      //this.specialItemList = [
      //  {purpose: '4', name: '新人红包', url: '../../../app/images/new_person_red_bag.png'},
      //  {purpose: '2', name: '红包裂变', url: '../../../app/images/red_bag_change.png'},
      //  {purpose: '8', name: '裂变激励', url: '../../../app/images/change_excitation.png'},
      //  {purpose: '2', name: '分享有礼', url: '../../../app/images/share_excitation.png'},
      //  {purpose: '6', name: '口令红包', url: '../../../app/images/word_red_bag.png'},
      //  {purpose: '7', name: '天将红包', url: '../../../app/images/descending_red_bag.png'}
      //];
      $.ajax({
         url: '/rpc/specialActivityShowMethod',
         dataType: 'json',
         success: (result) => {
           if(result.code == '0') {
             this.specialItemList = result.result;
           }else{
             alert("提示：创建失败！"+result.msg);
           }
         }
      });
    }
  }
});
