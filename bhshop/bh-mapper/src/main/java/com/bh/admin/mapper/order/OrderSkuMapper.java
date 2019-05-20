package com.bh.admin.mapper.order;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.admin.pojo.order.OrderSku;
import com.bh.admin.pojo.order.ShopOrderRecordVo;


public interface OrderSkuMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(OrderSku record);

    int insertSelective(OrderSku record);

    OrderSku selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(OrderSku record);

    int updateByPrimaryKeyWithBLOBs(OrderSku record);

    int updateByPrimaryKey(OrderSku record);
    
    OrderSku selectById(Integer id);
    
    //List<OrderSku> selectByGoodsIdList(Integer id);
    
    List<OrderSku> selectByGoodsId(Integer id,Integer shopId);
    
    /*测试*/
    List<OrderSku> getAllList(Integer shopId, boolean isSend);
    
    /*获取订单所有商品*/
    List<OrderSku> getOrderGoodsList(Integer orderId);
    
    List<OrderSku> getByOrderShopId(Integer orderShopId);
    
    List<OrderSku> getByOrderShopId1(Integer orderShopId);
    
    List<OrderSku> getByOrderShopId2(Integer orderShopId);
    
    List<OrderSku> getByOrderShopIdAndRefund(Integer orderShopId);
    
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
    /*******************************程 凤云 2017-9-18 end
     * @param orderSkuRefundStatus ****************************/
  
  //统计--7日内商品销售TOP10
  List<OrderSku> getTopTenGoodsList(Date startTime, Date endTime, Integer orderSkuRefundStatus,Integer shopId);
  
  //根据订单号获取
  List<OrderSku> getOrderSkuByOrderId(Integer orderId);

  List<OrderSku> getOrderSkuByOrderIdAndSkuId(Integer id, int skuId);
  
  List<OrderSku> getOrderSkuByOrderShopId(Integer orderShopId);
  
  List<OrderSku> getListBySkuId(Integer valueOf);

  List<OrderSku> getSkuListByOrderId(Integer id);

  List<OrderSku> getSaleOrderList(Date parse, Date parse2, @Param("shopId") Integer shopId);

  List<OrderSku> getGoodsSale();

  List<OrderSku> getTopTenShop(Date startTime, Date endTime);

  List<OrderSku> getShopOrderAmount(@Param("startTime") Date startTime,@Param("endTime") Date endTime, @Param("shopId") Integer shopId);

List<OrderSku> getSkuByOrderShopId(Integer id);

List<ShopOrderRecordVo> shopOrderRecord(OrderSku orderSku);




}