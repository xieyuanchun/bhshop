package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.ActZoneGoods;

public interface ActZoneGoodsMapper {
	int deleteByPrimaryKey(Integer id);

    int insert(ActZoneGoods record);

    int insertSelective(ActZoneGoods record);

    ActZoneGoods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ActZoneGoods record);

    int updateByPrimaryKey(ActZoneGoods record);

	List<ActZoneGoods> selectByGoodsIdList(Integer goodsId);
	
	int deleteByGoodsAndSkuId(Integer goodsId,Integer skuId);
	
	int deleteByGoodsId(Integer goodsId);
	
	List<ActZoneGoods> getActZoneList(Integer goodsId);
	
	List<ActZoneGoods> getActZoneList1();
	
	String selectGoodsActZone(Integer id);
	
}