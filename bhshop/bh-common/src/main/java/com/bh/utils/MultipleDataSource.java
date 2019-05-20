package com.bh.utils;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 多数据源管理工具类
 * @author 
 *
 */
public class MultipleDataSource extends AbstractRoutingDataSource{

	    
    
    private static final ThreadLocal<String> dataSourceKey = new InheritableThreadLocal<String>();  
      
    public static void setDataSourceKey(String dataSource) {  
        dataSourceKey.set(dataSource);  
    }  
    
	@Override
	protected Object determineCurrentLookupKey() {
		return dataSourceKey.get();
	}

}
