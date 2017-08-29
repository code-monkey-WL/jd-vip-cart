<template>
<div class="ui modal small">
  <div class="header">
	  <span>选择城市</span>
  </div>
  <div class="content selected-items">
	  <div v-if="selectedItems.length <= 0" class="tips ui red basic label">未选择任何城市</div>
	  <div v-for="item in selectedItems" class="ui label">
		  {{item.name}}<i class="icon close" @click="onDeselect(item)"></i>
	  </div>
  </div>
  <div class="content table-content">
	  <div>
		  <table class="ui celled table mini selectable">
        <thead>
          <tr>
            <th width="50"><div class="ui checkbox"><input v-model="isCheckAll" type="checkbox" @change="checkAll($event)"></div></th>
            <th width="200">城市编号</th>
            <th>城市名称</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in resultList" @click="onSelectItem(item)" class="{{item.selected ? 'positive': ''}}">
            <td> <div class="ui checkbox" @click.stop=""><input v-model="item.selected" @change="onSelectItem(item,true)" type="checkbox"></div></td>
            <td>{{item.id}}</td>
            <td>{{item.name}}</td>
          </tr>
        </tbody>
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
import '../../filters';
import store from '../../store';

export default {
	components: {
	},
	data() {
		return {
      state: store.state,
      resultList: [],
			selectedItems: [],
      callback: null,
      isCheckAll: false
    }
	},
	ready() {
	},
  computed: {
    maxCount() {
      return 0;
    }
  },
	methods: {
    loadCity() {
      // $.ajax({
      //   url: '/baseInfo/city',
      //   dataType: 'json',
      //   success: (result) => {
          let result = {data:[{id: '1', name: '北京'},{id: '2', name: '上海'},{id: '3', name: '深圳'}]};
          this.resultList = result.data.map((item) => {
    			  var data = {
              id: item.id,
              name: item.name,
              selected: this.isSelected(item.id)
            };
    				return data;
    			});
          this.$nextTick(() => {
            $(".checkbox").checkbox();
            $(this.$el).modal("refresh");
          });
      //   }
      // });
    },
		show(selectedItems, callback) {
		  this.selectedItems = selectedItems;
      this.callback = callback;
			$(this.$el).modal("show");
			this.loadCity();
		},
		isSelected(id) {
			for(var i=0; i<this.selectedItems.length; i++) {
				var item = this.selectedItems[i];
				if(item.id == id) {
					return true;
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
        if(this.title === "商家") {
          if(this.selectedItems.length >= this.maxCount) {
	          item.selected = !item.selected;
            return;
          }
        }
				this.selectedItems.push(item);
			} else {
		    for(var i = 0; i < this.selectedItems.length; i++) {
          if(item.id == this.selectedItems[i].id) {
            item = this.selectedItems[i];
          }
        }
				this.selectedItems.$remove(item);
			}
			this.$nextTick(() => $(this.$el).modal("refresh"));
		},
		onDeselect(item) {
      item.selected = false;
      for(var i = 0; i < this.selectedItems.length; i++) {
        if(item.id == this.selectedItems[i].id) {
          item = this.selectedItems[i];
        }
      }
			this.selectedItems.$remove(item);
		},
		onCancel() {
		  this.callback && this.callback([]);
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
