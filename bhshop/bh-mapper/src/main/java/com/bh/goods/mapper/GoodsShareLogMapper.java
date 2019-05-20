package com.bh.goods.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.goods.pojo.GoodsShareLog;

public interface GoodsShareLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsShareLog record);

    int insertSelective(GoodsShareLog record);

    GoodsShareLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsShareLog record);

    int updateByPrimaryKey(GoodsShareLog record);
    
    
    
    List<GoodsShareLog> getListByTeamNo(String teamNo);
    
    List<GoodsShareLog> pageList(String orderNo, String goodsName, @Param("orderType") String orderType);
    
    GoodsShareLog getBymIdAndSkuId(Integer mId, Integer skuId,@Param("teamNo")String teamNo);
}