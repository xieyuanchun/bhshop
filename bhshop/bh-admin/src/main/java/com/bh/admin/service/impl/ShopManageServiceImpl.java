package com.bh.admin.service.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bh.admin.mapper.order.OrderRefundDocMapper;
import com.bh.admin.mapper.order.OrderShopMapper;
import com.bh.admin.mapper.order.OrderSkuMapper;
import com.bh.admin.mapper.order.ShopWithdrawMapper;
import com.bh.admin.mapper.user.MemberShopInfoMapper;
import com.bh.admin.mapper.user.MemberShopMapper;
import com.bh.admin.pojo.order.OrderRefundDoc;
import com.bh.admin.pojo.order.OrderShop;
import com.bh.admin.pojo.order.OrderSku;
import com.bh.admin.pojo.order.ShopOrderRecordVo;
import com.bh.admin.pojo.order.ShopWithdraw;
import com.bh.admin.pojo.order.ShopWithdrawVo;
import com.bh.admin.pojo.user.MemberShopInfo;
import com.bh.admin.service.ShopManageService;
import com.bh.admin.util.ExcelFileGenerator;
import com.bh.util.enterprise.BankCardVerify;
import com.bh.util.enterprise.pojo.BankCardVerifyPojo;
import com.bh.utils.JedisUtil;
import com.bh.utils.MoneyUtil;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;

/**
 * @Description: 后台管理系统---统计接口
 * @author xieyc
 * @date 2018年1月6日 下午12:09:02 
 */
@Service
@Transactional
public class ShopManageServiceImpl implements ShopManageService {
	 SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
	 
	 private static final Logger logger = LoggerFactory.getLogger(ShopManageServiceImpl.class);
	 @Autowired
	 private OrderShopMapper orderShopMapper;//商家订单
	 @Autowired
	 private OrderSkuMapper orderSkuMapper; //订单商品
	 @Autowired
	 private ShopWithdrawMapper shopWithdrawMapper;
	 @Autowired
	 private OrderRefundDocMapper orderRefundDocMapper;
	 @Autowired
	 private  MemberShopInfoMapper  memberShopInfoMapper;
	 @Autowired
	 private MemberShopMapper memberShopMapper; //店铺
	 

	 
	 	 
