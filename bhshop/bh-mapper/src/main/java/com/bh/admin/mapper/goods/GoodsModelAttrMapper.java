package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.GoodsModelAttr;


public interface GoodsModelAttrMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsModelAttr record);

    int insertSelective(GoodsModelAttr record);

    GoodsModelAttr selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsModelAttr record);

    int updateByPrimaryKeyWithBLOBs(GoodsModelAttr record);

    int updateByPrimaryKey(GoodsModelAttr record);
    
    
    
    
    List<GoodsModelAttr> getBymId(Integer mId);
    
    List<GoodsModelAttr> selectAllBymId(Integer mId);
    
    int countAllBymId(Integer mId);
    
    List<GoodsModelAttr> selectByName(String name, Integer mId);
    
    List<GoodsModelAttr> getByName(String name, Integer modelId);
    
    List<GoodsModelAttr> selectUpdateByName(String name, Integer id, Integer mId);
}