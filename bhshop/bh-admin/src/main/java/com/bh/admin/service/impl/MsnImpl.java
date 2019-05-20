package com.bh.admin.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.admin.enums.UnionPayInterfaceEnum;
import com.bh.admin.mapper.order.MsnApplyMapper;
import com.bh.admin.mapper.order.MsnSendNumberMapper;
import com.bh.admin.mapper.order.MsnTemplateMapper;
import com.bh.admin.mapper.user.MemberShopMapper;
import com.bh.admin.pojo.order.MsnApply;
import com.bh.admin.pojo.order.MsnSendNumber;
import com.bh.admin.pojo.order.MsnTemplate;
import com.bh.admin.pojo.order.MyMsnApply;
import com.bh.admin.pojo.order.Phone;
import com.bh.admin.pojo.user.MemberShop;
import com.bh.admin.service.MsnService;
import com.bh.admin.util.JedisUtil;
import com.bh.admin.vo.UnionPayRefundVO;
import com.bh.admin.vo.UnionPayVO;
import com.bh.config.Contants;
import com.bh.utils.IDUtils;
import com.bh.utils.MoneyUtil;
import com.bh.utils.PageBean;
import com.bh.utils.PageParams;
import com.bh.utils.pay.HttpService;
import com.mysql.jdbc.StringUtils;

@Service
public class MsnImpl implements MsnService {
	@Autowired
	private MsnApplyMapper msnApplyMapper;
	@Autowired
	private MsnTemplateMapper msnTemplateMapper;
	@Autowired
	private MsnSendNumberMapper msnSendNumberMapper;
	@Autowired
	private MemberShopMapper memberShopMapper;

	/**
	 * @Description: 进入短信群发的页面
	 * @author fanjh
	 * @date 2018年9月11日 下午2:00:25
	 */
	public Map getMsnShopInfo(Integer shopId, String memberType) {
		MemberShop memberShop = memberShopMapper.selectByPrimaryKey(shopId);
		int count = msnApplyMapper.selectCustCount(shopId, memberType);
		Map<String, Object> map = new HashMap<>();
		map.put("count", count); // 人数
		double money = (double) count / 100;
		map.put("money", money); // 费用 测试用缩小10倍
		map.put("freeNum", memberShop.getFreeNum());
		map.put("surplusNum", memberShop.getSurplusNum());
		return map;
	}

