package com.bh.admin.service;

import com.bh.admin.pojo.user.SysCacheDb;
import com.bh.utils.PageBean;

public interface SysCacheDbService {
	/**
	 * <p>Description: 添加</p>
	 *  @author scj  
	 *  @date 2018年7月30日
	 */
	int add (SysCacheDb entity) throws Exception;
	
	/**
	 * <p>Description: 删除</p>
	 *  @author scj  
	 *  @date 2018年7月30日
	 */
	int delete (SysCacheDb entity) throws Exception;
	
	/**
	 * <p>Description: 编辑</p>
	 *  @author scj  
	 *  @date 2018年7月30日
	 */
	int edit (SysCacheDb entity) throws Exception;
	
	/**
	 * <p>Description: 详情</p>
	 *  @author scj  
	 *  @date 2018年7月30日
	 */
	SysCacheDb get (SysCacheDb entity) throws Exception;
	
	/**
	 * <p>Description: 列表</p>
	 *  @author scj  
	 *  @date 2018年7月30日
	 */
	PageBean<SysCacheDb> listPage (SysCacheDb entity) throws Exception;
	
	/**
	 * <p>Description: 根据组名-定时更新缓存表数据</p>
	 *  @author scj  
	 *  @date 2018年7月30日
	 */
	int loadSysDb(String[] params)throws Exception;
	
	/**
	 * <p>Description: 根据key获取val</p>
	 *  @author scj  
	 *  @date 2018年7月30日
	 */
	String getValByDbKey(String key) throws Exception;
	
	/**
	 * <p>Description: 定时更新缓存表val</p>
	 *  @author scj  
	 *  @date 2018年7月30日
	 */
	int updateAllVal() throws Exception;
}
