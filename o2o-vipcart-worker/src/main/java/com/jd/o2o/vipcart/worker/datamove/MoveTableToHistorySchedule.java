//package com.jd.o2o.vipcart.worker.datamove;
//
//import com.jd.o2o.vipcart.common.utils.DateFormatUtils;
//import com.jd.o2o.vipcart.service.base.busi.SpyBasicService;
//import com.jd.o2o.vipcart.domain.inside.worker.ScheduleInput;
//import com.jd.o2o.vipcart.worker.domain.SqlQueryEntity;
//import com.jd.o2o.vipcart.common.plugins.log.track.LoggerTrackFactory;
//import com.jd.o2o.vipcart.common.utils.BeanHelper;
//import com.jd.o2o.vipcart.common.utils.json.JsonUtils;
//import com.taobao.pamirs.schedule.IScheduleTaskDealMulti;
//import com.taobao.pamirs.schedule.TaskItemDefine;
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang.StringUtils;
//import org.slf4j.Logger;
//
//import javax.annotation.Resource;
//import java.util.*;
//
///**
// * 通用数据库表迁移worker
// * 前置条件：
// * 1. 迁移表和被迁移表数据结构完全一致
// * 2. 迁移表要提前生成并且名称是被迁移表+"_his"的格式
// * <p/>
// * Created by liuhuiqing on 2017/6/16
// */
//public class MoveTableToHistorySchedule implements IScheduleTaskDealMulti<SqlQueryEntity> {
//    private static final Logger LOGGER = LoggerTrackFactory.getLogger(MoveTableToHistorySchedule.class);
//    private static final Map<String, String> tableConfigMap = new HashMap<String, String>();
//    @Resource
//    private SpyBasicService spyBasicServiceImpl;
//
//    static {
//        // key是要迁移表名，value是要迁移数据源表的sql查询条件
//        tableConfigMap.put("batch_push_vipcart", " where push_state=1 ");// 状态 0:未推送 1:已推送
//        tableConfigMap.put("message_push", " where push_status=2 ");// 发送状态 1:未发送 2:发送成功 3:已过期
//        tableConfigMap.put("user_vipcart_temp_relation", " where (yn=1 and (UNIX_TIMESTAMP(NOW())-UNIX_TIMESTAMP(ts)) >= 7200) " + // 两小时没有更新（对应逻辑有回滚的情况）vipcart_end_time
//                "OR (NOW()-vipcart_end_time) >= 7776000"); // 优惠券已经过期超过90天
//    }
//
//    @Override
//    public boolean execute(SqlQueryEntity[] tasks, String ownSign) throws Exception {
//        long startTime = System.currentTimeMillis();
//        for (SqlQueryEntity sqlEntity : tasks) {
//            try {
//                moveOneTable(sqlEntity);
//            } catch (Throwable e) {
//                LOGGER.info("优惠券数据库表迁移历史任务出现异常------- " + JsonUtils.toJson(sqlEntity), e);
//            }
//
//        }
//        LOGGER.info("优惠券数据库表迁移历史任务结束------- " + "耗时[{}]ms", (System.currentTimeMillis() - startTime));
//        return true;
//    }
//
//    /**
//     * 根据条件，查询当前可处理的任务
//     *
//     * @param taskParameter    任务的自定义参数
//     * @param ownSign          当前环境名称
//     * @param taskItemNum      当前任务类型的任务队列数量
//     * @param taskItemList     当前调度服务器，分配到的可处理队列
//     * @param eachFetchDataNum 每次获取数据的数量
//     * @return
//     * @throws Exception
//     */
//    public List<SqlQueryEntity> selectTasks(String taskParameter, String ownSign, int taskItemNum, List<TaskItemDefine> taskItemList, int eachFetchDataNum) throws Exception {
//        try {
//            if (CollectionUtils.isEmpty(taskItemList)) {
//                throw new Exception("taskItemList 配置为空");
//            }
//            ScheduleInput scheduleParam = new ScheduleInput();
//            scheduleParam.setEachFetchDataNum(eachFetchDataNum);
//            scheduleParam.setItemParamList(BeanHelper.copyTo(taskItemList, ScheduleInput.ItemParam.class));
//            scheduleParam.setTaskItemNum(taskItemNum);
//            scheduleParam.setTaskParameter(taskParameter);
//            Map<String, Object> extParamMap = new HashMap<String, Object>();
//            scheduleParam.setExtParamMap(extParamMap);
//            LOGGER.info("优惠券数据库表迁移历史任务启动-------taskParameter=[{}] taskItemNum=[{}] taskItemList=[{}] eachFetchDataNum=[{}],extParamMap=[{}]",
//                    new Object[]{taskParameter, taskItemNum, scheduleParam.toStringItemParam(), eachFetchDataNum, JsonUtils.toJson(extParamMap)});
//            List<SqlQueryEntity> sqlList = new ArrayList<SqlQueryEntity>();
//            for (Map.Entry<String, String> entry : tableConfigMap.entrySet()) {
//                String tableName = entry.getKey();
//                String columnNames = buildColumnNames(tableName);
//                if (StringUtils.isEmpty(columnNames)) {
//                    continue;
//                }
//                StringBuilder sqlBuilder = new StringBuilder();
//                sqlBuilder.append("select ").append(columnNames).append(" from ")
//                        .append(tableName).append(" ").append(entry.getValue())
//                        .append(" and  MOD(id,").append(taskItemNum).append(") IN (")
//                        .append(getConditionShardBuilder(scheduleParam.getItemList())).append(") LIMIT ").append(eachFetchDataNum > 0 ? eachFetchDataNum : 500);
//                SqlQueryEntity sqlQueryEntity = new SqlQueryEntity();
//                sqlQueryEntity.setColumnNames(columnNames);
//                sqlQueryEntity.setSql(sqlBuilder.toString());
//                sqlQueryEntity.setTableName(tableName);
//                sqlList.add(sqlQueryEntity);
//            }
//            return sqlList;
//        } catch (Throwable e) {
//            LOGGER.error("优惠券数据库表迁移历史任务出错!", e);
//        }
//        return Collections.EMPTY_LIST;
//    }
//
//    public Comparator<SqlQueryEntity> getComparator() {
//        return null;
//    }
//
//
//    /**
//     * 构建表查询字段属性名称集合
//     *
//     * @param tableName
//     * @return
//     */
//    private String buildColumnNames(String tableName) {
//        try {
//            StringBuilder colsBuilder = new StringBuilder();
//            List<String> columnNameList = spyBasicServiceImpl.getColumnNameByTableName(tableName);
//            int i = 0;
//            for (String columnName : columnNameList) {
//                if (i > 0) {
//                    colsBuilder.append(",");
//                }
//                colsBuilder.append(columnName);
//                i++;
//            }
//            return colsBuilder.toString();
//        } catch (Exception e) {
//            LOGGER.error("查询数据库表" + tableName + "的列信息出现异常！", e);
//        }
//        return null;
//    }
//
//    /**
//     * 依据任务分片生成sql查询条件
//     *
//     * @param itemList
//     * @return
//     */
//    private String getConditionShardBuilder(List<String> itemList) {
//        StringBuilder conditionShardBuilder = new StringBuilder();
//        int i = 0;
//        for (String item : itemList) {
//            if (i > 0) {
//                conditionShardBuilder.append(",");
//            }
//            conditionShardBuilder.append(item);
//            i++;
//        }
//        return conditionShardBuilder.toString();
//    }
//
//    /**
//     * 单表迁移
//     *
//     * @param sqlEntity
//     */
//    private void moveOneTable(SqlQueryEntity sqlEntity) {
//        List<HashMap<String, Object>> resultMapList = spyBasicServiceImpl.findListBySpySql(sqlEntity.getSql());
//        if (CollectionUtils.isEmpty(resultMapList)) {
//            return;
//        }
//        StringBuilder insertSqlBuilder = new StringBuilder();
//        StringBuilder deleteSqlBuilder = new StringBuilder();
//        String[] columnNames = sqlEntity.getColumnNames().split(",");
//        insertSqlBuilder.append("insert into ").append(sqlEntity.getTableName()).append("_his").append("(")
//                .append(sqlEntity.getColumnNames()).append(") values ");
//        deleteSqlBuilder.append("delete from ").append(sqlEntity.getTableName()).append(" where id in ")
//                .append("(");
//        int n = 0;
//        for (HashMap<String, Object> resultMap : resultMapList) {
//            Object id = resultMap.get("id");
//            if (id == null) {
//                break;
//            }
//            if (n > 0) {
//                insertSqlBuilder.append(",");
//                deleteSqlBuilder.append(",");
//            }
//            n++;
//            deleteSqlBuilder.append(Long.valueOf(id.toString()));
//            StringBuilder valueBuilder = new StringBuilder();
//            int i = 0;
//            for (String columnName : columnNames) {
//                if (i > 0) {
//                    valueBuilder.append(",");
//                }
//                i++;
//                Object obj = resultMap.get(columnName);
//                if (obj instanceof Date) {
//                    valueBuilder.append("'").append(DateFormatUtils.format((Date) obj, DateFormatUtils.DATE_MODEL_2)).append("'");
//                    continue;
//                }
//                if (obj instanceof String) {
//                    valueBuilder.append("'").append(((String) obj).replaceAll("\'","\\\\'")).append("'");
//                    continue;
//                }
//                valueBuilder.append(obj);
//            }
//            insertSqlBuilder.append("(").append(valueBuilder.toString())
//                    .append(")");
//        }
//        deleteSqlBuilder.append(")");
//        int r = spyBasicServiceImpl.moveToHistory(insertSqlBuilder.toString(), deleteSqlBuilder.toString());
//        LOGGER.info("迁移数据库表sql语句{}已结束，迁移数量{}条记录", sqlEntity.getSql(), r);
//    }
//
//}
