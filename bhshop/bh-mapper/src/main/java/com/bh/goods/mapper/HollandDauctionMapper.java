package com.bh.goods.mapper;

import java.util.Date;
import java.util.List;

import com.bh.goods.pojo.HollandDauction;
import com.bh.goods.pojo.HollandDauctionLog;

public interface HollandDauctionMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HollandDauction record);

    int insertSelective(HollandDauction record);

    HollandDauction selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HollandDauction record);

    int updateByPrimaryKey(HollandDauction record);
    
    
    
    List<HollandDauction> apiDauctionList();
    
    HollandDauction getByGoodsId(HollandDauction record);

	List<HollandDauction> getListByGoodsId(Integer goodsId);
	
	List<HollandDauction> getLostTimeRecord();
	
	HollandDauction getCurrentTime();
	
}