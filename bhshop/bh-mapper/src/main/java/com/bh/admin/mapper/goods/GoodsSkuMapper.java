package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.GoodsSku;


public interface GoodsSkuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsSku record);

    int insertSelective(GoodsSku record);

    GoodsSku selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsSku record);

    int updateByPrimaryKeyWithBLOBs(GoodsSku record);

    int updateByPrimaryKey(GoodsSku record);
    
    
    List<GoodsSku> selectByJdGoods();
    
    int countByGoodsId(Integer goodsId);
    
    /*批量删除*/
    int batchDelete(List<String> list);
    
    List<GoodsSku> selectListByGoodsId(Integer goodsId);
    
    List<GoodsSku> selectListByGoodsIdAndStatus(Integer goodsId);
    
    List<GoodsSku> selectListByGidAndJdSkuNoAndStatus(Integer goodsId);
    
    List<GoodsSku> selectListByJdSkuNo();
    
    List<GoodsSku> selectByJdSkuNo(Long jdSkuNo);
    
    int selectByGoodsId1(Integer goodsId);

    
    /** ************ 2017-9-15 cheng****************/
   List<GoodsSku> selectByGoodsId(List<String> goodsId);
   int batchUpdateSkuByGoodsId(Integer goodsId);
   /** ************ 2017-10-13 cheng ********/
   GoodsSku selectGoodsSkuById(Integer id);
   List<GoodsSku> selectDouGoods(GoodsSku goodsSku);
   
   List<GoodsSku> getListByGoodsIdAndOrderByStore(Integer goodsId);

   //zlk
   List<GoodsSku> selectPricebyGoodsId(Integer goodsId);
   //zlk
   List<GoodsSku> getByJdSkuNo(GoodsSku goodsSku);
   
   List<GoodsSku> isExistByJdSkuNo(Long jdSkuNo);
   
   //处理goods 表已删除，sku表未删除
   List<GoodsSku> syncSkuDelete();
   //处理goods表 非京东，sku京东
   List<GoodsSku> syncSkuDeleteTwo();
}