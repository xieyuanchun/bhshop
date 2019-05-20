package com.bh.admin.pojo.user;

import java.util.Date;

public class SysCacheConf {
	/**
	 * ID
	 */
    private Integer cacheConfId;
    /**
     * 键
     */
    private String configKey;
    /**
     * 分组名
     */
    private String configGroup;
    /**
     * 新增时间
     */
    private Date addTime;
    /**
     * 修改时间
     */
    private Date editTime;
    /**
     * 值
     */
    private String configVal;
    /**
     * 起始页
     */
    private String currentPage;
    
    public Integer getCacheConfId() {
        return cacheConfId;
    }

    public void setCacheConfId(Integer cacheConfId) {
        this.cacheConfId = cacheConfId;
    }

    public String getConfigKey() {
        return configKey;
    }

    public void setConfigKey(String configKey) {
        this.configKey = configKey == null ? null : configKey.trim();
    }

    public String getConfigGroup() {
        return configGroup;
    }

    public void setConfigGroup(String configGroup) {
        this.configGroup = configGroup == null ? null : configGroup.trim();
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

    public String getConfigVal() {
        return configVal;
    }

    public void setConfigVal(String configVal) {
        this.configVal = configVal == null ? null : configVal.trim();
    }

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
}