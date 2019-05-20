package com.order.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bh.goods.mapper.ActZoneGoodsMapper;
import com.bh.goods.pojo.ActZoneGoods;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.pojo.MyOrderFail;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.result.BhResult;
import com.order.user.service.UserOrderService;
import com.order.util.JedisUtil;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;

/**
 * 任务Handler的一个Demo（Bean模式）
 * 
 * 开发步骤：
 * 1、新建一个继承com.xxl.job.core.handler.IJobHandler的Java类；
 * 2、该类被Spring容器扫描为Bean实例，如加“@Component”注解；
 * 3、添加 “@JobHander(value="自定义jobhandler名称")”注解，注解的value值为自定义的JobHandler名称，该名称对应的是调度中心新建任务的JobHandler属性的值。
 * 4、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 * 
 * @author xuxueli 2015-12-19 19:43:36
 * 订单失效 
 */

@JobHander(value="orderFailJob")
@Component
public class OrderFailJob extends IJobHandler {
	
	private final static String ENCODE = "UTF-8"; 
	
    @Autowired
    private OrderShopMapper orderShopMapper;
    @Autowired
    private OrderSkuMapper orderSkuMapper;
    @Autowired
    private ActZoneGoodsMapper actZoneGoodsMapper;
    @Autowired
    private UserOrderService userOrderService;
    
    public static final  int oneDayLen =1440; //一天时间
    
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		// TODO Auto-generated method stub
		XxlJobLogger.log("XXL-JOB, Hello World.");
		
