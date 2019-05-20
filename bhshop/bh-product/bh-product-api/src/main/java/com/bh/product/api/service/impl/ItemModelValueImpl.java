package com.bh.product.api.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.goods.mapper.ItemModelValueMapper;
import com.bh.goods.pojo.ItemModelValue;
import com.bh.product.api.service.ItemModelValueService;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service
public class ItemModelValueImpl implements ItemModelValueService{
	@Autowired
	private ItemModelValueMapper mapper;
	@Override
	public int insert(ItemModelValue entity) throws Exception {
		entity.setAddTime(new Date());
		return mapper.insertSelective(entity);
	}

	@Override
	public int edit(ItemModelValue entity) throws Exception {
		int row = 0;
		List<ItemModelValue> list = mapper.selectByGoodsId(entity);
		if(list.size()>0){
			ItemModelValue modelValue = mapper.selectByPrimaryKey(list.get(0).getId());
			modelValue.setParamData(entity.getParamData());
			row = mapper.updateByPrimaryKeySelective(modelValue);
		}
		return row;
	}

	@Override
	public ItemModelValue get(ItemModelValue entity) throws Exception {
		ItemModelValue modelValue = mapper.selectByPrimaryKey(entity.getId());
		if(modelValue!=null){
			JSONArray array = JSONArray.fromObject(modelValue.getParamData()); //value转义
			modelValue.setValue(array);
		}
		return modelValue;
	}

	@Override
	public int delete(ItemModelValue entity) throws Exception {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(entity.getId());
	}

}