	/**
	 * @Description: PC端：平台提现记录导出
	 * @author xieyc
	 * @throws IOException 
	 * @date 2018年8月15日 上午10:53:34
	 */
	public void excelExport(String startTime, String endTime, String isPay, String type, String state, String shopId,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		   ArrayList<String> fieldName =new ArrayList<>();  
		   fieldName.add("商家id");
	       fieldName.add("申请时间");
	       fieldName.add("提现金额");
	       fieldName.add("到账金额");
	       fieldName.add("手续费");
	       fieldName.add("开户行");
	       fieldName.add("银行卡号");
	       fieldName.add("持卡人/公司名字");
	       fieldName.add("联系人号码");
	       fieldName.add("审核状态");
	       fieldName.add("提现类型");
	       fieldName.add("是否转过账");
	       fieldName.add("审核人");
	       fieldName.add("备注");
	
	       ShopWithdraw findShopWithdraw=new  ShopWithdraw();
	       findShopWithdraw.setStartTime(startTime);
	       findShopWithdraw.setEndTime(endTime);
	       if(StringUtils.isNotBlank(isPay)){
	    	   findShopWithdraw.setIsPay(Integer.valueOf(isPay));
	       }
	       if(StringUtils.isNotBlank(type)){
	    	   findShopWithdraw.setType(Integer.valueOf(type));
	       }
	       if(StringUtils.isNotBlank(state)){
	    	   findShopWithdraw.setState(Integer.valueOf(state));
	       }
	       if(StringUtils.isNotBlank(shopId)){
	    	   findShopWithdraw.setShopId(Integer.valueOf(shopId));
	       }
	       ArrayList<ArrayList<String>> fieldData = new ArrayList<>();
	       List<ShopWithdrawVo> list = shopWithdrawMapper.getList(findShopWithdraw);
	       for (ShopWithdrawVo shopWithdrawVo : list) {
	    	   ArrayList<String> array = new ArrayList<>();
	    	   array.add(0, shopWithdrawVo.getShopId()+"");  //商家id
	    	   array.add(1, df.format(shopWithdrawVo.getApplyTime())+"");  //申请时间
	    	   array.add(2, shopWithdrawVo.getWithdrawAmount()+"");  //提现金额
	    	   array.add(3, shopWithdrawVo.getArrivalAmount()+"");  //到账金额
	    	   array.add(4, shopWithdrawVo.getServiceCharge()+"");  //手续费
	    	   array.add(5, shopWithdrawVo.getBankName()+"");  //开户行
	    	   array.add(6, shopWithdrawVo.getBankCardNo()+"");  //银行卡号
	    	   array.add(7, shopWithdrawVo.getBankCardOwner()+"");  //持卡人/公司名字
	    	   array.add(8, shopWithdrawVo.getLinkmanPhone()+"");  //联系人号码
	    	   if(shopWithdrawVo.getState()==0){
	    		   array.add(9, "审核中");  //审核状态
	    	   }else if(shopWithdrawVo.getState()==1){
	    		   array.add(9, "审核成功");  //审核状态
	    	   }else{
	    		   array.add(9, "审核失败");  //审核状态
	    	   }
	    	   if(shopWithdrawVo.getType()==0){
	    		   array.add(10, "对私转账");  //提现类型
	    	   }else{
	    		   array.add(10, "对公转账");  //提现类型
	    	   }
	    	   if(shopWithdrawVo.getIsPay()==0){
	    		   array.add(11, "未转账");  //是否转过账  
	    	   }else{
	    		   array.add(11, "已转账");  //是否转过账
	    	   }
	    	   array.add(12, shopWithdrawVo.getAuditor()+"");  //审核人
	    	   array.add(13, shopWithdrawVo.getRefuseReason()+"");  //备注
	    	   fieldData.add(array);
	       }
	       ExcelFileGenerator generator = new ExcelFileGenerator(fieldName,fieldData);  
	       OutputStream os = response.getOutputStream();
	       Date date = new Date();
	       String filename = "商家提现记录excel（"+ df.format(date).replace(":", "_") +"）.xls";  
	       filename = ExcelFileGenerator.processFileName(request,filename);  
	       //可以不加，但是保证response缓冲区没有任何数据，开发时建议加上  
	       response.reset();  
	       response.setContentType("application/vnd.ms-excel;charset=utf-8");  
	       response.setHeader("Content-disposition", "attachment;filename="+filename);  
	       response.setBufferSize(1024);  
	       /**将生成的excel报表，写到os中*/  
	       generator.expordExcel(os);  
	}

