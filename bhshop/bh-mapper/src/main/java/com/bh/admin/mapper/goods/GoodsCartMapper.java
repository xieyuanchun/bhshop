package com.bh.admin.mapper.goods;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.bh.admin.pojo.goods.GoodsCart;
import com.bh.admin.pojo.goods.Goods;

public interface GoodsCartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsCart record);

    int insertSelective(GoodsCart record);

    GoodsCart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsCart record);

    int updateByPrimaryKey(GoodsCart record);
    
    
    
    List<GoodsCart> batchSelectByGskuId(List<String> list);
    
   
    /************** ************** ************** ************** ************** chengfengyun ************** ************** ************** ************** ************** **************/
    //2017-9-14chengfengyun 写
    List<GoodsCart> selectCoodsCartByUserId(GoodsCart record);
    
    //2017-9-15
    List<GoodsCart> selectCoodsCartByIds(List<String> ids);
    
    //2017-9-22 根据用户的mId和goodsId更新商品的数量
    int updateGoodsCartBymIdAndgoodId(GoodsCart goodsCart);
    
    //批量插入2017-9-22
    int insertGoodCartByBatch(List<GoodsCart> goodsCarts);
    
    //2017-9-30
    List<GoodsCart> selectCoodsCartByIdsandmId (List<String> id);
    /***************************************************************************************2017-10-9星期一    根据购物车cart的id批量更新                           *****/
    int updateGoodsCartByPrimaryKey(List<String> id);
    
    /***************************************************************************************2017-10-10星期二    根据购物车cart的id批量更新                           *****/
   int updateGoodsCartByPrimaryKey2(@Param("mId") Integer mId,@Param("gIds") List<String> gIds);
   
   /***************************************************************************************2017-10-103星期五                               *****/
   List<GoodsCart> selectGoodsCartShopIds(GoodsCart goodsCart);
   
  /* List<GoodsCart> selectGoodsCartShopIds(Integer mId,List<Integer> list);*/
   
   
   /***************************************************************************************2017-10-16   星期一                            *****/
   int updateGoodsCartByPrimaryKeyAndgId(@Param("mId") Integer mId,@Param("gskuid") Integer gskuid,@Param("gIds") List<String> gIds);
   
   /***************************************************************************************2017-10-17   星期二                            *****/
   List<GoodsCart> selectCoodsCartByIds1(List<String> ids);
   
   /********************************************2017-10-23*******************************************************************/
   int updateGoodsCartByPrimaryKeyAndgId1(GoodsCart list);
   
   /** ************              2017-10-24 cheng          ********/
   /**2017-10-24根据是否删除、用户的id、商品的id、skuid的id去查找**/
  GoodsCart selectGoodsCartBySelect(GoodsCart goodsCart);
  
  /***************************************************************************************2017-10-25星期三    根据购物车cart的id批量更新                           *****/
   int updateGoodsCartByPrimaryKeyAndSetIsDel4(List<String> ids);
   /***************************************************************************************2017-10-26星期四    根据购物车cart的id查询shopId                          *****/
   /** 2017-10-26根据goodsId查询对应的shopId*/
   List<GoodsCart> selectShopIdsByPrimarykey(@Param("list") List<String> list);
   /*************************2017-11-2 星期二， 根据用户的id查询它所在的购物车的数量****************************/
   int totalCartNum(GoodsCart goodsCart);
   /*************************2017-11-8 星期三， 根据用id和shipId查询它所在的购物车****************************/
   List<GoodsCart> selectGoodsCartByIdAndShopIds(@Param("list") List<String> list,@Param("shopId") Integer shopId);
   List<GoodsCart> selectGoodsCart(@Param("list") List<String> list);
   int selectTotalNum(@Param("list") List<String> list,@Param("gId") Integer gId);
   List<GoodsCart> selectGoodsCartByBHOrJD(@Param("list") List<String> list,@Param("gId") Integer gId);
   List<GoodsCart> selectJDGoodsCart(@Param("list") List<String> list,@Param("shopId") Integer shopId);
   List<GoodsCart> selectJDGoodsCartByIsJD(@Param("list") List<String> list,@Param("shopId") Integer shopId,@Param("isJD")Integer isJD);
}