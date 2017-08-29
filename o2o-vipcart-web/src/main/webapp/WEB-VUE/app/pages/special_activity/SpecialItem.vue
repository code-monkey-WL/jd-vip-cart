<template>
  <div v-for="item in list" class="four wide column">
    <a href="index.html?purpose={{item.purpose}}&activityType={{item.activityType}}">
      <div class="special-item">
        <div class="img-panel">
          <img :src="item.url">
        </div>
        <div class="button-panel">
          <a class="ui secondary button">{{item.name}}</a>
        </div>
      </div>
    </a>
  </div>
</template>

<script>
import '../../filters';
import store from '../../store';
import dimmer from '../../mixins/ok-dimmer';
import Copy from '../common/Copy.vue';

export default {
  mixins: [dimmer],
  components: { Copy },
  props: ['list'],
	data() {
		return {
      enums: store.state.enums
    }
	},
	ready() {

	},
	methods: {
    onOpen(event, item) {
      if(item.activityState == this.enums.activityState.cancel.code) {
        this.showDimmer(event, '启动成功');
        item.activityState = this.enums.activityState.open.code;
      } else {
        this.showDimmer(event, '取消成功');
        item.activityState = this.enums.activityState.cancel.code;
      }
    },
    onCopy(event, type, item) {
      if(type == 0) {
        this.showDimmer(event, '复制活动编码成功');
      } else {
        this.showDimmer(event, '复制URL<br>地址成功');
      }
    },
    showDimmer(event, content) {
      this.dimmer({el: $(event.target).parents(".city-content"),content: content});
    }
	}
}
</script>
<style scoped lang="less">
@borderColor:#e5e5e5;

.special-item {
  border: 2px solid @borderColor;
  padding: 20px;
  padding-top: 0px;

  .img-panel {
    text-align: center;
    border-bottom: 1px dotted @borderColor;

    img {
      width: 100%;
      max-width: 276px;
    }
  }

  .button-panel {
    margin-top: 20px;
    text-align: center;
  }
}
</style>
