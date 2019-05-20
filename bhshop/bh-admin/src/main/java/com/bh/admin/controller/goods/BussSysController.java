package com.bh.admin.controller.goods;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.enums.BhResultEnum;
import com.bh.admin.pojo.order.OrderPayment;
import com.bh.result.BhResult;
import com.bh.admin.service.BussSysService;
import com.bh.admin.service.MemberService;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.pojo.user.MemberUser;
import com.bh.admin.pojo.user.MemberUserDetail;
import com.bh.admin.pojo.user.ShowTiaoZheng;
import com.bh.utils.LoggerUtil;
import com.bh.utils.MD5Util1;
import com.bh.utils.MixCodeUtil;
import com.bh.utils.PageBean;
import com.bh.utils.RegExpValidatorUtils;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@Controller
@RequestMapping("/sys")
public class BussSysController {

	@Autowired
	private BussSysService bussSysService;

	@Autowired
	private MemberService memberService;

	/**
	 * CHENG-201801-09 支付列表
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/paylist")
	@ResponseBody
	public BhResult payList(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {

			String page = map.get("page");
			if (StringUtils.isEmpty(page)) {
				page = "1";
			}
			OrderPayment orderPayment = new OrderPayment();
			PageBean<OrderPayment> list = bussSysService.selectOrderPayment(orderPayment, Integer.parseInt(page), 10);
			bhResult = new BhResult(200, "查询成功", list);

		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return bhResult;
	}

	/**
	 * CHENG-201801-09 新增支付方式
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/addpay")
	@ResponseBody
	public BhResult addPay(@RequestBody OrderPayment orderPayment, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			int row = 0;

			/*
			 * String note = map.get("note");//支付方式名称 String description =
			 * map.get("description"); String thumb = map.get("thumb");//支付方式图片
			 * String payFee = map.get("payFee");//支付手续费 String isCod =
			 * map.get("isCod");//是否货到付款 0否 1是 String isOnline =
			 * map.get("isOnline");//是否支持线上支付 0否 1是
			 */ // OrderPayment orderPayment = new OrderPayment();
			/*
			 * if (StringUtils.isEmpty(note)) { bhResult = new BhResult(400,
			 * "支付方式名称 不能为空", null); }else if (StringUtils.isEmpty(description))
			 * { bhResult = new BhResult(400, "描述不能为空", null); }else if
			 * (!RegExpValidatorUtils.IsNumber(isCod)) { bhResult = new
			 * BhResult(400, "否货到付款不能为空", null); }else if
			 * (!RegExpValidatorUtils.IsNumber(isOnline)) { bhResult = new
			 * BhResult(400, "是否支持线上支付 不能为空", null); }else{
			 * orderPayment.setNote(note);
			 * orderPayment.setDescription(description); if
			 * (StringUtils.isNotEmpty(thumb)) { orderPayment.setThumb(thumb);
			 * }if (StringUtils.isNotEmpty(payFee)) {
			 * orderPayment.setPayFee(payFee); } if
			 * (StringUtils.isNotEmpty(isCod)) {
			 * orderPayment.setIsCod(Integer.parseInt(isCod)); }if
			 * (StringUtils.isNotEmpty(isOnline)) {
			 * orderPayment.setIsOnline(Integer.parseInt(isOnline)); }
			 */

