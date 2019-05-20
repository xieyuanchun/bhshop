package com.bh.goods.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.goods.pojo.GoodsSku;
import com.bh.jd.bean.order.NewStock;

public interface GoodsSkuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsSku record);

    int insertSelective(GoodsSku record);

    GoodsSku selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsSku record);

    int updateByPrimaryKeyWithBLOBs(GoodsSku record);

    int updateByPrimaryKey(GoodsSku record);
    
    
    
    
    int countByGoodsId(Integer goodsId);
    
    /*批量删除*/
    int batchDelete(List<String> list);
    
    List<GoodsSku> selectListByGoodsId(Integer goodsId);
    
    List<GoodsSku> selectListByGoodsIdAndStatus(Integer goodsId);
    
    List<GoodsSku> selectListByGidAndJdSkuNoAndStatus(Integer goodsId);
    
    List<GoodsSku> selectListByJdSkuNo();
    
    GoodsSku selectByJdSkuNo(Long jdSkuNo);

    
    /** ************ 2017-9-15 cheng****************/
   List<GoodsSku> selectByGoodsId(List<String> goodsId);
   int batchUpdateSkuByGoodsId(Integer goodsId);
   /** ************ 2017-10-13 cheng ********/
   GoodsSku selectGoodsSkuById(Integer id);
   List<GoodsSku> selectDouGoods(GoodsSku goodsSku);
   //通过jdSkuNo查找id
   List<GoodsSku> selectSkuIdByJDSkuNo(@Param("list")List<NewStock> stockList);
   List<GoodsSku> selectGoodsSkuByJDSku(Long jdSkuNo); 
   
   List<GoodsSku> getListByGoodsIdAndOrderByStore(Integer goodsId);

   //zlk
   List<GoodsSku> selectPricebyGoodsId(Integer goodsId);
   List<GoodsSku> selectGoodsSkuList(Integer goodsId);
    
}