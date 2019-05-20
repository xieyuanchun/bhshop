package com.bh.user.api.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bh.bean.Sms;
import com.bh.pojo.ShopMsg;
import com.bh.user.api.service.MemberShopInfoService;
import com.bh.user.mapper.MemberShopInfoMapper;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberShopInfo;
import com.bh.util.enterprise.BankCardVerify;
import com.bh.util.enterprise.QueryBusinessInfo;
import com.bh.util.enterprise.pojo.BankCardVerifyPojo;
import com.bh.util.enterprise.pojo.QueryBusinessInfoPojo;
import com.bh.utils.JedisUtil;
import com.bh.utils.SmsUtil;

@Service
public class MemberShopInfoServiceImpl implements MemberShopInfoService{
	 @Autowired
	 private  MemberShopInfoMapper memberShopInfoMapper;
	 @Autowired
	 private  MemberShopMapper memberShopMapper;

	/**
	 * @Description: 个人申请入驻信息保存于修改
	 * @author xieyc
	 * @throws Exception 
	 * @date 2018年7月6日 下午2:06:15
	 */
	public int saveOrUpdateByPerson(MemberShopInfo entity) throws Exception {
		if(phoneVerify(entity.getApplicantPhone())){
			return -2;//手机号已经被注册过(sys_user 里面的username是唯一的  审核通过时 默认设置用户就是联系人手机号码)
		}
		int row = 0;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date dateTime=sdf.parse("2019-01-01 00:00:00");
		boolean bankCardVerify = this.bankCardVerify(entity.getApplicantName(),entity.getBankReservationNumber(),
				entity.getBankCardNo(), entity.getApplicantIdcard());//验证银行卡
		entity.setCardRealName(entity.getApplicantName());//设置银行卡卡主
		if(bankCardVerify){
			MemberShopInfo memberShopInfo = memberShopInfoMapper.selectByOpenidAndType(entity.getOpenid(),0);
			if (memberShopInfo != null) {//更新
				entity.setUpdatetime(new Date(JedisUtil.getInstance().time()));//更新时间
				entity.setId(memberShopInfo.getId());
				row=memberShopInfoMapper.updateByPrimaryKeySelective(entity);
				
				MemberShop memberShop=memberShopMapper.selectByPrimaryKey(memberShopInfo.getShopId());
				if(new Date(JedisUtil.getInstance().time()).getTime()<dateTime.getTime()){
					memberShop.setExistPos(3);//不需要交押金
				}else{
					memberShop.setExistPos(2);//需要交押金
				}
				memberShop.setLinkmanName("BH"+entity.getApplicantPhone());;//联系人
				memberShop.setShopName(entity.getApplicantPhone());//店铺名字
				memberShop.setStep(6);//审核中状态
				memberShop.setLinkmanPhone(entity.getApplicantPhone());//联系人
				memberShopMapper.updateByPrimaryKeySelective(memberShop);
				
			} else {// 插入
				MemberShop binHuiShop=memberShopMapper.selectByPrimaryKey(1);//滨惠自营店信息
				
				MemberShop saveMemberShop=new MemberShop();
				saveMemberShop.setMd5Key(binHuiShop.getMd5Key());//md5key
				saveMemberShop.setBusiPayPre(binHuiShop.getBusiPayPre());//支付前缀
				saveMemberShop.setToken(entity.getOpenid());
				saveMemberShop.setStep(6);
				
				saveMemberShop.setShopName(entity.getApplicantPhone());
				saveMemberShop.setAddtime(new Date(JedisUtil.getInstance().time()));
				if(new Date(JedisUtil.getInstance().time()).getTime()<dateTime.getTime()){
					saveMemberShop.setExistPos(3);//不需要交押金
				}else{
					saveMemberShop.setExistPos(2);//需要交押金
				}
				saveMemberShop.setLinkmanName("BH"+entity.getApplicantPhone());;//联系人
				saveMemberShop.setLinkmanPhone(entity.getApplicantPhone());//联系人
				memberShopMapper.insertSelective(saveMemberShop);
				
				entity.setShopId(saveMemberShop.getmId());//商家id
				entity.setAddtime(new Date(JedisUtil.getInstance().time()));//新增时间
				entity.setUpdatetime(new Date(JedisUtil.getInstance().time()));//更新时间
				row=memberShopInfoMapper.insertSelective(entity);
				//是否进行了企业申请
				MemberShopInfo busMemberShopInfo = memberShopInfoMapper.selectByOpenidAndType(entity.getOpenid(),1);
				if(busMemberShopInfo!=null){
					if(busMemberShopInfo.getShopId()==0){//如果只是进行完了第一步（shopId=0）那么删除该记录
						memberShopInfoMapper.deleteByPrimaryKey(busMemberShopInfo.getId());
					}
				}
			}
			//发送短信===》（滨惠商城）您的入驻申请已成功提交，审核结果会在三天内发送短信通知，请耐心等待！
			Sms sms = new Sms();
			sms.setPhoneNo(entity.getApplicantPhone());//要发送到的手机号
			SmsUtil.aliPushSubmitEnterAudit(sms);
		}else{//银行卡验证失败
			row= -1;
		}
		return row;
	}
	/**
	 * @Description: 企业申请入驻信息保存于修改
	 * @author xieyc
	 * @throws Exception
	 * @date 2018年7月6日 下午2:06:15
	 */
	public int saveOrUpdateByBusiness(MemberShopInfo entity) throws Exception {
		if(phoneVerify(entity.getLegalPersonPhone())){
			return -2;//手机号已经被注册过(sys_user 里面的username是唯一的  审核通过时 默认设置用户就是联系人手机号码)
		}
		int row = -1;
		QueryBusinessInfoPojo queryBusinessInfoPojo = QueryBusinessInfo.queryBusinessInfo(entity.getCompanyName());// 验证企业信息是否正确
		if (queryBusinessInfoPojo.getStatus() == 0 || queryBusinessInfoPojo.getStatus().intValue()==210) {//210没有信息也让过
			String creditno = queryBusinessInfoPojo.getResult().getCreditno();//统一信用代码
			String companyName = queryBusinessInfoPojo.getResult().getName();//公司名字
			String legalPersonName = queryBusinessInfoPojo.getResult().getLegalperson();//公司法人
			if(creditno==null){
				creditno=entity.getCreditNo();
			}
			if(companyName==null){
				companyName=entity.getCompanyName();
			}
			if(legalPersonName==null){
				legalPersonName=entity.getLegalPersonName();//查询的法人为null ,那么不验证法人了
			}
			if (creditno.equals(entity.getCreditNo()) && companyName.equals(entity.getCompanyName())
					&& legalPersonName.equals(entity.getLegalPersonName())) {//验证用上3要素（法人、公司名字、统一信用代码）
				MemberShopInfo memberShopInfo = memberShopInfoMapper.selectByOpenidAndType(entity.getOpenid(), 1);
				if (memberShopInfo != null) {// 更新
					entity.setUpdatetime(new Date(JedisUtil.getInstance().time()));
					entity.setId(memberShopInfo.getId());
					row = memberShopInfoMapper.updateByPrimaryKeySelective(entity);
				} else {// 插入
					entity.setApplyType(1);//企业申请
					entity.setAddtime(new Date(JedisUtil.getInstance().time()));
					entity.setUpdatetime(new Date(JedisUtil.getInstance().time()));
					row = memberShopInfoMapper.insertSelective(entity);
				}
			}
			return row;
		}
		return row;// 企业信息验证失败
	}

