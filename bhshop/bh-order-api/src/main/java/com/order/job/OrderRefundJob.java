package com.order.job;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderRefundDocMapper;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderRefundDoc;
import com.bh.order.pojo.OrderShop;
import com.bh.result.BhResult;
import com.bh.user.mapper.WalletLogMapper;
import com.bh.user.mapper.WalletMapper;
import com.bh.user.pojo.Wallet;
import com.bh.user.pojo.WalletLog;
import com.order.shop.service.OrderMainService;
import com.bh.utils.IDUtils;
import com.bh.utils.JedisUtil;
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
 * @param <OrderMainService>
 */

@JobHander(value="orderRefundJob")
@Component
public class OrderRefundJob extends IJobHandler {
	
	private final static String ENCODE = "UTF-8"; 
	
	@Autowired
	private OrderMainService service;
    @Autowired
    private OrderRefundDocMapper orderRefundDocMapper;
    @Autowired
	private WalletMapper walletMapper;
    @Autowired
   	private WalletLogMapper walletLogMapper;
    @Autowired
	private OrderMapper mapper; //订单
    
    public static final  int fiveDayLen =7200; //五天时间
    
	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		// TODO Auto-generated method stub
		XxlJobLogger.log("XXL-JOB, Hello World.");
		
		try {
			Date newDate = new Date(JedisUtil.getInstance().time());// 当前时间
			
			List<OrderRefundDoc> orderRefundDocList = orderRefundDocMapper.selectByStatus();
            
			// 退款退货待审核
			if (orderRefundDocList.size() > 0) {
				for (OrderRefundDoc o : orderRefundDocList) {
					if(o.getRefundValidTime()!=null) {
						//当前时间大于审核设置的失效时间
						String refuseReason="";
						String username="系统审核成功";
						if(newDate.getTime()>o.getRefundValidTime().getTime()) {
							/*OrderRefundDoc ord=new OrderRefundDoc();
							ord.setOrderSkuId(o.getOrderSkuId());*/
							
							//申请退款
							if(o.getStatus()==0&&o.getReason().equals("退款")) { //申请退款
								o.setStatus(2); //退款成功
								o.setDisposeTime(newDate); //处理时间
								o.setAdminUser(username);
								auditRefund(o.getId().toString(),"2",o.getmId(),refuseReason,username);
								orderRefundDocMapper.updateByPrimaryKeySelective(o);
							}
							
							
							//申请退款退货
							if(o.getStatus()==0&&o.getReason().equals("退款退货")) {   
								o.setStatus(5);  //客服同意退货
								o.setDisposeTime(newDate); //处理时间
								o.setAdminUser(username);
								Calendar ca = Calendar.getInstance();
								ca.setTime(o.getDisposeTime());
								ca.add(Calendar.MINUTE, Contants.fiveDayLen);
								o.setLogisticsValidTime(ca.getTime());
								orderRefundDocMapper.updateByPrimaryKeySelective(o);
							}
							if(o.getLogisticsValidTime()!=null&&o.getReason().equals("退款退货")) {
                            	//当前时间大于设置的填写物流时间
                            	if(newDate.getTime()>o.getLogisticsValidTime().getTime()) {
                               	   if(o.getStatus()==5) { //换货审核成功
                                    	o.setStatus(9);  //已过填写物流时间换货失败
                                    	o.setDisposeTime(newDate); //处理时间
                                    	orderRefundDocMapper.updateByPrimaryKeySelective(o);
                                   }
                                }
                            	
                            }
							if(newDate.getTime()>o.getRefundValidTime().getTime()&&o.getSaveTime()!=null) { //当前时间大于设置的审核时间并且填写物流时间不为空
							   if((o.getStatus()==5||o.getStatus()==11)&&o.getReason().equals("退款退货")) {
								  o.setStatus(2); //退款成功
								  o.setDisposeTime(newDate); //处理时间
								  o.setAdminUser(username);
								  auditRefund(o.getId().toString(),"2",o.getmId(),refuseReason,username);
								  orderRefundDocMapper.updateByPrimaryKeySelective(o);
							   }
							}
							
							
							//申请换货
                            if(o.getStatus()==0&&o.getReason().equals("换货")) { 
                            	o.setStatus(7);  //审核成功
                            	o.setDisposeTime(newDate); //处理时间
                            	o.setAdminUser(username);
                            	Calendar ca = Calendar.getInstance();
								ca.setTime(o.getDisposeTime());
								ca.add(Calendar.MINUTE, Contants.fiveDayLen);
								o.setLogisticsValidTime(ca.getTime());
								orderRefundDocMapper.updateByPrimaryKeySelective(o);
							}
                            if(o.getLogisticsValidTime()!=null&&o.getReason().equals("换货")) {
                            	//当前时间大于设置的填写物流时间
                            	if(newDate.getTime()>o.getLogisticsValidTime().getTime()) {
                               	   if(o.getStatus()==7) { //换货审核成功
                                    	o.setStatus(9);  //已过填写物流时间换货失败
                                    	o.setDisposeTime(newDate); //处理时间
                                    	orderRefundDocMapper.updateByPrimaryKeySelective(o);
                                   }
                                }
                            }
                            //当前时间大于设置的换货时间
                            if(newDate.getTime()>o.getRefundValidTime().getTime()&&o.getSaveTime()!=null) { //当前时间大于设置的审核时间并且填写物流时间不为空
                               if(o.getStatus()==7&&o.getReason().equals("换货")) {
                            	  o.setStatus(8); //换货成功
                            	  o.setDisposeTime(newDate); //处理时间
                            	  o.setAdminUser(username);
                            	  orderRefundDocMapper.updateByPrimaryKeySelective(o);
                               }
                            }
                          
						}
					}
				}	
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		XxlJobLogger.log("beat 2 seconds");
		TimeUnit.SECONDS.sleep(2);
		return ReturnT.SUCCESS;
	}

	//退款代码
	public BhResult auditRefund(String id, String status,Integer userId,String refuseReason,String username) {
		BhResult result = null;
		   try {
			   
			    OrderRefundDoc o = orderRefundDocMapper.selectByPrimaryKey(Integer.valueOf(id));
			    
			    //判断订单支付类型是微信 还是 钱包,如果是钱包，钱退到钱包
			    Order order= mapper.selectByPrimaryKey(o.getOrderId());
			    if(order.getPaymentId()==7) {
			    	
			    	if(status.equals("2")) {
			    	 //根据  用户id 获取 钱包信息
		             List<Wallet> wallet = walletMapper.getWalletByUid(o.getmId());
		             wallet.get(0).setMoney(wallet.get(0).getMoney()+o.getAmount());
		             int num = walletMapper.updateByUid(wallet.get(0));
		             
		 			 //自己生成订单号
		 			 IDUtils iDUtils = new IDUtils();
		 			 WalletLog walletLog = new WalletLog();
					 walletLog.setAddTime(new Date()); //时间
					 walletLog.setAmount(o.getAmount());  //金额
					 walletLog.setmId(o.getmId());    //登录者id
					 walletLog.setOrderNo(iDUtils.getOrderNo(Contants.PLAT_ORDER_NO_PRE)); //订单号
					 walletLog.setInOut(0);  //进账
					 walletLog.setOrderId(order.getId());
					 walletLog.setRemark("订单退款");//2018.7.10 zlk
					 if(num>0) {
					       walletLog.setStatus(1); //成功 状态 1
					 }else {
						   walletLog.setStatus(2); //失败状态 2
					 }
				     walletLogMapper.insertSelective(walletLog); //保存一条充值记录到WalletLog表
					 if(num>0) {//退款到钱包成功
						 OrderRefundDoc doc = new OrderRefundDoc();
						 doc.setId(Integer.valueOf(id));
						 doc.setStatus(2);
						 orderRefundDocMapper.updateByPrimaryKeySelective(doc);
						 return result = new BhResult(BhResultEnum.SUCCESS, null);
					 }else {
						 OrderRefundDoc doc = new OrderRefundDoc();
						 doc.setId(Integer.valueOf(id));
						 doc.setStatus(1);
						 orderRefundDocMapper.updateByPrimaryKeySelective(doc);
						 return result = new BhResult(BhResultEnum.VISIT_FAIL, null);
					 }
			    	}
			    }
			    //微信支付的退款
			       OrderShop row = service.auditRefund(id, status, userId, refuseReason,username);
				   if (row == null) {
					   result = new BhResult(BhResultEnum.FAIL, null);
					} else {
						result = new BhResult(BhResultEnum.SUCCESS, row);
					}
				} catch (Exception e) {
					result = new BhResult(BhResultEnum.VISIT_FAIL, null);
					e.printStackTrace();
					//LoggerUtil.getLogger().error(e.getMessage());
				}
		return result;
	}
	
	
	
	
	
	
}
