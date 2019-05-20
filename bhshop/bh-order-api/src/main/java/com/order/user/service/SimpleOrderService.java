package com.order.user.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.OrderCleanAccount;
import com.bh.goods.pojo.OrderGoodsCartListShopIdList;
import com.bh.jd.bean.order.OrderStock;
import com.bh.order.pojo.BHSeed;
import com.bh.order.pojo.MyOrderShopDetail;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderShop;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;

import net.sf.json.JSONObject;

public interface SimpleOrderService {
	int insertAddress(String addressId) throws Exception;	
	//程凤云 2018-4-12判断拍卖商品是否过了活动时间如果为0代表已过时间
    int isOverTopicTime(Integer orderId) throws Exception;
    
    /** 在购物车列表中点击‘去结算’ */
	OrderCleanAccount selecOnePreOrderInfo(String goodsIds,String mId, String fz, String teamNo) throws Exception;
	/*************************2017-11-3 星期五， 订单取消后再次购买****************************/
	OrderCleanAccount orderCancleBuy(OrderShop orderShop, String fz) throws Exception;
	
	List<OrderShop> selectOrderShopListByOrderId(Integer orderId)throws Exception;
	
	
	
	BhResult getSimpleBhBean(Map<String, String> map, Member member, HttpServletRequest request);
	
	//查看购物车的商品的数量是否大于或者等于1
	BhResult getGoodsByCartId(String ids)throws Exception;
	
	Order selectOrderById(Integer id)throws Exception;
 	
	Order getOrder(Map<String, String> map, Member member, HttpServletRequest request);
	
	public BHSeed getBhSeed1(String goodsCartIds, String mId, String fz, Integer isCart,List<GoodsSku> list) throws Exception;
	
	public BHSeed getBhSeed(String goodsCartIds, String mId, String fz, Integer isCart) throws Exception;
	
	/**
	 * 方法说明:该接口是用户的订单列表
	 * 参数：对象orderShop
	 * 返回的类型：Map<String, Object>
	 * **/
	public Map<String, Object> selectOrderList(OrderShop orderShopParam, Integer currentPage, Integer pageSize) throws Exception ;
	
	public MyOrderShopDetail selectOrderShopBySelectSingle(OrderShop orderShop) throws Exception;
	
	OrderStock orderStock(String id, String jdSkuNo);
	JSONObject queryLogistics(String expressNo) throws Exception;
	
}