	/**
	 * @Description: 企业申请入驻==》银行卡信息保存与修改
	 * @author xieyc
	 * @date 2018年7月6日 下午2:06:15
	 */
	public int updateByBusiness(MemberShopInfo entity) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date dateTime = sdf.parse("2019-01-01 00:00:00");
		int row = 0;
		MemberShopInfo memberShopInfo = memberShopInfoMapper.selectByOpenidAndType(entity.getOpenid(), 1);

		if (memberShopInfo.getShopId() == 0) {
			MemberShop binHuiShop=memberShopMapper.selectByPrimaryKey(1);//滨惠自营店信息
			
			MemberShop saveMemberShop = new MemberShop();
			if (new Date(JedisUtil.getInstance().time()).getTime() < dateTime.getTime()) {
				saveMemberShop.setExistPos(3);// 不需要交押金
			} else {
				saveMemberShop.setExistPos(2);// 需要交押金
			}
			saveMemberShop.setShopName(memberShopInfo.getLegalPersonPhone());
			saveMemberShop.setMd5Key(binHuiShop.getMd5Key());//md5key
			saveMemberShop.setBusiPayPre(binHuiShop.getBusiPayPre());//支付前缀
			saveMemberShop.setToken(entity.getOpenid());
			saveMemberShop.setStep(6);
			saveMemberShop.setAddtime(new Date(JedisUtil.getInstance().time()));
			saveMemberShop.setLinkmanName("BH"+memberShopInfo.getLegalPersonPhone());;//联系人
			saveMemberShop.setLinkmanPhone(memberShopInfo.getLegalPersonPhone());
			memberShopMapper.insertSelective(saveMemberShop);

			entity.setShopId(saveMemberShop.getmId());

		} else {
			MemberShop memberShop = memberShopMapper.selectByPrimaryKey(memberShopInfo.getShopId());
			if (new Date().getTime() < dateTime.getTime()) {
				memberShop.setExistPos(3);// 不需要交押金
			} else {
				memberShop.setExistPos(2);// 需要交押金
			}
			memberShop.setShopName(memberShopInfo.getLegalPersonPhone());
			memberShop.setStep(6);
			memberShop.setLinkmanName("BH"+memberShopInfo.getLegalPersonPhone());;//联系人
			memberShop.setLinkmanPhone(memberShopInfo.getLegalPersonPhone());
			memberShopMapper.updateByPrimaryKeySelective(memberShop);
		}
		entity.setUpdatetime(new Date(JedisUtil.getInstance().time()));
		entity.setId(memberShopInfo.getId());
		row = memberShopInfoMapper.updateByPrimaryKeySelective(entity);

