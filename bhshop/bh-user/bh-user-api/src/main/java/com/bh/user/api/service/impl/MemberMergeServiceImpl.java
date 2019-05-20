package com.bh.user.api.service.impl;

import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.jredis.JredisUtils;
import org.springframework.stereotype.Service;
import com.bh.goods.mapper.CouponLogMapper;
import com.bh.order.mapper.OrderMapper;
import com.bh.user.api.service.MemberMergeService;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.mapper.MergeUserLogMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MergeUserLog;
import com.bh.utils.EmojiFilter;
import com.bh.utils.JedisUtil;
import com.bh.utils.pay.WXPayUtil;
import com.fasterxml.jackson.core.JsonParser;
import com.google.gson.JsonObject;

@Service
public class MemberMergeServiceImpl implements MemberMergeService {
	private static final Logger logger = LoggerFactory.getLogger(MemberMergeServiceImpl.class);
	@Autowired
	public MemberMapper memberMapper;
	@Autowired
	public MergeUserLogMapper mergeUserLogMapper;
	@Autowired
	public CouponLogMapper couponLogMapper;
	@Autowired
	public OrderMapper orderMapper;
	 
	/**
	 * 
	 * @Description: 判断当前微信的绑定情况
	 * @author xieyc
	 * @date 2018年8月30日 下午3:29:29
	 */
	public Map<Object, Object> wxBindingInfo(String openid) {
		Map<Object, Object> map=new HashMap<Object, Object>();
		Member findMember = new Member();
		findMember.setOpenid(openid);
		List<Member> list = memberMapper.selectByOpenId(findMember);
		if(list.size()>0){
			if(StringUtils.isNotBlank(list.get(0).getPhone())){
				map.put("code",0);
				map.put("msg", "该微信已经绑定了手机");
			}else{
				map.put("code", 1);
				map.put("msg","该微信已注册但未绑定手机");
			}
		}else{
			map.put("code", 2);
			map.put("msg","该微信未注册");
		}
		return map;
	}
	
	/**
	 * @Description: 判断手机号的绑定情况
	 * @author xieyc
	 * @date 2018年8月30日 下午3:29:29
	 */
	public Map<Object, Object> phoneBindingInfo(String phone) {
		Map<Object, Object> map=new HashMap<Object, Object>();		
		List<Member> list = memberMapper.getByPhone(phone);
		if(list.size()>0){
			if(!list.get(0).getOpenid().equals("0")){
				map.put("code",0);
				map.put("msg", "该手机已经被其他微信绑定了");
			}else{
				map.put("code", 1);
				map.put("msg","该手机已被注册但未绑定微信");
			}
		}else{
			map.put("code", 2);
			map.put("msg","该手机号未注册");
		}
		return map;
	}
	
	/**
	 * 
	 * @Description: 判断登入用户的绑定情况
	 * @author xieyc
	 * @date 2018年8月30日 下午3:29:29 
	 * 
	 */
	public Map<Object, Object> bindingInfo(Integer mId) {
		Map<Object, Object> map=new HashMap<Object, Object>();
		Member member=memberMapper.selectByPrimaryKey(mId);	
		if(!member.getOpenid().equals("0") && StringUtils.isBlank(member.getPhone())){//微信用户未绑定手机号
			map.put("sign",0);
			map.put("msg","微信号用户未绑定手机号");
			map.put("phone",null);
		}else if(member.getOpenid().equals("0") && member.getPhone()!=null){//手机用户未绑定微信号
			map.put("sign",1);
			map.put("msg","手机号用户未绑定微信号");
			map.put("phone", member.getPhone());//该用户的手机号
		}else{
			map.put("sign",2);
			map.put("msg","微信号与手机号已经互绑");
			map.put("phone", member.getPhone());//该用户的手机号
		}
		return  map;
	}
	
