package com.bh.admin.service;


import com.bh.admin.pojo.order.ExtFiles;
import com.bh.admin.pojo.order.MyExtFiles;
import com.bh.utils.PageBean;

/**
 * @Description: 后台管理系统-图库管理
 * @author xieyc
 * @date 2018年1月9日 下午3:31:25 
 */
public interface ExtFilesService {
	//查询某种类型的文件
	PageBean<MyExtFiles> getExtFilesList(String currentPage, String pageSize,Integer shopId,String name) throws Exception;
	
	PageBean<MyExtFiles> getFilesList(String currentPage, String pageSize,Integer shopId,String cateId,String startTime,String endTime) throws Exception;
	//删除某个文件
	Integer deleteExtFiles(String id);
	
	//插入图库
	int insertSkuImageStore(ExtFiles extFile)throws Exception;
	
	int updateExtFiles(ExtFiles extFiles)throws Exception;
	
	int insertExtFiles(String cateId,String fileUrl,Integer shopId)throws Exception;
}