	/**
	 * @Description: PC端：平台提现记录列表
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34
	 */
	public PageBean<ShopWithdrawVo> withdrawRecordListByPt(ShopWithdraw entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getPageSize(), true);
		List<ShopWithdrawVo> list = shopWithdrawMapper.getList(entity);
		PageBean<ShopWithdrawVo> page = new PageBean<>(list);
		return page;
	}
	/**
	 * @Description: PC端：平台更新审核状态
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	public int update(ShopWithdraw entity,Integer userId) {
		 String userName = memberShopMapper.selectUsernameBymId(userId); //查找审核人
		if(entity.getState()!=null){//不为null的时候表示: 更新审核步骤,是null的时候表示更新:是否已转账状态
			entity.setAuditor(userName);//审核人
			entity.setAuditTime(new Date(JedisUtil.getInstance().time()));//审核时间
		}else{
			entity.setTransferPeople(userName);//确认转账人
			entity.setConfirmTime(new Date(JedisUtil.getInstance().time()));//确认转账时间
		}
		entity.setEditTime(new Date(JedisUtil.getInstance().time()));//编辑时间
		return shopWithdrawMapper.updateByPrimaryKeySelective(entity);
	}
	
	/**
	 * @Description: PC端：某个商家交易记录列表
	 * @author xieyc
	 * @throws Exception 
	 * @date 2018年8月15日 上午10:53:34 
	 */
	public PageBean<ShopOrderRecordVo> shopOrderRecord(OrderSku orderSku) throws Exception {
		PageHelper.startPage(orderSku.getCurrentPage(), orderSku.getPageSize(), true);
		List<ShopOrderRecordVo> list = orderSkuMapper.shopOrderRecord(orderSku);
		for (ShopOrderRecordVo shopOrderRecordVo : list) {
			shopOrderRecordVo.setUsername(URLDecoder.decode(shopOrderRecordVo.getUsername(), "UTF-8"));
			
			OrderRefundDoc findDoc=new OrderRefundDoc();
			findDoc.setOrderSkuId(shopOrderRecordVo.getOrderSkuId());
			OrderRefundDoc orderRefundDoc=orderRefundDocMapper.selectByOrderSkuId(findDoc);
			//商家订单状态：1待付，2待发货，3已发货，5待评价、6已取消、7已评价、8已删除、9备货中、10已失效  、11商品下架 、12sku已删除
			if(shopOrderRecordVo.getOrderIdStatus()==1){
				//待付款订单
				shopOrderRecordVo.setStatus("待付款");
			}else{
				if(orderRefundDoc==null){//没有发生退款的时候
					if(shopOrderRecordVo.getOrderIdStatus()==2||shopOrderRecordVo.getOrderIdStatus()==3||shopOrderRecordVo.getOrderIdStatus()==9){
						//没有退款且待发货、已发货、备货的订单
						shopOrderRecordVo.setStatus("待结算");
					}else if(shopOrderRecordVo.getOrderIdStatus()==5 ||shopOrderRecordVo.getOrderIdStatus()==7){
						//没有退款且待评价、已评价
						shopOrderRecordVo.setStatus("交易完成");
					}else if(shopOrderRecordVo.getOrderIdStatus()==8){
						//没有退款且已删除但已经付款
						if(shopOrderRecordVo.getPaymentStatus()==2){//已删除但已经付款
							shopOrderRecordVo.setStatus("交易完成");
						}else{//已删除但没有付款
							shopOrderRecordVo.setStatus("交易失败");
						}
					}else{
						shopOrderRecordVo.setStatus("交易失败");
					}
				}else{//发生退款的时候
					//退款状态，0:申请退款 1:退款失败 2:退款成功，3：已拒绝 5:申请退货中 6:申请退货失败 7:换货中 8：换货成功 9：换货失败 10客服通过退款审核 11收货客服审核通过
					if (orderRefundDoc.getStatus() == 0 || orderRefundDoc.getStatus() == 5
							|| orderRefundDoc.getStatus() == 7 || orderRefundDoc.getStatus() == 10
							|| orderRefundDoc.getStatus() == 11) {
						shopOrderRecordVo.setStatus("售后中");
					} else {
						shopOrderRecordVo.setStatus("售后完成");
					}
				}
			}
		}
		PageBean<ShopOrderRecordVo> page = new PageBean<>(list);
		return page;
		
	}	
	/**
	 * @Description: PC端：计算交易金额、待结算金额
	 * @author xieyc
	 * @throws Exception 
	 * @date 2018年8月15日 上午10:53:34 
	 */
	public Map<Object, Object> countMoney(String startTime, String endTime, Integer shopId) throws Exception {
		Map<Object, Object> returnMap=new HashMap<Object, Object>();
					
		/**************************1、交易金额  start*****************************************/
		int trandeAmount=0;//交易金额
		OrderShop findTrandeList=new OrderShop();
		findTrandeList.setShopId(shopId);
		findTrandeList.setStartDateTime(startTime);
		findTrandeList.setEndDateTime(endTime);
		List<OrderShop> trandeList=orderShopMapper.getTrandeList(findTrandeList);
		for (OrderShop orderShop : trandeList) {
			int  refundMoney=orderRefundDocMapper.getSucessRefundMoneyByOrderShopId(orderShop.getId());//某个商家订单下的退款总金额
			trandeAmount+=orderShop.getOrderPrice()-refundMoney;
		}	
		/**************************交易金额  end*****************************************/
		
		/**************************2、待结算金额  start*****************************************/
		int settleAmount=0;//结算金额
		OrderShop findSettleList=new OrderShop();
		findSettleList.setShopId(shopId);
		findSettleList.setStartDateTime(startTime);
		findSettleList.setEndDateTime(endTime);
		List<OrderShop> settleList=orderShopMapper.getSettleList(findSettleList);
		for (OrderShop orderShop : settleList) {
			int shopDeliveryPrice=0;
			int  refundMoney=orderRefundDocMapper.getSucessRefundMoneyByOrderShopId(orderShop.getId());//某个商家订单下的退款总金额
			if(orderShop.getIsRefund()==2){
				int cha=orderShop.getOrderPrice()-orderShop.getgDeliveryPrice();
				if(cha==refundMoney){//全部退款成功减邮费
					shopDeliveryPrice=orderShop.getgDeliveryPrice();
				}
			}
			settleAmount+=orderShop.getOrderPrice()-refundMoney-shopDeliveryPrice;
		}	
		/**************************待结算金额  end*****************************************/
		
		returnMap.put("trandeAmount", MoneyUtil.IntToDouble(trandeAmount));//交易金额
		returnMap.put("settleAmount", MoneyUtil.IntToDouble(settleAmount));//结算金额
		
		return returnMap;
	}
	/**
	 * @Description: 微信端：计算交易金额、待结算金额
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	public Map<Object, Object> mCountMoney(String startTime, String endTime, int shopId) throws Exception{
		Map<Object, Object> returnMap=new HashMap<Object, Object>();
		
		/**************************1、交易金额  start*****************************************/
		int trandeAmount=0;//交易金额
		OrderShop findTrandeList=new OrderShop();
		findTrandeList.setShopId(shopId);
		findTrandeList.setStartDateTime(startTime);
		findTrandeList.setEndDateTime(endTime);
		List<OrderShop> trandeList=orderShopMapper.getTrandeList(findTrandeList);
		for (OrderShop orderShop : trandeList) {
			int  refundMoney=orderRefundDocMapper.getSucessRefundMoneyByOrderShopId(orderShop.getId());//某个商家订单下的退款总金额
			trandeAmount+=orderShop.getOrderPrice()-refundMoney;
		}	
		/**************************交易金额  end*****************************************/
		
		/**************************2、待结算金额  start*****************************************/
		int settleAmount=0;//结算金额
		OrderShop findSettleList=new OrderShop();
		findSettleList.setShopId(shopId);
		findSettleList.setStartDateTime(startTime);
		findSettleList.setEndDateTime(endTime);
		List<OrderShop> settleList=orderShopMapper.getSettleList(findSettleList);
		for (OrderShop orderShop : settleList) {
			int shopDeliveryPrice=0;
			int  refundMoney=orderRefundDocMapper.getSucessRefundMoneyByOrderShopId(orderShop.getId());//某个商家订单下的退款总金额
			if(orderShop.getIsRefund()==2){
				int cha=orderShop.getOrderPrice()-orderShop.getgDeliveryPrice();
				if(cha==refundMoney){//全部退款成功减邮费
					shopDeliveryPrice=orderShop.getgDeliveryPrice();
				}
			}
			settleAmount+=orderShop.getOrderPrice()-refundMoney-shopDeliveryPrice;
		}	
		/**************************待结算金额  end*****************************************/
		
		returnMap.put("trandeAmount", MoneyUtil.IntToDouble(trandeAmount));//交易金额
		returnMap.put("settleAmount", MoneyUtil.IntToDouble(settleAmount));//结算金额
		
		return returnMap;
	}
	
	/**
	 * @Description: PC端：可提现金额、已提现金额、锁定金额 
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	public Map<Object, Object> countWithdraMoney(Integer shopId) {
		Map<Object, Object> returnMap=new HashMap<Object, Object>();
		int canWithdraAmount=this.getCanWithdraAmount(shopId);//可提现金额  
		int alreadyWithdraAmount=shopWithdrawMapper.getAlreadyWithdraAmount(shopId);//已提现金额  
		
		int  lockWithdraAmount=shopWithdrawMapper.lockWithdraAmount(shopId);//审核中或审核通过但是没有确认已转账的金额
		int  lockRefundAmount=orderRefundDocMapper.getRefunMoneyByShopId(shopId);//某个商家的正在退款或退款退货的总金额
		int lockAmount=lockRefundAmount+lockWithdraAmount;//锁定金额
		
		returnMap.put("canWithdraAmount", MoneyUtil.IntToDouble(canWithdraAmount));//可提现金额
		returnMap.put("alreadyWithdraAmount", MoneyUtil.IntToDouble(alreadyWithdraAmount));//已提现金额
		returnMap.put("lockAmount", MoneyUtil.IntToDouble(lockAmount));//锁定金额 
		return returnMap;
	}

	/**
	 * @Description: 微信端：可提现金额、已提现金额、锁定金额 
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	public Map<Object, Object> mCountWithdraMoney(int shopId) {
		Map<Object, Object> returnMap=new HashMap<Object, Object>();
		int canWithdraAmount=this.getCanWithdraAmount(shopId);//可提现金额  
		int alreadyWithdraAmount=shopWithdrawMapper.getAlreadyWithdraAmount(shopId);//已提现金额  
		
		int  lockWithdraAmount=shopWithdrawMapper.lockWithdraAmount(shopId);//审核中或审核通过但是没有确认已转账的金额
		int  lockRefundAmount=orderRefundDocMapper.getRefunMoneyByShopId(shopId);//某个商家的正在退款或退款退货的总金额
		int lockAmount=lockRefundAmount+lockWithdraAmount;//锁定金额
		
		returnMap.put("canWithdraAmount", MoneyUtil.IntToDouble(canWithdraAmount));//可提现金额
		returnMap.put("alreadyWithdraAmount", MoneyUtil.IntToDouble(alreadyWithdraAmount));//已提现金额
		returnMap.put("lockAmount", MoneyUtil.IntToDouble(lockAmount));//锁定金额 
		return returnMap;
	}
		
	/**
	 * @Description: 获取某个商家的可以提现金额
	 * @author xieyc
	 * @date 2018年8月17日 下午2:02:04 
	 */
	public int getCanWithdraAmount(int shopId){
		int canWithdraAmount=0;
		List<OrderShop>canWithdraList=orderShopMapper.getCanWithdraList(shopId);
		for (OrderShop orderShop : canWithdraList) {
			int  refundMoney=orderRefundDocMapper.getRefundMoneyByOrderShopId(orderShop.getId());//某个商家订单下的退款总金额
			canWithdraAmount+=orderShop.getOrderPrice()-refundMoney;
		}
		int  withdraAmounting=shopWithdrawMapper.countWithdraAmounting(shopId);//审核或审核通过的提现金额
		canWithdraAmount=canWithdraAmount-withdraAmounting;
		return canWithdraAmount;
	}

	
	/**
	 * @Description: PC端：判断本日是否已经提现
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	public boolean isWithdrawByToday(Integer shopId) {
		ShopWithdraw shopWithdraw=shopWithdrawMapper.getByShopIAndApplyTime(shopId);
		if(shopWithdraw!=null){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * @Description: 微信端：判断本日是否已经提现
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34
	 */
	public boolean mIsWithdrawByToday(int shopId) {
		ShopWithdraw shopWithdraw=shopWithdrawMapper.getByShopIAndApplyTime(shopId);
		if(shopWithdraw!=null){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * @Description: PC端：提现提交操作 
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	public int withdraw(ShopWithdraw entity) {
		int returnRow=0;
		ShopWithdraw shopWithdraw=shopWithdrawMapper.getByShopIAndApplyTime(entity.getShopId());	
		int canWithdraAmount=this.getCanWithdraAmount(entity.getShopId());
		logger.info("可提现金额："+canWithdraAmount);
		if(canWithdraAmount==0){
			return -4;//可提现金额为0
		}
		if(shopWithdraw==null){
			if(MoneyUtil.IntToDouble(canWithdraAmount)>=entity.getRelaWithdrawAmount()){//本次提现不能大于可提现金额
				if(entity.getType()==0){//对私提现（验证银行卡信息）
					boolean bankCardVerify = this.bankCardVerify(entity.getBankCardOwner(),entity.getLinkmanPhone(),
							entity.getBankCardNo(), entity.getIdcard());//验证银行卡
					if(!bankCardVerify){
						return -3;//银行卡信息不匹配
					}
				}
				entity.setApplyTime(new Date(JedisUtil.getInstance().time()));//提交提现申请时间
				entity.setWithdrawAmount(MoneyUtil.doubeToInt(entity.getRelaWithdrawAmount()));//提现金额(单位元)
				if(entity.getRelaWithdrawAmount()<=2){
					entity.setServiceCharge(1);//手续费
				}else{
					double serviceCharge=Math.floor(entity.getRelaWithdrawAmount()*0.5);
					entity.setServiceCharge(MoneyUtil.doubeToInt(serviceCharge/100));//手续费
				}
				entity.setArrivalAmount(entity.getWithdrawAmount()-entity.getServiceCharge());//实际到账金额
				entity.setAddTime(new Date(JedisUtil.getInstance().time()));//新增时间
				entity.setEditTime(new Date(JedisUtil.getInstance().time()));//编辑时间
				returnRow=shopWithdrawMapper.insertSelective(entity);
			}else{
				return -2;//可提现金额不足
			}
		}else{
			return -1;//每天只可以操作提现一次
		}
		return returnRow;
	}
			
	/**
	 * @Description: 微信端：提现提交操作 
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	public int mWithdraw(ShopWithdraw entity) {
		int returnRow=0;
		ShopWithdraw shopWithdraw=shopWithdrawMapper.getByShopIAndApplyTime(entity.getShopId());	
		int canWithdraAmount=this.getCanWithdraAmount(entity.getShopId());
		logger.info("可提现金额："+canWithdraAmount);
		if(canWithdraAmount==0){
			return -4;//可提现金额为0
		}
		if(shopWithdraw==null){
			if(MoneyUtil.IntToDouble(canWithdraAmount)>=entity.getRelaWithdrawAmount()){//本次提现不能大于可提现金额
				if(entity.getType()==0){//对私提现（验证银行卡信息）
					boolean bankCardVerify = this.bankCardVerify(entity.getBankCardOwner(),entity.getLinkmanPhone(),
							entity.getBankCardNo(), entity.getIdcard());//验证银行卡
					if(!bankCardVerify){
						return -3;//银行卡信息不匹配
					}
				}
				entity.setApplyTime(new Date(JedisUtil.getInstance().time()));//提交提现申请时间
				entity.setWithdrawAmount(MoneyUtil.doubeToInt(entity.getRelaWithdrawAmount()));//提现金额(单位元)
				if(entity.getRelaWithdrawAmount()<=2){
					entity.setServiceCharge(1);//手续费
				}else{
					double serviceCharge=Math.floor(entity.getRelaWithdrawAmount()*0.5);
					entity.setServiceCharge(MoneyUtil.doubeToInt(serviceCharge/100));//手续费
				}
				entity.setArrivalAmount(entity.getWithdrawAmount()-entity.getServiceCharge());//实际到账金额
				entity.setAddTime(new Date(JedisUtil.getInstance().time()));//新增时间
				entity.setEditTime(new Date(JedisUtil.getInstance().time()));//编辑时间
				returnRow=shopWithdrawMapper.insertSelective(entity);
			}else{
				return -2;//可提现金额不足
			}
		}else{
			return -1;//每天只可以操作提现一次
		}
		return returnRow;
	}
	/**
	 * @Description: 验证银行卡号
	 * @author xieyc
	 * @date 2018年7月31日 下午5:31:30 
	 */
	private  boolean bankCardVerify (String name,String phoneNo,String cardNo,String idNo) {
		boolean flag =false;
		BankCardVerifyPojo  entity=new BankCardVerifyPojo();
		entity.setName(name);//卡主
		entity.setPhoneNo(phoneNo);//预留号码
		entity.setCardNo(cardNo);//卡号
		entity.setIdNo(idNo);//身份证号码
		BankCardVerifyPojo  bankCardVerifyPojo=BankCardVerify.bankCardVerify(entity);
		if(bankCardVerifyPojo.getRespCode().equals("0000")){
			flag =true;//验证成功
		}else{
			flag =false;//验证失败
		}
		return flag;
	}
	/**
	 * @Description: PC端：某商家提现记录列表
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	public PageBean<ShopWithdrawVo> withdrawRecordListByShop(ShopWithdraw entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getPageSize(), true);
		List<ShopWithdrawVo> list = shopWithdrawMapper.getList(entity);
		PageBean<ShopWithdrawVo> page = new PageBean<>(list);
		return page;
	}

	/**
	 * @Description: 微信端：某商家提现记录列表
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	public PageBean<ShopWithdrawVo> mWithdrawRecordListByShop(ShopWithdraw entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getPageSize(), true);
		List<ShopWithdrawVo> list = shopWithdrawMapper.getList(entity);
		PageBean<ShopWithdrawVo> page = new PageBean<>(list);
		return page;
	}

	/**
	 * @Description: PC端：某商家入驻银行卡信息
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34
	 */
	public Map<Object, Object> lastWithdrawRecord(ShopWithdraw entity) {
		Map<Object, Object> returnMap = new HashMap<Object, Object>();
		MemberShopInfo memberShopInfo=memberShopInfoMapper.getByShopId(entity.getShopId());
		if(memberShopInfo!=null){//商家提现
			returnMap.put("applyType", memberShopInfo.getApplyType());//入驻时的类型
			if(memberShopInfo.getApplyType()==0){//申请类型（0 - 个人申请 ;1 - 企业申请 ）
				returnMap.put("idcard", memberShopInfo.getApplicantIdcard());//申请入驻时的身份证号码
				returnMap.put("bankCardNo", memberShopInfo.getBankCardNo());//申请入驻时的的银行卡账号
				returnMap.put("bankCardOwner", memberShopInfo.getApplicantName());//申请入驻时的的申请人
				returnMap.put("bankName", memberShopInfo.getBankName());//申请入驻时的的开户行
				returnMap.put("linkmanPhone", memberShopInfo.getBankReservationNumber());//申请入驻时的银行卡预留号码
			}else{
				returnMap.put("bankCardNo", memberShopInfo.getBankCardNo());//申请入驻时的的银行卡账号
				returnMap.put("bankCardOwner",memberShopInfo.getCompanyName());//申请入驻时的的公司名字
				returnMap.put("bankName", memberShopInfo.getBankName());//申请入驻时的的开户行
				returnMap.put("linkmanPhone", memberShopInfo.getBankReservationNumber());//申请入驻时的银行卡预留号码
			}
		}else{//平台提现
			returnMap.put("applyType", 1);//入驻时的类型
			returnMap.put("bankCardNo","");//申请入驻时的的银行卡账号
			returnMap.put("bankCardOwner","");//申请入驻时的的公司名字
			returnMap.put("bankName", "");//申请入驻时的的开户行
			returnMap.put("linkmanPhone","");//申请入驻时的银行卡预留号码
		}
		return returnMap;
	}
	/**
	 * @Description: 微信端：某商家入驻银行卡信息
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34
	 */
	public Map<Object, Object> mLastWithdrawRecord(ShopWithdraw entity) {
		Map<Object, Object> returnMap = new HashMap<Object, Object>();
		MemberShopInfo memberShopInfo=memberShopInfoMapper.getByShopId(entity.getShopId());
		if(memberShopInfo!=null){//商家提现
			returnMap.put("applyType", memberShopInfo.getApplyType());//入驻时的类型
			if(memberShopInfo.getApplyType()==0){//申请类型（0 - 个人申请 ;1 - 企业申请 ）
				returnMap.put("idcard", memberShopInfo.getApplicantIdcard());//申请入驻时的身份证号码
				returnMap.put("bankCardNo", memberShopInfo.getBankCardNo());//申请入驻时的的银行卡账号
				returnMap.put("bankCardOwner", memberShopInfo.getApplicantName());//申请入驻时的的申请人
				returnMap.put("bankName", memberShopInfo.getBankName());//申请入驻时的的开户行
				returnMap.put("linkmanPhone", memberShopInfo.getBankReservationNumber());//申请入驻时的银行卡预留号码
			}else{
				returnMap.put("bankCardNo", memberShopInfo.getBankCardNo());//申请入驻时的的银行卡账号
				returnMap.put("bankCardOwner",memberShopInfo.getCompanyName());//申请入驻时的的公司名字
				returnMap.put("bankName", memberShopInfo.getBankName());//申请入驻时的的开户行
				returnMap.put("linkmanPhone", memberShopInfo.getBankReservationNumber());//申请入驻时的银行卡预留号码
			}
		}else{//平台提现
			returnMap.put("applyType", 1);//入驻时的类型
			returnMap.put("bankCardNo","");//申请入驻时的的银行卡账号
			returnMap.put("bankCardOwner","");//申请入驻时的的公司名字
			returnMap.put("bankName", "");//申请入驻时的的开户行
			returnMap.put("linkmanPhone","");//申请入驻时的银行卡预留号码
		}
		return returnMap;
	}


	/**
	 * @Description: PC端：某商家最新一次提现记录展示
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	public Map<Object, Object> lastWithdrawInfo(ShopWithdraw entity) {
		Map<Object, Object> returnMap = null;
		entity.setType(0);
		List<ShopWithdraw> list = shopWithdrawMapper.getListByTypeAndShopId(entity);
		if (list.size() > 0) {
			returnMap = new HashMap<Object, Object>();
			ShopWithdraw shopWithdraw = list.get(0);
			if (shopWithdraw.getType() == 0) {
				returnMap.put("idcard", shopWithdraw.getIdcard());
			}
			returnMap.put("bankCardNo", shopWithdraw.getBankCardNo());
			returnMap.put("bankCardOwner", shopWithdraw.getBankCardOwner());
			returnMap.put("bankName", shopWithdraw.getBankName());
			returnMap.put("linkmanPhone", shopWithdraw.getLinkmanPhone());
		}
		return returnMap;
	}

	/**
	 * @Description: 微信端：某商家最新一次提现记录展示
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	public Map<Object, Object> mLastWithdrawInfo(ShopWithdraw entity) {
		Map<Object, Object> returnMap = null;
		entity.setType(0);
		List<ShopWithdraw> list = shopWithdrawMapper.getListByTypeAndShopId(entity);
		if (list.size() > 0) {
			returnMap = new HashMap<Object, Object>();
			ShopWithdraw shopWithdraw = list.get(0);
			if (shopWithdraw.getType() == 0) {
				returnMap.put("idcard", shopWithdraw.getIdcard());
			}
			returnMap.put("bankCardNo", shopWithdraw.getBankCardNo());
			returnMap.put("bankCardOwner", shopWithdraw.getBankCardOwner());
			returnMap.put("bankName", shopWithdraw.getBankName());
			returnMap.put("linkmanPhone", shopWithdraw.getLinkmanPhone());
		}
		return returnMap;
	}



	
}
