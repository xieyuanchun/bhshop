package com.bh.goods.mapper;

import java.util.List;

import com.bh.goods.pojo.GoodsImage;

public interface GoodsImageMapper {
    int deleteByPrimaryKey(Integer id);

    int insertGoodsImage(GoodsImage record);

    int insertSelective(GoodsImage record);

    GoodsImage selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsImage record);

    int updateByPrimaryKey(GoodsImage record);
    
    GoodsImage selectByGoodsId(Integer goodsId);
    
    List<GoodsImage> selectListByGoodsId(Integer goodsId);
    
    /*批量删除*/
    int batchDelete(List<String> list);
}