			row = bussSysService.insertOrderPayment(orderPayment);
			if (row == 0) {
				bhResult = new BhResult(200, "新增成功", null);
			} else {
				bhResult = new BhResult(200, "新增成功", null);
			}

		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return bhResult;
	}

	/**
	 * CHENG-201801-09 新增支付方式
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/selectOrderpayById")
	@ResponseBody
	public BhResult selectOrderpayById(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {

			OrderPayment orderPayment = new OrderPayment();
			String id = map.get("id");
			if (StringUtils.isEmpty(id)) {
				bhResult = new BhResult(400, "id 不能为空", null);
			} else {
				orderPayment.setId(Integer.parseInt(id));
				OrderPayment orderPayment2 = bussSysService.selectOrderPayment(orderPayment);
				bhResult = new BhResult(200, "查询成功", orderPayment2);
			}
		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return bhResult;
	}

	/**
	 * CHENG-201801-09 修改支付方式
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/updateOrderpay")
	@ResponseBody
	public BhResult updateOrderpay(@RequestBody OrderPayment orderPayment, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			int row = 0;
			if (orderPayment.getId() == null) {
				bhResult = new BhResult(400, "id 不能为空", null);
			}

			else {
				row = bussSysService.updateOrderPayment(orderPayment);
				if (row == 0) {
					bhResult = new BhResult(400, "修改失败", null);
				} else {
					bhResult = new BhResult(200, "修改成功", null);
				}
			}

		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return bhResult;
	}

	/**
	 * CHENG-201801-09 删除支付方式
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/deleteOrderpay")
	@ResponseBody
	public BhResult deleteOrderpay(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			String id = map.get("id");
			OrderPayment orderPayment = new OrderPayment();
			int row = 0;
			if (StringUtils.isEmpty(id)) {
				bhResult = new BhResult(400, "id 不能为空", null);
			} else {
				orderPayment.setId(Integer.parseInt(id));
				orderPayment.setIsDel(1);// 0未删除，1已删除
				row = bussSysService.deleteOrderPayment(orderPayment);
				if (row == 0) {
					bhResult = new BhResult(400, "删除失败", null);
				} else {
					bhResult = new BhResult(200, "删除成功", null);
				}
			}

		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return bhResult;
	}

	/**********************************************************************
	 * 会 员
	 ***************************************************************************/

	/**
	 * CHENG-201801-09 会员列表
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/memberlist")
	@ResponseBody
	public BhResult memberList(@RequestBody MemberUser memberUser, HttpServletRequest request) {
		BhResult bhResult = null;
		try {

			PageBean<MemberUser> list = bussSysService.memberUserList(memberUser);
			bhResult = new BhResult(200, "查询成功", list);
		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return bhResult;
	}

	/**
	 * CHENG-201801-09 会员详情
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/memberdetail")
	@ResponseBody
	public BhResult memberDetail(@RequestBody MemberUser memberUser, HttpServletRequest request) {
		BhResult bhResult = null;
		try {

			MemberUserDetail detail = bussSysService.selectMemberUserDetail(memberUser);
			bhResult = new BhResult(200, "查询成功", detail);
		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return bhResult;
	}

	/**
	 * CHENG-201801-09 新增会员
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/addmember")
	@ResponseBody
	public BhResult addMember(@RequestBody MemberUser memberUser, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			if (memberUser.getUsername() == null) {
				bhResult = new BhResult(400, "用户名不能为空", null);
			} else {

				Member member = new Member();
				member.setUsername(memberUser.getUsername());
				int row = memberService.checkuser(member);
				if (row == 0) {
					MemberUser memberUser2 = new MemberUser();
					if (memberUser.getFullName() != null) {
						memberUser2.setFullName(memberUser.getFullName());
						List<MemberUser> list = bussSysService.selectMemberUserByFullName(memberUser2);
						if (list.size() > 0) {
							bhResult = new BhResult(400, "该用名已存在", null);
						} else {
							bhResult = bussSysService.addMember(memberUser);
						}
					}
				} else {
					bhResult = new BhResult(400, "该账号昵称已存在", null);
				}
			}

		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return bhResult;
	}

	/**
	 * CHENG-201801-09 编辑会员
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/updatemember")
	@ResponseBody
	public BhResult updateMember(@RequestBody MemberUser memberUser, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			if (memberUser.getUsername() != null) {
				Member member = new Member();
				member.setUsername(memberUser.getUsername());
				int row = memberService.checkuser(member);
				if (row == 0) {
					bhResult = bussSysService.updateMember(memberUser);
				} else {
					Member m = new Member();
					m.setId(memberUser.getmId());
					m.setUsername(memberUser.getUsername());
					List<Member> list = bussSysService.selectMember(m);
					if (list.size() > 0) {
						bhResult = bussSysService.updateMember(memberUser);
					}else{
						bhResult = new BhResult(400, "该账号昵称已存在", null);
					}
					
				}

			}else{
				bhResult = bussSysService.updateMember(memberUser);
			}

		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return bhResult;
	}
	
	
	/**
	 * CHENG-201801-25显示调整的内容
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/selecttiaozheng")
	@ResponseBody
	public BhResult selecttiaozheng(@RequestBody MemberUser memberUser, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			if (memberUser.getmId() !=null) {
				ShowTiaoZheng tiaoZheng = bussSysService.selecttiaozheng(memberUser);
				bhResult = new BhResult(BhResultEnum.SUCCESS, tiaoZheng);
			}
		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return bhResult;
	}
	
	/**
	 * CHENG-201801-25更新调整的内容
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/updatetiaozheng")
	@ResponseBody
	public BhResult updatetiaozheng(@RequestBody ShowTiaoZheng showTiaoZheng, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			if (showTiaoZheng.getmId() !=null) {
				int tiaoZheng = bussSysService.updatetiaozheng(showTiaoZheng);
				if (tiaoZheng == 1) {
					bhResult = new BhResult(BhResultEnum.SUCCESS, null);
				}else{
					bhResult = new BhResult(BhResultEnum.FAIL, null);
				}
				
			}
		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return bhResult;
	}

}
