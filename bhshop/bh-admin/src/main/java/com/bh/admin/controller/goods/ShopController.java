package com.bh.admin.controller.goods;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message.RecipientType;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bh.bean.BankCard;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.alibaba.fastjson.JSON;
import com.bh.admin.enums.CardTypeEnum;
import com.bh.admin.service.ShopService;
import com.bh.admin.util.JedisUtil;
import com.bh.admin.util.JedisUtil.Strings;
import com.bh.admin.pojo.user.BusBankCard;
import com.bh.admin.pojo.user.MemberShop;
import com.bh.admin.pojo.user.MemberShopInfo;
import com.bh.admin.pojo.user.ShopCashDeposit;
import com.bh.admin.pojo.user.ShopInfoByBusiness;
import com.bh.admin.pojo.user.ShopInfoByPerson;
import com.bh.admin.pojo.user.ShopMsg;
import com.bh.utils.AliMarketUtil;
import com.bh.utils.BankUtil;
import com.bh.utils.GetLatitude;
import com.bh.utils.LoggerUtil;
import com.bh.utils.MixCodeUtil;
import com.bh.utils.PageBean;
import com.bh.utils.RegExpValidatorUtils;
import com.bh.utils.SmsUtil;

@Controller
public class ShopController {
	private static final Logger logger = LoggerFactory.getLogger(ShopController.class);

	@Autowired
	private ShopService shopService;

	
	@RequestMapping(value = "/shop/getCardType", method = RequestMethod.POST)
	@ResponseBody
	public BhResult getCardType(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		// 获得身份证的类型
		try {
			// 返回身份证列表
			bhResult = new BhResult(200, "查询成功", CardTypeEnum.getReasonList());
		} catch (Exception e) {
			bhResult = BhResult.build(500, "操作失败!");
			e.printStackTrace();
		}
		return bhResult;
	}

