<template>
<div class="ui modal yunyin-modal mini">
  <div class="header">
	  <span>选择用户</span>
	  <div class="ui search mini">
      <div class="ui icon input">
        <input class="prompt" type="text" placeholder="输入批次号，回车进行搜索" v-model="code" @keyup.enter="onQuery()">
        <i class="search icon"></i>
      </div>
      <div class="results"></div>
    </div>
  </div>
  <div class="content selected-items">
	  <div v-if="selectedItems.length <= 0" class="tips ui red basic label">未选择任何用户</div>
	  <div v-for="item in selectedItems" class="ui label">
		  {{item.name}}<i class="icon close" @click="onDeselect(item)"></i>
	  </div>
  </div>
  <div  class="content table-content">
    <div class="tabs">
      <ul>
        <li :class="{'active': tabIndex == 2}" @click="selectTab(2)">京东pin</li>
        <li :class="{'active': tabIndex == 1}" @click="selectTab(1)">到家pin</li>
      </ul>
  	  <div>
  		  <table class="ui celled table mini selectable">
          <thead>
            <tr>
              <th>批次号</th>
              <th>描述</th>
              <th>用户数</th>
              <th>验证状态</th>
              <th>过滤用户数</th>
              <th>已推用户</th>
              <th>未推用户</th>
              <th>选择</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in resultList" class="{{item.selected ? 'positive': ''}}">
              <td>{{item.name}}</td>
              <td>{{item.remark}}</td>
              <td>{{item.pushNum}}</td>
              <td>{{item.riskState | enum 'riskState'}}</td>
              <td>{{item.filterUserNum}}</td>
              <td>{{item.isPushUserNum}}</td>
              <td>{{item.notPushUserNum}}</td>
              <td>
                <div @click="onSelectItem(item,true,0)" class="ui radio checkbox">
                  <input name="userCheckbox" v-model="item.checkType" value="0" type="radio">
                  <label>选择</label>
                </div>
                <!--&nbsp;&nbsp;&nbsp;&nbsp;-->
                <!--<div @click="onSelectItem(item,true,1)" class="ui radio checkbox">-->
                  <!--<input name="userCheckbox" v-model="item.checkType" value="1" type="radio">-->
                  <!--<label>未推送</label>-->
                <!--</div>-->
                <!--&nbsp;&nbsp;&nbsp;&nbsp;-->
                <!--<div @click="onSelectItem(item,true,2)" class="ui radio checkbox">-->
                  <!--<input name="userCheckbox" v-model="item.checkType" value="2" type="radio">-->
                  <!--<label>已推送</label>-->
                <!--</div>-->
              </td>
            </tr>
          </tbody>
          <tfoot>
            <tr>
              <th colspan="8"><pagination :page="pagination.page"></pagination></th>
  	        </tr>
          </tfoot>
        </table>
  	  </div>
    </div>
  </div>
  <div class="actions">
  	<div class="ui ok button green" @click="onClickOk()">确定</div>
  	<div class="ui cancel button" @click="onCancel()">取消</div>
  </div>
</div>
</template>

<script>
import tableMixin from '../../mixins/table/index';
import '../../filters';
import store from '../../store';

