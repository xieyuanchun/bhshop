package com.order.extend.service;

import com.bh.order.pojo.ExtFiles;
import com.bh.utils.PageBean;

/**
 * @Description: 后台管理系统-扩展-图片管理
 * @author xieyc
 * @date 2018年1月9日 下午3:31:25 
 */
public interface ExtFilesService {
	//查询某种类型的文件
	PageBean<ExtFiles> getExtFilesList(String currentPage, String pageSize,String file_type) throws Exception;
	//删除某个文件
	Integer deleteExtFiles(String id);
	//分页查询某个分类下的所有文件
	PageBean<ExtFiles> getExtFilesByCateId(String cate_id,String currentPage, String pageSize);
	
}
