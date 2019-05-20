package com.bh.goods.mapper;

import com.bh.goods.pojo.ActZone;
import com.bh.goods.pojo.ActZoneGoods;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;


public interface ActZoneGoodsMapper {
	    int deleteByPrimaryKey(Integer id);

	    int insert(ActZone record);

	    int insertSelective(ActZone record);

	    ActZone selectByPrimaryKey(Integer id);

	    int updateByPrimaryKeySelective(ActZone record);

	    int updateByPrimaryKey(ActZone record);
    
		List<Integer> selectIsFreePostage(@Param("set")Set<Integer> goodsId);
		List<Integer> selectIsLockScore(@Param("set")Set<Integer> goodsId);
		List<Integer> selectIsRefund(@Param("goodsId")Integer goodsId);
		
		List<Integer> selectIsCart(@Param("set")Set<Integer> goodsId);
		
		List<ActZoneGoods> selectIsCoupon(Integer id);

		List<Integer> selectByGoodsIds(@Param("set")Set<Integer> goodsId);

        //根据Goods Id 获取信息
		List<ActZoneGoods> getByGoodsId(Integer id);

}