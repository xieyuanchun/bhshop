package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.CouponAndGift;

public interface CouponAndGiftMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponAndGift record);

    int insertSelective(CouponAndGift record);

    CouponAndGift selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponAndGift record);

    int updateByPrimaryKey(CouponAndGift record);
    
    
    List<CouponAndGift> selectByCGid(CouponAndGift record);
}