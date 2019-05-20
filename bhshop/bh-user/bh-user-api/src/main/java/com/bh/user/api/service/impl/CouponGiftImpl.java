package com.bh.user.api.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.config.Contants;
import com.bh.goods.mapper.CouponAndGiftMapper;
import com.bh.goods.mapper.CouponGiftMapper;
import com.bh.goods.mapper.CouponLogMapper;
import com.bh.goods.mapper.CouponMapper;
import com.bh.goods.pojo.Coupon;
import com.bh.goods.pojo.CouponAndGift;
import com.bh.goods.pojo.CouponGift;
import com.bh.goods.pojo.CouponLog;
import com.bh.goods.pojo.CouponPojo;
import com.bh.user.api.service.CouponGiftService;
import com.bh.user.mapper.GiftLogMapper;
import com.bh.user.mapper.GiftMemberMapper;
import com.bh.user.mapper.UserLoginLogMapper;
import com.bh.user.pojo.GiftLog;
import com.bh.user.pojo.GiftMember;
import com.bh.user.pojo.UserLoginLog;

@Service
public class CouponGiftImpl implements CouponGiftService{
    @Autowired
    private   CouponMapper couponMapper;
    @Autowired
    private   CouponLogMapper couponLogMapper;
    @Autowired
    private  UserLoginLogMapper userLoginLogMapper;
    @Autowired
    private  GiftLogMapper giftLogMapper;
    @Autowired
    private  CouponGiftMapper couponGiftMapper ;
    @Autowired
    private CouponAndGiftMapper  couponAndGiftMapper;
    @Autowired
    private  GiftMemberMapper giftMemberMapper;

	/**
	 * @Description: 用户第一次登入的时候大礼包展示接口（作废）
	 * @author xieyc
	 * @date 2018年6月5日 上午11:09:51
	 */
	public List<CouponPojo> showCouponGift(Integer mId) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		UserLoginLog userLoginLog = userLoginLogMapper.getLogByMid(mId);

