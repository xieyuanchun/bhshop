package com.bh.user.api.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.user.api.service.WalletLogService;
import com.bh.user.api.service.WalletService;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberBankCard;
import com.bh.user.pojo.TbBank;
import com.bh.user.pojo.Wallet;
import com.bh.user.pojo.WalletLog;
import com.bh.utils.IDUtils;
import com.bh.utils.MD5Util1;
import com.bh.utils.PageBean;
import com.bh.utils.TopicUtils;
import com.bh.utils.pay.HttpService;
import com.user.enums.UnionPayInterfaceEnum;
import com.user.vo.UnionPayVO;

@RestController
@RequestMapping("/wallet")
public class WalletController {
	private static final Logger logger = LoggerFactory.getLogger(WalletController.class);
	@Autowired
	private WalletService walletService;
	@Autowired
	private WalletLogService walletLogService;

	@RequestMapping("/add")
	@ResponseBody
	public BhResult add(@RequestBody Wallet entity, HttpServletRequest request) {
		BhResult r = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			if (StringUtils.isBlank(entity.getPayPassword())) {
				return r = BhResult.build(500, "密码不能为空!");
			}
			// 32位盐值
			entity.setSalt(TopicUtils.getRandomString(32));
			// 用户id
			entity.setUid(member.getId());
			// 钱包名称
			entity.setName("个人账户余额");
			// 钱包类型
			entity.setType("1");
			// 钱包说明
			entity.setDes("个人钱包");
			// 密码加密
			String salt = "";
			String first = MD5Util1.encode(entity.getPayPassword());
			salt = MD5Util1.genCodes(first);
			first = first + salt;
			String second = MD5Util1.encode(first);
			entity.setPayPassword(second); // 加密后的密码

			// 判断是否有数据
			List<Wallet> list = walletService.getByUid(entity);
			if (list != null && list.size() > 0) {
				return BhResult.build(400, "已经有密码，不能重复添加", null);
			}
			int row = walletService.add(entity);
			if (row > 0) {
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				return r;
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.VISIT_FAIL.getStatus());
				return r;
			}

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######add#######" + e);
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}

	// 更新
	@RequestMapping("/update")
	@ResponseBody
	public BhResult update(@RequestBody Wallet entity) {
		BhResult r = null;
		try {
			walletService.update(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######update#######" + e);
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}

	// 获取
	@RequestMapping("/get")
	@ResponseBody
	public BhResult get(@RequestBody Wallet entity) {
		BhResult r = null;
		try {
			Wallet e = walletService.get(entity.getId());
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(e);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######get#######" + e);
			r = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return r;
		}

	}

	// 列表
	@RequestMapping("/listPage")
	@ResponseBody
	public BhResult listPage(@RequestBody Wallet entity) {
		BhResult r = null;
		try {
			PageBean<Wallet> page = walletService.listPage(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######listPage#######" + e);
			r = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return r;
		}

	}

	// 删除
	@RequestMapping("/delete")
	@ResponseBody
	public BhResult delete(@RequestBody Wallet entity) {
		BhResult r = null;
		try {
			walletService.delete(entity.getId());
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######delete#######" + e);
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}

	// 根据用户id获取钱包的余额、银行卡信息
	@RequestMapping("/getByUid")
	@ResponseBody
	public BhResult getByUid(HttpServletRequest request) {
		BhResult result = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			Wallet wallet = new Wallet();
			wallet.setUid(member.getId());

			// 获取余额
			List<Wallet> list = walletService.getByUid(wallet);
			if (list != null && list.size() > 0) {
				double money = list.get(0).getMoney();
				wallet.setMoney2(money / 100 + "");// 分转成元
				// 获取银行卡信息
				List<MemberBankCard> cardList = walletService.getCardByMid(wallet);
				if (cardList != null && cardList.size() > 0) {
					for (int i = 0; i < cardList.size(); i++) {
						cardList.get(i).setBankCardNo(cardList.get(i).getBankCardNo());
						// 获取银行logo
						TbBank tbBank = walletService.getByBankName(cardList.get(i).getBankName());
						if (tbBank != null && !tbBank.equals("")) {
							cardList.get(i).setLogoImg(tbBank.getImg());
						} else {
							cardList.get(i).setLogoImg(Contants.DEFAULT_BANK_IMG);
						}
					}
					wallet.setMemberBankCard(cardList);

				} else {
					wallet.setMemberBankCard(cardList);

				}
			} else {// 余额没值
				List<MemberBankCard> cardList2 = new ArrayList<MemberBankCard>();
				wallet.setMoney2("0.0");
				wallet.setMemberBankCard(cardList2);
				result = new BhResult();
				result.setStatus(BhResultEnum.SUCCESS.getStatus());
				result.setMsg(BhResultEnum.SUCCESS.getMsg());
				// 获取银行卡信息 如果余额没钱
				wallet.setUid(member.getId());
				List<MemberBankCard> cardList = walletService.getCardByMid(wallet);
				if (cardList != null && cardList.size() > 0) {
					for (int i = 0; i < cardList.size(); i++) {
						cardList.get(i).setBankCardNo(cardList.get(i).getBankCardNo());
						// 获取银行logo
						TbBank tbBank = walletService.getByBankName(cardList.get(i).getBankName());
						if (tbBank != null && !tbBank.equals("")) {
							cardList.get(i).setLogoImg(tbBank.getImg());
						} else {
							// 如果没有匹配的银行卡图片则设置默认的一张
							cardList.get(i).setLogoImg(Contants.DEFAULT_BANK_IMG);
						}
					}
					wallet.setMemberBankCard(cardList);
				} else {
					wallet.setMemberBankCard(cardList);

				}
				result.setData(wallet);
				return result;
			}
			result = new BhResult();
			result.setStatus(BhResultEnum.SUCCESS.getStatus());
			result.setMsg(BhResultEnum.SUCCESS.getMsg());
			result.setData(wallet);
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######getByUid#######" + e);
			result = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return result;
		}
	}

	// 森惠-钱包-充值功能 zlk
	@RequestMapping("/addWalletLog")
	@ResponseBody
	public BhResult addWalletLog(@RequestBody WalletLog entity, HttpServletRequest request) {
		BhResult r = null;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			// 自己生成订单号
			IDUtils iDUtils = new IDUtils();
			if (StringUtils.isBlank(entity.getMoney2()) || entity.getMoney2().equals("0")
					|| entity.getMoney2().equals("0.0")) {// 金额空
				return BhResult.build(400, "充值金额不能为0", null);
			}
			Wallet wallet = new Wallet();
			wallet.setUid(member.getId());
			List<Wallet> list = walletService.getByUid(wallet);// 获取当前的用户的数据
			if (list != null && list.size() > 0) {

			} else {// 没数据提示，输入密码
				return BhResult.build(400, "请设置密码", null);
			}

			// 前端传来充值金额
			double amount2 = Double.valueOf(entity.getMoney2());

			WalletLog walletLog = new WalletLog();
			walletLog.setAddTime(new Date()); // 时间
			walletLog.setAmount(Integer.parseInt(new java.text.DecimalFormat("0").format(amount2 * 10 * 10))); // 金额
			walletLog.setmId(member.getId()); // 登录者id
			walletLog.setOrderNo(iDUtils.getOrderNo(Contants.PLAT_ORDER_NO_PRE)); // 订单号
			walletLog.setInOut(0); // 进账
			walletLog.setRemark("充值");// 2018.6.10 zlk

			walletLogService.addWalletLog(walletLog); // 保存一条充值记录到WalletLog表

			UnionPayVO vo = new UnionPayVO();
			String orderBody = "null,0000" + "," + member.getId();
			vo.setTotalAmount(Integer.parseInt(new java.text.DecimalFormat("0").format(amount2 * 10 * 10)) + "");
			vo.setOriginalAmount(Integer.parseInt(new java.text.DecimalFormat("0").format(amount2 * 10 * 10)) + "");
			vo.setAttachedData(orderBody);
			vo.setMerOrderId(walletLog.getOrderNo());
			vo.setMd5Key(Contants.PLAT_MD5_KEY);
			String jsonStr = HttpService.doPostJson(UnionPayInterfaceEnum.WXRECHARGE.getMethod(), vo);
			System.out.println("before jsonStr--->" + jsonStr);
			jsonStr = jsonStr.replaceAll("&", "&amp");
			return BhResult.build(200, "操作成功", jsonStr);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######addWalletLog#######" + e);
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}

}
