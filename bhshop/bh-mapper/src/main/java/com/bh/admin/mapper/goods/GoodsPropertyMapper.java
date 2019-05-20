package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.GoodsProperty;


public interface GoodsPropertyMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsProperty record);

    int insertSelective(GoodsProperty record);

    GoodsProperty selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsProperty record);

    int updateByPrimaryKey(GoodsProperty record);
    
    
    List<GoodsProperty> listPage();
    
    List<GoodsProperty> selectByType(GoodsProperty record);
    
    List<GoodsProperty> selectGoodsPro(GoodsProperty record);
}