package com.bh.admin.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bh.admin.mapper.goods.GoodsCategoryMapper;
import com.bh.admin.mapper.goods.ItemModelMapper;
import com.bh.admin.pojo.goods.GoodsCategory;
import com.bh.admin.pojo.goods.ItemModel;
import com.bh.admin.service.ItemModelService;
import com.bh.config.Contants;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;

import net.sf.json.JSONArray;
@Service
public class ItemModelImpl implements ItemModelService{
	@Autowired 
	private ItemModelMapper mapper;
	@Autowired 
	private GoodsCategoryMapper catMapper;
	@Override
	public int insert(ItemModel entity) throws Exception {
		entity.setAddTime(new Date());
		return mapper.insertSelective(entity);
	}

	@Override
	public int edit(ItemModel entity) throws Exception {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	public ItemModel get(ItemModel entity) throws Exception {
		ItemModel model = mapper.selectByPrimaryKey(entity.getId());
		if(model!=null){
			JSONArray array = JSONArray.fromObject(model.getParamData()); //value转义
			model.setValue(array);
			GoodsCategory category = catMapper.selectByPrimaryKey(model.getCatId());
			if(category!=null){
				model.setCatName(category.getName());
			}
			return model;
		}else{
			return null;
		}
	}

	@Override
	public int delete(ItemModel entity) throws Exception {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(entity.getId());
	}

	@Override
	public PageBean<ItemModel> listPage(ItemModel entity) throws Exception {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Contants.PAGE_SIZE, true);
		List<ItemModel> list = mapper.listPage(entity);
		if(list.size()>0){
			for(ItemModel model : list){
				GoodsCategory category = catMapper.selectByPrimaryKey(model.getCatId());
				if(category!=null){
					model.setCatName(category.getName());
				}
			}
		}
		PageBean<ItemModel> pageBean = new PageBean<>(list);
		return pageBean;
	}

	@Override
	public ItemModel getByCatId(ItemModel entity) throws Exception {
		List<ItemModel> list = mapper.getByCatId(entity);
		if(list.size()>0){
			ItemModel model = list.get(0);
			JSONArray array = JSONArray.fromObject(model.getParamData()); //value转义
			model.setValue(array);
			return model;
		}else{
			return null;
		}
	}

}
