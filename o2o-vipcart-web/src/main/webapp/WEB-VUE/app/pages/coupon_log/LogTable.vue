<template language="html">
  <div class="table-panel">
    <table v-if="tables.length > 0" class="ui radio table mini">
      <thead>
      <tr>
        <th>用户ID</th>
        <th>领券时间</th>
        <th>用券时间</th>
        <th>用券订单</th>
        <th>用券门店</th>
        <th>券状态</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="item in tables">
        <td>{{item.userPin}}</td>
        <td>{{item.grabTime}}</td>
        <td>{{item.consumeTime}}</td>
        <td>{{item.orderNo}}</td>
        <td>{{item.stationName}}</td>
        <td>{{item.state | enum 'state'}}</td>
      </tr>
      </tbody>
      <tfoot>
      <tr>
        <th colspan="6">
          <pagination :page="pagination.page"></pagination>
        </th>
      </tr>
      </tfoot>
    </table>
    <div v-else class="table-none">
      <div class="content">
        <img src="../../images/icon-data-none.png"/>
        <span>当前优惠券未产生日志哦～～</span>
      </div>
    </div>
  </div>
</template>

<script>
  import * as utils from '../utils';
  import tableMixin from '../../mixins/table/index';
  import query from '../../utils/query';
  import '../../filters';

  export default {
    mixins: [tableMixin],
            data() {
      return {
        table: {
          ajaxURL: '/vipcart/uservipcartInfoPageQuery'
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
        params.vipcartId = query("vipcartId");
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
