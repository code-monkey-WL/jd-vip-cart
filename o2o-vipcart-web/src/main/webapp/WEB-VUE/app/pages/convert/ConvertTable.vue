<template language="html">
  <div class="table-panel">
    <table v-if="tables.length > 0" class="ui radio table mini">
      <thead>
        <tr>
            <th>活动编码</th>
            <th>活动名称</th>
            <!--<th>城市</th>-->
            <th>领取模式</th>
            <th>码开始时间</th>
            <th>码结束时间</th>
            <th>创建时间</th>
            <th>创建人</th>
            <th>生码数</th>
            <th width="80">状态</th>
            <th width="160">操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="item in tables">
          <td><vue-tip :text="item.activityCode" width="100"></vue-tip></td>
          <td>{{item.activityName}}</td>
          <!--<td>{{item.cityName}}</td>-->
          <td>{{item.grabMode == 2 ? '领取全部' : '领取一张'}}</td>
          <td>{{item.codeBeginTime}}</td>
          <td>{{item.codeEndTime}}</td>
          <td>{{item.createTime}}</td>
          <td>{{item.createPin}}
          <td>{{item.totalNum}}</td>
          <td>
            {{item.activityState | enum 'activityState'}}
          </td>
          <td>
            <div class="ui teal buttons">
              <a class="ui button" href="vipcart.html?activityCode={{item.activityCode}}&activityName={{item.activityName}}&activityState={{item.activityState}}&pageType=convert">管理优惠券</a>
              <div class="ui floating dropdown icon button">
                <i class="dropdown icon"></i>
                <div class="menu">
                  <a class="item"  href="/vipcartCode/exchangeCodeExportExcel?activityName={{item.activityName}}&activityCode={{item.activityCode}}">导出兑换码</a>
              </div>
            </div>
          </td>
        </tr>
      </tbody>
      <tfoot>
        <tr>
          <th colspan="10">
            <pagination :page="pagination.page"></pagination>
          </th>
        </tr>
      </tfoot>
    </table>
    <div v-else class="table-none">
      <div class="content">
        <img src="../../images/icon-data-none.png"/>
        <span>您还没有进行搜索哦～～</span>
      </div>
    </div>
  </div>
</template>

<script>
import * as utils from '../utils';
import tableMixin from '../../mixins/table/index';
import query from '../../utils/query';
import VueTip from '../common/VueTip.vue';
import '../../filters';

export default {
	mixins: [tableMixin],
  components:{
    VueTip
  },
  data() {
    return {
			table: {
				ajaxURL: '/activity/pageQueryExchangeCodeActivity'
			},
      tables: [],
      checkList: [],
      query: {
      }
    }
  },
  ready() {
  },
  methods: {
    setParams(query) {
      this.query = query;
    },
		fillCustomQueryParams(params) {
      for(var filed in this.query) {
        if(this.query[filed] && this.query[filed] != -1) {
          params[filed] = this.query[filed];
        }
      }
		},
		updateTable(data, params) {
			this.updateTableMixins(data, params);
			this.setPage(data.result);
			this.tables = data.result.resultList;
      this.$nextTick(() => {
        $(this.$el).find(".ui.dropdown").dropdown();
      });
		}
  }
}
</script>

<style scoped lang="less">
  .table-panel {
    margin-top: 20px;
  }
  .table-none {
    width: 100%;
    height: 400px;
    line-height: 400px;
    text-align: center;
    position: relative;

    .content {
      height: 50px;

      span {
        height: 100px;
        line-height: 50px;
        display: inline-block;
        vertical-align: middle;
        color: #666;
      }
    }
  }
</style>
