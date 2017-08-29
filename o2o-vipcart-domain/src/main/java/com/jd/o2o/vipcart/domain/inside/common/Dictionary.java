package com.jd.o2o.vipcart.domain.inside.common;
import com.jd.o2o.vipcart.common.domain.BaseEntityBean;

/**
 * 字典对象实体
 * Created by liuhuiqing on 2016/2/25.
 */
public class Dictionary extends BaseEntityBean {
	/**
	 * 字典ID
	 */
	private Long id;
	/**
	 * 字典编号
	 */
	private String dictCode;
	/**
	 * 字典名称
	 */
	private String dictName;
	/**
	 * 排序
	 */
	private Integer sortNo;
	/**
	 * 备注
	 */
	private String remark;
    /**
     * 字典类型ID
     */
    private Long dictTypeId;
    /**
     * 字典类型编号
     */
    private String dictTypeCode;

    /**
     * 字典类型名称
     */
    private String dictTypeName;
    
	public Long getId(){
		return id;
	}
	public void setId(Long id){
		this.id = id;
	}
	public String getDictCode(){
		return dictCode;
	}
	public void setDictCode(String dictCode){
		this.dictCode = dictCode;
	}
	public String getDictName(){
		return dictName;
	}
	public void setDictName(String dictName){
		this.dictName = dictName;
	}
	public Integer getSortNo(){
		return sortNo;
	}
	public void setSortNo(Integer sortNo){
		this.sortNo = sortNo;
	}
	public String getRemark(){
		return remark;
	}
	public void setRemark(String remark){
		this.remark = remark;
	}

    public Long getDictTypeId() {
        return dictTypeId;
    }

    public void setDictTypeId(Long dictTypeId) {
        this.dictTypeId = dictTypeId;
    }

    public String getDictTypeCode() {
        return dictTypeCode;
    }

    public void setDictTypeCode(String dictTypeCode) {
        this.dictTypeCode = dictTypeCode;
    }

    public String getDictTypeName() {
        return dictTypeName;
    }

    public void setDictTypeName(String dictTypeName) {
        this.dictTypeName = dictTypeName;
    }
}