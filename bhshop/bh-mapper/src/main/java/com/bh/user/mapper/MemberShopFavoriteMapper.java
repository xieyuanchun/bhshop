package com.bh.user.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.user.pojo.MemberShopFavorite;

public interface MemberShopFavoriteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemberShopFavorite record);

    int insertSelective(MemberShopFavorite record);

    MemberShopFavorite selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemberShopFavorite record);

    int updateByPrimaryKey(MemberShopFavorite record);

    
    //cheng
    List<MemberShopFavorite> selectByparams(@Param("mId") Integer mId,@Param("categoryId") Integer categoryId,@Param("shopName") String shopName);
    int selectCountByShopId(Integer shopId);
    int deleteByBatch(@Param("id") List<String> id);
    int selectShopfavoriteNumber(MemberShopFavorite memberShopFavorite);
    
    MemberShopFavorite findByShopIdAndMid(Integer shopId, Integer mId);

}