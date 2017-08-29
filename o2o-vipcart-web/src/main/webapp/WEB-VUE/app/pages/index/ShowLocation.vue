<template>
<div class="ui modal small">
  <div class="header">
	  <span>显示设置</span>
  </div>
  <div  class="content">
    <div class="info">你可以设置此活动的优惠券在指定的页面显示：</div>
    <div class="item">
      <div class="ui checkbox">
        <input type="checkbox" value="101" v-model="showLocation">
        <label>门店页</label>
      </div>
    </div>
  </div>
  <div class="actions">
  	<div class="ui button green" @click="onClickOk()">确定</div>
  	<div class="ui cancel button">取消</div>
  </div>
</div>
</template>

<script>

export default {
	data() {
      return {
        item: {},
        showLocation: []
      }
	},
	ready() {
	},
  computed: {
  },
	methods: {
		show(item, callback) {
          if(!item.showLocation) {
            item.showLocation = [];
          } else {
            if(typeof item.showLocation == 'string') {
              item.showLocation = item.showLocation.split(',');
            }
          }
          this.item = item;
            let showLocation = [];
            item.showLocation.forEach((v) => {
                if(v != '100') {
                    showLocation.push(v);
                }
            });
          this.showLocation = showLocation;
          $(this.$el).modal("show");
		},
		onClickOk() {
          let showLocation = '';
          if(Object.prototype.toString.call(this.showLocation) == '[object Array]') {
            showLocation = this.showLocation.join(',');
          }
          $.ajax({
            url: "/activity/updateActivityShowLocation",
            dataType: 'json',
            data: Object.assign(this.item, {showLocation: showLocation}),
            success: (result) => {
              if(result.code == '0') {
                alert("提示： 设置成功！");
                this.item.showLocation = showLocation;
                this.close();
              }else{
                alert(result.msg+":"+result.detail || '提示：操作失败！');
              }
            }
          });
      },
		close() {
      $(this.$el).modal("hide");
		}
	}
}
</script>
<style>

</style>
<style scoped lang="less">
.content {
  min-height: auto;
  padding-left: 100px !important;

  .item {
    margin-top: 30px;
    margin-bottom: 30px;
  }
}
</style>
