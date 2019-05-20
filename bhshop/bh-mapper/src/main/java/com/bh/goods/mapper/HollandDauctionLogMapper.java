package com.bh.goods.mapper;

import java.util.List;

import com.bh.goods.pojo.HollandDauctionLog;

public interface HollandDauctionLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HollandDauctionLog record);

    int insertSelective(HollandDauctionLog record);

    HollandDauctionLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HollandDauctionLog record);

    int updateByPrimaryKey(HollandDauctionLog record);
    
    
    List<HollandDauctionLog> getListByGoodsId(HollandDauctionLog record);
    
    List<HollandDauctionLog> getListByGoodsIdRecode(HollandDauctionLog record);
    
    List<HollandDauctionLog> getListByGoodsIdAndStatus(HollandDauctionLog record);
    
    List<HollandDauctionLog> getListByGoodsIdAndCurrentPeriods(HollandDauctionLog record);
    
    List<HollandDauctionLog> getListByDStatusAndPayStatus();
    
	HollandDauctionLog getPayPrice(Integer getmId, Integer getgId);
	
	List<HollandDauctionLog> getListByDStatusAndPayStatusAndSecond();

	HollandDauctionLog getLogByOrderNo(String orderNo);

	List<HollandDauctionLog> getFailMIdLog(int mId, int goodsId, int currentPeriods);
	
	List<HollandDauctionLog> getByUserLog(HollandDauctionLog record);
}