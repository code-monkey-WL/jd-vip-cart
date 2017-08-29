<template>
<div class="ui modal yunyin-modal mini">
  <div class="header">
	  <span>已选择的{{title}}</span>
  </div>
  <div class="content table-content">
	  <div>
		  <table class="ui celled table mini selectable">
        <thead>
          <tr>
            <th width="200">编号</th>
            <th>名称</th>
            <th v-if="isEdit" width="50">操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in resultList">
            <td>{{item.id}}</td>
            <td>{{item.name}}</td>
            <td v-if="isEdit"><a href="javascript:void(0);" @click="onDeselect(item)">删除</a></td>
          </tr>
        </tbody>
      </table>
	  </div>
  </div>
  <div class="actions">
  	<div v-if="isEdit" class="ui ok button green" @click="onClickOk()">确定</div>
  	<div class="ui cancel button" @click="onCancel()">取消</div>
  </div>
</div>
</template>

<script>

export default {
  props: ['title'],
	data() {
		return {
      resultList: [],
      callback: null,
      isEdit: false
    }
	},
	ready() {
	},
  computed: {
  },
	methods: {
		show(list, isEdit, callback) {
      this.isEdit = isEdit;
		  this.resultList = list.concat([]);
      this.callback = callback;
			$(this.$el).modal("show");
            this.$nextTick(() => {
                $(this.$el).modal("refresh");   
            });
		},
		onClickOk() {
      this.callback && this.callback(this.resultList);
		},
		onDeselect(item) {
			this.resultList.$remove(item);
		},
		onCancel() {

		}
	}
}
</script>
<style>

</style>
<style scoped lang="less">
    .table-content {
        max-height: 500px;
        overflow: auto;
    }
</style>
