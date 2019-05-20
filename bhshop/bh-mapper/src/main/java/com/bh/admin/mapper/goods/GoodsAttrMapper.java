package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.GoodsAttr;


public interface GoodsAttrMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsAttr record);

    int insertSelective(GoodsAttr record);

    GoodsAttr selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsAttr record);

    int updateByPrimaryKey(GoodsAttr record);
    
    
    
    List<GoodsAttr> getListByAttrId(Integer attrId);
    
    List<GoodsAttr> selectAllByGoodsId(Integer goodsId);
    
    List<GoodsAttr> getListByGoodsId(Integer goodsId);
    //获取某个模型某个商品的所有属性 XIEYC
	List<GoodsAttr> getModelByGoodsIdAndModelId(Integer id, Integer modelId);

}