		try {
			Calendar ca = Calendar.getInstance();
			Date newDate = new Date(JedisUtil.getInstance().time());// 当前时间
			List<MyOrderFail> list = orderShopMapper.selectByStatus();
            
			// 待支付订单
			if (list.size() > 0) {
				for (MyOrderFail m : list) {
					
					int is_fail = 0;
					if(m.getValidLen()!=null) {
                      if (newDate.getTime() > m.getValidLen().getTime()) {
                    	  
						// 当前时间 大于专区商品下单后的设置时间 就失效
						OrderShop ordershop = new OrderShop();
						ordershop.setId(m.getId());
						ordershop.setStatus(10);
						
						
						orderShopMapper.updateByStatus(ordershop);
						userOrderService.updateBhdAndCoupon(ordershop);
						is_fail = 1;
					  }
					}
					// 如果没有失效
					if (is_fail == 0) {
						// 判断订单是否包换已下架商品
						//int num = orderSkuMapper.getByGoodsStatus(m.getOrderId());
						int num = orderSkuMapper.getByGoodsStatus(m.getId());
						if (num > 0) {
							OrderShop ordershop = new OrderShop();
							ordershop.setId(m.getId());
							ordershop.setStatus(11);
							
							orderShopMapper.updateByStatus(ordershop);
							userOrderService.updateBhdAndCoupon(ordershop);
						} else {
							// 商品没下架，判断订单是否包含有sku删除的
							//List<OrderSku> orderSkuList = orderSkuMapper.getOrderSkuByOrderId(m.getOrderId());
							List<OrderSku> orderSkuList = orderSkuMapper.getSkuListByOrderShopId(m.getId());
							for (OrderSku o : orderSkuList) {
								int numsku = orderSkuMapper.getByGoodsSkuStatus(o.getOrderShopId());
								if (numsku > 0) {
									OrderShop ordershop = new OrderShop();
									ordershop.setId(m.getId());
									ordershop.setStatus(12);
									
									orderShopMapper.updateByStatus(ordershop);
									userOrderService.updateBhdAndCoupon(ordershop);
									break;
								}
							}
						}
					}
				}
			}
		//待支付订单
//		if(list.size()>0) {
//			for(MyOrderFail m:list) {
//				List<Integer> list2 = new ArrayList<Integer>();
//				int is_fail=0;
//				//查询当前订单有多少个商品
//				List<OrderSku> listOrderSku = orderSkuMapper.getSkuListByOrderShopId(m.getId());
//				if(listOrderSku.size()>1) {
//					//1个商品以上
//				    for(int j=0;j<listOrderSku.size();j++) {
//				    	List<ActZoneGoods> listActZoneGoods = actZoneGoodsMapper.getByGoodsId(listOrderSku.get(j).getGoodsId());
//				    	if(listActZoneGoods.size()>0) {
//				    		//专区商品
//						    for(ActZoneGoods a :listActZoneGoods) {
//						    	if(StringUtils.isNotBlank(a.getFailuretime())) {
//						    	     list2.add(Integer.valueOf(a.getFailuretime()));
//						        }
//						    }
//				    	}else{
//				    		//普通商品
//				    		list2.add(oneDayLen);
//				    	}
//				    }
//				    ca.setTime(m.getAddtime());
//					ca.add(Calendar.MINUTE,Collections.min(list2));
//					if(newDate.getTime()>ca.getTime().getTime()) {
//						//当前时间  大于专区商品下单后的设置时间  就失效 
//						OrderShop ordershop = new OrderShop();
//						ordershop.setId(m.getId());
//						ordershop.setStatus(10);
//						orderShopMapper.updateByStatus(ordershop);
//						userOrderService.updateBhdAndCoupon(ordershop);
//						is_fail =1;
//					}
//					list2=null;
//					//如果没有失效
//					if(is_fail==0) {
//					   //判断订单是否包换已下架商品
//				       int num = orderSkuMapper.getByGoodsStatus(m.getOrderId());
//				       if(num>0) {
//				    	   OrderShop ordershop = new OrderShop();
//						   ordershop.setId(m.getId());
//						   ordershop.setStatus(11);
//						   orderShopMapper.updateByStatus(ordershop);
//						   userOrderService.updateBhdAndCoupon(ordershop);
//				       }else {
//				    	   //商品没下架，判断订单是否包含有sku删除的
//				    	   List<OrderSku> orderSkuList = orderSkuMapper.getOrderSkuByOrderId(m.getOrderId());
//				    	   for(OrderSku o:orderSkuList) {
//				    		  int numsku = orderSkuMapper.getByGoodsSkuStatus(o.getOrderId());
//				    		  if(numsku>0) {
//				    			 OrderShop ordershop = new OrderShop();
//								 ordershop.setId(m.getId());
//								 ordershop.setStatus(12);
//								 orderShopMapper.updateByStatus(ordershop);
//								 userOrderService.updateBhdAndCoupon(ordershop);
//								 break;
//				    		  }
//				    	   }
//				        }
//					}
//				}else if(listOrderSku.size()>0){
//					//1个商品
//					List<ActZoneGoods> listActZoneGoods = actZoneGoodsMapper.getByGoodsId(listOrderSku.get(0).getGoodsId());
//					int is_fail1=0;
//					if(listActZoneGoods.size()>0) {
//						//证明是专区商品
//					    for(ActZoneGoods a :listActZoneGoods) {
//					    	if(StringUtils.isNotBlank(a.getFailuretime())) {
//					         	list2.add(Integer.valueOf(a.getFailuretime()));
//					    	}
//					    }
//					    					    
//					    if(list2.size()>0) {
//						     ca.setTime(m.getAddtime());
//						     ca.add(Calendar.MINUTE,Collections.min(list2));						 
//						     if(newDate.getTime()>ca.getTime().getTime()) {
//							     //当前时间  大于专区商品下单后的设置时间  就失效 
//							     OrderShop ordershop = new OrderShop();
//							     ordershop.setId(m.getId());
//							     ordershop.setStatus(10);
//							     orderShopMapper.updateByStatus(ordershop);
//							     userOrderService.updateBhdAndCoupon(ordershop);
//							     is_fail1 = 1;
//						     }
//					    }
//						list2=null;
//						//订单没有失效
//						if(is_fail1 ==0) {
//						   //判断订单是否包换已下架商品
//					       int num = orderSkuMapper.getByGoodsStatus(m.getOrderId());
//					       if(num>0) {
//					    	   OrderShop ordershop = new OrderShop();
//							   ordershop.setId(m.getId());
//							   ordershop.setStatus(11);
//							   orderShopMapper.updateByStatus(ordershop);
//							   userOrderService.updateBhdAndCoupon(ordershop);
//					       }else {
//					    	   //商品没下架，判断订单是否包含有sku删除的
//					    	   List<OrderSku> orderSkuList = orderSkuMapper.getOrderSkuByOrderId(m.getOrderId());
//					    	   for(OrderSku o:orderSkuList) {
//					    		  int numsku = orderSkuMapper.getByGoodsSkuStatus(o.getOrderId());
//					    		  if(numsku>0) {
//					    			 OrderShop ordershop = new OrderShop();
//									 ordershop.setId(m.getId());
//									 ordershop.setStatus(12);
//									 orderShopMapper.updateByStatus(ordershop);
//									 userOrderService.updateBhdAndCoupon(ordershop);
//									 break;
//					    		  }
//					    	   }
//					       }
//						}
//					}else {
//					    //普通商品 失效时间是24小时
//						int is_fail2 =0;
//					    ca.setTime(m.getAddtime());
//					    ca.add(Calendar.MINUTE,oneDayLen);
//					    if(newDate.getTime()>ca.getTime().getTime()) {
//						   //当前时间  大于 普通商品下单后的二十小时  就失效 
//						   OrderShop ordershop = new OrderShop();
//						   ordershop.setId(m.getId());
//						   ordershop.setStatus(10);
//						   orderShopMapper.updateByStatus(ordershop);
//						   userOrderService.updateBhdAndCoupon(ordershop);
//						   is_fail2 = 1;
//					    }
//					    
//					    if(is_fail2==0) {
//					         //判断订单是否包换已下架商品
//					         int num = orderSkuMapper.getByGoodsStatus(m.getOrderId());
//					         if(num>0) {
//					    	       OrderShop ordershop = new OrderShop();
//							       ordershop.setId(m.getId());
//							       ordershop.setStatus(11);
//							       orderShopMapper.updateByStatus(ordershop);
//							       userOrderService.updateBhdAndCoupon(ordershop);
//					         }else {
//					    	       //商品没下架，判断订单是否包含有sku删除的
//					    	       List<OrderSku> orderSkuList = orderSkuMapper.getOrderSkuByOrderId(m.getOrderId());
//					    	       for(OrderSku o:orderSkuList) {
//					    		     int numsku = orderSkuMapper.getByGoodsSkuStatus(o.getOrderId());
//					    		     if(numsku>0) {
//					    			    OrderShop ordershop = new OrderShop();
//									    ordershop.setId(m.getId());
//									    ordershop.setStatus(12);
//									    orderShopMapper.updateByStatus(ordershop);
//									    userOrderService.updateBhdAndCoupon(ordershop);
//									    break;
//					    	  	      }
//					    	       }
//					         }
//					    }
//					}
//				}
//			}
//		}
		ca=null;
		}catch(Exception e) {
			e.printStackTrace();
		}
		XxlJobLogger.log("beat 2 seconds");
		TimeUnit.SECONDS.sleep(2);
		return ReturnT.SUCCESS;
	}

	
	
	
	
	
}
