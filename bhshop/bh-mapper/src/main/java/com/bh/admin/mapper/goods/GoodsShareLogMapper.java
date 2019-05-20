package com.bh.admin.mapper.goods;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.admin.pojo.goods.GoodsShareLog;

public interface GoodsShareLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsShareLog record);

    int insertSelective(GoodsShareLog record);

    GoodsShareLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsShareLog record);

    int updateByPrimaryKey(GoodsShareLog record);
    
    
    
    List<GoodsShareLog> getListByTeamNo(String teamNo);
    
    List<GoodsShareLog> pageList(String orderNo, String goodsName, @Param("orderType") String orderType,@Param("shopId") Integer shopId);
    
    GoodsShareLog getBymIdAndSkuId(Integer mId, Integer skuId);
}