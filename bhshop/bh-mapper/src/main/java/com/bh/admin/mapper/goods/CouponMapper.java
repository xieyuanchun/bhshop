package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.Coupon;




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
    
    List<Coupon> fixListPage(Coupon record);
    
}