	// 商家入驻-入驻联系人信息
	@RequestMapping(value = "/shop/insertStepOne", method = RequestMethod.POST)
	@ResponseBody
	public BhResult insertStep(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			String token = (String) request.getSession().getAttribute("token");// map.get("token");
			System.out.println("appid -- >" + token);

			String linkmanName = map.get("linkmanName");// 联系人姓名
			String linkmanPhone = map.get("linkmanPhone");// 联系人电话
			String isInvite = map.get("isInvite");// 是否接受其他商场邀请0否，1是
			String code = map.get("code");// 如果接受了其他商场的邀请则传推荐商家的id,如果没有则传0

			// 如果为null，该openid不存在
			if (StringUtils.isEmpty(linkmanName)) {
				bhResult = new BhResult(400, "联系人姓名不能为空", null);
			} else if (StringUtils.isEmpty(linkmanPhone)) {
				bhResult = new BhResult(400, "联系人电话不能为空", null);
			} else {

				MemberShop memberShop = new MemberShop();
				memberShop.setLinkmanName(linkmanName);
				memberShop.setLinkmanPhone(linkmanPhone);

				if (StringUtils.isNotBlank(isInvite)) {
					memberShop.setIsinvite(Integer.parseInt(isInvite));
				}
				if (StringUtils.isNotEmpty(code)) {
					memberShop.setCode(code);
				}
				memberShop.setIsPc(1);
				memberShop.setToken(token);
				// step=1
				memberShop.setStep(2);
				bhResult = shopService.insertStep(memberShop);
			}

		} catch (Exception e) {
			bhResult = BhResult.build(500, "操作失败!");
			e.printStackTrace();
		}
		return bhResult;
	}

	// 完善商家的基本信息
	@RequestMapping(value = "/shop/insertshopMsgByStepTwo", method = RequestMethod.POST)
	@ResponseBody
	public BhResult insertshopMsg(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			String token = (String) request.getSession().getAttribute("token");// map.get("token");
			System.out.println("appid -- >" + token);
			MemberShop param = new MemberShop();
			param.setToken(token);
			List<MemberShop> memberShop1 = shopService.selectMemberShop(param);

			MemberShop memberShop = new MemberShop();

			String shopName = map.get("shopName");
			String licenseTypeName = map.get("licenseTypeName");// 营业执照类型
			String prov = map.get("prov");// 省
			String city = map.get("city");// 市
			String area = map.get("area");// 区
			String address = map.get("address");// 公司详细地址
			String four = map.get("four");// 第四级地址（如果有的话则四级地址的id,如果没有第四级则传0）
			String tel = map.get("tel");// 公司电话
			String emergencyContactPerson = map.get("ecPerson");// 公司紧急联系人
			String emergencyContactPhone = map.get("ecPhone");// 公司紧急联系人手机
			String cardType = map.get("cardType");// 法定代表人的证件类型
			String card = map.get("card");// 法定代表人证件号
			String beginTime = map.get("beginTime");// 营业期限（开始时间）
			String endTime = map.get("endTime");// 结束时间

			if (StringUtils.isNotEmpty(licenseTypeName)) {
				memberShop.setLicenseTypeName(licenseTypeName);
			}
			if (StringUtils.isNotEmpty(prov)) {
				if (RegExpValidatorUtils.IsNumber(prov)) {
					memberShop.setProv(Integer.parseInt(prov));
				}
			}
			if (StringUtils.isNotEmpty(city)) {
				if (RegExpValidatorUtils.IsNumber(city)) {
					memberShop.setCity(Integer.parseInt(city));
				}
			}
			if (StringUtils.isNotEmpty(area)) {
				if (RegExpValidatorUtils.IsNumber(area)) {
					memberShop.setArea(Integer.parseInt(area));
				}
			}
			if (StringUtils.isNotEmpty(four)) {
				if (RegExpValidatorUtils.IsNumber(four)) {
					memberShop.setFour(Integer.parseInt(four));
				}
			}
			if (StringUtils.isNotEmpty(address)) {
				memberShop.setAddress(address);
				try {
					Map<String, String> map1 = GetLatitude.getlnglat(address);// 根据用户地址获取经纬度
					memberShop.setLat(Double.parseDouble(map1.get("lat")));
					memberShop.setLon(Double.parseDouble(map1.get("lng")));
				} catch (Exception e) {
					// TODO: handle exception
				}

			}
			if (StringUtils.isNotEmpty(tel)) {
				memberShop.setTel(tel);
			}
			if (StringUtils.isNotEmpty(emergencyContactPerson)) {
				memberShop.setEmergencyContactPerson(emergencyContactPerson);
			}
			if (StringUtils.isNotEmpty(emergencyContactPhone)) {
				memberShop.setEmergencyContactPhone(emergencyContactPhone);
			}
			if (StringUtils.isNotEmpty(cardType)) {
				memberShop.setCardType(Integer.parseInt(cardType));
			}
			if (StringUtils.isNotEmpty(card)) {
				memberShop.setCard(card);
			}
			if (StringUtils.isNotEmpty(beginTime)) {
				memberShop.setBeginTime(beginTime);
			}
			if (StringUtils.isNotEmpty(endTime)) {
				memberShop.setEndTime(endTime);
			}
			if (StringUtils.isNotEmpty(shopName)) {
				memberShop.setShopName(shopName);
			}
			// step=2
			memberShop.setStep(3);
			memberShop.setmId(memberShop1.get(0).getmId());
			memberShop.setIsPc(1);
			memberShop.setToken(token);
			bhResult = shopService.updateMemberShop(memberShop);

		} catch (Exception e) {
			bhResult = BhResult.build(500, "操作失败!");
			e.printStackTrace();
		}
		return bhResult;
	}

	// 上传图片
	@RequestMapping(value = "/shop/updateImage", method = RequestMethod.POST)
	@ResponseBody
	public BhResult updateImage(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		// 获得身份证的类型
		try {
			String token = (String) request.getSession().getAttribute("token");// map.get("token");
			MemberShop param = new MemberShop();
			param.setToken(token);
			List<MemberShop> memberShop1 = shopService.selectMemberShop(param);
			String cardImage = map.get("cImage");// 法人证件照，请按顺序依次上传身份证正面图，身份证背面图，手持身份证图
			String businessLicense = map.get("bLicense");// 营业执照(图片)
			String licenseImage = map.get("bImage");// 银行开户许可证图片
			String organizationImage = map.get("oImage");// 组织机构代码证电子版
			String taxImage = map.get("taxImage");// 纳税资质图片上传
  
			MemberShop memberShop = new MemberShop();
			if (StringUtils.isEmpty(cardImage)) {
				bhResult = new BhResult(400, "法人证件照不能为空", null);
			} else if (StringUtils.isEmpty(businessLicense)) {
				bhResult = new BhResult(400, "营业执件不能为空", null);
			} else {
				memberShop.setCardImage(cardImage);
				memberShop.setBusinessLicense(businessLicense);
				if (StringUtils.isNotEmpty(licenseImage)) {
					memberShop.setLicenseImage(licenseImage);
				}
				if (StringUtils.isNotEmpty(organizationImage)) {
					memberShop.setOrganizationImage(organizationImage);
				}
				if (StringUtils.isNotEmpty(taxImage)) {
					memberShop.setTaxImage(taxImage);
				}
				memberShop.setmId(memberShop1.get(0).getmId());
				// step=4
				memberShop.setStep(4);
				bhResult = shopService.updateMemberShop(memberShop);
			}

		} catch (Exception e) {
			bhResult = BhResult.build(BhResultEnum.VISIT_FAIL);
			e.printStackTrace();
		}
		return bhResult;
	}

	/**
	 * CHENG-201801-05 绑定银行卡
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/shop/bindbank")
	@ResponseBody
	@Transactional
	public BhResult bindbank(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			String token = (String) request.getSession().getAttribute("token");// map.get("token");
			MemberShop param = new MemberShop();
			param.setToken(token);
			List<MemberShop> memberShop1 = shopService.selectMemberShop(param);
			System.out.println("appid -- >" + token + ",mId-->" + memberShop1.get(0).getmId());
			String fullName = map.get("fullName");// 真实姓名
			String identity = map.get("identity");// 身份证号
			String bankNum = map.get("bankNum");// 银行卡号
			String bankType = map.get("bankType");// 用户的身份证号
			String tel = map.get("tel");
			BankCard input = new BankCard();
			input.setName(fullName);
			input.setPhoneNo(tel);
			input.setCardNo(bankNum);
			input.setIdNo(identity);
			int size = 0;
			if (memberShop1 == null) {
				size = 0;
			} else {
				size = memberShop1.size();
			}

			BankCard ret = AliMarketUtil.verifyBankCard(input);
			if (ret == null) {
				bhResult = new BhResult(400, "认证失败", null);
			} else {
				System.out.println("ret result-->" + ret.getRespMessage());
				// 如果=0000,在认证成功
				if (ret.getRespCode().equals("0000")) {
					BusBankCard card = new BusBankCard();
					card.setRealName(ret.getName());// 真实姓名
					card.setIdNo(ret.getIdNo());// 身份证号
					card.setBankCardNo(ret.getCardNo());// 银行卡号
					card.setBankName(ret.getBankName());// 银行名称
					card.setPhone(ret.getPhoneNo());// 手机号
					card.setmId(memberShop1.get(0).getmId());// 用户的id
					card.setBankCode(ret.getBankCode());
					card.setBankKind(ret.getBankKind());
					card.setBankType(ret.getBankType());
					int row = shopService.insertBankCard(card);
					if (row == 1) {
						MemberShop memberShop = new MemberShop();
						// step=4
						memberShop.setmId(memberShop1.get(0).getmId());
						memberShop.setStep(5);
						bhResult = shopService.updateMemberShop(memberShop);
						bhResult = new BhResult(200, "绑定银行卡成功", null);
					} else if (row == 0) {
						bhResult = new BhResult(BhResultEnum.FAIL, null);
					} else if (row == 2) {
						bhResult = new BhResult(400, "该卡已存在，请勿重复提交", null);
					}
				} else {
					bhResult = new BhResult(400, ret.getRespMessage(), null);
				}

			}

		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return bhResult;
	}

	/**
	 * CHENG-201801-05 用户是否同意滨惠商家入驻的条款（移动端）
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/shop/isAgree")
	@ResponseBody
	public BhResult isAgree(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			String token = (String) request.getSession().getAttribute("token");// map.get("token");
			MemberShop param = new MemberShop();
			param.setToken(token);
			List<MemberShop> memberShop1 = shopService.selectMemberShop(param);
			String isAgree = map.get("isAgree");// 是否同意：0不同意，1同意

			if (RegExpValidatorUtils.IsNumber(isAgree)) {
				if (isAgree.equals("1")) {
					MemberShop memberShop = new MemberShop();
					memberShop.setIsAgree(Integer.parseInt(isAgree));// 是否同意：0不同意，1同意
					memberShop.setmId(memberShop1.get(0).getmId());
					// step=5
					memberShop.setStep(6);
					bhResult = shopService.updateMemberShop(memberShop);
					if (bhResult.getStatus() == 200) {
						MemberShop me = new MemberShop();
						me.setStep(6);
						me.setmId(memberShop1.get(0).getmId());
						shopService.updateStep(me);
						bhResult = new BhResult(200, "操作成功", null);
					}
				} else if (isAgree.equals("0")) {
					bhResult = new BhResult(400, "请同意滨惠条款", null);
				}
			} else {
				bhResult = new BhResult(400, "isAgree不是数字", null);
			}

		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return bhResult;
	}

	/**-
	 * CHENG-201712-09-01 回显银行卡的类型
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/banktype")
	@ResponseBody
	public BhResult bankType(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {

			String bankNum = map.get("bankNum");
			if (bankNum == null || bankNum.length() < 6 || bankNum.length() > 19) {
				result = new BhResult(400, "银行卡查询失败", null);
			} else {
				String type = BankUtil.getNameOfBank(bankNum);
				result = new BhResult(200, "查询成功", type);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * CHENG-201801-05 通过token查询目前已经提交资料到哪一步
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/shop/selectStep1")
	@ResponseBody
	public BhResult selectStep1(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			String token = map.get("openid");
			MemberShop param = new MemberShop();
			param.setToken(token);
			List<MemberShop> memberShop1 = shopService.selectMemberShop(param);
			//
			HttpSession sesson = request.getSession();
			if (StringUtils.isNotEmpty(token)) {
				sesson.setAttribute("token", token);
			}

			ShopMsg msg = new ShopMsg();
			msg.setStep(-1);
			// 0不需要支付pos押金，1需要支付pos押金
			msg.setIsNeedPay(0);
			if (memberShop1.size() < 1) {
				MemberShop memberShop = new MemberShop();
				memberShop.setToken(token);
				memberShop.setStep(-1);
				memberShop.setAddtime(new Date());
				shopService.insertMemberShopByStepOne(memberShop);
				msg.setStep(-1);
				bhResult = new BhResult(200, "该用户未有入驻信息", msg);
			} else {
				System.out.println("appid -- >" + token + ",mId-->" + memberShop1.get(0).getmId());
				MemberShop memShop = memberShop1.get(0);
				Integer step = memShop.getStep();
				if (step == -1) {
					msg.setStep(-1);
					bhResult = new BhResult(200, "该用户未有入驻信息", msg);
				} else if (step == 0) {
					msg.setStep(0);
					msg.setErrNote("pos正在申请中");
					bhResult = new BhResult(200, "pos正在申请中", msg);
				} else if (step == 1) {
					if ((memShop.getExistPos().equals(3)) && (memShop.getHandleStatus().equals(0))) {
						msg.setStep(0);
						msg.setErrNote("pos正在申请中");
						bhResult = new BhResult(200, "pos正在申请中", msg);
					} else if ((memShop.getExistPos().equals(3)) && (memShop.getHandleStatus().equals(1))) {
						msg.setStep(-1);
						msg.setErrNote("该用户未有入驻信息");
						bhResult = new BhResult(200, "该用户未有入驻信息", msg);
					} else {
						msg.setStep(1);
						bhResult = new BhResult(200, "信息未完善", msg);
					}
				} else if (step == 2) {
					msg.setStep(step);
					bhResult = new BhResult(200, "信息未完善", msg);
				} else if (step == 3) {
					msg.setStep(step);
					bhResult = new BhResult(200, "信息未完善", msg);
				} else if (step == 4) {
					msg.setStep(step);
					bhResult = new BhResult(200, "信息未完善", msg);
				} else if (step == 5) {
					msg.setStep(step);
					bhResult = new BhResult(200, "信息未完善", msg);
				} else if (step == 6) {
					msg.setStep(6);
					bhResult = new BhResult(200, "商家入驻信息 审核中", msg);
				} else if (step == 7) {
					// 是否存在pos机，1有,2无(需要提交押金，可退回押金)，3无(不需要提交押金)
					if (memShop.getExistPos().equals(2)) {
						// 支付状态：1未支付，2已支付
						if (memShop.getPayStatus().equals(1)) {
							msg.setIsNeedPay(1);
							msg.setStep(7);
							bhResult = new BhResult(200, "商家入住成功但是未支付", msg);
						} else if (memShop.getPayStatus().equals(2)) {
							msg.setStep(10);
							msg.setIsNeedPay(0);
							bhResult = new BhResult(200, "商家入住成功且已支付", msg);
						} else {
							msg.setIsNeedPay(1);
							msg.setStep(7);
							bhResult = new BhResult(200, "商家入住成功但是未支付", msg);
						}
					} else {
						msg.setStep(7);
						bhResult = new BhResult(200, "商家入驻成功", msg);
					}
				} else if (step == 8) {
					msg.setStep(8);
					msg.setErrNote(memShop.getNote());
					bhResult = new BhResult(200, "商家入驻的信息 审核不通过", msg);
				} else {
					msg.setStep(-1);
					bhResult = new BhResult(200, "该用户未有入驻信息", msg);
				}
			}
		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return bhResult;
	}

	/**
	 * CHENG-201801-05 重新提交
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/shop/recommit")
	@ResponseBody
	public BhResult recommit(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			String token = map.get("openid");// map.get("token");

			HttpSession sesson = request.getSession();
			if (StringUtils.isNotEmpty(token)) {
				sesson.setAttribute("token", token);
			}
			MemberShop param = new MemberShop();
			param.setToken(token);
			List<MemberShop> memberShop1 = shopService.selectMemberShop(param);
			ShopMsg msg = new ShopMsg();
			if (memberShop1.size() < 1) {
				msg.setStep(1);
				bhResult = new BhResult(200, "该用户未有入驻信息", msg);
			} else {
				MemberShop memShop = memberShop1.get(0);
				MemberShop memberShop = new MemberShop();
				memberShop.setStep(1);
				memberShop.setmId(memShop.getmId());
				memberShop.setNote("");
				memberShop.setLinkmanPhone("");
				shopService.updateStep(memberShop);
				bhResult = new BhResult(200, "请调到重新注册页面", msg);
			}

		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return bhResult;
	}

	/************************************************************* pc端 ***********************************************************/
	// 商家入驻-入驻联系人信息
	@RequestMapping(value = "/shop/checkStepOne", method = RequestMethod.POST)
	@ResponseBody
	public BhResult checkStepOne(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			String linkmanName = map.get("linkmanName");// 联系人姓名
			String linkmanPhone = map.get("linkmanPhone");// 联系人电话
			String linkmanEmail = map.get("linkmanEmail");// 联系人电子邮箱

			if (StringUtils.isEmpty(linkmanName)) {
				bhResult = new BhResult(400, "联系人姓名不能为空", null);
			} else if (StringUtils.isEmpty(linkmanPhone)) {
				bhResult = new BhResult(400, "联系人电话不能为空", null);
			} else if (StringUtils.isEmpty(linkmanEmail)) {
				bhResult = new BhResult(400, "联系人电子邮箱不能为空", null);
			} else {

				MemberShop memberShop = new MemberShop();
				memberShop.setLinkmanName(linkmanName);
				memberShop.setLinkmanPhone(linkmanPhone);
				memberShop.setLinkmanEmail(linkmanEmail);
				bhResult = shopService.insertMemberShop(memberShop);
			}

		} catch (Exception e) {
			bhResult = BhResult.build(500, "操作失败!");
			e.printStackTrace();
		}
		return bhResult;
	}

	// 验证公司名称和营业执照号是否存在
	@RequestMapping(value = "/shop/checkStepTwo", method = RequestMethod.POST)
	@ResponseBody
	public BhResult checkStepTwo(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			MemberShop memberShop = new MemberShop();

			String shopName = map.get("shopName");// 公司名称
			String licenseNumber = map.get("licenseNumber");// 营业执照注册号

		} catch (Exception e) {
			bhResult = BhResult.build(500, "操作失败!");
			e.printStackTrace();
		}
		return bhResult;
	}

	/**
	 * CHENG-2018验证银行卡
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/shop/checkbank")
	@ResponseBody
	public BhResult checkbank(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {

			String fullName = map.get("fullName");// 真实姓名
			String identity = map.get("identity");// 身份证号
			String bankNum = map.get("bankNum");// 银行卡号
			String bankType = map.get("bankType");// 用户的身份证号
			String tel = map.get("tel");
			BankCard input = new BankCard();
			input.setName(fullName);
			input.setPhoneNo(tel);
			input.setCardNo(bankNum);
			input.setIdNo(identity);

			BankCard ret = AliMarketUtil.verifyBankCard(input);
			if (ret == null) {
				bhResult = new BhResult(400, "认证失败", null);
			} else {
				System.out.println("ret result-->" + ret.getRespMessage());
				// 如果=0000,在认证成功
				if (ret.getRespCode().equals("0000")) {
					bhResult = new BhResult(200, "验证成功,可提交商家入驻的资料", null);
				} else {
					bhResult = new BhResult(400, ret.getRespMessage(), null);
				}

			}

		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return bhResult;
	}

	// pc端
	@RequestMapping("/shop/addshopbypc")
	@ResponseBody
	@Transactional
	public BhResult addshopbypc(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			MemberShop memberShop = new MemberShop();

			// 是否存在pos机，1有,2无，3无
			String existPos = map.get("existPos");
			if (StringUtils.isNotEmpty(existPos)) {
				memberShop.setExistPos(Integer.parseInt(existPos));
			}
			// 用户存放pos机的信息,如果exist_pos=1话该字段存商户号，商户名称并以逗号隔开；
			// 如果exit_pos=2的话存姓名，身份证，银行卡号，手机号，
			// 如果exist_pos=3的话存姓名，联系号码
			String posMsg = map.get("posMsg");
			if (StringUtils.isNotEmpty(posMsg)) {
				memberShop.setPosMsg(posMsg);
			}

			String linkmanName = map.get("linkmanName");// 联系人姓名
			if (StringUtils.isNotEmpty(linkmanName)) {
				memberShop.setLinkmanName(linkmanName);
			}

			String linkmanPhone = map.get("linkmanPhone");// 联系人电话
			if (StringUtils.isNotEmpty(linkmanPhone)) {
				memberShop.setLinkmanPhone(linkmanPhone);
			}

			String linkmanEmail = map.get("linkmanEmail");// 联系人电子邮箱
			if (StringUtils.isNotEmpty(linkmanEmail)) {
				memberShop.setLinkmanEmail(linkmanEmail);
			}

			String isInvite = map.get("isInvite");// 是否接受其他商场邀请0否，1是
			if (StringUtils.isNotEmpty(isInvite)) {
				boolean flag = RegExpValidatorUtils.IsNumber(isInvite);
				if (flag) {
					memberShop.setIsinvite(Integer.parseInt(isInvite));
				} else {
					bhResult = new BhResult(400, "isInvite不是数字", null);
				}

			}

			String code = map.get("code");// 如果接受了其他商场的邀请则传推荐商家的id,如果没有则传0
			if (StringUtils.isNotEmpty(code)) {
				memberShop.setCode(code);
			}

			String licenseTypeName = map.get("licenseTypeName");// 营业执照类型
			if (StringUtils.isNotEmpty(licenseTypeName)) {
				memberShop.setLicenseTypeName(licenseTypeName);
			}
			String shopName = map.get("shopName");// 公司名称
			if (StringUtils.isNotEmpty(shopName)) {
				memberShop.setShopName(shopName);
			}
			String licenseNumber = map.get("licenseNumber");// 营业执照注册号
			if (StringUtils.isNotEmpty(licenseNumber)) {
				memberShop.setLicenseNumber(licenseNumber);
			}
			String cardName = map.get("cardName");// 法定代表人姓名
			if (StringUtils.isNotEmpty(cardName)) {
				memberShop.setCardName(cardName);
			}
			String licenseProv = map.get("licenseProv");// 营业执照所在地的 省
			if (StringUtils.isNotEmpty(licenseProv)) {
				// 如果是数字则添加，否则会报错
				if (RegExpValidatorUtils.IsNumber(licenseProv)) {
					memberShop.setLicenseProv(Integer.parseInt(licenseProv));

				}
			}
			String licenseCity = map.get("licenseCity");// 市
			if (StringUtils.isNotEmpty(licenseCity)) {
				if (RegExpValidatorUtils.IsNumber(licenseCity)) {
					memberShop.setLicenseCity(Integer.parseInt(licenseCity));
				}
			}
			String licenseTown = map.get("licenseTown");// 区
			if (StringUtils.isNotEmpty(licenseTown)) {
				if (RegExpValidatorUtils.IsNumber(licenseTown)) {
					memberShop.setLicenseTown(Integer.parseInt(licenseTown));
				}
			}
			String licenseAddress = map.get("licenseAddress");// 营业执照 的详细地址
			if (StringUtils.isNotEmpty(licenseAddress)) {
				memberShop.setLicenseAddress(licenseAddress);
			}
			String founddate = map.get("founddate");// 公司成立日期
			if (StringUtils.isNotEmpty(founddate)) {
				memberShop.setFounddate(founddate);
			}
			String beginTime = map.get("beginTime");// 营业期限(开始日期)
			if (StringUtils.isNotEmpty(beginTime)) {
				memberShop.setBeginTime(beginTime);
			}
			String endTime = map.get("endTime"); // 营业期限(结束日期)
			if (StringUtils.isNotEmpty(endTime)) {
				memberShop.setEndTime(endTime);
			}
			String regMoney = map.get("regMoney");// 公司注册资本
			if (StringUtils.isNotEmpty(regMoney)) {
				memberShop.setRegMoney(regMoney);
			}
			String busScope = map.get("busScope");// 公司经营范围
			if (StringUtils.isNotEmpty(busScope)) {
				memberShop.setBusScope(busScope);
			}

			// 公司所在地
			String prov = map.get("prov");// 省
			if (StringUtils.isNotEmpty(prov)) {
				if (RegExpValidatorUtils.IsNumber(prov)) {
					memberShop.setProv(Integer.parseInt(prov));
				}
			}
			String city = map.get("city");// 市
			if (StringUtils.isNotEmpty(city)) {
				if (RegExpValidatorUtils.IsNumber(city)) {
					memberShop.setCity(Integer.parseInt(city));
				}
			}
			String area = map.get("area");// 区
			if (StringUtils.isNotEmpty(area)) {
				if (RegExpValidatorUtils.IsNumber(area)) {
					memberShop.setArea(Integer.parseInt(area));
				}
			}
			// 密码a123456789a
			String address = map.get("address");// 公司详细地址
			if (StringUtils.isNotEmpty(address)) {
				memberShop.setAddress(address);
				try {
					Map<String, String> map1 = GetLatitude.getlnglat(address);// 根据用户地址获取经纬度
					memberShop.setLat(Double.parseDouble(map1.get("lat")));
					memberShop.setLon(Double.parseDouble(map1.get("lng")));
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}

			String tel = map.get("tel");// 公司电话
			if (StringUtils.isNotEmpty(tel)) {
				// 不用判断是否是数字，因为是固定电话有
				memberShop.setTel(tel);

			}
			String emergencyContactPerson = map.get("ecPerson");// 公司紧急联系人
			if (StringUtils.isNotEmpty(emergencyContactPerson)) {
				memberShop.setEmergencyContactPerson(emergencyContactPerson);
			}
			String emergencyContactPhone = map.get("ecPhone");// 公司紧急联系人手机
			if (StringUtils.isNotEmpty(emergencyContactPhone)) {
				memberShop.setEmergencyContactPhone(emergencyContactPhone);
			}
			String cardType = map.get("cardType");// 法定代表人的证件类型
			if (StringUtils.isNotEmpty(cardType)) {
				memberShop.setCardType(Integer.parseInt(cardType));
			}
			String card = map.get("card");// 法定代表人证件号
			if (StringUtils.isNotEmpty(card)) {
				memberShop.setCard(card);
			}
			String cardBegintime = map.get("cardBegintime");// 法定代表人证件的有效期（开始时间）
			if (StringUtils.isNotEmpty(cardBegintime)) {
				memberShop.setCardBegintime(cardBegintime);
			}
			String cardEndtime = map.get("cardEndtime");// 法定代表人证件的有效期（结束时间）\
			if (StringUtils.isNotEmpty(cardEndtime)) {
				memberShop.setCardEndtime(cardEndtime);
			}

			String cardImage = map.get("cImage");// 法人证件照，请按顺序依次上传身份证正面图，身份证背面图，手持身份证图
			if (StringUtils.isNotEmpty(cardImage)) {
				memberShop.setCardImage(cardImage);
			}
			String businessLicense = map.get("bLicense");// 营业执照电子版（图片）
			if (StringUtils.isNotEmpty(businessLicense)) {
				memberShop.setBusinessLicense(businessLicense);
			}
			String licenseImage = map.get("bImage");// 银行开户许可证图片
			if (StringUtils.isNotEmpty(licenseImage)) {
				memberShop.setLicenseImage(licenseImage);
			}
			String organizationCode = map.get("oCode");// 组织机构代码
			if (StringUtils.isNotEmpty(organizationCode)) {
				memberShop.setOrganizationCode(organizationCode);
			}
			String orgBegintime = map.get("oBegintime");
			if (StringUtils.isNotEmpty(orgBegintime)) {
				memberShop.setOrgBegintime(orgBegintime);
			}
			String orgEndtime = map.get("oEndtime");
			if (StringUtils.isNotEmpty(orgEndtime)) {
				memberShop.setOrganizationEndTime(orgEndtime);
			}
			String organizationImage = map.get("oImage");// 组织机构代码证电子版
			if (StringUtils.isNotEmpty(organizationImage)) {
				memberShop.setOrganizationImage(organizationImage);
			}

			String taxCode = map.get("taxCode");// 纳税人识别号
			if (StringUtils.isNotEmpty(taxCode)) {
				memberShop.setTaxCode(taxCode);
			}
			String taxType = map.get("taxType");// 纳税人类型
			if (StringUtils.isNotEmpty(taxType)) {
				memberShop.setTaxType(Integer.parseInt(taxType));
			}
			String taxOtherCode = map.get("taxOtherCode");// 纳税类型税码
			if (StringUtils.isNotEmpty(taxOtherCode)) {
				memberShop.setTaxCode(taxOtherCode);
			}
			String taxImage = map.get("taxImage");// 纳税资质图片上传
			if (StringUtils.isNotEmpty(taxImage)) {
				memberShop.setTaxImage(taxImage);
			}
			memberShop.setStep(6);
			memberShop.setIsPc(0);
			memberShop.setIsAgree(1);
			bhResult = new BhResult(200, "", null);
			if (bhResult.getStatus() == 200) {
				// 银行卡的入库
				String fullName = map.get("fullName");// 真实姓名
				String identity = map.get("identity");// 身份证号
				String bankNum = map.get("bankNum");// 银行卡号
				String bankType = map.get("bankType");// 银行卡类型
				String bankphone = map.get("bankphone");
				BankCard input = new BankCard();
				input.setName(fullName);
				input.setPhoneNo(bankphone);
				input.setCardNo(bankNum);
				input.setIdNo(identity);
				BankCard ret = AliMarketUtil.verifyBankCard(input);
				if (ret == null) {
					bhResult = new BhResult(400, "认证失败", null);
				} else {
					System.out.println("ret result-->" + ret.getRespMessage());
					// 如果=0000,在认证成功
					if (ret.getRespCode().equals("0000")) {
						bhResult = shopService.insertStepByPc(memberShop);
						if (bhResult.getStatus() == 200) {
							BusBankCard bankCard = new BusBankCard();
							bankCard.setRealName(ret.getName());// 真实姓名
							bankCard.setIdNo(ret.getIdNo());// 身份证号
							bankCard.setBankCardNo(ret.getCardNo());// 银行卡号
							bankCard.setBankName(ret.getBankName());// 银行名称
							bankCard.setPhone(ret.getPhoneNo());// 手机号
							bankCard.setmId((Integer) bhResult.getData());// 用户的id
							bankCard.setBankCode(ret.getBankCode());
							bankCard.setBankKind(ret.getBankKind());
							bankCard.setBankType(ret.getBankType());
							int row = shopService.insertBankCard(bankCard);
							if (row == 1) {
								bhResult = new BhResult(200, "商家注册成功", null);
							} else if (row == 0) {
								bhResult = new BhResult(BhResultEnum.FAIL, null);
							} else if (row == 2) {
								bhResult = new BhResult(400, "该卡已存在，请勿重复提交", null);
							}
						}
					} else {
						bhResult = new BhResult(400, ret.getRespMessage(), null);
					}

				}
			}

		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return bhResult;
	}

	/**
	 * CHENG-2018商家列表
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/shop/shoplist")
	@ResponseBody
	public BhResult shopList(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {

			String page = map.get("page");// 第几页

			if (StringUtils.isEmpty(page)) {
				page = "1";
			}
			String mId = map.get("mId");// 商家的id
			String shopName = map.get("shopName");
			String tel = map.get("tel");
			String step = map.get("step");

			MemberShop memberShop = new MemberShop();
			if (StringUtils.isNotEmpty(mId)) {
				memberShop.setmId(Integer.parseInt(mId));
			}
			if (StringUtils.isNotEmpty(shopName)) {
				memberShop.setShopName(shopName);
			}
			if (StringUtils.isNotEmpty(tel)) {
				memberShop.setTel(tel);
			}
			if (StringUtils.isNotEmpty(step)) {
				memberShop.setStep(Integer.parseInt(step));
			}
			PageBean<MemberShop> list = shopService.selectShopList(memberShop, Integer.parseInt(page), 10);
			bhResult = new BhResult(200, "查询成功", list);
		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return bhResult;
	}

	public void sendEmail(String email, HttpServletRequest request, Integer step, Integer mId) {
		try {
			String str = "";
			if (step == 6) {
				str = "正在审核中";
			} else if (step == 7) {
				String username = shopService.selectUsernameBymId(mId);
				str = "审核通过,请登录商家后台管理系统 "+Contants.BIN_HUI_URL+"/MallBackstage/index.html ,用户名是" + username
						+ ",初始密码123456,登录后请尽快修改密码";
			} else if (step == 8) {
				str = " 审核不通过,请重新提交信息";
			}
			if (RegExpValidatorUtils.isEmail(email)) {
				Properties props = new Properties();
				props.setProperty("mail.host", "smtp.mxhichina.com");

				props.setProperty("mail.smtp.auth", "true");
				Session session = Session.getDefaultInstance(props, new Authenticator() {

					@Override
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication("chengfengyun@zhiyesoft.com", "cheng18877827237*");
					}

				});
				// 打开debug:看到整个邮件发送的过程
				session.setDebug(false);
				// 2.写邮件
				MimeMessage mail = new MimeMessage(session);
				// 发件人
				mail.setFrom(new InternetAddress("chengfengyun@zhiyesoft.com"));
				// 收件人
				// 参数一：收件类型
				// TO: 收件人
				// CC: 抄送人
				// BCC: 密送人
				// A->B(TO) C(CC) D(BCC)
				// 参数二：收件人地址
				mail.setRecipient(RecipientType.TO, new InternetAddress(email));
				// user.setValidateCode(MD5Util.encode2hex(email));
				String path = request.getContextPath(); // 得到当前工程的根路径，代码如下
				StringBuffer sb = new StringBuffer();
				sb.append("尊敬的滨惠  用户：</br>" +

				"您好！</br>" +

				"您的入驻信息 " + str + " </br>");
				String code = MixCodeUtil.getCode(6);
				sb.append(
						"<strong>" + " <div style='text-align:center;font-size:30px'>" + "</div>" + "</strong> </br>");
				sb.append("此致</br> " + "滨惠 开发团队敬上</br>");
				sb.append("此电子邮件地址无法接收回复。如需更多信息，请访问"+Contants.BIN_HUI_URL+"/binhui/home 帮助中心。");
				// 主题
				mail.setSubject("滨惠  邮件");

				// 正文
				// 参数一：内容
				// 参数二：指定内容类型"<font color='red' size='+6'>这是邮件的正文3</font><br/>"
				mail.setContent(sb.toString(), "text/html;charset=utf-8");
				// 3.发送
				Transport.send(mail);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// 商城后台
	@RequestMapping("/shop/addshopbypt")
	@ResponseBody
	public BhResult addshopbypt(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			MemberShop memberShop = new MemberShop();

			// 是否存在pos机，1有,2无，3无
			String existPos = map.get("existPos");
			if (StringUtils.isNotEmpty(existPos)) {
				memberShop.setExistPos(Integer.parseInt(existPos));
			}
			// 用户存放pos机的信息,如果exist_pos=1话该字段存商户号，商户名称并以逗号隔开；
			// 如果exit_pos=2的话存姓名，身份证，银行卡号，手机号，
			// 如果exist_pos=3的话存姓名，联系号码
			String posMsg = map.get("posMsg");
			if (StringUtils.isNotEmpty(posMsg)) {
				memberShop.setPosMsg(posMsg);
			}

			String linkmanName = map.get("linkmanName");// 联系人姓名
			if (StringUtils.isNotEmpty(linkmanName)) {
				memberShop.setLinkmanName(linkmanName);
			}

			String linkmanPhone = map.get("linkmanPhone");// 联系人电话
			if (StringUtils.isNotEmpty(linkmanPhone)) {
				memberShop.setLinkmanPhone(linkmanPhone);
			}

			String linkmanEmail = map.get("linkmanEmail");// 联系人电子邮箱
			if (StringUtils.isNotEmpty(linkmanEmail)) {
				memberShop.setLinkmanEmail(linkmanEmail);
			}

			String isInvite = map.get("isInvite");// 是否接受其他商场邀请0否，1是
			if (StringUtils.isNotEmpty(isInvite)) {
				boolean flag = RegExpValidatorUtils.IsNumber(isInvite);
				if (flag) {
					memberShop.setIsinvite(Integer.parseInt(isInvite));
				} else {
					bhResult = new BhResult(400, "isInvite不是数字", null);
				}

			}

			String code = map.get("code");// 如果接受了其他商场的邀请则传推荐商家的id,如果没有则传0
			if (StringUtils.isNotEmpty(code)) {
				memberShop.setCode(code);
			}

			String licenseTypeName = map.get("licenseTypeName");// 营业执照类型
			if (StringUtils.isNotEmpty(licenseTypeName)) {
				memberShop.setLicenseTypeName(licenseTypeName);
			}
			String shopName = map.get("shopName");// 公司名称
			if (StringUtils.isNotEmpty(shopName)) {
				memberShop.setShopName(shopName);
			}
			String licenseNumber = map.get("licenseNumber");// 营业执照注册号
			if (StringUtils.isNotEmpty(licenseNumber)) {
				memberShop.setLicenseNumber(licenseNumber);
			}
			String cardName = map.get("cardName");// 法定代表人姓名
			if (StringUtils.isNotEmpty(cardName)) {
				memberShop.setCardName(cardName);
			}
			String licenseProv = map.get("licenseProv");// 营业执照所在地的 省
			if (StringUtils.isNotEmpty(licenseProv)) {
				// 如果是数字则添加，否则会报错
				if (RegExpValidatorUtils.IsNumber(licenseProv)) {
					memberShop.setLicenseProv(Integer.parseInt(licenseProv));

				}
			}
			String licenseCity = map.get("licenseCity");// 市
			if (StringUtils.isNotEmpty(licenseCity)) {
				if (RegExpValidatorUtils.IsNumber(licenseCity)) {
					memberShop.setLicenseCity(Integer.parseInt(licenseCity));
				}
			}
			String licenseTown = map.get("licenseTown");// 区
			if (StringUtils.isNotEmpty(licenseTown)) {
				if (RegExpValidatorUtils.IsNumber(licenseTown)) {
					memberShop.setLicenseTown(Integer.parseInt(licenseTown));
				}
			}
			String licenseAddress = map.get("licenseAddress");// 营业执照 的详细地址
			if (StringUtils.isNotEmpty(licenseAddress)) {
				memberShop.setLicenseAddress(licenseAddress);
			}
			String founddate = map.get("founddate");// 公司成立日期
			if (StringUtils.isNotEmpty(founddate)) {
				memberShop.setFounddate(founddate);
			}
			String beginTime = map.get("beginTime");// 营业期限(开始日期)
			if (StringUtils.isNotEmpty(beginTime)) {
				memberShop.setBeginTime(beginTime);
			}
			String endTime = map.get("endTime"); // 营业期限(结束日期)
			if (StringUtils.isNotEmpty(endTime)) {
				memberShop.setEndTime(endTime);
			}
			String regMoney = map.get("regMoney");// 公司注册资本
			if (StringUtils.isNotEmpty(regMoney)) {
				memberShop.setRegMoney(regMoney);
			}
			String busScope = map.get("busScope");// 公司经营范围
			if (StringUtils.isNotEmpty(busScope)) {
				memberShop.setBusScope(busScope);
			}

			// 公司所在地
			String prov = map.get("prov");// 省
			if (StringUtils.isNotEmpty(prov)) {
				if (RegExpValidatorUtils.IsNumber(prov)) {
					memberShop.setProv(Integer.parseInt(prov));
				}
			}
			String city = map.get("city");// 市
			if (StringUtils.isNotEmpty(city)) {
				if (RegExpValidatorUtils.IsNumber(city)) {
					memberShop.setCity(Integer.parseInt(city));
				}
			}
			String area = map.get("area");// 区
			if (StringUtils.isNotEmpty(area)) {
				if (RegExpValidatorUtils.IsNumber(area)) {
					memberShop.setArea(Integer.parseInt(area));
				}
			}
			// 密码a123456789a
			String address = map.get("address");// 公司详细地址
			if (StringUtils.isNotEmpty(address)) {
				memberShop.setAddress(address);
				try {
					Map<String, String> map1 = GetLatitude.getlnglat(address);// 根据用户地址获取经纬度
					memberShop.setLat(Double.parseDouble(map1.get("lat")));
					memberShop.setLon(Double.parseDouble(map1.get("lng")));
				} catch (Exception e) {
					// TODO: handle exception
					//e.printStackTrace();
					System.out.println("the addshopbypt method test----------------> in add(lat、lng) is error,the param address is:" + address);
				}
			}

			String tel = map.get("tel");// 公司电话
			if (StringUtils.isNotEmpty(tel)) {
				// 不用判断是否是数字，因为是固定电话有
				memberShop.setTel(tel);

			}
			String emergencyContactPerson = map.get("ecPerson");// 公司紧急联系人
			if (StringUtils.isNotEmpty(emergencyContactPerson)) {
				memberShop.setEmergencyContactPerson(emergencyContactPerson);
			}
			String emergencyContactPhone = map.get("ecPhone");// 公司紧急联系人手机
			if (StringUtils.isNotEmpty(emergencyContactPhone)) {
				memberShop.setEmergencyContactPhone(emergencyContactPhone);
			}
			String cardType = map.get("cardType");// 法定代表人的证件类型
			if (StringUtils.isNotEmpty(cardType)) {
				memberShop.setCardType(Integer.parseInt(cardType));
			}
			String card = map.get("card");// 法定代表人证件号
			if (StringUtils.isNotEmpty(card)) {
				memberShop.setCard(card);
			}
			String cardBegintime = map.get("cardBegintime");// 法定代表人证件的有效期（开始时间）
			if (StringUtils.isNotEmpty(cardBegintime)) {
				memberShop.setCardBegintime(cardBegintime);
			}
			String cardEndtime = map.get("cardEndtime");// 法定代表人证件的有效期（结束时间）\
			if (StringUtils.isNotEmpty(cardEndtime)) {
				memberShop.setCardEndtime(cardEndtime);
			}

			String cardImage = map.get("cImage");// 法人证件照，请按顺序依次上传身份证正面图，身份证背面图，手持身份证图
			if (StringUtils.isNotEmpty(cardImage)) {
				memberShop.setCardImage(cardImage);
			}
			String businessLicense = map.get("bLicense");// 营业执照电子版（图片）
			if (StringUtils.isNotEmpty(businessLicense)) {
				memberShop.setBusinessLicense(businessLicense);
			}
			String licenseImage = map.get("bImage");// 银行开户许可证图片
			if (StringUtils.isNotEmpty(licenseImage)) {
				memberShop.setLicenseImage(licenseImage);
			}
			String organizationCode = map.get("oCode");// 组织机构代码
			if (StringUtils.isNotEmpty(organizationCode)) {
				memberShop.setOrganizationCode(organizationCode);
			}
			String orgBegintime = map.get("oBegintime");
			if (StringUtils.isNotEmpty(orgBegintime)) {
				memberShop.setOrgBegintime(orgBegintime);
			}
			String orgEndtime = map.get("oEndtime");
			if (StringUtils.isNotEmpty(orgEndtime)) {
				memberShop.setOrganizationEndTime(orgEndtime);
			}
			String organizationImage = map.get("oImage");// 组织机构代码证电子版
			if (StringUtils.isNotEmpty(organizationImage)) {
				memberShop.setOrganizationImage(organizationImage);
			}

			String taxCode = map.get("taxCode");// 纳税人识别号
			if (StringUtils.isNotEmpty(taxCode)) {
				memberShop.setTaxCode(taxCode);
			}
			String taxType = map.get("taxType");// 纳税人类型
			if (StringUtils.isNotEmpty(taxType)) {
				memberShop.setTaxType(Integer.parseInt(taxType));
			}
			String taxOtherCode = map.get("taxOtherCode");// 纳税类型税码
			if (StringUtils.isNotEmpty(taxOtherCode)) {
				memberShop.setTaxCode(taxOtherCode);
			}
			String taxImage = map.get("taxImage");// 纳税资质图片上传
			if (StringUtils.isNotEmpty(taxImage)) {
				memberShop.setTaxImage(taxImage);
			}
			memberShop.setStep(6);
			memberShop.setIsPc(0);
			memberShop.setIsAgree(1);
			bhResult = new BhResult(200, "", null);
			if (bhResult.getStatus() == 200) {
				// 银行卡的入库
				String fullName = map.get("fullName");// 真实姓名
				String identity = map.get("identity");// 身份证号
				String bankNum = map.get("bankNum");// 银行卡号
				String bankType = map.get("bankType");// 银行卡类型
				String bankphone = map.get("bankphone");
				BankCard input = new BankCard();
				input.setName(fullName);
				input.setPhoneNo(bankphone);
				input.setCardNo(bankNum);
				input.setIdNo(identity);
				BankCard ret = AliMarketUtil.verifyBankCard(input);
				if (ret == null) {
					bhResult = new BhResult(400, "认证失败", null);
				} else {
					System.out.println("ret result-->" + ret.getRespMessage());
					// 如果=0000,在认证成功
					if (ret.getRespCode().equals("0000")) {
						BhResult bhResult2 = shopService.insertStepByPc(memberShop);
						if (bhResult2.getStatus() == 200) {
							BusBankCard bankCard = new BusBankCard();
							bankCard.setRealName(ret.getName());// 真实姓名
							bankCard.setIdNo(ret.getIdNo());// 身份证号
							bankCard.setBankCardNo(ret.getCardNo());// 银行卡号
							bankCard.setBankName(ret.getBankName());// 银行名称
							bankCard.setPhone(ret.getPhoneNo());// 手机号
							bankCard.setmId((Integer) bhResult2.getData());// 用户的id
							bankCard.setBankCode(ret.getBankCode());
							bankCard.setBankKind(ret.getBankKind());
							bankCard.setBankType(ret.getBankType());
							int row = shopService.insertBankCard(bankCard);
							if (row == 1) {
								bhResult = new BhResult(200, "商家注册成功", null);
							} else if (row == 0) {
								bhResult = new BhResult(BhResultEnum.FAIL, null);
							} else if (row == 2) {
								bhResult = new BhResult(400, "该卡已存在，请勿重复提交", null);
							}
						}else{
							bhResult = new BhResult(400, bhResult2.getMsg(), null);
						}

					} else {
						bhResult = new BhResult(400, ret.getRespMessage(), null);
					}

				}
			}

		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return bhResult;
	}

	/**
	 * CHENG-2018将商铺设为自营店
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/shop/updateShop")
	@ResponseBody
	public BhResult updateShop(@RequestBody MemberShop memberShop, HttpServletRequest request) {
		BhResult bhResult = null;
		try {

			shopService.updateShop(memberShop);

			bhResult = new BhResult(200, "修改成功", null);
		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return bhResult;
	}
	
	/**
	 * @Description:个人申请 商家列表
	 * @author xieyc
	 * @date 2018年5月21日 下午7:19:40
	 */
	@RequestMapping("/shop/shopInfoListByPerson")
	@ResponseBody
	public BhResult shopInfoListByPerson(@RequestBody MemberShopInfo entity) {
		BhResult r = null;
		try {
			PageBean<ShopInfoByPerson> page = shopService.shopInfoListByPerson(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("个人申请 商家列表"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	/**
	 * @Description:企业申请 商家列表
	 * @author xieyc
	 * @date 2018年5月21日 下午7:19:40
	 */
	@RequestMapping("/shop/shopInfoListByBusiness")
	@ResponseBody
	public BhResult shopInfoListByBusiness(@RequestBody MemberShopInfo entity) {
		BhResult r = null;
		try {
			PageBean<ShopInfoByBusiness> page  = shopService.shopInfoListByBusiness(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("企业申请 商家列表"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	
	
	/**
	 * @Description:个人申请 详情
	 * @author xieyc
	 * @date 2018年5月21日 下午7:19:40
	 */
	@RequestMapping("/shop/detailsByPerson")
	@ResponseBody
	public BhResult detailsByPerson(@RequestBody Map<String, String> map) {
		BhResult r = null;
		try {
			Map<Object, Object> returnMap = shopService.detailsByPerson(Integer.valueOf(map.get("id")));
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(returnMap);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("个人申请 详情"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * @Description:企业申请 详情
	 * @author xieyc
	 * @date 2018年5月21日 下午7:19:40
	 */
	@RequestMapping("/shop/detailsByBusiness")
	@ResponseBody
	public BhResult detailsByBusiness(@RequestBody Map<String, String> map) {
		BhResult r = null;
		try {
			Map<Object, Object> returnMap = shopService.detailsByBusiness(Integer.valueOf(map.get("id")));
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(returnMap);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("企业申请 详情"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * @Description: 审核步骤更改
	 * @author xieyc
	 * @date 2018年8月2日 上午11:16:16
	 */
	@RequestMapping("/shop/updateStep")
	@ResponseBody
	public BhResult updateStep(@RequestBody MemberShop entity,HttpServletRequest request) {
		BhResult r = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil= JedisUtil.getInstance();  
			JedisUtil.Strings strings=jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map map = JSON.parseObject(userJson, Map.class);
			Integer userId = (Integer)map.get("userId");
			if(StringUtils.isBlank(String.valueOf(userId))){
				userId = 1;
			}
			int row = shopService.updateMemberShop(entity,userId);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(row);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("审核步骤更改"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	/**
	 * @Description:商家保证金记录列表
	 * @author xieyc
	 * @date 2018年5月21日 下午7:19:40
	 */
	@RequestMapping("/shop/shopCashDepositList")
	@ResponseBody
	public BhResult shopCashDepositList(@RequestBody MemberShopInfo entity) {
		BhResult r = null;
		try {
			PageBean<ShopCashDeposit> page = shopService.shopCashDepositList(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("商家保证金记录列表"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
}
	

