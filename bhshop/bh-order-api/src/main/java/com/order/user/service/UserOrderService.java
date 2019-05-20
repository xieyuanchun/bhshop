package com.order.user.service;



import java.util.List;
import java.util.Map;

import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsCart;
import com.bh.goods.pojo.GoodsSku;
import com.bh.order.pojo.BHSeed;
import com.bh.order.pojo.CleanAccount;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderInfoPojo;
import com.bh.order.pojo.OrderSeed;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderTeam;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberUserAddress;
import com.bh.utils.PageBean;

public interface UserOrderService {
	
	Goods selectBygoodsId(Integer id) throws Exception;
	//插入购物车goodsCart
	GoodsCart insertGoodsCart(GoodsCart goodsCart) throws Exception;
	
	//更新购物车
	GoodsCart updateByPrimaryKeySelective(GoodsCart goodsCart) throws Exception;
	
	GoodsCart selectCoodsCartByPrimaryKey(Integer id) throws Exception;
	
	//删除购物车
	List<GoodsCart> deleteCoodsCartByPrimaryKey(GoodsCart goodsCart) throws Exception;
		
	/********************************************2017-9-15星期五 *****************************************************************/
	//根据前端传来的cart的多个id查询
	List<Goods> selectById(String Ids) throws Exception;
	
	
	//通过id查询GoodsSku
	GoodsSku selectGoodsSkuByGoodsId(String goodId) throws Exception;
	

	//用户取消订单
	int updateOrderById(Order order,String note,String mId,String skuId) throws Exception;
	
	//用户删除订单2017-11-1
	int updateOrderStatus(OrderShop order,String orderId) throws Exception;
	
	/*****************************************2017-9-21 星期四*******************************************************************************************************/
	//删除购物车
	List<GoodsCart> selectCoodsCartByPrimaryKeyAndmId(GoodsCart g) throws Exception;
	
	MemberShop selectMemberShopByGoodId(Integer goodId) throws Exception;
	
	//q
	GoodsCart selectCoodsCartByGoodsId(GoodsCart goodsCart);
	
	//9月26 根据用户的mId查询购物车
	List<GoodsCart> selectCoodsCartByUserId(GoodsCart goodsCart) throws Exception;
	//更新购物车
	int updateGoodsCartBymIdAndgoodId(GoodsCart goodsCart) throws Exception;
	
	//9月27 根据用户的mId和商品的gId插入
	int insertGoodCartByselect(GoodsCart goodsCart) throws Exception;
	
	//批量插入goodCart 2017-9-28
	int insertGoodCartByBatch(List<GoodsCart> goodsCarts) throws Exception;
	
	//分页显示购物车201-9-28
	PageBean<GoodsCart> getCartByPage(GoodsCart g,Integer page ,Integer rows) throws Exception;
	
	//2017-9-29
	int updateGoodsCartByPrimaryKey(GoodsCart goodsCart) throws Exception;
	
	/**** 根据mId和is_De=0 选择有多少个  */
	int selectCountCartsByisDel(Integer mId) throws Exception;
	
	/**  根据用户选择默认的地址 2017-9-30    ****/
	MemberUserAddress selecUseraddresstBySelect(MemberUserAddress address) throws Exception;
	/** 在购物车列表中点击‘去结算’ */
	CleanAccount selecOnePreOrderInfo(String goodsIds,String mId, String fz, String teamNo) throws Exception;
	
    /*****2017-10-9星期一    根据购物车cart的id批量更新*****/
    int updateGoodsCartByPrimaryKey(List<String> id) throws Exception;
    int updateGoodsCartByPrimaryKeyAndgId(Integer mId,Integer skuId,List<String> id) throws Exception;
    int updateGoodsCartByPrimaryKeyAndgId1(List<GoodsCart> goodsCart) throws Exception;
    /***************************************************************************************2017-10-10星期二    根据购物车cart的id批量更新*****/
    int updateGoodsCartByPrimaryKey2(Integer mId,List<String> id) throws Exception;
    //无用到
    PageBean<GoodsCart> getPage(int page,int size,List<GoodsCart> goodsCartsList) throws Exception;

    /***************************************************************************************2017-10-13星期五    根据购物车cart的id批量更新                           *****/
    List<GoodsCart> selectGoodsCartShopIds(GoodsCart goodsCart) throws Exception;
    
    List<GoodsCart> getGoodscart(GoodsCart g) throws Exception;
    //通过goodsku找到它
    GoodsSku getGoodsSkuById(Integer id) throws Exception;
    
    /** ************              2017-10-24 cheng          ********/
    /**2017-10-24根据是否删除、用户的id、商品的id、skuid的id去查找**/
   GoodsCart selectGoodsCartBySelect(GoodsCart goodsCart) throws Exception;
    
   /*********2017-11-1星期一**********/
     //订单列表（全部订单--通过orderShop查询）
 	PageBean<OrderShop> selectAllOrderShopList(OrderShop orderShop,Integer page,Integer rows) throws Exception;
 	//2017-11-1
 	OrderShop selectOrderShopBySelectSingle(OrderShop orderShop) throws Exception;
 	
    /*************************2017-11-2 星期二， 根据用户的id查询它所在的购物车的数量****************************/
 	OrderInfoPojo totalShopOrderNum(OrderShop orderShop) throws Exception;
 	int totalCartNum(Integer mId) throws Exception;
 	
 	/*************************2017-11-3 星期五， 订单取消后再次购买****************************/
 	CleanAccount orderCancleBuy(OrderShop orderShop, String fz) throws Exception;
 	
 	/****************2017-11-06星期二 根据用户的mId查询他的所有地址cheng****************/
 	List<MemberUserAddress> selectUserAllAddress(Integer mId)throws Exception;
 	//2017-11-06星期二 根据地址主键查询地址cheng
 	MemberUserAddress selectUserAllAddressByPrimarykey(Integer id)throws Exception;
 	//通过orderMain的id查询,2017-11-13
 	List<OrderShop> selectOrderMainById(OrderShop orderShop) throws Exception;
 	/**  通过order_main id查找order_main的记录 2017-11-17    ****/
	Order selectOrderById(Order order) throws Exception;
	Order selectOrderById1(Order order) throws Exception;
	 
    /**
     * author xxj
     * 更新订单状态
     * @return
     */
     void updateStatusByOrderNo(Order order);
     /**
      * author xxj
      * 根据订单号获取订单
      * @param orderNo
      * @return
      */
     Order getOrderByOrderNo(String orderNo);
     
     /*判断是否重复拼单*/
     int getByMidAndTeamNo(int mId, String teamNo,String goodsId) throws Exception;
     
     void submitJdOrder(Order order);
     //通过商品的id获得当前的拍卖价
     public Integer returnDauPrice(Integer goodsId);
   
     //判断当前商品的拼团人数是否超额 2018.5.8 zlk
     int teamNumExcess(String teamNo,String goodsId);
     
     Map<String,Object> updateUserStoreSecond(Order order3,String fz);
     
     void updateBhdAndCoupon(OrderShop orderShop);
     
     void updateBhdAndCouponSecond(OrderShop orderShop);
     
}
