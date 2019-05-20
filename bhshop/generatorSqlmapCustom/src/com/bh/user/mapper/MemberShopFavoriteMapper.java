package com.bh.user.mapper;

import com.bh.user.pojo.MemberShopFavorite;

public interface MemberShopFavoriteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberShopFavorite record);

    int insertSelective(MemberShopFavorite record);

    MemberShopFavorite selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberShopFavorite record);

    int updateByPrimaryKey(MemberShopFavorite record);
}