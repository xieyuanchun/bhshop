package com.bh.goods.mapper;

import java.util.List;

import com.bh.goods.pojo.CouponAndGift;

public interface CouponAndGiftMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CouponAndGift record);

    int insertSelective(CouponAndGift record);

    CouponAndGift selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CouponAndGift record);

    int updateByPrimaryKey(CouponAndGift record);

	CouponAndGift getByTwoId(Integer id, Integer valueOf);

	List<CouponAndGift> selectByCGid(CouponAndGift findCouponAndGift);

	List<CouponAndGift> getByCGidAndNum(CouponAndGift findCouponAndGift);
}