	/**
	 * 
	 * @Description: 更新手机号前判断该微信是否可以换手机
	 * @author xieyc
	 * @date 2018年8月30日 下午3:30:39 
	 *
	 */
	public int changePhoneBefore(Integer mId) {
		int returnRow=0;
		Member member=memberMapper.selectByPrimaryKey(mId);
		if(member.getOpenid().equals("0")){
			return -1;//请用微信号登入（微信用户才能更换手机号码）
		}
		if(member.getChangePhoneTime()!=null){
			 Calendar theCa = Calendar.getInstance();
			 theCa.setTime(member.getChangePhoneTime());
			 theCa.add(theCa.DATE, 30);
			 Date date = theCa.getTime();
		     if(new Date().getTime()<=date.getTime()){
		    	 return -2;//为了您的账号安全,30天内不可更改绑定手机号
		     }
		}else{
			return -3;//未知异常，请联系管理员
		}
		return returnRow;
	}
	/**
	 * 
	 * @Description: 微信号更新手机号码
	 * @author xieyc
	 * @date 2018年8月30日 下午3:30:39 
	 *
	 */
	public int changePhone(String newPhone, Integer mId) {
		int returnRow=0;
		Member member=memberMapper.selectByPrimaryKey(mId);
		if(member.getOpenid().equals("0")){
			return -1;//请用微信号登入（微信用户才能更换手机号码）
		}
		if(member.getChangePhoneTime()!=null){
			 Calendar theCa = Calendar.getInstance();
			 theCa.setTime(member.getChangePhoneTime());
			 theCa.add(theCa.DATE, 30);
			 Date date = theCa.getTime();
		     if(new Date().getTime()<=date.getTime()){
		    	 return -2;//为了您的账号安全,30天内不可更改绑定手机号
		     }
		}
		List<Member> memberList = memberMapper.getByPhone(newPhone);
		if(memberList.size()>0){
			return -3;//该手机已经被注册
		}
		
		Member saveMember = new Member();
		saveMember.setChangePhoneTime(new Date(JedisUtil.getInstance().time()));//更换手机号手机
		saveMember.setId(mId);
		saveMember.setPhone(newPhone);
		returnRow=memberMapper.updateByPrimaryKeySelective(saveMember);
		return returnRow;
	}

	/**
	 * @Description: 手机绑定微信接口
	 * @author xieyc
	 * @throws Exception 
	 * @date 2018年8月30日 下午3:29:29
	 */
	public int svaePhoneBindingWx(String phone, Map<String, Object> mapWx,String openid) throws Exception {
					
		String lang = "zh_CN";
		Map<String, Object> mapUser = WXPayUtil.getUser(String.valueOf(mapWx.get("access_token")),
				openid, lang);//用access_token, openid,lang获取当前登录的用户信息
		
		if (!String.valueOf(mapUser.get("errmsg")).equals(" invalid openid ")) {
			Member member = new Member();
			member.setOpenid(openid);
			//member.setUnionid(String.valueOf(mapUser.get("unionid")));	
			logger.info("phoneBindingWx openid"+String.valueOf(mapUser.get("openid")));
			logger.info("phoneBindingWx unionid"+String.valueOf(mapUser.get("unionid")));
			member.setChangePhoneTime(new Date(JedisUtil.getInstance().time()));//更换手机号时间
					
			List<Member> listWx = memberMapper.selectByOpenId(member);// 根据openid获取数据
			List<Member> listPhone = memberMapper.getByPhone(phone);
		/*				
			String username = null;
			boolean isContainsEmoji = EmojiFilter
					.containsEmoji(new String(mapUser.get("nickname").toString().getBytes("ISO-8859-1"), "UTF-8"));
			if (isContainsEmoji) {
				username = URLEncoder.encode(
						new String(mapUser.get("nickname").toString().getBytes("ISO-8859-1"), "UTF-8"), "utf-8");
			} else {
				username = new String(mapUser.get("nickname").toString().getBytes("ISO-8859-1"), "UTF-8");
			}
			member.setUsername(username);//微信昵号
			member.setHeadimgurl(String.valueOf(mapUser.get("headimgurl")));// 头像					
*/			
			if(listWx.size()>0){
				if(StringUtils.isNotBlank(listWx.get(0).getPhone())){
					logger.info("phoneBindingWx step"+0);
					return -1;//该微信已经绑定了手机
				}else{//该微信已注册但未绑定手机:手机整合微信)
					//TODO 储存过程
					logger.info("phoneBindingWx step"+1);
					this.saveMergeLog(listWx.get(0).getId(), listPhone.get(0).getId(), listWx.get(0).getOpenid());//保存合并微信记录
					memberMapper.mergeUserByWxAndPhone(listWx.get(0).getId(),listPhone.get(0).getId());//调用储存过程合并数据	
					member.setId(listPhone.get(0).getId());
					memberMapper.updateByPrimaryKeySelective(member);	
				}
			}else{//该微信未注册,直接绑定
				logger.info("phoneBindingWx step"+2);
				member.setId(listPhone.get(0).getId());
				memberMapper.updateByPrimaryKeySelective(member);		
			}		
		}else{
			return -2;//微信授权失败
		}
		return 0;
	}