	/**
	 * @Description: 保存群发信息
	 * @author fanjh
	 * @date 2018年9月11日 下午2:00:25
	 */
	public String saveMsnApply(Integer shopId, String template, String groupNum, String memberType, String isFreeNum,
			String money, String apymsnId) {
		MemberShop memberShop = memberShopMapper.selectByPrimaryKey(1);
		MsnApply msnApply = new MsnApply();
		Date nowDate = new Date(JedisUtil.getInstance().time());
		String jsonStr = null;
		MemberShop memberShop2 = memberShopMapper.selectByPrimaryKey(shopId);
		if (Integer.valueOf(apymsnId) == 0) {
			if (memberShop2.getSurplusNum() <= 0) { // 防止一个账号多个人同时操作
				jsonStr = "1";
			} else {
				if (Integer.valueOf(isFreeNum) == 1) {
					if (memberShop2.getFreeNum() <= 0) { // 防止一个账号多个人同时操作
						jsonStr = "2";
					} else {
						jsonStr=this.updateShopInfo(memberShop,msnApply,shopId, template,groupNum, memberType, isFreeNum, money,apymsnId);
					}
				} else {
					jsonStr=this.updateShopInfo(memberShop,msnApply,shopId, template,groupNum, memberType, isFreeNum, money,apymsnId);
				}
			}
		} else {
			if (Integer.valueOf(isFreeNum) == 1) {
				msnApply.setApymsnId(Integer.valueOf(apymsnId));
				msnApply.setPayStatus(1);// 已支付
				msnApply.setPayTime(nowDate);
				msnApplyMapper.updateByPrimaryKey(msnApply);
				jsonStr = "0"; //使用免费次数
			} else {
				msnApply = msnApplyMapper.selectByPrimaryKey(Integer.valueOf(apymsnId));
				String orderNo = IDUtils.getOrderNo(memberShop.getBusiPayPre());
				msnApply.setApymsnId(Integer.valueOf(apymsnId));
				msnApply.setOrderNo(orderNo);
				msnApplyMapper.updateByPrimaryKeySelective(msnApply);
				jsonStr = this.messagePay(apymsnId, money, memberShop, orderNo);
			}
		}

		return jsonStr;
	}
	
	
	public String updateShopInfo(MemberShop memberShop,MsnApply msnApply,Integer shopId, String template, String groupNum, String memberType, String isFreeNum,
			String money, String apymsnId) {
		String jsonStr="";
		String orderNo = IDUtils.getOrderNo(memberShop.getBusiPayPre());
		Date nowDate = new Date(JedisUtil.getInstance().time());
		msnApply.setAddTime(nowDate);
		if (!StringUtils.isNullOrEmpty(money)) {
			Integer money1 = (int) (MoneyUtil.yuan2Fen(money));
			msnApply.setMoney(money1); // 价格
		}
		if (!StringUtils.isNullOrEmpty(template)) {
			msnApply.setTemplate(template); // 内容
		}
		if (!StringUtils.isNullOrEmpty(groupNum)) {
			msnApply.setGroupNum(Integer.valueOf(groupNum)); // 人数
		}
		msnApply.setShopId(shopId);
		Calendar ca = Calendar.getInstance();
		ca.setTime(msnApply.getAddTime());
		ca.add(Calendar.MINUTE, Contants.semih);
		msnApply.setValidTime(ca.getTime());
		msnApply.setIsFreeNum(Integer.valueOf(isFreeNum));
		msnApply.setOrderNo(orderNo);
		msnApply.setSendStatus(0);
		msnApply.setMemberType(Integer.valueOf(memberType));
		msnApplyMapper.insertSelective(msnApply);// 保存群发信息

		MsnTemplate msnTemplate = new MsnTemplate();
		msnTemplate.setApymsnId(msnApply.getApymsnId());
		msnTemplate.setShopId(shopId);
		msnTemplate.setContains(template);
		msnTemplate.setAddTime(nowDate);
		msnTemplateMapper.insertSelective(msnTemplate);// 保存短信模板信息表

		// 保存手机号
		List<Phone> list = msnApplyMapper.selectCustList(shopId, memberType);
		if (list.size() > 0) {
			for (Phone phone : list) {
				MsnSendNumber msn = new MsnSendNumber();
				msn.setPhone(phone.getPhone());
				msn.setAddTime(nowDate);
				msn.setApymsnId(msnApply.getApymsnId());
				msn.setmId(phone.getmId());
				msnSendNumberMapper.insertSelective(msn);
			}
		}
		// 更新商家表
		MemberShop memberShop1 = memberShopMapper.selectByPrimaryKey(shopId);
		if (Integer.parseInt(isFreeNum) == 1) {
			memberShop1.setFreeNum(memberShop1.getFreeNum() - 1);
		}
		memberShop1.setSurplusNum(memberShop1.getSurplusNum() - 1);
		memberShopMapper.updateByPrimaryKeySelective(memberShop1);
		if (Integer.valueOf(isFreeNum) == 1) {
			msnApply.setPayStatus(1);// 已支付
			msnApply.setPayTime(nowDate);
			msnApplyMapper.updateByPrimaryKey(msnApply);
			jsonStr = "0";
		} else {
			apymsnId = msnApply.getApymsnId().toString();
			jsonStr = this.messagePay(apymsnId, money, memberShop, msnApply.getOrderNo());
		}
		return jsonStr;
	}

	/**
	 * @Description: 短信支付
	 * @author fanjh
	 * @date 2018年9月11日 下午2:00:25
	 */
	public String messagePay(String apymsnId, String money, MemberShop memberShop, String orderNo) {
		String orderBody = "null,0004" + "," + apymsnId;
		UnionPayVO vo = new UnionPayVO();
		vo.setOriginalAmount(MoneyUtil.yuan2Fen(money) + "");
		vo.setTotalAmount(MoneyUtil.yuan2Fen(money) + "");
		vo.setMd5Key(memberShop.getMd5Key());
		vo.setAttachedData(orderBody);
		vo.setMerOrderId(orderNo);
		String jsonStr = HttpService.doPostJson(UnionPayInterfaceEnum.MESSAGEPAY.getMethod(), vo);
		jsonStr = jsonStr.substring(1, jsonStr.length() - 1);
		return jsonStr;
	}

