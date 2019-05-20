package com.bh.admin.pojo.user;

import java.util.Date;

public class SysCacheDb {
    private Integer cacheDbId;

    private String dbKey;

    private String dbGroup;

    private String dbSql;

    private Date addTime;

    private Date editTime;

    private String dbVal;
    
    private String currentPage;

    public Integer getCacheDbId() {
        return cacheDbId;
    }

    public void setCacheDbId(Integer cacheDbId) {
        this.cacheDbId = cacheDbId;
    }

    public String getDbKey() {
        return dbKey;
    }

    public void setDbKey(String dbKey) {
        this.dbKey = dbKey == null ? null : dbKey.trim();
    }

    public String getDbGroup() {
        return dbGroup;
    }

    public void setDbGroup(String dbGroup) {
        this.dbGroup = dbGroup == null ? null : dbGroup.trim();
    }

    public String getDbSql() {
        return dbSql;
    }

    public void setDbSql(String dbSql) {
        this.dbSql = dbSql == null ? null : dbSql.trim();
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public String getDbVal() {
        return dbVal;
    }

    public void setDbVal(String dbVal) {
        this.dbVal = dbVal == null ? null : dbVal.trim();
    }

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
}