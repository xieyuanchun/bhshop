package com.order.extend.service;

import com.bh.order.pojo.ExtFileCate;
import com.bh.utils.PageBean;

/**
 * @Description: 后台管理系统-扩展-多媒体分类
 * @author xieyc
 * @date 2018年1月9日 下午3:31:25 
 */
public interface ExtFileCateService {

	//分页查询所有文件分类 xieyc
	PageBean<ExtFileCate> getExtFileCateList(String currentPage, String pageSize) throws Exception;
	//删除某个文件分类 xieyc
	Integer deleteExtFileCate(String id) throws Exception;
	//添加文件分类 xieyc
	int addExtFileCate(ExtFileCate extFileCate) throws Exception;
	//更新某个文件分类 xieyc
	int updateExtFileCate(ExtFileCate extFileCate) throws Exception;
	
}
