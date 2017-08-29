<template>
  <div v-if="getList.length > 0" class="list-panel">
    <div v-if="getList.length <= maxCount" class="selected-items">
      <div v-for="item in getList" class="ui label">
  		  {{item.name}}<i v-if="getIsEdit" class="icon close" @click="onDeselect(item)"></i>
  	  </div>
    </div>
    <div v-else class="list-count">
      已选择{{getList.length}}个{{title}} <a href="javascript:void(0);" @click="onEdit()">{{getIsEdit ? '编辑' : '查看'}}</a>
      <edit-list-item v-ref:edit :title="title"></edit-list-item>
    </div>
  </div>
</template>

<script>
import '../../filters';
import store from '../../store';
import EditListItem from './EditListItem.vue';

export default {
  props: ['list', 'title', 'count', 'isedit'],
  components: {
    EditListItem
  },
	data() {
		return {
      maxCount: 1
    }
	},
  computed: {
    maxCount() {
      if(this.count) {
        return this.count;
      }
      return 10;
    },
    getIsEdit() {
      if(this.isedit === 'true' || this.isedit === true) {
        return true;
      }
      return false;
    },
    getList() {
      if(this.list) {
        return this.list;
      }
      return [];
    }
  },
	ready() {
	},
	methods: {
    onDeselect(item) {
      this.list.$remove(item);
    },
    onEdit() {
      this.$refs.edit.show(this.getList, this.getIsEdit, (list) => {
        if(list.length > 0) {
          let t = [];
          this.list.forEach((item) => {
            let remove = true;
            list.forEach((temp) => {
              if(temp.id == item.id) {
                remove = false;
              }
            });
            if(remove) {
              t.push(item);
            }
          });
          t.forEach((item) => {
            this.list.$remove(item);
          });
        } else {
          let t = []
          this.list.forEach((item) => {
            t.push(item);
          });
          t.forEach((item) => {
            this.list.$remove(item);
          });
        }
      });
    }
	}
}
</script>
<style scoped lang="less">
  .list-panel {
    display: inline-block;

    .selected-items {
    	font-size: 0 !important;
    	> .label {
    		margin-top: 2px;
    	}

    	.tips {
    		border: none;
    	}
    }
  }
</style>
