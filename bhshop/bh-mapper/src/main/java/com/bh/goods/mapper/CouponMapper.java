package com.bh.goods.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.goods.pojo.Coupon;
import com.bh.goods.pojo.CouponLog;



public interface CouponMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Coupon record);

    int insertSelective(Coupon record);

    Coupon selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Coupon record);

    int updateByPrimaryKey(Coupon record);
    //PC端条件查询优惠劵
    List<Coupon> listPage(Coupon record);
    
    //PC端条件查询优惠劵
    List<Coupon> moblieListPage(Coupon record);
    
    List<Coupon> selectCouponByParam(@Param("shopId")Integer shopId,@Param("list")List<String> list);
    int selectSkuPriceByOShopId(@Param("shopId")Integer shopId,@Param("orderShopId")Integer orderShopId);
    List<Coupon> selectSkuListByOShopId(@Param("shopId")Integer shopId,@Param("orderShopId")Integer orderShopId);
    //按分类
    int selectSkuPriceByCatId(@Param("shopId")Integer shopId,@Param("logId")Integer logId,@Param("orderShopId")Integer orderShopId,@Param("catId")Long catId);
    List<Coupon> selectSkuListByCatId(@Param("shopId")Integer shopId,@Param("logId")Integer logId,@Param("orderShopId")Integer orderShopId,@Param("catId")Long catId);
}