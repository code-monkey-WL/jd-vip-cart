<template language="html">
  <div class="table-panel">
    <table v-if="tables.length > 0" class="ui radio table mini">
      <thead>
      <tr>
        <th>优惠券ID</th>
        <th>优惠券名称</th>
        <th>优惠券类型</th>
        <th>创建时间</th>
        <th>创建人</th>
        <th>领券开始时间</th>
        <th>领券结束时间</th>
        <th>面值</th>
        <th>权重</th>
        <th width="80">状态</th>
        <th width="150">操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="item in tables">
        <td>{{item.vipcartId}}</td>
        <td>{{item.vipcartName}}</td>
        <td>
          {{item.vipcartType | enum 'vipcartType'}}
        </td>
        <td>{{item.createTime}}</td>
        <td>{{item.createPin}}</td>
        <td>{{item.grabBeginTime}}</td>
        <td>{{item.grabEndTime}}</td>
        <td>{{item | enum 'showName'}}</td>
        <td>
          <div v-if="item.vipcartState != 6 && item.vipcartState != 7 && item.vipcartState != 8">
            <table-edit :text="item.winProbality" :url="'/vipcart/updatevipcartWinProbability?vipcartId=' + item.vipcartId"></table-edit>
          </div>
          <div v-else>
            {{item.winProbality}}
          </div>
        </td>
        <td>{{item.vipcartState | enum 'vipcartState'}}</td>
        <td>
          <div class="ui teal buttons">
            <a class="ui button" href="edit_vipcart.html?vipcartId={{item.vipcartId}}&type=3">查看详情</a>
            <div class="ui floating dropdown icon button">
              <i class="dropdown icon"></i>
              <div class="menu">
                <a class="item" href="vipcart_log.html?vipcartId={{item.vipcartId}}">发券日志</a>
                <a v-if="item.vipcartState !=6 && item.vipcartState !=7 && item.vipcartState !=8"  class="item" @click="onCancel(item.vipcartId)">取消</a>
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
        <span>您还没有创建有效的优惠券哦～～</span>
      </div>
    </div>
  </div>
</template>

<script>
  import * as utils from '../utils';
  import tableMixin from '../../mixins/table/index';
  import query from '../../utils/query';
  import TableEdit from '../common/TableEdit.vue';
  import '../../filters';
  import confirm from '../../utils/confirm';

  export default {
    mixins: [tableMixin],
    components: {
      TableEdit
    },
    data() {
      return {
        table: {
          ajaxURL: '/vipcart/plist'
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
        params.activityCode = query("activityCode");
        params.yn = 0;
        for(var filed in this.query) {
          if(this.query[filed]) {
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
      },
      onCancel(vipcartId) {
        confirm('确定取消吗？', () => {
          $.ajax({
            url: '/vipcart/cancel',
            dataType: 'json',
            data: {vipcartId: vipcartId},
            success: (result) => {
              if(result.code == '0') {
                location.reload();
              } else {
                alert(result.msg || "提示：操作失败！");
              }
            }
          });
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
