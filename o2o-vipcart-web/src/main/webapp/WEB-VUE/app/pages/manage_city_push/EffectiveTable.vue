<template language="html">
  <div class="table-panel">
    <table v-if="tables.length > 0" class="ui radio table mini">
      <thead>
      <tr>
        <th>源码</th>
        <th>映射码</th>
        <th>创建时间</th>
        <th>创建人</th>
        <th>可用次数</th>
        <th>已用次数</th>
        <th>开始时间</th>
        <th>结束时间</th>
        <th width="80">状态</th>
        <th width="150">操作</th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="item in tables">
        <td>{{item.vipcartCode}}</td>
        <td>
          <table-edit :text="item.reflectionCode" :url="'/vipcartCode/setReflectionCode?vipcartCode=' + item.vipcartCode"></table-edit>
        </td>
        <td>{{item.createTime}}</td>
        <td>{{item.createPin}}</td>
        <td>{{item.canUseTimes}}</td>
        <td>{{item.usedTimes}}</td>
        <td>{{item.beginTime}}</td>
        <td>{{item.endTime}}</td>
        <td>{{item.yn == 0 ? '有效' : '无效'}}</td>
        <td>
          <div class="ui teal buttons">
            <a class="ui button" @click="onCopy($event)"><copy text="复制链接" :content="'https://daojia.jd.com/activity/vipcarts/index.html?code=' + item.vipcartCode"></copy></a>
            <div class="ui floating dropdown icon button">
              <i class="dropdown icon"></i>
              <div class="menu">
                <a class="item" @click="onCancel(item)">取消</a>
              </div>
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
        <span>您还没有创建有效的地推码哦～～</span>
      </div>
    </div>
  </div>
</template>

<script>
  import * as utils from '../utils';
  import tableMixin from '../../mixins/table/index';
  import query from '../../utils/query';
  import TableEdit from '../common/TableEdit.vue';
  import Copy from '../common/Copy.vue';
  import '../../filters';

  export default {
    mixins: [tableMixin],
    components: {
      TableEdit,
      Copy
    },
    data() {
      return {
        table: {
          ajaxURL: '/vipcartCode/plist'
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
      onCancel(item) {
        $.ajax({
           url: '/vipcartCode/cancelDTvipcartCode',
           dataType: 'json',
           data: {vipcartCode: item.vipcartCode},
           success: (result) => {
             if(result.code == '0') {
               alert('提示：保存成功！');
               this.$parent.onQuery();
             } else {
               alert(result.msg || '提示：操作失败！');
             }
           }
        });
      },
      onCopy(event) {
        $(event.target).find(".copy").click();
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
