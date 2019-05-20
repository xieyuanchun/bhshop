package com.order.extend.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bh.order.mapper.ExtFilesMapper;
import com.bh.order.pojo.ExtFiles;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;
import com.order.extend.service.ExtFilesService;

/**
 * @Description: 后台管理系统-扩展-图片管理
 * @author xieyc
 * @date 2018年1月9日 下午3:31:25 
 */
@Service
@Transactional
public class ExtFilesServiceImpl implements ExtFilesService {
	@Autowired
	ExtFilesMapper extFilesMapper;//文件

	/**
	 * @Description: 分页查询某种类型的文件
	 * @author xieyc
	 * @date 2018年1月9日 下午3:34:19 
	 */
	public PageBean<ExtFiles> getExtFilesList(String currentPage, String pageSize,String file_type) {
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize), true);
		List<ExtFiles> listExtFiles = extFilesMapper.getExtFilesList(Integer.valueOf(file_type));
		PageBean<ExtFiles> pageExtFiles = new PageBean<>(listExtFiles);
		return pageExtFiles;
	}

	/**
	 * @Description:删除某个文件
	 * @author xieyc
	 * @date 2018年1月10日 下午12:01:40 
	 */
	public Integer deleteExtFiles(String id) {
		Integer returnRow=0;
		if (!StringUtils.isEmptyOrWhitespaceOnly(id)) {
		  returnRow=extFilesMapper.deleteByPrimaryKey(Integer.valueOf(id));
		}
		return  returnRow ;
	}

	/**
	 * @Description: 分页查询某个分类下的所有文件
	 * @author xieyc
	 * @date 2018年1月9日 下午3:34:19 
	 */
	public PageBean<ExtFiles> getExtFilesByCateId(String cate_id,String currentPage, String pageSize){
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize), true);
		List<ExtFiles> listExtFiles = extFilesMapper.getExtFilesByCateId(Integer.valueOf(cate_id));
		PageBean<ExtFiles> pageExtFiles = new PageBean<>(listExtFiles);
		return pageExtFiles;
	}
	
	
}
