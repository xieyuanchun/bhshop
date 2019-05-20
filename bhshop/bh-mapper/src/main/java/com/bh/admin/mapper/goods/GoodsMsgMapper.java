package com.bh.admin.mapper.goods;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.admin.pojo.goods.GoodsMsg;


public interface GoodsMsgMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsMsg record);

    int insertSelective(GoodsMsg record);

    GoodsMsg selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsMsg record);

    int updateByPrimaryKey(GoodsMsg record);
    
	List<GoodsMsg> getAll( @Param("shopId") Integer shopId);
}