		// 发送短信===》（滨惠商城）您的入驻申请已成功提交，审核结果会在三天内发送短信通知，请耐心等待！
		Sms sms = new Sms();
		sms.setPhoneNo(memberShopInfo.getLegalPersonPhone());//要发送到的手机号
		SmsUtil.aliPushSubmitEnterAudit(sms);

		return row;
	}
	/**
	 * @Description: 验证 bh+手机号 在sys_user里面是否存在
	 * @author xieyc
	 * @date 2018年7月31日 下午5:31:30 
	 */
	private  boolean phoneVerify (String phone) {
		boolean flag =false;
		int count=memberShopMapper.phoneVerify("BH"+phone);
		if(count==0){
			flag =false;//不存在
		}else{
			flag =true;//存在
		}
		return flag;
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
	 * @Description: 获取申请详情
	 * @author xieyc
	 * @date 2018年7月6日 下午2:06:15
	 */
	public Map<Object, Object> getApplyDetails(String openid,String page,String applyType) {
		Map<Object, Object> returnMap=null;
		MemberShopInfo memberShopInfo = memberShopInfoMapper.selectByOpenidAndType(openid,Integer.valueOf(applyType));
		if(memberShopInfo!=null){
			MemberShop memberShop=memberShopMapper.selectByPrimaryKey(memberShopInfo.getShopId());
			returnMap=new HashMap<Object, Object>();
			if(Integer.valueOf(applyType)==0){//个人申请
				returnMap.put("applicantName", memberShopInfo.getApplicantName());//申请人名字
				returnMap.put("applicantIdcard", memberShopInfo.getApplicantIdcard());//申请人身份证号码
				returnMap.put("applicantPhone", memberShopInfo.getApplicantPhone());//联系人号码
				returnMap.put("cardRealName", memberShopInfo.getCardRealName());//卡主
				returnMap.put("bankName", memberShopInfo.getBankName());//开户行
				returnMap.put("bankReservationNumber", memberShopInfo.getBankReservationNumber());//预留号码
				returnMap.put("idcardImage",memberShopInfo.getIdcardImage());//身份证照片
				returnMap.put("step", memberShop.getStep());//审核步骤
			}else if(Integer.valueOf(applyType)==1){//企业申请
				if(Integer.valueOf(page)==1){//企业申请第一页
					returnMap.put("companyName", memberShopInfo.getCompanyName());//公司名字
					returnMap.put("creditNo", memberShopInfo.getCreditNo());//信用代码
					returnMap.put("legalPersonIdcard", memberShopInfo.getLegalPersonIdcard());//法人身份证号码
					returnMap.put("businessLicenseImage", memberShopInfo.getBusinessLicenseImage());//营业执照照片
					returnMap.put("legalPersonPhone", memberShopInfo.getLegalPersonPhone());//联系人号码
					returnMap.put("legalPersonIdcardImage",memberShopInfo.getLegalPersonIdcardImage());//法人照片
					returnMap.put("legalPersonName", memberShopInfo.getLegalPersonName());//法人名字
				}else if(Integer.valueOf(page)==2){//企业申请第二页
					returnMap.put("step", memberShop.getStep());//审核步骤
					returnMap.put("bankCardNo", memberShopInfo.getBankCardNo());//卡号
					returnMap.put("bankName", memberShopInfo.getBankName());//开户行
					returnMap.put("bankReservationNumber", memberShopInfo.getBankReservationNumber());//预留号码
				}
			}
		}
		return returnMap;
	}
	/**
	 * @Description: 通过token查询目前已经提交资料到哪一步
	 * @author xieyc
	 * @date 2018年8月1日 上午11:15:43 
	 */
	public ShopMsg selectStep(String openid) {
		ShopMsg returnShopMsg=new ShopMsg();
		List<MemberShopInfo> listInfo = memberShopInfoMapper.getByOpenid(openid);
		if(listInfo.size()>0){
			MemberShopInfo memberShopInfo=listInfo.get(0);
			if(memberShopInfo.getApplyType()==0){//个人申请
				MemberShop memberShop=memberShopMapper.selectByPrimaryKey(memberShopInfo.getShopId());
				if(memberShop.getStep()==6){//审核中
					returnShopMsg.setStep("01");
					returnShopMsg.setMsg("个人申请审核中");
				}else if(memberShop.getStep()==7){//审核成功
					// existPos 是否存在pos机，1有pos(没用到),2无pos(需要提交押金，可退回押金)，3无pos(不需要提交押金)
					if (memberShop.getExistPos().equals(2)) {
						if (memberShop.getPayStatus().equals(1)) {
							returnShopMsg.setStep("04");
							returnShopMsg.setMsg("个人申请成功但是未支付押金");
						} else if (memberShop.getPayStatus().equals(2)) {
							returnShopMsg.setStep("05");
							returnShopMsg.setMsg("个人申请成功且已支付押金");
						} 
					} else {
						returnShopMsg.setStep("02");
						returnShopMsg.setMsg("个人申请审核成功不需要交押金");
					}
				}else if(memberShop.getStep()==8){//审核拒绝
					returnShopMsg.setStep("03");
					returnShopMsg.setMsg("个人申请审核失败");
					returnShopMsg.setNote(memberShop.getNote());
				}
			}else if(memberShopInfo.getApplyType()==1){//企业申请
				if(memberShopInfo.getShopId()==0){
					returnShopMsg.setStep("11");
					returnShopMsg.setMsg("企业申请第一步完成（第一页信息已经提交完成）");
				}else{
					MemberShop memberShop=memberShopMapper.selectByPrimaryKey(memberShopInfo.getShopId());
					if(memberShop.getStep()==6){//审核中
						returnShopMsg.setStep("12");
						returnShopMsg.setMsg("企业申请审核中");
					}else if(memberShop.getStep()==7){//审核成功
						// existPos 是否存在pos机，1有pos(没用到),2无pos(需要提交押金，可退回押金)，3无pos(不需要提交押金)
						if (memberShop.getExistPos().equals(2)) {
							if (memberShop.getPayStatus().equals(1)) {
								returnShopMsg.setStep("14");
								returnShopMsg.setMsg("企业申请成功但是未支付押金");
							} else if (memberShop.getPayStatus().equals(2)) {
								returnShopMsg.setStep("15");
								returnShopMsg.setMsg("企业申请成功且已支付押金");
							} 
						} else {
							returnShopMsg.setStep("13");
							returnShopMsg.setMsg("企业申请审核成功不需要交押金");
						}
					}else if(memberShop.getStep()==8){//审核拒绝
						returnShopMsg.setStep("16");
						returnShopMsg.setMsg("企业申请审核失败");
						returnShopMsg.setNote(memberShop.getNote());
					}
				}
			}
		}else{//为null 没有注册
			returnShopMsg.setStep("00");
			returnShopMsg.setMsg("未注册");
		}
		return returnShopMsg;
	}

	/**
	 * @Description: 获取某个微信的申请类型
	 * @author xieyc
	 * @date 2018年7月6日 下午2:06:15
	 */
	public int getApplyType(String openid) {
		int num = -1;// 未申请
		List<MemberShopInfo> listInfo = memberShopInfoMapper.getByOpenid(openid);
		if (listInfo.size() > 0) {
			MemberShopInfo memberShopInfo = listInfo.get(0);
			if (memberShopInfo.getApplyType() == 0) {
				num = 0;// 个人申请
			} else {
				if(memberShopInfo.getShopId()!=0){
					num = 1;// 企业申请
				}
			}
		}
		return num;
	}
	
	
}