		if (userLoginLog!=null && userLoginLog.getLoginNum() == 1) {// 第一次登入,显示赠送的大礼包
			List<CouponPojo> returnList = new ArrayList<CouponPojo>();

			List<CouponLog> myCouponList = couponLogMapper.getLogByMid(mId);// 我的优惠卷列表统计
			for (CouponLog couponLog : myCouponList) {
				Coupon coupon = couponMapper.selectByPrimaryKey(couponLog.getCouponId());// 优惠卷
				CouponPojo couponPojo = new CouponPojo();
				couponPojo.setNum(couponLog.getNum());// 优惠卷数量
				couponPojo.setAmount((double) coupon.getAmount() / 100);// 优惠金额
				couponPojo.setName(coupon.getTitle());// 优惠卷名字
				couponPojo.setType(coupon.getCouponType());//优惠券类型，1普通券，2免邮券，3红包券
				long difference = (couponLog.getExpireTime().getTime()-new Date().getTime())/86400000;
				String effectiveTime=null;
				if(Math.abs(difference)>10000){
					effectiveTime="永久有效";
				}else{
					StringBuffer sb = new StringBuffer();
					sb.append(sDateFormat.format(couponLog.getCreateTime()));
					sb.append(" 至 ");
					sb.append(sDateFormat.format(couponLog.getExpireTime()));
					
					effectiveTime=sb.toString();
				}
				couponPojo.setEffectiveTime(effectiveTime);// 有效时间段
				returnList.add(couponPojo);
			}
			return returnList;

		} else {
			return null;// 不是第一次登入,不显示赠送的大礼包，返回null
		}

	}
	/**
	 * @Description: 更新优惠卷(点击领取)
	 * @author xieyc
	 * @date 2018年6月5日 上午11:09:51
	 */
	public int changeLogStatus(String logId) {
		Integer row=0;
		CouponLog updateCouponLog=couponLogMapper.selectByPrimaryKey(Integer.valueOf(logId));
		Coupon coupon =couponMapper.selectByPrimaryKey(updateCouponLog.getCouponId());
		
		if(coupon!=null){//没有被禁用,优惠卷剩余量>0
			Date expireTime=null;	
			Calendar calendar = Calendar.getInstance();
	        calendar.setTime(new Date());
			if(coupon.getPeriodDay()!=-1){//-1表示永久有效
		        calendar.add(Calendar.DATE, coupon.getPeriodDay());
			}else{
				calendar.add(Calendar.DATE,36000);
			}
			expireTime=calendar.getTime();//自领取后多少天过期的时间
			updateCouponLog.setCreateTime(new Date());
			updateCouponLog.setStatus(0);
			updateCouponLog.setExpireTime(expireTime);
			row=couponLogMapper.updateByPrimaryKeySelective(updateCouponLog);
			
			coupon.setIsGet(coupon.getIsGet()+1);//已经使用量
			couponMapper.updateByPrimaryKeySelective(coupon);//更新优惠卷领取数
		}
		
		return row;
	}
	/**
	 * @Description: 第一次登入标识
	 * @author xieyc
	 * @date 2018年6月5日 上午11:09:51
	 */
	public boolean isFirstLogin(Integer mId) {
		boolean flag=false;
		UserLoginLog userLoginLog = userLoginLogMapper.getLogByMid(mId);
		if(userLoginLog!=null && userLoginLog.getLoginNum() == 1){//第一次登入
			CouponLog findCouponLog=new CouponLog();//查询条件
			findCouponLog.setmId(mId);
			findCouponLog.setStatus(2);
			List <CouponLog> couponLogList=couponLogMapper.listPage(findCouponLog);//判断该用户未领取数量（size=0的时候也不跳出）
			if(couponLogList.size()>0){
				flag=true;
			}
		}
		return flag;
	}
	/**
	 * @Description: 是否发送过大礼包
	 * @author xieyc
	 * @date 2018年6月8日 下午5:58:30 
	 */
	public boolean isGetGift(Integer mId) {
		GiftLog findGiftLog=new GiftLog();//查询条件
		findGiftLog.setmId(mId);//用户id
		findGiftLog.setIsget(1);//领取过
		List <GiftLog> giftLogList =giftLogMapper.getGiftLog(findGiftLog);
		if(giftLogList.size()==0){
			boolean isGet=this.giveCouponGift(mId);//发放礼包
			if(isGet){
				GiftLog saveGiftLog=new GiftLog();//保存条件
				saveGiftLog.setmId(mId);//用户id
				saveGiftLog.setIsget(1);//现在发放
				saveGiftLog.setGetTime(new Date());//发放时间
				giftLogMapper.insertSelective(saveGiftLog);
			}
			return isGet;
		}else{//发送过大礼包
			return false;
		}	
	}
	/**
	 * @Description: 用户注册成功赠送大礼包逻辑
	 * @author xieyc
	 * @date 2018年6月5日 上午9:48:49
	 */
	public boolean giveCouponGift(int mId) {
		String giftName = Contants.NEWUSERCOUPONGIFTNAME;
		CouponGift couponGift = couponGiftMapper.getGiftByName(giftName);// 根据礼包名字获取大礼包
		
		if(couponGift.getIsWhiteList()==0){//白名单开启的时候才要去判断该用户是否在白名单内（没有开启的时候默认所有用户享受）
			List<GiftMember> giftMemberList=giftMemberMapper.getByGiftIdAndMid(couponGift.getId(),mId);
			if(giftMemberList.size()==0){
				return false;//该用户不拥有该礼包的享受权
			}
		}
		if (couponGift != null && couponGift.getGiftStatus() == 0 ) {
			CouponAndGift findCouponAndGift=new CouponAndGift();
			findCouponAndGift.setcGId(couponGift.getId());
			List <CouponAndGift> couponAndGiftList =couponAndGiftMapper.getByCGidAndNum(findCouponAndGift);
			if(couponAndGiftList.size()==0){
				return false;//大礼包里面没有一张能满足要求的优惠卷
			}
			for (CouponAndGift couponAndGift : couponAndGiftList) {
				Coupon coupon=couponMapper.selectByPrimaryKey(couponAndGift.getcId());
				if(coupon!=null &&coupon.getStatus()==1 && coupon.getStock()>0 ){//没有被禁用,优惠卷剩余量>0
					for(int i=0;i<couponAndGift.getcNum();i++){
						CouponLog saveCouponLog = new CouponLog();
						saveCouponLog.setmId(mId);// 当前领取的客户的id
						saveCouponLog.setCouponId(coupon.getId());// CouponId
						int shopId=coupon.getShopId();
						if(shopId==0){
							shopId=1;
						}
						saveCouponLog.setStatus(0);//未使用
						saveCouponLog.setShopId(shopId); // 商家id
						Date expireTime=null;	
						Calendar calendar = Calendar.getInstance();
				        calendar.setTime(new Date());
						if(coupon.getPeriodDay()!=-1){//-1表示永久有效
					        calendar.add(Calendar.DATE, coupon.getPeriodDay());
						}else{
							calendar.add(Calendar.DATE,36000);
						}
						expireTime=calendar.getTime();//自领取后多少天过期的时间
						saveCouponLog.setCreateTime(new Date());//领取时间
						saveCouponLog.setExpireTime(expireTime);//过期时间
						
						couponLogMapper.insertSelective(saveCouponLog);//插入礼包记录
						
						coupon.setStock(coupon.getStock()-1);//剩余量
						coupon.setIsGet(coupon.getIsGet()+1);//已经使用量
						couponMapper.updateByPrimaryKeySelective(coupon);//更新优惠卷剩余数
					}
				}
			}
			return true;
		}else{
			return false;//大礼包没开启或者没有大礼
		}
	}
}
