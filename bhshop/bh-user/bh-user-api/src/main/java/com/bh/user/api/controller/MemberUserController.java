package com.bh.user.api.controller;

import java.io.File;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import org.springframework.web.multipart.MultipartFile;

import com.bh.bean.BankCard;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.GoodsCategory;
import com.bh.result.BhResult;
import com.bh.user.api.service.MemberService;
import com.bh.user.api.service.MemberUserAddressService;
import com.bh.user.api.service.MemberUserService;
import com.bh.user.pojo.BankSimple;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberBankCard;
import com.bh.user.pojo.MemberUser;
import com.bh.user.pojo.MemberUserAddress;
import com.bh.user.pojo.MemberUserPojo;
import com.bh.user.pojo.MemberUserSimple;
import com.bh.user.pojo.TbBank;
import com.bh.utils.AliMarketUtil;
import com.bh.utils.BankUtil;
import com.bh.utils.EmojiFilter;
import com.bh.utils.MD5Util1;
import com.bh.utils.RegExpValidatorUtils;
import com.bh.utils.StringUtil;

import com.control.file.upload;

import com.bh.user.api.controller.WangEditorInfo;

@Controller
@RequestMapping("/member")
public class MemberUserController {
	private static final Logger logger = LoggerFactory.getLogger(MemberUserController.class);

	@Value("${USERINFO}")
	private String USERINFO; // 用户信息
	@Autowired
	private MemberService memberService;
	@Autowired
	private MemberUserService memberUserService;
	@Autowired
	private MemberUserAddressService memberUserAddressService;

	/** 完善用户的基本信息 */
	@RequestMapping(value = "/updateMemberUserMsg", method = RequestMethod.POST)
	@ResponseBody
	public BhResult updateMemberUserMsg(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		Member member = (Member) request.getSession(false).getAttribute(USERINFO);
		try {

			String fullName = map.get("fullName"); // 名
			String email = map.get("email"); //
			String prov = map.get("prov"); // 省
			String city = map.get("city"); // 市
			String area = map.get("area"); // 区
			String sex = map.get("sex");

			String year = map.get("year"); // 2017-9-8添加
			String month = map.get("month");
			String day = map.get("day");
			String career = map.get("career");// 职业1.学生2.在职3.自由职业0.其他,
			String condition = map.get("condition");// 职业1.学生2.在职3.自由职业0.其他
			String categoryId = map.get("categoryId");// 兴趣爱好的id
			String img = map.get("img");

			MemberUser memberUser = new MemberUser();
			memberUser.setmId(member.getId());

			if (StringUtils.isNotEmpty(fullName)) {
				memberUser.setFullName(fullName);
			}
			if (StringUtils.isNotEmpty(email)) {
				memberUser.setEmail(email);
			}

			if (StringUtils.isNotEmpty(prov)) {
				memberUser.setProv(Integer.parseInt(prov));
			}
			if (StringUtils.isNotEmpty(city)) {
				memberUser.setCity(Integer.parseInt(city));
			}
			if (StringUtils.isNotEmpty(area)) {
				memberUser.setArea(Integer.parseInt(area));
			}
			if (StringUtils.isNotEmpty(sex)) {
				memberUser.setSex(Integer.parseInt(sex));
			}
			if (StringUtils.isNotEmpty(year)) {
				memberUser.setYear(year);
			}
			if (StringUtils.isNotEmpty(month)) {
				memberUser.setMonth(month);
			}
			if (StringUtils.isNotEmpty(day)) {
				memberUser.setDay(day);
			}
			if (StringUtils.isNotEmpty(career)) {
				memberUser.setCareer(Integer.parseInt(career));
			}
			if (StringUtils.isNotEmpty(condition)) {
				memberUser.setSingle(Integer.parseInt(condition));
			}
			if (StringUtils.isNotEmpty(categoryId)) {
				memberUser.setCategoryId(categoryId);
			}
			if (StringUtils.isNotEmpty(img)) {
				memberUser.setAddress(img);
			}
			Member row = memberUserService.updateByPrimaryKeySelective(memberUser);
			result = new BhResult(200, "添加成功", row);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######updateMemberUserMsg#######" + e);
			result = BhResult.build(500, "操作失败!");
		}
		return result;
	}

