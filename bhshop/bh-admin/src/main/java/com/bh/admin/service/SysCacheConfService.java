package com.bh.admin.service;

import com.bh.admin.pojo.user.SysCacheConf;
import com.bh.utils.PageBean;

public interface SysCacheConfService {
	/**
	 * <p>Description: 添加</p>
	 *  @author scj  
	 *  @date 2018年7月24日
	 */
	int add (SysCacheConf entity) throws Exception;
	
	/**
	 * <p>Description: 删除</p>
	 *  @author scj  
	 *  @date 2018年7月24日
	 */
	int delete (SysCacheConf entity) throws Exception;
	
	/**
	 * <p>Description: 编辑</p>
	 *  @author scj  
	 *  @date 2018年7月24日
	 */
	int edit (SysCacheConf entity) throws Exception;
	
	/**
	 * <p>Description: 详情</p>
	 *  @author scj  
	 *  @date 2018年7月24日
	 */
	SysCacheConf get (SysCacheConf entity) throws Exception;
	
	/**
	 * <p>Description: 列表</p>
	 *  @author scj  
	 *  @date 2018年7月24日
	 */
	PageBean<SysCacheConf> listPage (SysCacheConf entity) throws Exception;
	
	/**
	 * <p>Description: 根据组名-定时更新缓存表数据</p>
	 *  @author scj  
	 *  @date 2018年7月25日
	 */
	int loadSysCacheConf(String[] params)throws Exception;
	
	/**
	 * <p>Description: 根据key获取val</p>
	 *  @author scj  
	 *  @date 2018年7月27日
	 */
	String getValByConfigKey(String key) throws Exception;
}