export default {
	mixins: [tableMixin],
	components: {
	},
	data() {
		return {
      state: store.state,
			table: {
				orderby: false,
				ajaxURL: '/activity/bpiUsersChoose',
				dimmer: true,
			},
      resultList: [],
			selectedItems: [],
      callback: null,
      code: '',
      tabIndex: 0
    }
	},
	ready() {
	},
  computed: {
    maxCount() {
      return 1;
    }
  },
	methods: {
    selectTab(tabIndex) {
      this.tabIndex = tabIndex;
      this.selectedItems.forEach((item) => {
        item.checkType = null;
        item.selected = false;
        this.selectedItems.$remove(item);
      });
      this.onQuery();
    },
//    loadTable() {
//      // $.ajax({
//      //   url: this.url,
//      //   dataType: 'json',
//      //   success: (result) => {
//          let result = {data:[{bpiNo: '1', remark: '北京'},{bpiNo: '2', remark: '上海'},{bpiNo: '3', remark: '深圳'}]};
//          this.resultList = result.data.map((item) => {
//            var selected = this.isSelected(item.id);
//            if(selected) {
//              return selected;
//            }
//            var data = {
//              id: item.bpiNo,
//              name: item.remark,
//              pushNum: item.pushNum,
//              riskState: item.riskState,
//              filterUserNum: item.filterUserNum,
//              isPushUserNum: item.isPushUserNum,
//              notPushUserNum: item.notPushUserNum,
//              selected: selected,
//              checkType: null
//            };
//    				return data;
//    			});
//          this.$nextTick(() => {
//            $(".checkbox").checkbox();
//            $(this.$el).modal("refresh");
//          });
//      //   }
//      // });
//    },
    onQuery() {
      this.loadTable();
    },
      show(selectedItems, callback) {
		  this.selectedItems = selectedItems;
          this.callback = callback;
		  $(this.$el).modal("show");
		  this.onQuery();
		},
      fillCustomQueryParams(params) {
      params.batchsNo = this.code;
      params.pinType = this.tabIndex;
		},
    updateTable(data, params) {
      this.setPage(data.result);
      this.resultList = data.result.resultList.map((item) => {
        var selected = this.isSelected(item.bpiId);
        if(selected) {
          return selected;
        }
        var data = {
          id: item.bpiId,
          name:item.bpiNo,
          remark: item.remark,
          pushNum: item.pushNum,
          riskState: item.riskState,
          filterUserNum: item.filterUserNum,
          isPushUserNum: item.isPushUserNum,
          notPushUserNum: item.notPushUserNum,
          selected: selected,
          checkType: null
        };
        return data;
      });
      this.$nextTick(() => {
        $(".checkbox").checkbox();
        $(this.$el).modal("refresh");
      });
    },
		isSelected(id) {
			for(var i=0; i<this.selectedItems.length; i++) {
				var item = this.selectedItems[i];
				if(item.id == id) {
					return item;
				}
			}
			return false;
		},
		onClickOk() {
      this.callback && this.callback(this.selectedItems.concat([]));
		},
		onSelectItem(item, selected, checkType) {
      this.selectedItems.forEach((item) => {
        item.checkType = null;
        item.selected = false;
  			this.selectedItems.$remove(item);
      });
      item.checkType = checkType;
      item.selected = true;
			this.selectedItems.push(item);
			this.$nextTick(() => $(this.$el).modal("refresh"));
		},
		onDeselect(item) {
      item.selected = false;
			this.selectedItems.$remove(item);
		},
		onCancel() {
		  this.callback && this.callback(this.selectedItems.concat([]));
		}
	}
}
</script>
<style>

</style>
<style scoped lang="less">
// .ui.modal {
//   width: 500px;
//   margin-left: calc(~'(100% - 500px) * 0.5') !important;
// }
.selected-items {
	border-bottom: 1px dashed #dddddd;
	font-size: 0 !important;
	> .label {
		margin-top: 2px;
	}

	.tips {
		border: none;
	}
}
.searchbar {
	padding-bottom: 20px;

	.query-btn {
		margin-left: 20px;
	}
}

.header .search {
	float: right;
	font-size: 0.6em;

	input {
		width: 300px;
	}
}

tbody tr {
	cursor: pointer;
}
.ui.table tr.positive, .ui.table td.positive {
    background: #FCFFF5 !important;
    color: #2C662D !important;
}


// .yunyin-modal.ui.modal {
// 	.header {
// 		padding-top: 10px;
// 		padding-bottom: 10px;
// 		font-size: 16px;
// 	}
// 	.selected-items {
// 		padding-top: 8px;
// 		padding-bottom: 10px;
// 	}
// }
</style>
