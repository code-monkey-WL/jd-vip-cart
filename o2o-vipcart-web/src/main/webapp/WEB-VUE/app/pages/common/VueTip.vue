<template>
  <div class="vue-panel">
    <span class="vue-tip" data-position="top center">
      {{text}}
    </span>
    <div class="ui popup vue-popup">
      {{text}}<a href="javascript:void(0);"><copy text="复制" :content="text"></copy></a>
    </div>
  </div>
</template>

<script>
import Copy from './Copy.vue';

export default {
  props: ['text', 'width'],
  components:{
    Copy
  },
	data() {
		return {
    }
	},
	ready() {
    if(this.width) {
      $(this.$el).find('.vue-tip').css('width', this.width + 'px');
    }
    $(this.$el).popup({
      popup: $(this.$el).find('.vue-popup'),
      hoverable: true
    });
	},
	methods: {
	}
}
</script>
<style>

</style>
<style scoped lang="less">
  .vue-panel {
    white-space: nowrap;
  }
  .vue-tip {
    width: 200px;
    display: inline-block !important;
    overflow: hidden;
    text-overflow:ellipsis;
    white-space: nowrap;
  }
  .ui.popup.vue-popup {
    width: auto !important;
    min-width: auto !important;
    max-width: 1000px !important;

    a {
      margin-left: 10px;
    }
  }
</style>
