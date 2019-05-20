package com.bh.goods.mapper;

import java.util.List;

import com.bh.goods.pojo.ActZone;

public interface ActZoneMapper {
	 int deleteByPrimaryKey(Integer id);

	    int insert(ActZone record);

	    int insertSelective(ActZone record);

	    ActZone selectByPrimaryKey(Integer id);

	    int updateByPrimaryKeySelective(ActZone record);

	    int updateByPrimaryKey(ActZone record);
    
    /*分类专区*/
    List<ActZone> apiCategoryName(String id);
    
    List<ActZone> selectListByReid(Integer id);
    
    ActZone selectByUuid(String uuid);
    
    /*ActZone selectByGoodsId(Integer goodsId);*/
    
    List<ActZone> selectByGoodsId(Integer goodsId);
}