	// 通过orderNo查询该订单是否支付成功
	public MsnApply checkIsPaySeccuss(String orderNo) {
		MsnApply msnApply = msnApplyMapper.checkIsPaySeccuss(orderNo);
		return msnApply;
	}

	/**
	 * @Description: 审核
	 * @author fanjh
	 * @date 2018年9月11日 下午2:00:25
	 */
	public Boolean audit(Integer shopId, Integer userId, String apymsnId, String reviewResult,
			String reviewResultRemkar) {
		Boolean flag = true;
		Date nowDate = new Date(JedisUtil.getInstance().time());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
		String userName = memberShopMapper.selectUsernameBymId(userId); // 查找操作人
		MsnTemplate msnTemplate = new MsnTemplate();
		msnTemplate.setReviewResult(Integer.valueOf(reviewResult)); // 审核状态
		msnTemplate.setReviewerName(userName); // 审核人
		if (!StringUtils.isNullOrEmpty(reviewResultRemkar)) {
			msnTemplate.setReviewResultRemkar(reviewResultRemkar); // 备注
		}
		msnTemplate.setEditTime(nowDate); // 审核时间
		msnTemplate.setApymsnId(Integer.valueOf(apymsnId));
		int row = msnTemplateMapper.updateByApymsnId(msnTemplate);

		MsnApply msnApply = msnApplyMapper.selectByPrimaryKey(Integer.valueOf(apymsnId));

		if (reviewResult.equals("2")) { // 审核通过
			msnApply.setSendStatus(2); // 发送成功
			msnApply.setSendTime(nowDate); // 发送时间
			msnApplyMapper.updateByPrimaryKeySelective(msnApply);
		}

		if (reviewResult.equals("3")) { // 审核拒绝
			String date = sdf.format(nowDate); // 当前时间
			String nowmonth = date.substring(0, 6); // 当前时间的年月份
			String addTime = sdf.format(msnApply.getAddTime()); // 新增时间
			String addmonth = addTime.substring(0, 6); // 当前时间的月份
			if (msnApply != null) {
				MemberShop mShop = memberShopMapper.selectByPrimaryKey(msnApply.getShopId());
				if (msnApply.getIsFreeNum() == 1) { // 使用过免费次数
					mShop.setFreeNum(mShop.getFreeNum() + 1); // 免费次数+1
					if (nowmonth.equals(addmonth)) {
						mShop.setSurplusNum(mShop.getSurplusNum() + 1); // 本月剩余次数 +1
					}
					msnApply.setSendStatus(3); // 发送失败
					msnApplyMapper.updateByPrimaryKeySelective(msnApply);
					memberShopMapper.updateByPrimaryKeySelective(mShop);
				} else { // 未使用免费次数
					if (nowmonth.equals(addmonth)) {
						mShop.setSurplusNum(mShop.getSurplusNum() + 1); // 本月剩余次数 +1
						memberShopMapper.updateByPrimaryKeySelective(mShop);
					}
					msnApply.setSendStatus(3); // 发送失败
					msnApplyMapper.updateByPrimaryKeySelective(msnApply);
				}
			}
			if (msnApply.getMoney() > 0) {
				flag = auditRefund(msnApply);
			}
		}
		return flag;
	}

	/**
	 * @Description: 查看信息
	 * @author fanjh
	 * @date 2018年9月12日 下午2:00:25
	 */
	public MyMsnApply viewInfo(Integer apymsnId) {
		MyMsnApply myMsnApply = msnApplyMapper.selectByApymsnId(apymsnId);
		myMsnApply.setPrice(MoneyUtil.fen2Yuan(myMsnApply.getMoney() + ""));
		return myMsnApply;
	}

