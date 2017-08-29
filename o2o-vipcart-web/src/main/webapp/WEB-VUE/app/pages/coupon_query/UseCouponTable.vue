<template language="html">
  <div>
    <div class="page-search">
      <ul class="conditions">
        <li>
          <label>订单号</label>
          <div class="ui input">
            <input type="text" v-model="query.orderNo">
          </div>
          <span class="spacing"></span><span class="spacing"></span>
          <label>到家PIN</label>
          <div class="ui input">
            <input type="text" v-model="query.userPin">
          </div>
        </li>
        <li>
          <label>手机号</label>
          <div class="ui input">
            <input type="text" v-model="query.phone">
          </div>
        </li>
      </ul>
      <div class="search-button">
        <a @click="onReset()" class="reset">重置</a>
        <a @click="onQuery()"><i class="inverted circular search link icon"></i></a>
      </div>
    </div>
    <div class="table-panel">
      <table v-if="tables.length > 0" class="ui radio table mini">
        <thead>
        <tr>
          <th>订单号</th>
          <th>到家pin</th>
          <th>手机号</th>
          <th>用券时间</th>
          <th>状态</th>
          <th>手机号</th>
          <th>优惠券编码</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="item in tables">
          <td>{{item.orderNo}}</td>
          <td>{{item.userPin}}</td>
          <td>{{item.phone}}</td>
          <td>{{item.consumeTime}}</td>
          <td>{{item.state == 2 ? '已使用':item.state}}</td>
          <td>{{item.phone}}</td>
          <td>{{item.vipcartId}}</td>
        </tr>
        </tbody>
        <tfoot>
        <tr>
          <th colspan="7">
            <pagination :page="pagination.page"></pagination>
          </th>
        </tr>
        </tfoot>
      </table>
      <div v-else class="table-none">
        <div class="content">
          <img src="../../images/icon-data-none.png"/>
          <span>{{notQueryText}}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
  import * as utils from '../utils';
  import tableMixin from '../../mixins/table/index';
  import query from '../../utils/query';
  import '../../filters';
  let queryDefault = {
    orderNo: '',
    userPin: '',
    phone: ''
  };

  export default {
    mixins: [tableMixin],
    data() {
      return {
        table: {
          ajaxURL: '/vipcart/uservipcartInfoPageQuery'
        },
        tables: [],
        checkList: [],
        isQuery: false,
        query: Object.assign({}, queryDefault)
      }
    },
    computed: {
      notQueryText() {
        if(this.isQuery) {
          return '没有搜索到结果哦～～';
        }
        return '您还没有进行搜索哦～～';
      }
    },
    ready() {
    },
    methods: {
      fillCustomQueryParams(params) {
        for(var filed in this.query) {
          if(this.query[filed]) {
            params[filed] = this.query[filed];
          }
        }
        this.query.state = 2;
      },
      updateTable(data, params) {
        this.updateTableMixins(data, params);
        this.setPage(data.result);
        this.tables = data.result.resultList;
      },
      onQuery() {
        this.isQuery = true;
        this.loadTable();
      },
      onReset() {
        this.query = Object.assign({}, queryDefault);
        // $(this.$el).find('.conditions').form("clear");
      }
    }
  }
</script>

<style scoped lang="less">
  .page-search {
    border-bottom: 0px;
    > ul {
      > li {
        > label{
          width: 70px;
          text-align: right;
          display: inline-block;
        }
      }
    }
  }
  .table-panel {
    margin-top: 20px;
    .info {
      color: #999;
    }
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
