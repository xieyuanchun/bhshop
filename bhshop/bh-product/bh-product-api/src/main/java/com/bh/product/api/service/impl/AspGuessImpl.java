package com.bh.product.api.service.impl;

import com.bh.goods.mapper.ActivityTimeMapper;
import com.bh.goods.mapper.AspInviteMapper;
import com.bh.goods.mapper.AspUserGuessMapper;
import com.bh.goods.pojo.ActivityTime;
import com.bh.goods.pojo.AspInvite;
import com.bh.goods.pojo.AspUserGuess;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderRefundDocMapper;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderShop;
import com.bh.product.api.service.AspGuessService;
import com.bh.user.pojo.Member;
import com.bh.util.enterprise.BankCardVerify;
import com.bh.util.enterprise.pojo.BankCardVerifyPojo;
import com.bh.utils.JedisUtil;
import com.bh.utils.MoneyUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class AspGuessImpl implements AspGuessService{
	private static final Logger logger = LoggerFactory.getLogger(AspGuessImpl.class);
	@Autowired
	private AspInviteMapper aspInviteMapper;
	@Autowired
	private AspUserGuessMapper aspUserGuessMapper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private ActivityTimeMapper activityTimeMapper;
	@Autowired
	private OrderRefundDocMapper  orderRefundDocMapper;
	@Autowired
	private OrderShopMapper orderShopMapper;
	
	
	/**
	 * 
	 * @Description: 更新金额与中奖状态
	 * @author xieyc
	 * @date 2018年8月24日 上午10:28:40
	 */
	public int update(Integer guessOne, Integer guessTwo) {
		int returnRow=0;
		List<AspUserGuess> listByGoldMedal=aspUserGuessMapper.getListByGoldMedal(guessOne,guessTwo);//查询金牌数猜中的人
		logger.info("只猜中金牌的人数:"+listByGoldMedal.size());
		for (AspUserGuess aspUserGuess : listByGoldMedal) {
			aspUserGuess.setRetPrice(this.selectByCountMoney(aspUserGuess.getmId()));//购物金额
			aspUserGuess.setBackPrice(MoneyUtil.doubeToInt(aspUserGuess.getRetPrice()*0.003));//返还30%
			aspUserGuess.setGuessStatus(1);//猜中金牌
			aspUserGuess.setEditTime(new Date(JedisUtil.getInstance().time()));//更新时间
			returnRow=aspUserGuessMapper.updateByPrimaryKeySelective(aspUserGuess);
		}
		List<AspUserGuess> listByMedal=aspUserGuessMapper.getUpdateListByMedal(guessOne,guessTwo);//查询奖牌数猜中的人
		logger.info("只猜中奖牌的人数:"+listByMedal.size());
		for (AspUserGuess aspUserGuess : listByMedal) {
			aspUserGuess.setRetPrice(this.selectByCountMoney(aspUserGuess.getmId()));//购物金额
			aspUserGuess.setBackPrice(MoneyUtil.doubeToInt(aspUserGuess.getRetPrice()*0.003));//返还30%
			aspUserGuess.setGuessStatus(2);//猜中奖牌
			aspUserGuess.setEditTime(new Date(JedisUtil.getInstance().time()));//更新时间
			returnRow=aspUserGuessMapper.updateByPrimaryKeySelective(aspUserGuess);
		}
		List<AspUserGuess> listByTwo=aspUserGuessMapper.getUpdateListByTwo(guessOne,guessTwo);//查询金牌和奖牌都猜中的人
		logger.info("都猜中的人数:"+listByTwo.size());
		for (AspUserGuess aspUserGuess : listByTwo) {
			aspUserGuess.setRetPrice(this.selectByCountMoney(aspUserGuess.getmId()));//购物金额
			aspUserGuess.setBackPrice(aspUserGuess.getRetPrice());//全部//购物金额
			aspUserGuess.setGuessStatus(3);//都猜中
			aspUserGuess.setEditTime(new Date(JedisUtil.getInstance().time()));//更新时间
			returnRow=aspUserGuessMapper.updateByPrimaryKeySelective(aspUserGuess);
		}
		return returnRow;
	}
	
	/**
	 * @Description: 亚运会竞猜提交
	 * @author xieyc
	 * @date 2018年8月24日 上午10:28:40
	 */
	public int predictSubmit(AspUserGuess entity) {
		int returnRow=0;
		if(entity.getMedalNum()>165412864 ||entity.getMedalNum()<=0||entity.getGoldMedalNum()>165412864||entity.getGoldMedalNum()<=0){
			return -1;//输入数字不符合实际！
		}	
		AspUserGuess aspUserGuess=aspUserGuessMapper.selectByPrimaryKey(entity.getmId());
		if(aspUserGuess!=null){
			return -2;//你已竞猜过,请勿重复提交!
		}else{
			AspUserGuess saveAspUserGuess=new  AspUserGuess();
			saveAspUserGuess.setGuessOne(entity.getGoldMedalNum().intValue());//金牌数
			saveAspUserGuess.setGuessTwo(entity.getMedalNum().intValue());//奖牌数
			saveAspUserGuess.setmId(entity.getmId());
			saveAspUserGuess.setAddTime(new Date(JedisUtil.getInstance().time()));//添加时间
			saveAspUserGuess.setEditTime(new Date(JedisUtil.getInstance().time()));//编辑时间
			returnRow=aspUserGuessMapper.insertSelective(saveAspUserGuess);
		}
		return returnRow;
	}	
	
	/**
	 * 
	 * @Description: 进入亚运会页面时插入数据
	 * @author xieyc
	 * @date 2018年8月24日 上午10:28:40
	 */
	public int loadInsert(Integer mId) {
		int returnRow=0;
		AspUserGuess aspUserGuess=aspUserGuessMapper.selectByPrimaryKey(mId);
		if(aspUserGuess==null){//第一次进入此页面
			AspUserGuess saveAspUserGuess=new  AspUserGuess();
			saveAspUserGuess.setmId(mId);
			saveAspUserGuess.setAddTime(new Date(JedisUtil.getInstance().time()));//添加时间
			saveAspUserGuess.setEditTime(new Date(JedisUtil.getInstance().time()));//编辑时间
			aspUserGuessMapper.insertSelective(saveAspUserGuess);
		}
		return returnRow;
	}
	/**
	 * 
	 * @Description: 亚运会竞猜提交
	 * @author xieyc
	 * @date 2018年8月24日 上午10:28:40 
	 * 
	 */
	public int predictSubmit1(AspUserGuess entity) {
		int returnRow=0;
		AspUserGuess aspUserGuess=aspUserGuessMapper.selectByPrimaryKey(entity.getmId());
		if(aspUserGuess!=null){
			if(entity.getGuessNum()>165412864 ||entity.getGuessNum()<=0){
				return -3;//输入数字不符合实际
			}		
			if(aspUserGuess.getIsFull()==1){//2次机会
				if(aspUserGuess.getGuessOne()==null){
					aspUserGuess.setGuessOne(entity.getGuessNum().intValue());//第一次预测金牌数
				}else if(aspUserGuess.getGuessTwo()==null){
					aspUserGuess.setGuessTwo(entity.getGuessNum().intValue());//第二次预测金牌数
				}else{
					return -1;//2次竞猜机会已用完
				}
			}else{//一次机会
				if(aspUserGuess.getGuessOne()==null){
					aspUserGuess.setGuessOne(entity.getGuessNum().intValue());//第一次预测金牌数
				}else{
					return -2;//1次竞猜机会已用完
				}
			}
			aspUserGuess.setEditTime(new Date(JedisUtil.getInstance().time()));//更新时间
			aspUserGuessMapper.updateByPrimaryKey(aspUserGuess);
			if(entity.getReqUserId()!=null){
				this.insertAspInviteRecord(entity.getReqUserId(),entity.getmId());
			}
		}			
		return returnRow;
	}

	/**
	 * 
	 * @Description: 插入邀请记录
	 * @author xieyc
	 * @date 2018年8月24日 下午12:34:27 
	 */
	public void insertAspInviteRecord(int reqUserId,int invitedUserId){
		if(reqUserId!=0){//不为0的时候表示从邀请链接中进来的
			AspInvite aspInvite=aspInviteMapper.getByInvitedUserId(invitedUserId);//判断自己是否已经被人邀请成功过
			if(aspInvite==null){//还没有被人邀请成功过
				AspInvite saveAspInvite=new AspInvite();
				saveAspInvite.setAddTime(new Date(JedisUtil.getInstance().time()));//添加时间
				saveAspInvite.setEditTime(new Date(JedisUtil.getInstance().time()));//编辑时间
				saveAspInvite.setInvitedUserId(invitedUserId);//被邀请人id
				saveAspInvite.setReqUserId(reqUserId);//邀请人id
				aspInviteMapper.insertSelective(saveAspInvite);
				
				AspUserGuess aspUserGuess=aspUserGuessMapper.selectByPrimaryKey(reqUserId);//获取邀请人信息
				if(aspUserGuess!=null &&aspUserGuess.getIsFull()==0){//没有成功邀请4个人或以上人数时
					int userNum=aspInviteMapper.getSuccessInvitedNum(reqUserId);//获取邀请人已经邀请的人数
					if(userNum>=4){
						aspUserGuess.setIsFull(1);
						aspUserGuess.setEditTime(new Date(JedisUtil.getInstance().time()));//更新时间
						aspUserGuessMapper.updateByPrimaryKey(aspUserGuess);
					}
				}
			}
		}
	}
		
	/**
	 * @Description: 亚运会猜中提现接口
	 * @author xieyc
	 * @date 2018年8月24日 上午10:28:40
	 */
	public int withdraw(AspUserGuess entity) {
		int returnRow=0;
		AspUserGuess aspUserGuess=aspUserGuessMapper.selectByPrimaryKey(entity.getmId());
		if(aspUserGuess!=null){
			if(aspUserGuess.getGuessStatus()==0){
				return -1;//未中奖
			}
			boolean bankCardVerify=BankCardVerify.bankCardVerify(entity.getBankCardOwner(),entity.getPhone(),entity.getBankCardNo(),entity.getIdcard());
			if(!bankCardVerify){
				return -2;//银行卡信息不匹配
			}
			if(aspUserGuess.getIsApply()==1){
				return -3;//已经申请过转账
			}
			if(aspUserGuess.getBackPrice()<=0){
				return -4;//退还金额不能小于0
			}
			entity.setIsApply(1);
			entity.setApplyTime(new Date(JedisUtil.getInstance().time()));//提现时间
			entity.setEditTime(new Date(JedisUtil.getInstance().time()));//编辑时间
			returnRow=aspUserGuessMapper.updateByPrimaryKeySelective(entity);
		}
		return returnRow;
	}
	
	
	
	/**
	 * @Description: 获取用户信息和活动期间消费的总金额
	 * @author fanjh
	 * @date 2018年8月24日 上午10:30:42
	 */
	public Map<String,Object> selectByMember(Member member){
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("jsonbject",getByMedalNum()); 
		SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
		String time=sf.format(new Date(JedisUtil.getInstance().time()));
		ActivityTime activityTime=activityTimeMapper.selectByPrimaryKey(1);
		String startsTime=sf.format(activityTime.getStartTime());
		String endTime=sf.format(activityTime.getEndTime());
		String releaseTime =sf.format(activityTime.getReleaseTime());
		int result=time.compareTo(startsTime); //当前时间大于活动开始时间
		int result1=time.compareTo(endTime);//当前时间小于活动结束时间
		int result2=time.compareTo(releaseTime);
		int isEnd=0; //0是竞猜活动中 1竞猜活动结束 2是亚运会结束开奖
		if(result>0&&result1<0) {
			isEnd=0;
		}else if(result1>0&&result2<0) {
			isEnd=1;
		}else  if(result2>0){
			isEnd=2;
		}
		map.put("isEnd", isEnd); 
		if(member!=null) {
			AspUserGuess aspUserGuess=aspUserGuessMapper.selectByPrimaryKey(member.getId());
			int countMoney=this.selectByCountMoney(member.getId()); //计算活动期间消费的金额
			double realCountMoney = (double) countMoney / 100;
			if(aspUserGuess!=null) {
				map.put("guess_one", aspUserGuess.getGuessOne()); //竞猜金牌数
				map.put("guess_two", aspUserGuess.getGuessTwo()); //竞猜奖牌数
				map.put("guess_status", aspUserGuess.getGuessStatus()); //竞猜结果
				map.put("countMoney", realCountMoney); //活动期间消费的金额
				map.put("backPrice", aspUserGuess.getBackPrice()); //实际退还金额
				map.put("isTransfer", aspUserGuess.getIsTransfer()); //是否转账 0未转账 1已转账
				map.put("isApply", aspUserGuess.getIsApply()); //0是未申请  1是已申请
				map.put("isLogin", 1); //是否登录 0是未登录 1是登录
				map.put("bankCardNo", aspUserGuess.getBankCardNo()); 
				map.put("bankName", aspUserGuess.getBankName()); 
				map.put("bankCardOwner", aspUserGuess.getBankCardOwner());
				map.put("idcard", aspUserGuess.getIdcard()); 
				map.put("phone", aspUserGuess.getPhone()); 
			}else {
				map.put("guess_one", ""); //竞猜金牌数
				map.put("guess_two", ""); //竞猜奖牌数
				map.put("guess_status", 5); //竞猜结果
				map.put("countMoney", realCountMoney); //活动期间消费的金额
				map.put("backPrice", 0); //实际退还金额
				map.put("isTransfer", 2); //是否转账 0未转账 1已转账 2未参与活动
				map.put("isApply", 0); //0是未申请  1是已申请
				map.put("isLogin", 1); //是否登录 0是未登录 1是登录
			}
		}else {
			map.put("guess_one", ""); //竞猜金牌数
			map.put("guess_two", ""); //竞猜奖牌数
			map.put("guess_status", 5); //竞猜结果 未参与竞猜
			map.put("countMoney", 0); //竞猜结果
			map.put("backPrice", 0); //实际退还金额
			map.put("isTransfer", 2); //是否转账 0未转账 1已转账 2未参与活动
			map.put("isApply", 0); //0是未申请  1是已申请
			map.put("isLogin", 0);//是否登录 0是未登录 1是登录
		}
		
		return map;
	}
	
	/**
	 * @Description: 获取活动期间消费的总金额
	 * @author fanjh
	 * @date 2018年8月24日 上午10:30:42
	 */
	public int selectByCountMoney(int mId){
		int countMoney=0;
	    try {
	    	ActivityTime activityTime=activityTimeMapper.selectByPrimaryKey(1);//亚运会活动时间
	    	Order findOrder=new Order();//查询条件
	    	findOrder.setStartTime(activityTime.getStartTime());
	    	findOrder.setEndTime(activityTime.getEndTime());
	    	findOrder.setmId(mId);
	    	List<Order> orderList=orderMapper.getByTimeAndMid(findOrder);
	    	for (Order order : orderList) {
	    		List<OrderShop>orderShopList=orderShopMapper.getByOrderId(order.getId());
	    		for (OrderShop orderShop : orderShopList) {
	    			int  deliveryPrice=0;
	    			//查询这个商家订单下所有发起了退款或退款退货的订单的退款金额总和
		    		int  refundMoney=orderRefundDocMapper.countMoneyByOrderShopId(orderShop.getId());
		    		if(refundMoney==orderShop.getOrderPrice()-orderShop.getgDeliveryPrice()){
		    			deliveryPrice=orderShop.getgDeliveryPrice();//这个商家订单全部订单已经退款或已经退款退货的时候减去邮费
		    		}
		    		countMoney+=orderShop.getOrderPrice()-refundMoney-deliveryPrice;
				}
			}	    			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return countMoney;
	}
	
	/**
	 * @Description:获取中国金牌数
	 * @author xieyc
	 * @date 2018年8月24日 上午10:30:42
	 */
	public JSONObject getByMedalNum(){
		HttpResponse resp=null;
		try {
			HttpPost httpPost = new HttpPost("http://yayun.cctv.com/2018/data/medal/count.json");
			CloseableHttpClient client = HttpClients.createDefault();
			JSONObject jsonParam = new JSONObject();
			StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
			entity.setContentEncoding("UTF-8");
			entity.setContentType("application/json");
			httpPost.setEntity(entity);
			resp = client.execute(httpPost);
			Integer statusCode=null;
			if (resp != null) {
				statusCode = resp.getStatusLine().getStatusCode();
				if (statusCode != null && statusCode == 200) {
					JSONObject jsonbject = JSONObject.fromObject(EntityUtils.toString(resp.getEntity()));
					JSONObject jsonbjectDate = JSONObject.fromObject(jsonbject.get("msg"));
					JSONArray jsonArray =JSONArray.fromObject(jsonbjectDate.get("item"));
					for (Object object : jsonArray) {
						JSONObject ob =JSONObject.fromObject(object);
						if(ob.getString("countryID").equals("CHN")){
							logger.info(ob.toString());
							return ob;
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("获取中国金牌数");
			e.printStackTrace();
		}
		return  null;
	}
	
	public static void main(String[] arg) {
		AspGuessImpl aspGuessImpl=new AspGuessImpl();
		aspGuessImpl.getByMedalNum();
		    
	}

	


}