	/**
	 * @Description: 商家短信列表
	 * @author fanjh
	 * @date 2018年9月12日 下午2:00:25
	 */
	public PageBean<MyMsnApply> getShopListInfo(Integer shopId, String startTime, String endTime, String payStatus,
			String reviewResult, String currentPage) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String startTimes = "1994-01-01"; // 设置默认起始时间
		String endTimes = "2094-01-01";
		if (!StringUtils.isEmptyOrWhitespaceOnly(startTime)) {
			startTimes = startTime;
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(endTime)) {
			endTimes = endTime;
		}
		PageParams<MyMsnApply> params = new PageParams<MyMsnApply>();
		PageBean<MyMsnApply> pageBean = new PageBean<MyMsnApply>();
		params.setCurPage(Integer.parseInt(currentPage));
		params.setPageSize(Contants.PAGE_SIZE);

		List<MyMsnApply> list = null;
		try {
			list = msnApplyMapper.selectByShopMessageList(shopId, sf.parse(startTimes), sf.parse(endTimes), payStatus,
					reviewResult);
		} catch (NumberFormatException | ParseException e) {
			e.printStackTrace();
		}
		if (list.size() > 0) {
			for (MyMsnApply myMsnApply : list) {
				myMsnApply.setPrice(MoneyUtil.fen2Yuan(myMsnApply.getMoney().toString()));
			}
		}
		params.setResult(list);
		PageBean<MyMsnApply> retBean = pageBean.getPageResult(params);
		return retBean;
	}

	/**
	 * @Description: 平台短信列表
	 * @author fanjh
	 * @date 2018年9月12日 下午2:00:25
	 */
	public PageBean<MyMsnApply> getListInfo(String startTime, String endTime, String payStatus, String reviewResult,
			String currentPage) {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
		String startTimes = "1994-01-01"; // 设置默认起始时间
		String endTimes = "2094-01-01";
		if (!StringUtils.isEmptyOrWhitespaceOnly(startTime)) {
			startTimes = startTime;
		}
		if (!StringUtils.isEmptyOrWhitespaceOnly(endTime)) {
			endTimes = endTime;
		}
		PageParams<MyMsnApply> params = new PageParams<MyMsnApply>();
		PageBean<MyMsnApply> pageBean = new PageBean<MyMsnApply>();
		params.setCurPage(Integer.parseInt(currentPage));
		params.setPageSize(Contants.PAGE_SIZE);

		List<MyMsnApply> list = null;
		try {
			list = msnApplyMapper.selectByMessageList(sf.parse(startTimes), sf.parse(endTimes), payStatus,
					reviewResult);
		} catch (NumberFormatException | ParseException e) {
			e.printStackTrace();
		}
		if (list.size() > 0) {
			for (MyMsnApply myMsnApply : list) {
				myMsnApply.setPrice(MoneyUtil.fen2Yuan(myMsnApply.getMoney().toString()));
			}
		}
		params.setResult(list);
		PageBean<MyMsnApply> retBean = pageBean.getPageResult(params);
		return retBean;
	}

	/**
	 * @Description: 移动端保存群发信息
	 * @author fanjh
	 * @date 2018年9月11日 下午2:00:25
	 */
	public String msaveMsnApply(Integer shopId, String template, String groupNum, String memberType, String isFreeNum,
			String money, String apymsnId) {
		MemberShop memberShop = memberShopMapper.selectByPrimaryKey(1);
		MsnApply msnApply = new MsnApply();
		Date nowDate = new Date(JedisUtil.getInstance().time());
		String jsonStr = null;
		if (Integer.valueOf(apymsnId) == 0) {
			String orderNo = IDUtils.getOrderNo(memberShop.getBusiPayPre());
			msnApply.setAddTime(nowDate);
			if (!StringUtils.isNullOrEmpty(money)) {
				Integer money1 = (int) (MoneyUtil.yuan2Fen(money));
				msnApply.setMoney(money1); // 价格
			}
			if (!StringUtils.isNullOrEmpty(template)) {
				msnApply.setTemplate(template); // 内容
			}
			if (!StringUtils.isNullOrEmpty(groupNum)) {
				msnApply.setGroupNum(Integer.valueOf(groupNum)); // 人数
			}
			msnApply.setShopId(shopId);
			Calendar ca = Calendar.getInstance();
			ca.setTime(msnApply.getAddTime());
			ca.add(Calendar.MINUTE, Contants.semih);
			msnApply.setValidTime(ca.getTime());
			msnApply.setIsFreeNum(Integer.valueOf(isFreeNum));
			msnApply.setOrderNo(orderNo);
			msnApplyMapper.insertSelective(msnApply);// 保存群发信息

			MsnTemplate msnTemplate = new MsnTemplate();
			msnTemplate.setApymsnId(msnApply.getApymsnId());
			msnTemplate.setShopId(shopId);
			msnTemplate.setContains(template);
			msnTemplate.setAddTime(nowDate);
			msnTemplateMapper.insertSelective(msnTemplate);// 保存短信模板信息表

			// 保存手机号
			List<Phone> list = msnApplyMapper.selectCustList(shopId, memberType);
			if (list.size() > 0) {
				for (Phone phone : list) {
					MsnSendNumber msn = new MsnSendNumber();
					msn.setPhone(phone.getPhone());
					msn.setAddTime(nowDate);
					msn.setApymsnId(msnApply.getApymsnId());
					msn.setmId(phone.getmId());
					msnSendNumberMapper.insertSelective(msn);
				}
			}
			// 更新商家表
			MemberShop memberShop1 = memberShopMapper.selectByPrimaryKey(shopId);
			if (Integer.parseInt(isFreeNum) == 1) {
				memberShop1.setFreeNum(memberShop1.getFreeNum() - 1);
			}
			memberShop1.setSurplusNum(memberShop1.getSurplusNum() - 1);
			memberShopMapper.updateByPrimaryKeySelective(memberShop1);
			if (Integer.valueOf(isFreeNum) == 1) { // 勾选免费
				msnApply.setPayStatus(1);// 已支付
				msnApply.setPayTime(nowDate);
				msnApplyMapper.updateByPrimaryKey(msnApply);
				jsonStr = "0";
			} else {
				apymsnId = msnApply.getApymsnId().toString();
				jsonStr = this.mMessagePay(apymsnId, money, memberShop, msnApply.getOrderNo());
			}
		} else {
			if (Integer.valueOf(isFreeNum) == 1) { // 勾选免费
				msnApply.setApymsnId(Integer.valueOf(apymsnId));
				msnApply.setPayTime(nowDate);
				msnApply.setPayStatus(1);// 已支付
				msnApplyMapper.updateByPrimaryKey(msnApply);
				jsonStr = "0";
			} else {
				msnApply = msnApplyMapper.selectByPrimaryKey(Integer.valueOf(apymsnId));
				jsonStr = this.mMessagePay(apymsnId, money, memberShop, msnApply.getOrderNo());
			}
		}
		return jsonStr;
	}

	/**
	 * @Description: 短信支付
	 * @author fanjh
	 * @date 2018年9月11日 下午2:00:25
	 */
	public String mMessagePay(String apymsnId, String money, MemberShop memberShop, String orderNo) {
		String jsonStr = "";
		String orderBody = "null,0004" + "," + apymsnId;
		UnionPayVO vo = new UnionPayVO();
		vo.setOriginalAmount(MoneyUtil.yuan2Fen(money) + "");
		vo.setTotalAmount(MoneyUtil.yuan2Fen(money) + "");
		vo.setMd5Key(memberShop.getMd5Key());
		vo.setAttachedData(orderBody);
		vo.setMerOrderId(orderNo);
		if (Integer.parseInt(vo.getTotalAmount()) > 0) {
			jsonStr = HttpService.doPostJson(UnionPayInterfaceEnum.WXJSPAY.getMethod(), vo);
			jsonStr = jsonStr.replaceAll("&", "&amp");
		}
		return jsonStr;
	}

	/**
	 * @Description: 移动端商家短信列表
	 * @author fanjh
	 * @date 2018年9月12日 下午2:00:25
	 */
	public PageBean<MyMsnApply> mgetShopListInfo(Integer shopId, String currentPage) {
		PageParams<MyMsnApply> params = new PageParams<MyMsnApply>();
		PageBean<MyMsnApply> pageBean = new PageBean<MyMsnApply>();
		params.setCurPage(Integer.parseInt(currentPage));
		params.setPageSize(Contants.PAGE_SIZE);
		List<MyMsnApply> list = null;
		list = msnApplyMapper.mselectByShopMessageList(shopId);
		if (list.size() > 0) {
			for (MyMsnApply myMsnApply : list) {
				myMsnApply.setPrice(MoneyUtil.fen2Yuan(myMsnApply.getMoney().toString()));
			}
		}
		params.setResult(list);
		PageBean<MyMsnApply> retBean = pageBean.getPageResult(params);
		return retBean;
	}

	/**
	 * @Description: 手机号码的导出
	 * @author fanjh
	 * @date 2018年9月12日 下午2:00:25
	 */
	@SuppressWarnings("unused")
	public List<Phone> excelExportPhone(Integer apymsnId) throws Exception {
		return msnSendNumberMapper.selectExportGoods(apymsnId);
	}

	/**
	 * @Description: 短信订单失效
	 * @author fanjh
	 * @date 2018年9月13日 下午2:00:25
	 */
	public int msgPayFailJob() {
		int row = 0;
		List<MsnApply> msnApplyList = msnApplyMapper.selectNoPayList(); // 查询未支付的订单
		Date nowDate = new Date(JedisUtil.getInstance().time());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
		if (msnApplyList.size() > 0) {
			for (MsnApply msnApply : msnApplyList) {
				if (nowDate.getTime() > msnApply.getValidTime().getTime()) { // 订单失效
					msnApply.setPayStatus(2); // 已取消
					msnApply.setEditTime(nowDate);
					String date = sdf.format(nowDate); // 当前时间
					String nowmonth = date.substring(4, 6); // 当前时间的月份
					String addTime = sdf.format(msnApply.getAddTime()); // 新增时间
					String addmonth = addTime.substring(4, 6); // 当前时间的月份
					MemberShop mShop = memberShopMapper.selectByPrimaryKey(msnApply.getShopId());
					if (msnApply.getIsFreeNum() == 1) { // 使用过免费次数
						mShop.setFreeNum(mShop.getFreeNum() + 1); // 免费次数+1
						if (nowmonth.equals(addmonth)) {
							mShop.setSurplusNum(mShop.getSurplusNum() + 1); // 本月剩余次数 +1
						}
						memberShopMapper.updateByPrimaryKeySelective(mShop);
					} else { // 未使用免费次数
						if (nowmonth.equals(addmonth)) {
							mShop.setSurplusNum(mShop.getSurplusNum() + 1); // 本月剩余次数 +1
							memberShopMapper.updateByPrimaryKeySelective(mShop);
						}
					}
					row = msnApplyMapper.updateByPrimaryKeySelective(msnApply);
				}
			}
		}
		return row;
	}

	/**
	 * @Description: 月初将本月短信次数置为2次
	 * @author fanjh
	 * @date 2018年9月13日 下午2:00:25
	 */
	public int beginMonthJob() {
		int row = 0;
		Date nowDate = new Date(JedisUtil.getInstance().time());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");// 设置日期格式
		String date = sdf.format(nowDate);
		String thisDay = date.substring(6, 8); // 当前时间的日
		if (thisDay.equals("18")) { // 当前时间的日为01为月初
			row = memberShopMapper.updateSurplusNum(); // 修改所有商家的本月剩余次数
		}
		return row;
	}

	/**
	 * 商家拒绝之后退款
	 * 
	 * @param shopId
	 * @param money
	 * @param orderNo
	 * @return
	 */
	public Boolean auditRefund(MsnApply msnApply) {
		Boolean flag = false;
		try {
			// 微信支付的退款
			MemberShop memberShop = memberShopMapper.selectByPrimaryKey(msnApply.getShopId());
			UnionPayRefundVO vo = new UnionPayRefundVO();
			vo.setRefundAmount(msnApply.getMoney().toString());
			vo.setMerOrderId(msnApply.getOrderNo());
			vo.setMd5Key(memberShop.getMd5Key());
			String ret = HttpService.doPostJson(UnionPayInterfaceEnum.WXJSREFUND.getMethod(), vo);
			if ("SUCCESS".equals(ret.replaceAll("\"", ""))) {
				flag = true;// 退款成功
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;

	}

}
