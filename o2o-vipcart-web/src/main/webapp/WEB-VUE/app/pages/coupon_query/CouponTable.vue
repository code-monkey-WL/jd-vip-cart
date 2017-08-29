<template language="html">
  <div>
    <div class="page-search">
      <ul class="conditions">
        <li>
          <label>券名称</label>
          <div class="ui input">
            <input type="text" v-model="query.vipcartName">
          </div>
          <div class="ui checkbox">
            <input type="checkbox" v-model="query.isFilterTestActivity" value="1"><label>过滤测试数据</label>
          </div>
          <span class="spacing"></span><span class="spacing"></span>
          <label>优惠券编码</label>
          <div class="ui input">
            <input type="text" v-model="query.vipcartId">
          </div>
        </li>
        <li>
          <label>活动编码</label>
          <div class="ui input">
            <input type="text" v-model="query.activityCode">
          </div>
          <span class="spacing"></span><span class="spacing"></span>
          <label>创建渠道</label>
          <div class="ui input">
            <select class="ui dropdown" v-model="query.channelSource">
              <option value="-1">全部</option>
              <option value="1">运营平台</option>
              <option value="2">商家中心</option>
              <option value="3">精明管家</option>
              <option value="4">活动提报</option>
              <option value="5">用户画像</option>
            </select>
          </div>
          <span class="spacing"></span><span class="spacing"></span>
          <label>类型</label>
          <div class="ui input">
            <select class="ui dropdown" v-model="query.vipcartType">
              <option value="-1">全部</option>
              <option value="1">满减</option>
              <option value="5">满折</option>
              <option value="2">立减</option>
              <option value="3">免运费</option>
              <option value="4">免N元运费</option>
            </select>
          </div>
        </li>
        <li>
          <label>状态</label>
          <div class="ui input">
            <select class="ui dropdown" v-model="query.vipcartState">
              <option value="-1">全部</option>
              <option value="9">未开始</option>
              <option value="10">进行中</option>
              <!--<option value="5">已领完</option>-->
              <option value="7">已结束</option>
              <option value="8">已取消</option>
            </select>
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
          <th>券名称</th>
          <th>创建时间</th>
          <th>创建人</th>
          <th>所属活动</th>
          <th>类型</th>
          <th>平台承担比例</th>
          <th>发券量</th>
          <th>领券量</th>
          <th>用券量</th>
          <th width="80">状态</th>
          <th width="150">操作</th>
        </tr>
        </thead>
        <tbody>
        <tr v-for="item in tables">
          <td>
            {{item.vipcartName}}
            <div class="info">标题：{{item.showName}}</div>
          </td>
          <td>{{item.createTime}}</td>
          <td>{{item.createPin}}</td>
          <td>
            {{item.activityName}}
            <div class="info">类型：{{item.activityType | enum 'activityType'}}</div>
          </td>
          <td>
            {{item.vipcartType | enum 'vipcartType'}}
            <div class="info">{{item | enum 'showName'}}</div>
          </td>
          <td>{{item.platShareProportion}}</td>
          <td>{{item.totalNum}}</td>
          <td>{{item.currentNum}}</td>
          <td>{{item.usedNum}}</td>
          <td>{{item.vipcartState | enum 'vipcartState'}}</td>
          <td>
            <div class="ui teal buttons">
              <a class="ui button" href="edit_vipcart.html?vipcartId={{item.vipcartId}}&type=3">查看详情</a>
              <div class="ui floating dropdown icon button">
                <i class="dropdown icon"></i>
                <div class="menu">
                  <a class="item" href="vipcart_log.html?vipcartId={{item.vipcartId}}">发券日志</a>
                  <a class="item">取消活动</a>
                </div>
              </div>
            </div>
          </td>
        </tr>
        </tbody>
        <tfoot>
        <tr>
          <th colspan="11">
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
    vipcartName: '',
    isFilterTestActivity: '',
    vipcartId: '',
    activityCode: '',
    channelSource: '-1',
    vipcartType: '-1',
    vipcartState: '-1'
  };

  export default {
    mixins: [tableMixin],
    data() {
      return {
        table: {
          ajaxURL: '/vipcart/vipcartManagePage'
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
          $(this.$el).find(".table-panel .ui.dropdown").dropdown();
        });
      },
      onQuery() {
        this.isQuery = true;
        this.loadTable();
      },
      onReset() {
        $(this.$el).find("select").dropdown("restore defaults");
        this.query = Object.assign({}, queryDefault);
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
