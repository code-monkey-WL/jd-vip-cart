<template>
  <div class="table-edit">
    <span v-if="!isEdit">{{text}}<a href="javascript:void(0);"><i class="edit icon" @click="onEdit()"></i></a></span>
    <span v-else class="table-edit-panel">
      <div class="ui input input-code"><input type="text" v-model="editText"></div>
      <div class="edit-button-panel">
        <a href="javascript:void(0);" @click="onCancel()">取消</a>
        <a href="javascript:void(0);" @click="onSave()">确定</a>
      </div>
    </span>
  </div>
</template>

<script>

export default {
  props: ['url', 'text'],
	data() {
		return {
      isEdit: false,
      editText: ''
    }
	},
	ready() {
	},
	methods: {
    onEdit() {
      this.editText = this.text;
      this.isEdit = true;
      this.$nextTick(() => {
        $(this.$el).find('input').eq(0).focus();
      });
    },
    onSave() {
      $.ajax({
        url: this.url,
        dataType: "json",
        data: {text: this.editText},
        success: (result) => {
          if(result.code == '0') {
            alert('提示：操作成功！');

            this.text = this.editText;
            this.onCancel();
          } else {
            alert(result.msg || '提示：操作失败！');
          }
        }
      });
    },
    onCancel() {
      this.isEdit = false;
    }
	}
}
</script>
<style>

</style>
<style scoped lang="less">
  @editWidth: 120px;
  .table-edit {
    width: @editWidth;

    .edit {
      margin-left: 10px;
    }

    .table-edit-panel {
      text-align: center;

      .input-code {
        width: @editWidth;
      }

      .edit-button-panel {
        margin-top: 5px;
        width: @editWidth;
        text-align: center;

        a:last-child {
          margin-left: 30px;
        }
      }
    }
  }
</style>