	@RequestMapping(value = "/basemsg", method = RequestMethod.POST)
	@ResponseBody
	public BhResult accountBaseMsg(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			Member member1 = (Member) request.getSession(false).getAttribute(USERINFO);
			MemberUserPojo pojo = new MemberUserPojo();
			Map<String, Object> map2 = new HashMap<>();
			if (member1 != null) {
				Member member3 = memberUserService.getMemberById(member1.getId());
				if (member3 != null) {
					if (member3.getUsername() != null) {
						map2.put("username", URLDecoder.decode(member3.getUsername(), "utf-8"));
					} else {
						map2.put("username", "");
					}
					if (StringUtils.isNotBlank(member3.getHeadimgurl())) {
						map2.put("headimgurl", member3.getHeadimgurl());
					} else {
						map2.put("headimgurl", "");
					}
					if (StringUtils.isNotEmpty(member3.getPhone())) {
						String phone = member3.getPhone();
						phone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
						map2.put("phone", phone);
					} else {
						map2.put("phone", "");
					}
					if (StringUtils.isNotEmpty(member3.getOpenid())) {
						map2.put("openid", member3.getOpenid());
					} else {
						map2.put("openid", "");
					}
					if (StringUtils.isNotEmpty(member3.getsOpenid())) {
						map2.put("sOpenid", member3.getsOpenid());
					} else {
						map2.put("sOpenid", "");
					}
				} else {
					map2.put("username", "");
					map2.put("headimgurl", "");
					map2.put("phone", "");
					map2.put("openid", "");
					map2.put("sOpenid", "");
				}
				MemberUser memberUser = memberUserService.selectUserPoint(member1.getId());
				if (memberUser != null) {
					if (memberUser.getPoint() != null) {
						pojo.setPoint(memberUser.getPoint());
					}
					pojo.setSexName(memberUser.getSex() + "");
                    pojo.setRegisterDate(memberUser.getAddtime());
                    pojo.setMember(map2);
                    result = new BhResult(200, "查询成功", pojo);
                } else {
                    pojo.setPoint(0);
                    pojo.setRegisterDate(new Date());
					pojo.setMember(map2);
					pojo.setSexName("保密");
					result = new BhResult(200, "查询成功", pojo);
				}
			} else {
				result = new BhResult(200, "查询成功", pojo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######basemsg#######" + e);
			result = BhResult.build(500, "操作失败!");
		}
		return result;
	}

	@RequestMapping(value = "/mobilerebing", method = RequestMethod.POST)
	@ResponseBody
	public BhResult accountSetting(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			Member member1 = (Member) request.getSession(false).getAttribute(USERINFO);
			String phone = map.get("phone");
			Member member = new Member();
			member.setId(member1.getId());
			member.setPhone(phone);
			int count = memberService.selectCountByPhone(member);
			if (count > 0) {
				result = new BhResult(400, "该手机已存在", null);
			} else {
				Member m = memberService.updateByParams(member);
				if (m != null) {
					result = new BhResult(200, "更新手机号成功", m);
				} else {
					result = new BhResult(400, "更新失败,没有该id", null);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######mobilerebing#######" + e);
			result = BhResult.build(500, "操作失败!");
		}
		return result;
	}

	@RequestMapping(value = "/username", method = RequestMethod.POST)
	@ResponseBody
	public BhResult accountSettingUsername(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			Member member1 = (Member) request.getSession(false).getAttribute(USERINFO);
			String username = map.get("username");
			Member member = new Member();
			member.setId(member1.getId());
			member.setUsername(username);
			Member m = memberService.updateByParams(member);
			if (m != null) {
				result = new BhResult(200, "更新用户名称成功", m);
			} else {
				result = new BhResult(400, "更新用户名称", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######username#######" + e);
			result = BhResult.build(500, "操作失败!");
		}
		return result;
	}

	// 输入密码与数据库的密码是否正确
	@RequestMapping(value = "/password", method = RequestMethod.POST)
	@ResponseBody
	public BhResult accountSettingPassword(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			Member member1 = (Member) request.getSession(false).getAttribute(USERINFO);
			String password = map.get("password");
			Member member = new Member();
			member.setId(member1.getId());
			Member remember = memberService.selectById(member);
			String first = MD5Util1.encode(password);
			first = first + remember.getSalt();
			String second = MD5Util1.encode(first);
			if ((remember.getPassword()).equals(second)) {
				bhResult = new BhResult(200, "密码正确", null);
			} else {
				bhResult = new BhResult(400, "密码不正确", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######password#######" + e);
			bhResult = BhResult.build(500, "操作失败!");
		}
		return bhResult;
	}

	// 更新密码
	@RequestMapping(value = "/setpassword", method = RequestMethod.POST)
	@ResponseBody
	public BhResult setpassword(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			Member member1 = (Member) request.getSession(false).getAttribute(USERINFO);
			String password = map.get("password");
			Member member = new Member();
			String first = MD5Util1.encode(password);
			String salt = MD5Util1.genCodes(first);
			first = first + salt;
			String second = MD5Util1.encode(first);
			member.setId(member1.getId());
			member.setPassword(second);
			member.setSalt(salt);
			Member m = memberService.updateByParams(member);
			if (m != null) {
				bhResult = new BhResult(200, "密码更新成功", null);
			} else {
				bhResult = new BhResult(400, "更新失败", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######setpassword#######" + e);
			bhResult = BhResult.build(500, "操作失败!");
		}
		return bhResult;
	}

	// 修改email
	@RequestMapping(value = "/setemail", method = RequestMethod.POST)
	@ResponseBody
	public BhResult setemail(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			Member member1 = (Member) request.getSession(false).getAttribute(USERINFO);
			String email = map.get("email");
			MemberUser memberUser = new MemberUser();
			memberUser.setmId(member1.getId());
			memberUser.setEmail(email);
			int row = memberUserService.updateEmailBymId(memberUser);
			if (row > 0) {
				bhResult = new BhResult(200, "email更新成功", null);
			} else {
				bhResult = new BhResult(400, "email添加失败", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######setemail#######" + e);
			bhResult = BhResult.build(500, "操作失败!");
		}
		return bhResult;
	}

	// 设置,添加收货地址
	@RequestMapping(value = "/insertaddress", method = RequestMethod.POST)
	@ResponseBody
	public BhResult insertaddress(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			Member member1 = (Member) request.getSession(false).getAttribute(USERINFO);
			String fullName = map.get("fullName");// 收件人的名称
			String prov = map.get("prov");// 省
			String city = map.get("city");// 市
			String area = map.get("area");// 区
			String address = map.get("address");
			String tel = map.get("tel");// 移动电话
			String label = map.get("label");// 标签
			String telephone = map.get("telephone");// 固定电话
			String isJd = "1";// 是否是京东地址，0否，1是
			String four = map.get("four");// 如果没有第四级就传0
			if (StringUtils.isEmpty(fullName)) {
				return new BhResult(400, "收货人不能为空", null);
			}
			if (StringUtils.isEmpty(tel)) {
				return new BhResult(400, "联系方式不能为空", null);
			}
			if (StringUtils.isEmpty(address)) {
				return new BhResult(400, "详细地址不能为空", null);
			}
			if (EmojiFilter.containsEmoji(fullName)) {
				return new BhResult(400, "收货人不能包含表情", null);
			}
			if (EmojiFilter.containsEmoji(address)) {
				return new BhResult(400, "详细地址不能包含表情", null);
			}
			MemberUserAddress memberUserAddress = new MemberUserAddress();
			memberUserAddress.setmId(member1.getId());
			memberUserAddress.setFullName(EmojiFilter.emojiConvert1(fullName));
			memberUserAddress.setProv(Integer.parseInt(prov));
			memberUserAddress.setCity(Integer.parseInt(city));
			memberUserAddress.setArea(Integer.parseInt(area));
			memberUserAddress.setAddress(EmojiFilter.emojiConvert1(address));
			if (RegExpValidatorUtils.checkMobile(tel)) {
				memberUserAddress.setTel(tel);
			} else {
				return new BhResult(400, "您的手机号格式不正确,请重新输入!", null);
			}
			memberUserAddress.setIsDefault(false);// 2017-10-25,原来memberUserAddress.setIsDefault(true)默认,25号由chengfengyunmemberUserAddress.setIsDefault(false)
			memberUserAddress.setIsDel(0);
			if (StringUtils.isNotEmpty(label)) {
				memberUserAddress.setLabel(label);
			}
			if (StringUtils.isNotEmpty(telephone)) {
				memberUserAddress.setTelephone(telephone);
			}
			if (StringUtils.isNotEmpty(four)) {
				memberUserAddress.setFour(Integer.parseInt(four));
			} else {
				memberUserAddress.setFour(0);
			}
			memberUserAddress.setIsJd(Integer.parseInt(isJd));

			if (StringUtils.isEmpty(isJd)) {
				bhResult = new BhResult(BhResultEnum.REQUEST_FAIL, "isJd参数不能为空");
			} else {
				List<MemberUserAddress> list = memberUserAddressService.selectAllAddressByisDel(memberUserAddress);// 10月31日0未删除，1已删除
				if (list.size() < Contants.ADDRESS_COUNT) {
					int row = memberUserAddressService.insertSelective(memberUserAddress);
					if (row > 0) {
						bhResult = new BhResult(200, "地址添加成功", list.size() + 1);
					} else {
						bhResult = new BhResult(400, "地址添加失败", null);
					}
				} else {
					bhResult = new BhResult(600, "已有10个收货地址，不能再添加", list.size());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######insertaddress#######" + e);
			bhResult = BhResult.build(500, "操作失败!");
		}
		return bhResult;
	}

	// 查询list地址
	@RequestMapping(value = "/selectAddressBymId", method = RequestMethod.POST)
	@ResponseBody
	public BhResult selectAddressBymId(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			Member member1 = (Member) request.getSession(false).getAttribute(USERINFO);
			MemberUserAddress memberUserAddress = new MemberUserAddress();
			memberUserAddress.setmId(member1.getId());
			List<MemberUserAddress> list = memberUserAddressService.selectAllAddressByisDel(memberUserAddress);
			if (list != null) {
				bhResult = new BhResult(200, "查询成功,并返回信息", list);
			} else {
				bhResult = new BhResult(400, "查询不到信息", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######selectAddressBymId#######" + e);
			bhResult = BhResult.build(500, "操作失败!");
		}
		return bhResult;
	}

	// 修改收货地址
	@RequestMapping(value = "/selectAddressByPrimaryKey", method = RequestMethod.POST)
	@ResponseBody
	public BhResult selectAddressByPrimaryKey(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			String id = map.get("id");
			Member member1 = (Member) request.getSession(false).getAttribute(USERINFO);
			MemberUserAddress memberUserAddress = new MemberUserAddress();
			memberUserAddress.setId(Integer.parseInt(id));
			memberUserAddress.setmId(member1.getId());
			MemberUserAddress msg = memberUserAddressService.selectByPrimaryKey(memberUserAddress);
			if (msg != null) {
				bhResult = new BhResult(200, "查询成功,并返回信息", msg);
			} else {
				bhResult = new BhResult(400, "查询不到信息", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######selectAddressByPrimaryKey#######" + e);
			bhResult = BhResult.build(500, "操作失败!");
		}
		return bhResult;
	}

	// 更新默认的收货地址
	@RequestMapping(value = "/updatedefaultaddress", method = RequestMethod.POST)
	@ResponseBody
	public BhResult updatedefaultaddress(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			String id = map.get("id");
			Member member1 = (Member) request.getSession(false).getAttribute(USERINFO);
			MemberUserAddress memberUserAddress = new MemberUserAddress();
			memberUserAddress.setId(Integer.parseInt(id));
			memberUserAddress.setmId(member1.getId());
			int row = memberUserAddressService.updateDefaultAddress(memberUserAddress);
			if (row > 0) {
				bhResult = new BhResult(200, "默认地址更新成功", null);
			} else {
				bhResult = new BhResult(400, "默认地址更新失败", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######updatedefaultaddress#######" + e);
			bhResult = BhResult.build(500, "操作失败!");
		}
		return bhResult;
	}

	// 更新地址
	@RequestMapping(value = "/updateaddress", method = RequestMethod.POST)
	@ResponseBody
	public BhResult updateaddress(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			Member member1 = (Member) request.getSession(false).getAttribute(USERINFO);
			String id = map.get("id");

			String fullName = map.get("fullName");
			String prov = map.get("prov");
			String city = map.get("city");
			String area = map.get("area");
			String address = map.get("address");
			String tel = map.get("tel");
			String label = map.get("label");// 标签
			String telephone = map.get("telephone");// 固定电话

			String four = map.get("four");// 如果没有第四级就传0 2017-12-28添加该字段

			MemberUserAddress memberUserAddress = new MemberUserAddress();
			memberUserAddress.setId(Integer.parseInt(id));
			memberUserAddress.setmId(member1.getId());
			if (StringUtils.isEmpty(fullName)) {
				return new BhResult(400, "收货人不能为空", null);
			}
			if (StringUtils.isEmpty(tel)) {
				return new BhResult(400, "联系方式不能为空", null);
			}
			if (StringUtils.isEmpty(address)) {
				return new BhResult(400, "地址不能为空", null);
			}

			if (!fullName.isEmpty()) {
				if (EmojiFilter.containsEmoji(fullName)) {
					return new BhResult(400, "信息不能包含表情", null);
				} else {
					memberUserAddress.setFullName(fullName);
				}

			}
			if (!prov.isEmpty()) {
				memberUserAddress.setProv(Integer.parseInt(prov));
			}
			if (!city.isEmpty()) {
				memberUserAddress.setCity(Integer.parseInt(city));
			}
			if (!area.isEmpty()) {
				memberUserAddress.setArea(Integer.parseInt(area));
			}
			if (!address.isEmpty()) {
				if (EmojiFilter.containsEmoji(address)) {
					return new BhResult(400, "信息不能包含表情", null);
				} else {
					memberUserAddress.setAddress(address);
				}
			}
			if (!tel.isEmpty()) {
				if (RegExpValidatorUtils.checkMobile(tel)) {
					memberUserAddress.setTel(tel);
				} else {
					return new BhResult(400, "您的手机号格式不正确,请重新输入!", null);
				}
			}
			if (StringUtils.isNotEmpty(label)) {
				memberUserAddress.setLabel(label);
			}
			if (StringUtils.isNotEmpty(telephone)) {
				memberUserAddress.setTelephone(telephone);
			}
			if (StringUtils.isNotEmpty(four)) {
				memberUserAddress.setFour(Integer.parseInt(four));
			}

			if (StringUtils.isNotEmpty(id)) {
				int row = memberUserAddressService.updateAddress(memberUserAddress);

				if (row > 0) {
					bhResult = new BhResult(200, "地址更新成功", null);
				} else {
					bhResult = new BhResult(400, "地址更新失败", null);
				}
			} else {
				bhResult = new BhResult(400, "id不能为空", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######updateaddress#######" + e);
			bhResult = BhResult.build(500, "操作失败!");
		}
		return bhResult;
	}

	// 删除选择的收货地址
	@RequestMapping(value = "/deleteAddressById", method = RequestMethod.POST)
	@ResponseBody
	public BhResult deleteAddressById(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			String id = map.get("id");
			Member member1 = (Member) request.getSession(false).getAttribute(USERINFO);
			MemberUserAddress memberUserAddress = new MemberUserAddress();
			memberUserAddress.setId(Integer.parseInt(id));
			memberUserAddress.setmId(member1.getId());
			memberUserAddress.setIsDel(1);
			int row = memberUserAddressService.deleteAddressById(memberUserAddress);
			if (row > 0) {
				bhResult = new BhResult(200, "地址删除成功", null);
			} else {
				bhResult = new BhResult(400, "地址删除失败", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######deleteAddressById#######" + e);
			bhResult = BhResult.build(500, "操作失败!");
		}
		return bhResult;
	}

	/************************2017-9-8 chengfengyun*************************/
	// 选择兴趣爱好
	@RequestMapping(value = "/selectInterest", method = RequestMethod.POST)
	@ResponseBody
	public BhResult selectInterest(@RequestBody Map<String, String> map) {
		BhResult bhResult = null;
		try {

			List<GoodsCategory> list = memberUserAddressService.selectGoodsCategoryByReid();

			if (list != null && list.size() > 0) {
				bhResult = new BhResult(200, "成功", list);
			} else {
				bhResult = new BhResult(400, "失败", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######selectInterest#######" + e);
			bhResult = BhResult.build(500, "操作失败!");
		}
		return bhResult;
	}

	// 设置用户性别2017-11-08
	@RequestMapping(value = "/updatesex", method = RequestMethod.POST)
	@ResponseBody
	public BhResult updateSex(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		Member member1 = (Member) request.getSession(false).getAttribute(USERINFO);
		String sex = map.get("sex");
		try {
			MemberUser memberUser = new MemberUser();
			memberUser.setmId(member1.getId());
			if (StringUtils.isNotEmpty(sex)) {
				memberUser.setSex(Integer.parseInt(sex));
			}
			int row = memberUserAddressService.updateUserSex(memberUser);
			if (row > 0) {
				bhResult = new BhResult(200, "更新成功", null);
			} else {
				bhResult = new BhResult(400, "更新失败", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######updatesex#######" + e);
			bhResult = BhResult.build(500, "操作失败!");
		}
		return bhResult;
	}

	@Value(value = "${oss}")
	private String oss;

	@RequestMapping("/upload")
	@ResponseBody
	public WangEditorInfo uploadImg(MultipartFile files[], HttpServletRequest request) {
		String path = request.getSession(false).getServletContext().getRealPath("/");// 获得files目录的绝对路径
		WangEditorInfo w = new WangEditorInfo();
		w.setErrno(0);
		ArrayList<String> list = new ArrayList<>();
		for (MultipartFile file : files) {
			StringBuffer realPath = new StringBuffer(Contants.bucketHttp);
			StringBuffer key = new StringBuffer();
			String fileName = file.getOriginalFilename();// 获得上传文件的实际名称
			String[] types = fileName.split("\\.");
			String type = types[types.length - 1];
			type = "." + type;// 获得文件的后缀名
			String targetFileName = StringUtil.genUuId() + type;
			File targetFile = new File(path, targetFileName);

			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			try {
				file.transferTo(targetFile);// 保存文件
				key.append("headimages/");
				key.append(targetFileName);
				upload myupload = new upload();
				String localFilePath = path + targetFileName;
				boolean bl = myupload.singleupload(oss, localFilePath, key.toString());
				realPath.append("headimages/");
				realPath.append(targetFileName);
				if (bl) {
					list.add(realPath.toString());
				} else {
					w.setErrno(400);
				}
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("#######upload#######" + e);
			}

		}
		w.setData(list);
		return w;
	}

	@RequestMapping("/safetycenter")
	@ResponseBody
	public BhResult safetyCenter(@RequestBody Map<String, String> map, HttpServletRequest request) {
		// 用户账户安全
		BhResult bhResult = null;
		Member member1 = (Member) request.getSession(false).getAttribute(USERINFO);
		String sex = map.get("sex");
		try {
			MemberUser memberUser = new MemberUser();
			memberUser.setmId(member1.getId());
			if (StringUtils.isNotEmpty(sex)) {
				memberUser.setSex(Integer.parseInt(sex));
			}
			int row = memberUserAddressService.updateUserSex(memberUser);
			if (row > 0) {
				bhResult = new BhResult(200, "更新成功", null);
			} else {
				bhResult = new BhResult(400, "更新失败", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######safetycenter#######" + e);
			bhResult = BhResult.build(500, "操作失败!");
		}
		return bhResult;

	}

	@RequestMapping("/updatepaypwd")
	@ResponseBody
	public BhResult updatepaypwd(@RequestBody Map<String, String> map, HttpServletRequest request) {
		// 用户账户安全
		BhResult bhResult = null;
		Member member1 = (Member) request.getSession(false).getAttribute(USERINFO);
		String paypwd = map.get("paypwd");
		try {
			if (RegExpValidatorUtils.IsNumber(paypwd)) {
				if (paypwd.length() == 6) {
					MemberUser memberUser = new MemberUser();
					memberUser.setmId(member1.getId());
					memberUser.setPaycode(paypwd);
					int row = memberUserService.updatePayCode(memberUser);

					if (row == 1) {
						bhResult = new BhResult(200, "更新成功", null);
					} else {
						bhResult = new BhResult(400, "更新失败", null);
					}
				} else {
					// 长度必须是6位的数字
					bhResult = new BhResult(400, "长度必须是6位的数字", null);
				}
			} else {
				// 不是数字
				bhResult = new BhResult(400, "输入的不是数字", null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######updatepaypwd#######" + e);
			bhResult = BhResult.build(500, "操作失败!");
		}
		return bhResult;

	}

	@RequestMapping("/basecenter")
	@ResponseBody
	public BhResult baseCenter(@RequestBody Map<String, String> map, HttpServletRequest request) {
		// 用户账户安全
		BhResult bhResult = null;
		Member member1 = (Member) request.getSession(false).getAttribute(USERINFO);
		try {
			MemberUser memberUser = new MemberUser();
			MemberUser memberUser2 = new MemberUser();
			memberUser.setmId(member1.getId());
			if (member1.getPhone() != null) {
				String phone = member1.getPhone();
				phone = phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
				member1.setSalt(phone);
			}
			memberUser2 = memberUserService.selectMember(memberUser);
			memberUser2.setMember(member1);
			if (memberUser2.getPaycode() != null) {
				memberUser2.setPaycode(RegExpValidatorUtils.toAllString(memberUser2.getPaycode()).substring(0, 6));
			}
			if (memberUser2.getFullName() != null) {
				// 对字符串处理:将指定位置到指定位置的字符以星号代替
				String s1 = RegExpValidatorUtils.getStarString(memberUser2.getFullName(), 0, 1);
				memberUser2.setFullName(s1);
			}
			if (memberUser2.getIdentity() != null) {
				// 对字符加星号处理：除前面几位和后面几位外，其他的字符以星号代替
				String s2 = RegExpValidatorUtils.getStarString2(memberUser2.getIdentity(), 1, 1);
				memberUser2.setIdentity(s2);
			}
			bhResult = new BhResult(200, "查询成功", memberUser2);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######basecenter#######" + e);
			bhResult = BhResult.build(500, "操作失败!");
		}
		return bhResult;

	}

	/**
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
			e.printStackTrace();
			logger.error("#######banktype#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-201712-09-01 判断用户是否验证通过
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/isAuto")
	@ResponseBody
	public BhResult isAuto(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			MemberUser memberUser = new MemberUser();
			memberUser.setmId(member.getId());
			MemberUser memberUser2 = memberUserService.selectMember(memberUser);
			int status = memberUser2.getStatus();
			if (status == 0) {
				result = new BhResult(200, "该用户未认证过,请认证", null);
			} else if (status == 3) {
				result = new BhResult(203, "该用户信息在审核中,请勿重复验证", null);
			} else if (status == 4) {
				MemberUserSimple memberUserSimple = new MemberUserSimple();
				// 对字符串处理:将指定位置到指定位置的字符以星号代替
				String s1 = RegExpValidatorUtils.getStarString(memberUser2.getFullName(), 0, 1);
				// 对字符加星号处理：除前面几位和后面几位外，其他的字符以星号代替
				String s2 = RegExpValidatorUtils.getStarString2(memberUser2.getIdentity(), 1, 1);
				memberUserSimple.setFullName(s1);
				memberUserSimple.setIdentity(s2);
				result = new BhResult(204, "用户认证的信息已完善，请勿重复验证", memberUserSimple);
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######isAuto#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-201712-09-01 实名认证1
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/toAuth1")
	@ResponseBody
	public BhResult buySeed(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			String fullName = map.get("fullName");// 用户的真实姓名
			String identity = map.get("identity");// 用户的身份证号
			MemberUser memberUser = new MemberUser();
			memberUser.setmId(member.getId());
			if (StringUtils.isEmpty(fullName)) {
				result = new BhResult(400, "用户名不能为空", null);
			} else if (!RegExpValidatorUtils.IsIDcard(identity)) {
				result = new BhResult(400, "身份证号不能为空或格式不正确", null);
			} else {
				memberUser.setmId(member.getId());
				memberUser.setFullName(fullName);
				memberUser.setIdentity(identity);
				memberUser.setStatus(4);
				MemberUser memberUser2 = memberUserService.toAuth1u(memberUser);
				if (memberUser2.getStatus() == 4) {
					result = new BhResult(200, "认证成功", null);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######toAuth1#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-201712-09-01 实名认证2
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/toAuth2")
	@ResponseBody
	public BhResult toAuth2(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			String fullName = map.get("fullName");// 用户的真实姓名
			String identity = map.get("identity");// 用户的身份证号
			String bankNum = map.get("bankNum");// 银行卡号
			String tel = map.get("tel");
			BankCard input = new BankCard();
			input.setName(fullName);
			input.setPhoneNo(tel);
			input.setCardNo(bankNum);
			input.setIdNo(identity);
			BankCard ret = AliMarketUtil.verifyBankCard(input);
			if (ret == null) {
				result = new BhResult(400, "认证失败", null);
			} else {
				// 如果=0000,在认证成功
				if (ret.getRespCode().equals("0000")) {
					MemberBankCard card = new MemberBankCard();
					card.setRealName(ret.getName());// 真实姓名
					card.setIdNo(ret.getIdNo());// 身份证号
					card.setBankCardNo(ret.getCardNo());// 银行卡号
					card.setBankName(ret.getBankName());// 银行名称
					card.setPhone(ret.getPhoneNo());// 手机号
					card.setmId(member.getId());// 用户的id
					card.setBankCode(ret.getBankCode());
					card.setBankKind(ret.getBankKind());
					card.setBankType(ret.getBankType());
					TbBank tbBank = new TbBank();
					tbBank.setBankType(ret.getBankCode());
					List<TbBank> list = memberUserService.selectTbBankList(tbBank);
					if (list.size() > 0) {
						card.setTbbankId(list.get(0).getId());
					}
					int row = memberUserService.toAuth2(card);
					if (row == 1) {
						result = new BhResult(200, "提交成功", null);
					} else if (row == 0) {
						result = new BhResult(BhResultEnum.FAIL, null);
					} else if (row == 2) {
						result = new BhResult(400, "该卡已存在，请勿重复提交", null);
					}
				} else {
					result = new BhResult(Integer.parseInt(ret.getRespCode()), ret.getRespMessage(), null);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######toAuth2#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-201712-09-01 用户的商城钱包
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/userbalance")
	@ResponseBody
	public BhResult userBalance(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			MemberUserSimple simple = memberUserService.userBalance(member);
			result = new BhResult(BhResultEnum.SUCCESS, simple);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######userbalance#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-201712-09-01 选择银行卡
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/selectbankcard")
	@ResponseBody
	public BhResult selectBankCard(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			List<BankSimple> list = memberUserService.selectBankCard(member.getId());
			result = new BhResult(BhResultEnum.SUCCESS, list);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######selectbankcard#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

	/**
	 * CHENG-2018-4-23修改昵称
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/updatusername")
	@ResponseBody
	public BhResult updateUsername(@RequestBody Member member1, HttpServletRequest request) {
		BhResult result = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			member1.setId(member.getId());
			if (StringUtils.isNotEmpty(member1.getUsername())) {
				if (EmojiFilter.containsEmoji(member1.getUsername())) {
					return result = new BhResult(400, "信息不能包含表情", null);
				}
			}
			result = memberUserService.updateUsername(member1);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######updatusername#######" + e);
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}

}
