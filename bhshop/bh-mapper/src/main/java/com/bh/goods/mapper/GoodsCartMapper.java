package com.bh.goods.mapper;

import java.util.List;
import java.util.Set;

import org.apache.ibatis.annotations.Param;


import com.bh.goods.pojo.CartGoodsList;
import com.bh.goods.pojo.CartList;
import com.bh.goods.pojo.GoodsCart;
import com.bh.goods.pojo.GoodsCartListShopIdList;
import com.bh.goods.pojo.GoodsCartPojo;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.OrderGoodsCartListShopIdList;
import com.bh.jd.bean.order.StockParams;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;

public interface GoodsCartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsCart record);

    int insertSelective(GoodsCart record);

    GoodsCart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsCart record);

    int updateByPrimaryKey(GoodsCart record);
    
    List<GoodsCart> selectByOrderId(Integer orderId);
    
    
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
   int updateGoodsCartByPrimaryKeyAndgId2(@Param("list")List<String> list);
   
   /** ************              2017-10-24 cheng          ********/
   /**2017-10-24根据是否删除、用户的id、商品的id、skuid的id去查找**/
  GoodsCart selectGoodsCartBySelect(GoodsCart goodsCart);
  
  /***************************************************************************************2017-10-25星期三    根据购物车cart的id批量更新                           *****/
   int updateGoodsCartByPrimaryKeyAndSetIsDel4(List<String> ids);
   /***************************************************************************************2017-10-26星期四    根据购物车cart的id查询shopId                          *****/
   /** 2017-10-26根据goodsId查询对应的shopId*/
   List<GoodsCart> selectShopIdsByPrimarykey(@Param("list") List<String> list);
   List<Integer> selectShopId(@Param("list") List<String> list);
   /*************************2017-11-2 星期二， 根据用户的id查询它所在的购物车的数量****************************/
   int totalCartNum(Integer mId);
   /*************************2017-11-8 星期三， 根据用id和shipId查询它所在的购物车****************************/
   List<GoodsCart> selectGoodsCartByIdAndShopIds(@Param("list") List<String> list,@Param("shopId") Integer shopId);
   List<GoodsCart> selectGoodsName(@Param("list") List<String> list);
   int selectTotalNum(@Param("list") List<String> list,@Param("gId") Integer gId);
   List<GoodsCart> selectGoodsCartByBHOrJD(@Param("list") List<String> list,@Param("gId") Integer gId);
   List<GoodsCart> selectJDGoodsCart(@Param("list") List<String> list,@Param("shopId") Integer shopId);
   List<GoodsCart> selectJDGoodsCartByIsJD(@Param("list") List<String> list,@Param("shopId") Integer shopId,@Param("isJD")Integer isJD);
   //查询购买该商家的购买数量
   int selectSumSkuNum(@Param("list") List<String> list,@Param("shopId") Integer shopId);
   //查询买该商家的金额
   int selectSumPrice(@Param("list") List<String> list,@Param("shopId") Integer shopId);
   int selectSumSkuDeliv(@Param("list") List<String> list,@Param("shopId") Integer shopId);
   int selectSumSkuScore(@Param("list") List<String> list);
   int selectSumSkuScore1(@Param("goodsId")Integer goodsId,@Param("num")int num);
   int selectSumSkuScore2(@Param("goodsId")Integer goodsId,@Param("num")int num);
   List<StockParams> selectGoodsCartByJD(@Param("list") List<String> list,@Param("gId") Integer gId);
   
   //2018-5-18
   int insertSelectiveByBatch(@Param("list")List<GoodsCart> list);
   List<GoodsCart> selectGoodsMsg(GoodsCart goodsCart);
   //查询多少个商家
   List<CartList> selectShopMsg(GoodsCart goodsCart);
   //查询该商家有多少个商品
   List<CartGoodsList> selectCartGoodsList(GoodsCart goodsCart);
   //未登录
   List<CartList> selectShopMsgNotLogin(@Param("list")List<GoodsCartPojo> pojo);
   List<CartGoodsList> selectGoodsMsgNotLogin(@Param("list")List<GoodsCartPojo> pojo,@Param("shopId")Integer shopId);
   //失效商品列表
   List<GoodsCart> getLoseEfficacyCart(Integer mId);
   
   //去支付的商品回显
   List<OrderGoodsCartListShopIdList> selectGoodsCartListShopIdList(@Param("list") List<String> list);
   List<GoodsSku> selectCartGoodsList1(@Param("list") List<String> list,@Param("shopId")Integer shopId);
   //取消订单后再次支付的商品回调
   List<OrderShop> selectOrderGoodsCartList(@Param("orderId")Integer orderId);
   List<OrderSku> selectOrderSkuGoodsCartList(@Param("orderShopId")Integer orderShopId);
   
   //2018-5-22新增接口：购物车列表
   List<GoodsCartListShopIdList> selectGoodsList(GoodsCart goodsCart);
   List<GoodsCart> selectCartListByShopId(GoodsCart goodsCart);
   List<GoodsSku> selectJdSkuNo(@Param("list") List<String> list,@Param("mId")Integer mId);
   
   List<GoodsCart> getGoodsByCartId(@Param("list") List<String> list);
   
   List<GoodsCart> getGoodsSkuListByCartId(@Param("list") List<String> list,@Param("shopId")Integer shopId);
   //查询标签
   String selectTagName(@Param("id")Integer id);
   List<Integer> selectGoodsId(@Param("list") List<String> list);
   
   List<GoodsCart> selectByCartIds(@Param("list") List<String> list);


}