	/**
	 * @Description: 微信绑定手机
	 * @author xieyc
	 * @date 2018年8月30日 下午3:29:29
	 */
	public int saveWxBindingPhone(String phone, Map<String, Object> mapWx) throws Exception{
		int row=0;
		String lang = "zh_CN";
		Map<String, Object> mapUser = WXPayUtil.getUser(String.valueOf(mapWx.get("access_token")),
				String.valueOf(mapWx.get("openid")), lang);//用access_token, openid,lang获取当前登录的用户信息
		
		if (!String.valueOf(mapUser.get("errmsg")).equals(" invalid openid ")) {
			Member member = new Member();
			member.setOpenid(String.valueOf(mapWx.get("openid")));
			member.setUnionid(String.valueOf(mapWx.get("unionid")));
					
			List<Member> listWx = memberMapper.selectByOpenId(member);// 根据openid获取数据
			List<Member> listPhone = memberMapper.getByPhone(phone);
						
			String username = null;
			boolean isContainsEmoji = EmojiFilter
					.containsEmoji(new String(mapUser.get("nickname").toString().getBytes("ISO-8859-1"), "UTF-8"));
			if (isContainsEmoji) {
				username = URLEncoder.encode(
						new String(mapUser.get("nickname").toString().getBytes("ISO-8859-1"), "UTF-8"), "utf-8");
			} else {
				username = new String(mapUser.get("nickname").toString().getBytes("ISO-8859-1"), "UTF-8");
			}
			member.setUsername(username);//微信昵号
			member.setHeadimgurl(String.valueOf(mapUser.get("headimgurl")));// 头像
			
			if(listWx.size()>0){
				if(listPhone.size()>0){
					if(listPhone.get(0).getOpenid().equals("0")){
						//TODO 储存过程 ：手机微信整合
						this.saveMergeLog(listWx.get(0).getId(), listPhone.get(0).getId(), listWx.get(0).getOpenid());//保存合并微信记录
						memberMapper.mergeUserByWxAndPhone(listWx.get(0).getId(),listPhone.get(0).getId());//调用储存过程合并数据	
						member.setId(listPhone.get(0).getId());	
						row= memberMapper.updateByPrimaryKeySelective(member);
					}else{
						return -1;//手机号已经被其他微信绑定
					}
				}else{
					//微信直接绑定手机
					member.setId(listWx.get(0).getId());
					member.setPhone(phone);
					row= memberMapper.updateByPrimaryKeySelective(member);
				}
			}else{
				if(listPhone.size()>0){
					member.setId(listPhone.get(0).getId());
					row= memberMapper.updateByPrimaryKeySelective(member);		
				}else{
					return -2;//微信号和手机都没有注册
				}
			}	
		}else{
			return -3;//微信授权失败
		}
		return row;
	}
	/**
	 * 
	 * @Description: 合并微信记录
	 * @author xieyc
	 * @date 2018年9月5日 下午7:12:25 
	 * @param   
	 * @return  
	 *
	 */
	public void saveMergeLog(int srcUserId,int destUserId,String openid ){
		MergeUserLog saveMergeUserLog=new MergeUserLog();
		saveMergeUserLog.setDestUserId(destUserId);//要合并到的用户
		saveMergeUserLog.setSrcOpenid(openid);//合并的微信的openid
		saveMergeUserLog.setSrcUserId(srcUserId);//被合并的用户
		String 	couponLogId=couponLogMapper.getCouponLogIdByMid(srcUserId);
		String 	orderId=orderMapper.getOrderIdByMid(srcUserId);
		saveMergeUserLog.setCouponId(couponLogId);//合并的优惠卷id
		saveMergeUserLog.setOrderId(orderId);//合并的订单id
		saveMergeUserLog.setAddTime(new Date(JedisUtil.getInstance().time()));//新增时间
		saveMergeUserLog.setEditTime(new Date(JedisUtil.getInstance().time()));//编辑时间
		
		mergeUserLogMapper.insert(saveMergeUserLog);
	}

	/**
	 * 
	 * @Description: 解绑手机号
	 * @author xieyc
	 * @date 2018年8月30日 下午3:30:39 
	 *
	 */
	public int unBindPhone(Integer id) {
		Member member=memberMapper.selectByPrimaryKey(id);
		if(!member.getOpenid().equals("0") && member.getPhone()!=null){
			member.setPhone(null);
			return memberMapper.updateByPrimaryKeySelective(member);
		}else{
			return -1;
		}
		
	}

	
}
