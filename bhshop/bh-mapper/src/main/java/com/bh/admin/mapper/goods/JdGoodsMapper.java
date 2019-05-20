package com.bh.admin.mapper.goods;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.goods.JdGoods;

public interface JdGoodsMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JdGoods record);

    int insertSelective(JdGoods record);

    JdGoods selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JdGoods record);

    int updateByPrimaryKey(JdGoods record);
    
    List<JdGoods> selectByJdSkuNo(long jdSkuNo);
    
    //京东商品列表
    List<JdGoods> listPage(JdGoods record);
    
    //根据京东商品编码获取
    List<JdGoods> getByJdSkuNo (JdGoods record);
    
    //根据京东商品编码及商品池获取
    List<JdGoods> getByJdSkuNoAndPoolNum(JdGoods record);
    
  //京东商品列表
    List<JdGoods> excelExportGoodsList(@Param("jdSkuNo")Integer jdSkuNo, @Param("poolNum")String poolNum, @Param("goodsName")String goodsName, @Param("brandName")String brandName, @Param("catId")String catId, @Param("isUp")Integer isUp,
    		@Param("isDelete")Integer isDelete, @Param("isGet")Integer isGet, @Param("startJdPrice")Double startJdPrice, @Param("endJdPrice")Double endJdPrice, @Param("startStockPrice")Double startStockPrice, @Param("endStockPrice")Double endStockPrice);
    
    //获取所有京东商品编码 
    List<JdGoods> selectAllJdSkuNo();
    
    List<JdGoods> syncIsGetStatusOne();
    
    List<JdGoods> syncIsGetStatusTwo();
    
    List<JdGoods> syncJdGoodsPrice();
    
    int updateByJdSkuNo(JdGoods record);
}