package com.bh.user.api.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.bean.IDCardEntity;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.user.api.service.SendService;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberSend;
import com.bh.utils.AliMarketUtil;
import com.bh.utils.ImageBase64Util;

@Controller
public class SendController {
	private static final Logger logger = LoggerFactory.getLogger(SendController.class);
	@Autowired
	private SendService sendService;

	@Value(value = "${pageSize}")
	private String pageSize;

	/** 完善配送员的基本信息(pc端) */
	@RequestMapping(value = "/insertSendMsg", method = RequestMethod.POST)
	@ResponseBody
	public BhResult insertSendMsg(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String phone = map.get("phone");
			String fullName = map.get("fullName");
			String idcard_img_url = map.get("idcard_img_url"); // 身份证
			String idcard_hand_img_url = map.get("idcard_hand_img_url");
			String address = map.get("address"); // 住址
			String scope = map.get("scope");// 接单的范围
			String income = map.get("income"); // 预计收入
			String tool = map.get("tool"); // 工具
			String lon = map.get("lon"); //
			String lat = map.get("lat"); //
			String sex = map.get("sex");
			String age = map.get("age");
			MemberSend memberSend = new MemberSend();
			if (StringUtils.isNotEmpty(phone)) {
				memberSend.setPhone(phone);
			}
			if (StringUtils.isNotEmpty(fullName)) {
				memberSend.setFullName(fullName);
			}
			if (StringUtils.isNotEmpty(idcard_img_url)) {
				memberSend.setIdcardImgUrl(idcard_img_url);
			}
			if (StringUtils.isNotEmpty(idcard_hand_img_url)) {
				memberSend.setIdcardHandImgUrl(idcard_hand_img_url);
			}
			if (StringUtils.isNotEmpty(address)) {
				memberSend.setAddress(address);
			}
			if (StringUtils.isNotEmpty(scope)) {
				Integer scope1 = Integer.parseInt(scope);
				Integer scope2 = scope1 * 1000;
				memberSend.setScope(String.valueOf(scope2));
			}
			if (StringUtils.isNotEmpty(income)) {
				memberSend.setIncome(income);
			}
			if (StringUtils.isNotEmpty(tool)) {
				memberSend.setTool(Integer.parseInt(tool));
			}
			if (StringUtils.isNotEmpty(lon)) {
				memberSend.setLon(Double.parseDouble(lon));
			}
			if (StringUtils.isNotEmpty(lat)) {
				memberSend.setLat(Double.parseDouble(lat));
			}
			if (StringUtils.isNotEmpty(sex)) {
				memberSend.setSex(Integer.parseInt(sex));
			}
			if (StringUtils.isNotEmpty(age)) {
				memberSend.setAge(Integer.parseInt(age));
			}
			memberSend.setStatus(0);
			memberSend.setTime(new Date());

			String faceImgBase64Str = ImageBase64Util.getImageBase64StrByUrl(idcard_img_url);
			IDCardEntity idEntity = AliMarketUtil.verifyIDCardFace(faceImgBase64Str);
			if (idEntity == null) {
				result = new BhResult(500, "身份证正面照验证失败", null);
				return result;
			}
			memberSend.setAddress(idEntity.getAddress());
			String sex1 = idEntity.getSex();
			if (sex1.equals("女")) {// 1男2女
				memberSend.setSex(2);
			} else if (sex1.equals("男")) {
				memberSend.setSex(1);
			}
			memberSend.setIdentity(idEntity.getNum());
			memberSend.setFullName(idEntity.getName());

			int row = sendService.insertSendMsg(memberSend);
			if (row == 1) {
				result = new BhResult(200, "添加成功", null);
			} else {
				result = new BhResult(400, "添加失败", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######insertSendMsg#######" + e);
			result = BhResult.build(500, "失败!");
		}
		return result;
	}

	/** pc端 */
	@RequestMapping(value = "/getMsg", method = RequestMethod.POST)
	@ResponseBody
	public BhResult getMsg(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String phone = map.get("phone");
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			if (StringUtils.isEmpty(phone)) {
				result = new BhResult(BhResultEnum.FAIL, "参数不能为空");
			} else {
				if (member == null) {
					// 0,用户不存在，1用户未注册成配送员，2,用户注册成配送员
					MemberSend send = sendService.getMsg(phone);
					if ("0".equals(send.getFullName())) {
						result = new BhResult(201, "该用户不存在，请先成为普通用户才能注册成配送员", null);
					} else if ("1".equals(send.getFullName())) {
						result = new BhResult(BhResultEnum.SUCCESS, "该用户可以注册成为配送员");
					} else {
						if (send.getStatus().equals("0")) {
							result = new BhResult(202, "该用户已提交注册信息，待管理员审核", send);
						} else if (send.getStatus().equals("1")) {
							result = new BhResult(203, "该用户已成为配送员，不可以再次提交", null);
						} else if (send.getStatus().equals("2")) {
							result = new BhResult(204, "该用户提交的信息审核失败，可再次提交", send);
						}
					}
				} else {
					List<Member> member2 = sendService.getMsg1(phone, member.getId());// 0,用户不存在，1用户未注册成配送员，2,用户注册成配送员
					if (member2.size() > 0) {
						if (member2.get(0).getId().equals(member.getId())) {
							MemberSend send = sendService.getMsg(phone);// 0,用户不存在，1用户未注册成配送员，2,用户注册成配送员
							if ("0".equals(send.getFullName())) {
								result = new BhResult(201, "该用户不存在，请先成为普通用户才能注册成配送员", null);
							} else if ("1".equals(send.getFullName())) {
								result = new BhResult(BhResultEnum.SUCCESS, "该用户可以注册成为配送员");
							} else if ("2".equals(send.getFullName())) {
								System.out.print("fullname" + send.getFullName());
								if (send.getStatus().equals(0)) {
									result = new BhResult(202, "该用户已提交注册信息，待管理员审核", send);
								} else if (send.getStatus().equals(1)) {
									result = new BhResult(203, "该用户已成为配送员，不可以再次提交", null);
								} else if (send.getStatus().equals(2)) {
									result = new BhResult(204, "该用户提交的信息审核失败，可再次提交", send);
								}

							}
						} else {
							result = new BhResult(400, "该用户的手机号不匹配", null);
						}
					} else {
						result = new BhResult(205, "该手机号在系统中不存在", null);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######getMsg#######" + e);
			result = BhResult.build(500, "失败!");
		}
		return result;
	}

	/** 移动端实名认证(移动端) */
	@RequestMapping(value = "/send/autoByMove", method = RequestMethod.POST)
	@ResponseBody
	public BhResult autoByMove(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			String idcard_img_url = map.get("idcard_img_url"); // 身份证
			String idcard_hand_img_url = map.get("idcard_hand_img_url");
			String scope = "10";// 接单的范围
			String income = "0"; // 预计收入
			String tool = "1"; // 工具

			MemberSend memberSend = new MemberSend();
			if (StringUtils.isNotEmpty(member.getPhone())) {
				memberSend.setPhone(member.getPhone());
			}
			if (StringUtils.isNotEmpty(idcard_img_url)) {
				memberSend.setIdcardImgUrl(idcard_img_url);
			}
			if (StringUtils.isNotEmpty(idcard_hand_img_url)) {
				memberSend.setIdcardHandImgUrl(idcard_hand_img_url);
			}
			if (StringUtils.isNotEmpty(scope)) {
				Integer scope1 = Integer.parseInt(scope);
				Integer scope2 = scope1 * 1000;
				memberSend.setScope(String.valueOf(scope2));
			}
			if (StringUtils.isNotEmpty(income)) {
				memberSend.setIncome(income);
			}
			if (StringUtils.isNotEmpty(tool)) {
				memberSend.setTool(Integer.parseInt(tool));
			}
			memberSend.setStatus(0);
			memberSend.setTime(new Date());

			String faceImgBase64Str = ImageBase64Util.getImageBase64StrByUrl(idcard_img_url);
			IDCardEntity idEntity = AliMarketUtil.verifyIDCardFace(faceImgBase64Str);
			if (idEntity == null) {
				result = new BhResult(500, "身份证正面照验证失败", null);
				return result;
			}
			memberSend.setAddress(idEntity.getAddress());
			String sex1 = idEntity.getSex();
			if (sex1.equals("女")) {// 1男2女
				memberSend.setSex(2);
			} else if (sex1.equals("男")) {
				memberSend.setSex(1);
			}
			memberSend.setIdentity(idEntity.getNum());
			memberSend.setFullName(idEntity.getName());

			int row = sendService.insertSendMsg(memberSend);
			if (row == 1) {
				result = new BhResult(200, "添加成功", null);
			} else {
				result = new BhResult(400, "添加失败", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######autoByMove#######" + e);
			result = BhResult.build(500, "失败!");
		}
		return result;
	}

	/** 移动端端 */
	@RequestMapping(value = "/send/getMsgByMove", method = RequestMethod.POST)
	@ResponseBody
	public BhResult getMsgByMove(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			if (member.getPhone() == null) {
				result = new BhResult(206, "该手机号在系统中不存在,请绑定手机", null);
			} else {
				List<Member> member2 = sendService.getMsg1(member.getPhone(), member.getId());// 0,用户不存在，1用户未注册成配送员，2,用户注册成配送员
				if (member2.size() > 0) {
					if (member2.get(0).getId().equals(member.getId())) {
						MemberSend send = sendService.getMsg(member.getPhone());// 0,用户不存在，1用户未注册成配送员，2,用户注册成配送员
						if ("0".equals(send.getFullName())) {
							result = new BhResult(201, "该用户不存在，请先成为普通用户才能注册成配送员", null);
						} else if ("1".equals(send.getFullName())) {
							result = new BhResult(BhResultEnum.SUCCESS, "该用户可以注册成为配送员");
						} else if ("2".equals(send.getFullName())) {
							System.out.print("fullname" + send.getFullName());
							if (send.getStatus().equals(0)) {
								result = new BhResult(202, "该用户已提交注册信息，待管理员审核", send);
							} else if (send.getStatus().equals(1)) {
								result = new BhResult(203, "该用户已成为配送员，不可以再次提交", null);
							} else if (send.getStatus().equals(2)) {
								result = new BhResult(204, "该用户提交的信息审核失败，可再次提交", send);
							}
						}
					} else {
						result = new BhResult(206, "该手机号在系统中不存在,请绑定手机", null);
					}
				} else {
					result = new BhResult(206, "该手机号在系统中不存在,请绑定手机", null);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######getMsgByMove#######" + e);
			result = BhResult.build(500, "失败!");
		}
		return result;
	}

	/**
	 * CHENG-20171124-01 速达-个人中心
	 * 
	 * @param map(goodsId-商品的id,skuId-sku的id)
	 * @return
	 */
	@RequestMapping("/send/sendcenter")
	@ResponseBody
	public BhResult showGoodsFavorite(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			MemberSend memberSend = new MemberSend();
			memberSend.setmId(member.getId());
			MemberSend memberSend2 = sendService.selectMemberSendCenter(memberSend);
			result = new BhResult(BhResultEnum.SUCCESS, memberSend2);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######sendcenter#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * scj-20171128-03 配送员详情
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/memberSend/sendDetails")
	@ResponseBody
	public BhResult sendDetails(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String mId = map.get("mId");
			MemberSend send = sendService.sendDetails(mId);
			if (send != null) {
				result = new BhResult(BhResultEnum.SUCCESS, send);
			} else {
				result = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######sendDetails#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * scj-20171128-04 获取配送员身份证
	 * 
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/memberSend/getIdentity")
	@ResponseBody
	public BhResult getIdentity(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String mId = map.get("mId");
			String str = sendService.getIdentity(mId);
			result = new BhResult(BhResultEnum.SUCCESS, str);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######getIdentity#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

}
