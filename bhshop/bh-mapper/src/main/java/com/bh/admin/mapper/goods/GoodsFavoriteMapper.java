package com.bh.admin.mapper.goods;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.admin.pojo.goods.GoodsFavorite;


public interface GoodsFavoriteMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsFavorite record);

    int insertSelective(GoodsFavorite record);

    GoodsFavorite selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsFavorite record);

    int updateByPrimaryKey(GoodsFavorite record);
    
    
    GoodsFavorite findByGoodsIdAndMid(Integer goodsId, Integer mId);
    
    /***********************cheng**************************/
    GoodsFavorite selectGoodsfavoriteByParams(GoodsFavorite favorite);
    //List<GoodsFavorite> selectGoodsfavorite(GoodsFavorite favorite);
    //@Param("mId") Integer mId,@Param("gskuid") Integer gskuid,@Param("gIds") List<String> gIds
    List<GoodsFavorite> selectGoodsfavorite(@Param("mId") Integer mId,@Param("categoryId") Long categoryId,@Param("goodName") String goodName);
    int deleteByBatch(@Param("id") List<String> id);
    int selectGoodsfavoriteNumber(GoodsFavorite favorite);

}