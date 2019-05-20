package com.bh.user.pojo;

public class ScoreRuleExt {
    private Integer id;

    private Integer srId;//积分规则ID

    private Integer extKey;//键

    private Integer extValue;//值
    
    private Integer isDel;//是否删除，0是不删除，1是删除
    
    private Integer isSeries;//是否N天连续以上0否，1是

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSrId() {
        return srId;
    }

    public void setSrId(Integer srId) {
        this.srId = srId;
    }

	public Integer getExtKey() {
		return extKey;
	}

	public void setExtKey(Integer extKey) {
		this.extKey = extKey;
	}

	public Integer getExtValue() {
		return extValue;
	}

	public void setExtValue(Integer extValue) {
		this.extValue = extValue;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public Integer getIsSeries() {
		return isSeries;
	}

	public void setIsSeries(Integer isSeries) {
		this.isSeries = isSeries;
	}
	
	
  
}