package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.CouponGift;

public interface CouponGiftMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponGift record);

    int insertSelective(CouponGift record);

    CouponGift selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponGift record);

    int updateByPrimaryKey(CouponGift record);
    
    
    List<CouponGift> listPage(CouponGift record);
}