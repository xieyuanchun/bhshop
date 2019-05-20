package com.bh.goods.mapper;

import com.bh.goods.pojo.CouponGift;

public interface CouponGiftMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponGift record);

    int insertSelective(CouponGift record);

    CouponGift selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponGift record);

    int updateByPrimaryKey(CouponGift record);

	CouponGift getGiftByName(String giftName);
}