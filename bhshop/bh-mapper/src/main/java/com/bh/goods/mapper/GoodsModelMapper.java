package com.bh.goods.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.goods.pojo.GoodsModel;

public interface GoodsModelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsModel record);

    int insertSelective(GoodsModel record);

    GoodsModel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsModel record);

    int updateByPrimaryKey(GoodsModel record);
    
    List<GoodsModel> selectBycatId(@Param("catId") String catId);
    
    List<GoodsModel> selectAllModel();
    
    List<GoodsModel> selectPage(String name, @Param("catId") String catId);
    
    int countAll();
    
    List<GoodsModel> selectByName(String name, String catid);
    
    List<GoodsModel> selectUpdateByName(String name, Integer id);
    
    
    List<GoodsModel> selectAllByName(String name, String catid, Integer id);
    
    List<GoodsModel> batchSelect(List<String> list);
  
	List<GoodsModel> getModelByGoodsIdAndModelId(Integer id, Integer modelId);
}