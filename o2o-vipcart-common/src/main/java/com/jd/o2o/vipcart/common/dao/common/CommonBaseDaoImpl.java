package com.jd.o2o.vipcart.common.dao.common;

import com.jd.o2o.vipcart.common.dao.AbstractBaseDao;
import com.jd.o2o.vipcart.common.dao.TableDes;
import com.jd.o2o.vipcart.common.domain.criteria.Criteria;
import com.jd.o2o.vipcart.common.domain.exception.BaseMsgException;
import com.jd.o2o.vipcart.common.domain.id.SqlID;
import org.apache.commons.lang.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 公共基础服务类
 * Created by liuhuiqing on 2016/11/9.
 */
@TableDes(nameSpace = "commonRoadMapper",tableName = "commonRoadMapper")
public class CommonBaseDaoImpl extends AbstractBaseDao<Criteria, Long> implements CommonBaseDao {

    @Override
    public Date getCurrentDBTime() {
        return (Date) super.getSqlSession().selectOne("commonRoadMapper.selectCurrentDateTime");
    }

    @Override
    public Long nextSequence(String tableName) {
        SqlID sqlID = new SqlID();
        sqlID.setTableName(tableName);
        return nextSequence(sqlID);
    }

    @Override
    public Long nextSequence(SqlID sqlID) {
        if (sqlID == null || StringUtils.isEmpty(sqlID.getTableName())) {
            throw new BaseMsgException("生成序列的表名不能为空");
        }
        int row = super.getSqlSession().insert("commonRoadMapper.insertSequence", sqlID);
        Long id = sqlID.getId();
        if (row < 1 || id == null) {
            throw new BaseMsgException("生成序列是出现异常");
        }
        return sqlID.getId();
    }

    @Override
    public Integer clearSequence(SqlID sqlID) {
        if (sqlID == null || StringUtils.isEmpty(sqlID.getTableName())) {
            throw new BaseMsgException("生成序列的表名不能为空");
        }
        Long id = sqlID.getId();
        if (id == null) {
            id = selectCurrentSequence(sqlID.getTableName());
            sqlID.setId(id);
        }
        if (id == null) {
            throw new BaseMsgException("清空序列[" + sqlID.getTableName() + "]失败，未指定清空起始id");
        }
        return super.getSqlSession().delete("commonRoadMapper.deleteSequence", sqlID);
    }

    @Override
    public Long selectCurrentSequence(String tableName) {
        if (StringUtils.isEmpty(tableName)) {
            throw new BaseMsgException("生成序列的表名不能为空");
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("tableName",tableName);
        return (Long) super.getSqlSession().selectOne("commonRoadMapper.selectCurrentSequence", map);
    }
}
