package com.bh.admin.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bh.admin.mapper.goods.GoodsAttrMapper;
import com.bh.admin.mapper.goods.GoodsModelAttrMapper;
import com.bh.admin.mapper.goods.GoodsModelMapper;
import com.bh.admin.pojo.goods.GoodsAttr;
import com.bh.admin.pojo.goods.GoodsModel;
import com.bh.admin.pojo.goods.GoodsModelAttr;
import com.bh.admin.service.GoodsModelAttrService;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;

@Service
@Transactional
public class GoodsModelAttrImpl implements GoodsModelAttrService{
	@Autowired
	private GoodsModelAttrMapper mapper;
	
	@Autowired
	private GoodsAttrMapper goodsAttrMapper;
	
	@Autowired
	private GoodsModelMapper modelMapper;
	
	/**
	 * 平台后台模型属性值列表
	 */
	@Override
	public PageBean<GoodsModelAttr> selectPageBymId(String mId, String currentPage, String pageSize) throws Exception {
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize), true);
		List<GoodsModelAttr> list = mapper.selectAllBymId(Integer.parseInt(mId));
		for(GoodsModelAttr modelAttr : list){
			GoodsModel goodsModel = modelMapper.selectByPrimaryKey(modelAttr.getModelId());
			modelAttr.setModelName(goodsModel.getName());
		}
		int count = mapper.countAllBymId(Integer.parseInt(mId));
		PageBean<GoodsModelAttr> pageBean = new PageBean<>(list);
		pageBean.setList(list);
		pageBean.setTotal(count);
		return pageBean;
	}
	
	/**
	 * 模型属性值的添加
	 */
	@Override
	public int insertModelAttr(String mId, String name, String search, String value) throws Exception {
		int row = 0;
		List<GoodsModelAttr> list = mapper.selectByName(name, Integer.parseInt(mId));//查询是否重名
		if(list.size()==0){
			GoodsModelAttr modelAttr = new GoodsModelAttr();
			modelAttr.setModelId(Integer.parseInt(mId)); //模型id
			modelAttr.setName(name); //属性名称
			if(Integer.parseInt(search)==1){ //设置是否可搜索
				modelAttr.setSearch(true);
			}
			modelAttr.setValue(value); //属性值
			row = mapper.insertSelective(modelAttr);
		}else{
			row = 1000;
		}
		
		return  row;
	} 
	
	/**
	 * 模型属性的删除
	 * @param id
	 * @return
	 * @throws Exception
	 */
	@Override
	public int deleteModelAttr(String id) throws Exception {
		int row = 0;
		List<GoodsAttr> list = goodsAttrMapper.getListByAttrId(Integer.parseInt(id));
		if(list.size()==0){
			row = mapper.deleteByPrimaryKey(Integer.parseInt(id));
		}else{
			row = 2;
		}
		return row;
	}
	
	/**
	 * 模型属性值的修改
	 */
	@Override
	public int editModelAttr(String id, String name, String search, String value) throws Exception {
		int row = 0;
		GoodsModelAttr modelAttr = mapper.selectByPrimaryKey(Integer.parseInt(id));
		List<GoodsModelAttr> list = mapper.selectUpdateByName(name, Integer.parseInt(id), modelAttr.getModelId());//查询是否重名
		if(list.size()==0){
			if(!StringUtils.isEmptyOrWhitespaceOnly(name)){
				modelAttr.setName(name);
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(value)){
				modelAttr.setValue(value);
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(search)){
				if(Integer.parseInt(search)==1){
					modelAttr.setSearch(true);
				}else{
					modelAttr.setSearch(false);
				}
			}
			row =  mapper.updateByPrimaryKeySelective(modelAttr);
		}else{
			row = 1000;
		}
		return row;
	}
	
}
