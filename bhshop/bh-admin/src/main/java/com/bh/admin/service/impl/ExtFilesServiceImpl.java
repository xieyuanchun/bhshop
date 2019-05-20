package com.bh.admin.service.impl;


import java.util.Date;
import java.util.List;

import org.apache.poi.ss.usermodel.Row;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bh.admin.pojo.order.ExtFiles;
import com.bh.admin.pojo.order.MyExtFiles;
import com.bh.admin.service.ExtFilesService;
import com.bh.admin.mapper.order.ExtFilesMapper;
import com.bh.utils.JsonUtils;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;



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
	public PageBean<MyExtFiles> getExtFilesList(String currentPage, String pageSize,Integer shopId,String name) {
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize), true);
		List<MyExtFiles> listExtFile = extFilesMapper.getExtFilesList(shopId,name);
		PageBean<MyExtFiles> pageExtFiles = new PageBean<>(listExtFile);
		return pageExtFiles;
	}
	
	/**
	 * 查询所有图片
	 */
	public PageBean<MyExtFiles> getFilesList(String currentPage, String pageSize,Integer shopId,String cateId,String startTime,String endTime) {
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize), true);
		List<MyExtFiles> listExtFile = extFilesMapper.getFilesList(shopId,cateId,startTime,endTime);
		PageBean<MyExtFiles> pageExtFiles = new PageBean<>(listExtFile);
		return pageExtFiles;
	}

	/**
	 * @Description:删除某个文件
	 * @author xieyc
	 * @date 2018年1月10日 下午12:01:40 
	 */
	public Integer deleteExtFiles(String id) {
		Integer returnRow=0;
		if (org.apache.commons.lang.StringUtils.isNotBlank(id)) {
			List<String> ids=JsonUtils.stringToList(id);
			if (ids.size()>0) {
				for (String string : ids) {
					ExtFiles extFile=new ExtFiles();
					extFile.setId(Integer.parseInt(string));
					extFile.setIsDel(1);
					extFile.setAddTime(new Date());
				    returnRow=extFilesMapper.updateByPrimaryKeySelective(extFile);
				}
			}
		}
		return  returnRow ;
	}

	
	//插入图库
	public int insertSkuImageStore(ExtFiles extFile)throws Exception{
		return extFilesMapper.insertSelective(extFile);
	}
	
	
	public int updateExtFiles(ExtFiles extFiles)throws Exception{
		extFilesMapper.updateByPrimaryKeySelective(extFiles);
		return 1;
	}
	
	//新增
	public int insertExtFiles(String cateId,String fileUrl,Integer shopId)throws Exception{
		String[] fileUrls=fileUrl.split(",");
		ExtFiles extFiles=new ExtFiles();
		extFiles.setCateId(Integer.parseInt(cateId));
		extFiles.setShopId(shopId);
		extFiles.setIsDel(0);
		extFiles.setFileType(0);
		extFiles.setAddTime(new Date());
		int row=0;
		for (int i = 0; i < fileUrls.length; i++) {
			extFiles.setFileUrl(fileUrls[i]);
			row=extFilesMapper.insertSelective(extFiles);
		}
		return row;
	}
	
	
	
}
