<template>
<div class="ui modal small">
  <div class="header">
	  <span>选择{{title}}</span>
	  <div class="ui search mini">
      <div class="ui icon input">
        <input class="prompt" type="text" placeholder="输入{{title}}编号，回车进行搜索" v-model="code" @keyup.enter="onQuery()">
        <i class="search icon"></i>
      </div>
      <div class="results"></div>
    </div>
  </div>
  <div class="content selected-items">
	  <div v-if="selectedItems.length <= 0" class="tips ui red basic label">未选择任何{{title}}</div>
	  <div v-for="item in selectedItems" class="ui label">
		  {{item.name}}<i class="icon close" @click="onDeselect(item)"></i>
	  </div>
  </div>
  <div  class="content table-content">
	  <div>
		  <table class="ui celled table mini selectable">
        <thead>
          <tr>
            <th width="50"><div class="ui checkbox"><input v-if="getAll" v-model="isCheckAll" type="checkbox" @change="checkAll($event)"></div></th>
            <th width="200">{{title}}编号</th>
            <th>{{title}}名称</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in resultList" @click="onSelectItem(item)" class="{{item.selected ? 'positive': ''}}">
            <td> <div class="ui checkbox" @click.stop=""><input v-model="item.selected" @change="onSelectItem(item,true)" :disabled="this.selectedItems.length >= this.maxCount && item.selected == false" type="checkbox"></div></td>
            <td>{{item.id}}</td>
            <td>{{item.name}}</td>
          </tr>
        </tbody>
        <tfoot>
          <tr>
            <th colspan="3"><pagination :page="pagination.page"></pagination></th>
	        </tr>
        </tfoot>
      </table>
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
  props: ['url', 'title', 'selectcount', 'isall'],
	components: {
	},
	data() {
		return {
      state: store.state,
			table: {
				orderby: false,
				ajaxURL: this.url,
				dimmer: true,
			},
      resultList: [],
			selectedItems: [],
      callback: null,
      isCheckAll: false,
      code: ''
    }
	},
	ready() {
	},
  computed: {
    maxCount() {
      if(this.selectcount) {
        return this.selectcount;
      }
      return 0;
    },
    getAll() {
      if(this.isAll == "true") {
        return true;
      }
      return false;
    }
  },
	methods: {
    onQuery() {
      this.loadTable();
    },
		show(selectedItems, callback) {
        this.table.ajaxURL = this.url;
      this.clear();
		  this.selectedItems = selectedItems;
      this.callback = callback;
			$(this.$el).modal("show");
			this.onQuery();
		},
      fillCustomQueryParams(params) {

      params.code = this.code;
		},
    updateTable(data, params) {
      this.setPage(data.data);
      this.resultList = data.data.resultList.map((item) => {
        var selected = this.isSelected(item.id);
        if(selected) {
          return selected;
        }
        var data = {
          id: item.id,
          name: item.name,
          selected: selected
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
    checkAll(event) {
        if(this.isCheckAll) {
          this.selectedItems = this.resultList.concat([]);
          this.resultList.forEach((item) => {
            item.selected = true;
          });
        } else {
          this.selectedItems = [];
          this.resultList.forEach((item) => {
            item.selected = false;
          });
        }
    },
		onSelectItem(item, selected) {
      if(!selected){
  			item.selected = !item.selected;
      }
			if(item.selected) {
        if(this.selectedItems.length >= this.maxCount) {
          item.selected = !item.selected;
          // alert("提示：最多选择"+this.maxCount + "个" + this.title);
          // item.selected = false;
          return false;
        }
				this.selectedItems.push(item);
			} else {
				this.selectedItems.$remove(item);
			}
			this.$nextTick(() => $(this.$el).modal("refresh"));
		},
		onDeselect(item) {
      item.selected = false;
			this.selectedItems.$remove(item);
		},
		onCancel() {
		  this.callback && this.callback([]);
		},
    clear() {
      this.isCheckAll = false;
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
