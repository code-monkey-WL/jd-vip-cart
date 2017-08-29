package com.jd.o2o.vipcart.domain.inside.worker;

import java.io.Serializable;
import java.util.*;

/**
 * 分布式查询入参结构
 * Created by liuhuiqing on 2017/5/16.
 */
public class ScheduleInput implements Serializable {
    /**
     * 全局入参
     */
    private String taskParameter;
    /**
     * 总任务项数
     */
    private int taskItemNum;
    /**
     * 当前分配任务项参数
     */
    private Collection<ItemParam> itemParamList;
    /**
     * 取数据条数限制
     */
    private int eachFetchDataNum;
    /**
     * 环境标识
     */
    private String ownSign;
    /**
     * 业务扩展参数
     */
    private Map<String,Object> extParamMap;

    public String getTaskParameter() {
        return taskParameter;
    }

    public void setTaskParameter(String taskParameter) {
        this.taskParameter = taskParameter;
    }

    public int getTaskItemNum() {
        return taskItemNum;
    }

    public void setTaskItemNum(int taskItemNum) {
        this.taskItemNum = taskItemNum;
    }

    public Collection<ItemParam> getItemParamList() {
        return itemParamList;
    }

    public void setItemParamList(Collection<ItemParam> itemParamList) {
        this.itemParamList = itemParamList;
    }

    public int getEachFetchDataNum() {
        return eachFetchDataNum;
    }

    public void setEachFetchDataNum(int eachFetchDataNum) {
        this.eachFetchDataNum = eachFetchDataNum;
    }

    public String getOwnSign() {
        return ownSign;
    }

    public void setOwnSign(String ownSign) {
        this.ownSign = ownSign;
    }

    public Map<String, Object> getExtParamMap() {
        return extParamMap;
    }

    public void setExtParamMap(Map<String, Object> extParamMap) {
        this.extParamMap = extParamMap;
    }

    public List<String> getItemList() {
        if(itemParamList == null || itemParamList.size() == 0){
            return Collections.EMPTY_LIST;
        }
        List<String> itemList = new ArrayList<String>();
        for(ItemParam itemParam:itemParamList){
            itemList.add(itemParam.getTaskItemId());
        }
        return itemList;
    }

    public String toStringItemParam(){
        if(itemParamList == null || itemParamList.size() == 0){
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (ItemParam itemParam : itemParamList) {
            sb.append(itemParam.getTaskItemId()).append(":").append(itemParam.getParameter()).append(" ");
        }
        return sb.toString();
    }

    /**
     * 任务项入参实体
     */
    public static class ItemParam implements Serializable{
        private String taskItemId;
        private String parameter;

        public String getTaskItemId() {
            return taskItemId;
        }

        public void setTaskItemId(String taskItemId) {
            this.taskItemId = taskItemId;
        }

        public String getParameter() {
            return parameter;
        }

        public void setParameter(String parameter) {
            this.parameter = parameter;
        }
    }
}
