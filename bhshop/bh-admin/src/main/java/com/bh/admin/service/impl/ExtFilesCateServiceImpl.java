package com.bh.admin.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.order.ExtFileCate;
import com.bh.admin.service.ExtFileCateService;
import com.bh.admin.mapper.order.ExtFileCateMapper;
import com.bh.admin.mapper.order.ExtFilesMapper;
import com.bh.admin.pojo.order.ExtFiles;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;

/**
 * @Description: 后台管理系统-扩展-多媒体分类
 * @author xieyc
 * @date 2018年1月9日 下午3:31:25 
 */
@Service
@Transactional
public class ExtFilesCateServiceImpl implements ExtFileCateService {
	@Autowired
	ExtFileCateMapper extFileCateMapper;//文件分类
	@Autowired
	ExtFilesMapper extFilesMapper;//文件
	/**
	 * @Description: 分页查询所有文件分类
	 * @author xieyc
	 * @date 2018年1月9日 下午5:02:39 
	 */
	public PageBean<ExtFileCate> getExtFileCateList(Integer currentPage,Integer pageSize) {
		PageHelper.startPage(currentPage, pageSize, true);
		List<ExtFileCate> listExtFileCate = extFileCateMapper.getExtFileCateList();
		PageBean<ExtFileCate> page = new PageBean<>(listExtFileCate);
		return page;
	}
	/**
	 * @Description:删除某个文件分类
	 * @author xieyc
	 * @date 2018年1月9日 下午5:22:27 
	 */
	public Integer deleteExtFileCate(String id) {
		Integer returnRow=0;
		if (!StringUtils.isEmptyOrWhitespaceOnly(id)) {
			List <ExtFiles> listExtFiles=extFilesMapper.getExtFilesByCateId(Integer.valueOf(id));
			if(listExtFiles!=null&&listExtFiles.size()>0){
				returnRow=-1;//被引用
			}else{
				returnRow=extFileCateMapper.deleteByPrimaryKey(Integer.valueOf(id));
			}
		}
		return  returnRow ;
	}
	/**
	 * @Description: 添加文件分类
	 * @author xieyc
	 * @date 2018年1月9日 下午5:41:39 
	 */
	public int addExtFileCate(ExtFileCate extFileCate) {
		return extFileCateMapper.insert(extFileCate) ;
	}
	/**
	 * @Description: 更新某个文件分类
	 * @author xieyc
	 * @date 2018年1月9日 下午5:41:39 
	 */
	public int updateExtFileCate(ExtFileCate extFileCate) {
		return extFileCateMapper.updateByPrimaryKey(extFileCate);
	}

}
