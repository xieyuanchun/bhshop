package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.HollandDauctionLog;

public interface HollandDauctionLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HollandDauctionLog record);

    int insertSelective(HollandDauctionLog record);

    HollandDauctionLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(HollandDauctionLog record);

    int updateByPrimaryKey(HollandDauctionLog record);

	List<HollandDauctionLog> getLogList(HollandDauctionLog entity);

	com.bh.goods.pojo.HollandDauctionLog getPayPrice(int mId, Integer goodsId);

	HollandDauctionLog getLogByOrderNo(String orderNo);
}