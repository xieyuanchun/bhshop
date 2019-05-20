package com.bh.goods.mapper;

import com.bh.goods.pojo.GoodsOperLog;

public interface GoodsOperLogMapper {
	 int deleteByPrimaryKey(Integer id);

	    int insert(GoodsOperLog record);

	    int insertSelective(GoodsOperLog record);

	    GoodsOperLog selectByPrimaryKey(Integer id);

	    int updateByPrimaryKeySelective(GoodsOperLog record);

	    int updateByPrimaryKey(GoodsOperLog record);
}