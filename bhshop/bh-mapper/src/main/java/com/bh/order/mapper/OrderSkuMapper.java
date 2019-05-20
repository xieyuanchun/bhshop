package com.bh.order.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.order.pojo.MyOrder;
import com.bh.order.pojo.MyOrderSkuPojo;
import com.bh.order.pojo.OrderSku;

public interface OrderSkuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderSku record);

    int insertSelective(OrderSku record);

    OrderSku selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderSku record);

    int updateByPrimaryKeyWithBLOBs(OrderSku record);

    int updateByPrimaryKey(OrderSku record);
    
    /*测试*/
    List<OrderSku> getAllList(Integer shopId, boolean isSend);
    
    /*获取订单所有商品*/
    List<OrderSku> getOrderGoodsList(Integer orderId);
    
    List<OrderSku> getByOrderShopId(Integer orderShopId);
    
    List<OrderSku> getByOrderShopIdAndStatus(Integer orderShopId);
    
    String getMonthSaleCount(Integer goodsId);
    
    String getGoodsGroupSale(Integer goodsId);
    
    String getShopGroupSale(OrderSku entity);
    
    /*拼团使用*/
    List<OrderSku> getByOrderId(Integer orderId);
    
    /*scj-批量抛单*/
    int batchCastSheet(List<String> list);
    
    /*******************************程 凤云 2017-9-18 begin****************************/
    //批量插入ordergood  2017-9-18cheng
    int insertOrderSkuByBatch(List<OrderSku> list);
    
    //orderId查找它下面的order_sku
    List<OrderSku> getOrderGoodsListByOrderId(OrderSku sku);
    
    /**通过orderId查询shopIds**/
    List<OrderSku> selectShopIdsByOrderId(OrderSku orderSku);
    /********2017-11-1 添加该接口**********/
    List<OrderSku> selectOrderShopBySelect(OrderSku orderSku);
    List<OrderSku> selectOrderSkuByParams(OrderSku orderSku);
    List<OrderSku> selectOrderSkuByParams2(OrderSku orderSku);
    //根据
    List<OrderSku> selectOrderShopBy1(OrderSku orderSku);
    List<OrderSku> selectOrderSkuBymId(OrderSku orderSku);
   List<OrderSku> selectOrderSkuByOrderId(OrderSku orderSku);
   
   /********2017-11-30 添加该接口**********/
  List<OrderSku> selectOrderSkuByParams1(OrderSku orderSku);
  //通过orderSkuId查询order_shop_id再查询有多少个orderSku
  int selectCountOrderSku(@Param("id") Integer id);
  List<OrderSku> selectJDSkuId(OrderSku orderSku);
  List<OrderSku> selectJDSkuId1(OrderSku orderSku);
  List<OrderSku> selectJdSupport(OrderSku orderSku);
  int selectGoodsNum(Integer orderShopId);
  int selectGoodsTotalPrice(Integer orderShopId);
  int selectCountByGoods(@Param("mId") Integer mId,@Param("goodsId") Integer goodsId);
  List<OrderSku> selectOrderSkuByOrderId1(@Param("orderId") Integer orderId);
  int getOrderAllPrice(@Param("orderShopId") Integer orderShopId);
  int selectCountRefund(@Param("orderShopId") Integer orderShopId);
  List<OrderSku> selectJdSku(OrderSku orderSku);
  List<OrderSku> selectOrderSkuByOrderShopId(@Param("orderShopId") Integer orderShopId);
  List<OrderSku> selectOrderSkuByOrderShopId1(@Param("orderShopId") Integer orderShopId);
    /*******************************程 凤云 2017-9-18 end****************************/
  
  //统计--7日内商品销售TOP10
  List<OrderSku> getTopTenGoodsList(Date startTime, Date endTime,Integer orderSkuRefundStatus);
  
  //根据订单号获取
  List<OrderSku> getOrderSkuByOrderId(Integer orderId);

  List<OrderSku> getOrderSkuByOrderIdAndSkuId(Integer id, int skuId);
  List<OrderSku> getOrderSkuByOrderShopId(Integer orderShopId);

  
  List<OrderSku> getListBySkuId(Integer valueOf);

   List<OrderSku> getSkuListByOrderId(Integer id);

   
   List<OrderSku> selectByOrderNo(String orderNo);
   
   List<OrderSku> selectByOrderId(Integer orderId,Integer shopId);
   

   List<OrderSku> getSaleOrderList(Date parse, Date parse2, @Param("shopId") Integer shopId);
   List<OrderSku> selectIsRefund(@Param("orderShopId") Integer orderShopId);
   List<MyOrder> selectSkuScore(@Param("orderShopId") Integer orderShopId,@Param("list")List<String> list);
   int updateScore(OrderSku orderSku);
   int selectScore(@Param("orderShopId") Integer orderShopId);

   List<OrderSku> getSkuListByOrderShopId(Integer id);
   int selectSavePrice(@Param("orderShopId") Integer orderShopId);
   OrderSku selectOrderSkuMsg(Integer id);
   
   //支付时判断当前订单有没有下架商品
   int getByGoodsStatus(Integer id);
   
   //支付判断当前goods_sku 是否删除
   int getByGoodsSkuStatus(Integer id);
   
   List<OrderSku> getSelectByOrderNo(String orderNo);

  OrderSku getByJdSkuIdAndOrderShopId(Integer id, String long1);

OrderSku getByOrderShopIdAndSkuNo(Integer id, String string);

List<OrderSku> systemCommentGoods();

int countBhMoneyByOrderShopId(Integer orderShopId);

List<OrderSku> selectById(Integer orderShopId);

MyOrderSkuPojo selectBySkuId(Integer skuId);

}