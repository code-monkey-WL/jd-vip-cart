<template>
  <div v-for="item in list" class="four wide column city-item">
    <div class="city-content">
      <div class="ui ribbon huge label" :class="{'blue': item.activityState != enums.activityState.lock.code, 'gray': item.activityState == enums.activityState.lock.code}">{{item.cityName}}</div>
      <div class="city-title "><span>活动状态：{{item.activityState | enum 'activityState'}} </span>
      <a v-if="item.activityState != enums.activityState.create.code" href="javascript:void(0);" @click="onOpen($event, item)">{{item.activityState == enums.activityState.lock.code ? '启动活动' : '取消活动'}}</a></div>
      <div class="city-button">
        <a class="ui secondary button mini" href="manage_city_push.html?cityCode={{item.cityCode}}&activityName={{item.activityName}}&activityCode={{item.activityCode}}">管理地推码</a>
        &nbsp;&nbsp;&nbsp;&nbsp;
        <a class="ui secondary button mini" href="vipcart.html?activityCode={{item.activityCode}}&activityName={{item.activityName}}&activityState={{item.activityState}}&pageType=city_push">管理优惠券</a>
      </div>
      <ul>
        <li><span class="list-title">城市：</span><span>{{item.cityName}}</span></li>
        <li><span class="list-title">活动编码：</span><span>{{item.activityCode}}<br/><a href="javascript:void(0);"><copy text="复制活动编码" :content="item.activityCode" el='.city-content'></copy></a></span></li>
        <li><span class="list-title">URL地址：</span><span>https://daojia.jd.com/activity/vipcarts/index.html?code={{item.activityCode}}<br/><a href="javascript:void(0);"><copy text="复制URL地址" :content="'https://daojia.jd.com/activity/vipcarts/index.html?code=' + item.activityCode" el='.city-content'></copy></a></span></li>
      </ul>
    </div>
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
      $.ajax({
        url: '/activity/switchDTActivity',
        dataType: 'json',
        data: {
          activityCode: item.activityCode,
          activityState: item.activityState == this.enums.activityState.lock.code ? 3 : 4
        },
        success: (result) => {
          if(result.code == '0') {
            if(item.activityState == this.enums.activityState.lock.code) {
              this.showDimmer(event, '启动成功');
              item.activityState = this.enums.activityState.open.code;
            } else {
              this.showDimmer(event, '取消成功');
              item.activityState = this.enums.activityState.lock.code;
            }
          }else {
            alert(result.msg || '提示：取消活动失败！');
          }
        }
      });

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

.city-item {
  width: 100%;
  height: 190px;
  position: relative;
  &:after {
    content: '';
    display: inline-block;
    clear: both;
  }

  .city-content {
    width: calc(~'100% - 28px');
    height: calc(~'100% - 20px');
    border: 1px solid @borderColor;
    padding: 1em;
    position: absolute;
    background-color: #fff;
    z-index: 1;

    .city-title {
      border-bottom: 1px solid @borderColor;
      text-align: center;
      padding-top: 20px;
      padding-bottom: 20px;

      a {
        margin-left: 40px;
      }
    }

    .city-button {
      text-align: center;
      padding-top: 20px;
      padding-bottom: 20px;
    }

    ul {
      display: none;
      width: 100%;

      li {
        list-style: none;
        white-space: nowrap;
        margin-bottom: 20px;

        span {
          display: inline-block;
          white-space: normal;
          word-break: break-all;
          width: 75%;
        }

        .list-title {
          width: auto;
          vertical-align: top;
          white-space: nowrap;
        }
      }
    }

    &:hover {
      height: auto;
      z-index: 2;
      border: 2px solid #00a2ff;

      ul {
        display: block;
      }

      .city-button {
        border-bottom: 1px solid @borderColor;
      }
    }
  }
}

.ui.blue.labels .label, .ui.blue.label {
  background-color: #00a2ff !important;
  border-color: #00a2ff !important;
  color: #FFFFFF !important;
}

.ui.blue.ribbon.label {
  border-color: #0080bb !important;
}

.ui.gray.labels .label, .ui.gray.label {
  background-color: #cccccc !important;
  border-color: #cccccc !important;
  color: #FFFFFF !important;
}

.ui.gray.ribbon.label {
  border-color: #aaaaaa !important;
}

.ui.ribbon.label {
  margin-left: 0.7em;
  border-radius: 0px;
  padding-left: 50px;
  padding-right: 80px;
}

.ui.ribbon.label:after {
  position: absolute;
  content: '';
  top: 100%;
  left: 0%;
  background-color: transparent !important;
  border-style: solid;
  border-width: 0em 0.5em 0.3em 0em;
  border-color: transparent;
  border-right-color: inherit;
  width: 0em;
  height: 0em;
}

.ui.ribbon.label:before {
  position: absolute;
  content: '';
  top: 0%;
  right: 0%;
  background-color: transparent !important;
  border-style: solid;
  border-width: 1.1em 1.5em 1.05em 1.1em;
  border-color: transparent;
  border-right-color: #ffffff ;
  width: 0em;
  height: 0em;
}
</style>
