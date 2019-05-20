package com.bh.user.api.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.bh.config.Contants;
import com.bh.result.BhResult;
import com.bh.user.api.service.WalletWithdrawService;
import com.bh.user.mapper.WalletLogMapper;
import com.bh.user.mapper.WalletMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.Wallet;
import com.bh.user.pojo.WalletLog;
import com.bh.user.pojo.WalletWithdraw;
import com.bh.utils.IDUtils;
import com.bh.utils.MoneyUtil;
@RestController
@RequestMapping("/walletWithdraw")
public class WalletWithdrawController {
	private static final Logger logger = LoggerFactory.getLogger(WalletWithdrawController.class);
	@Autowired
	WalletWithdrawService walletWithdrawService;
	@Autowired
	private WalletMapper walletMapper;
	@Autowired
	private WalletLogMapper walletLogMapper;

	// 移动端提现接口
	@RequestMapping("/add")
	@ResponseBody
	public BhResult add(@RequestBody WalletWithdraw walletWithdraw, HttpServletRequest request) {
		BhResult result;
		try {
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			walletWithdraw.setmId(member.getId()); // 用户ID
			walletWithdraw.setAddTime(new Date()); // 申请时间
			if (StringUtils.isBlank(walletWithdraw.getMoney2()) || ("0").equals(walletWithdraw.getMoney2())) {
				return BhResult.build(400, "提现金额不能为0", null);
			}
			// 前端传提现金额
			walletWithdraw.setAmount(MoneyUtil.yuan2Fen(walletWithdraw.getMoney2()));
			// 根据 用户id 获取 钱包信息
			List<Wallet> wallet = walletMapper.getWalletByUid(member.getId());
			wallet.get(0).setMoney(wallet.get(0).getMoney() - walletWithdraw.getAmount());
			if (wallet.get(0).getMoney() < 0) {
				return BhResult.build(400, "账户余额不足", null);
			}
			// 根据 用户id 获取 钱包信息
			Wallet wallet2 = new Wallet();
			wallet2.setMoney(wallet.get(0).getMoney());
			wallet2.setUid(wallet.get(0).getUid());
			walletMapper.updateByUid(wallet2); // 更改钱包余额

			WalletLog walletLog = new WalletLog();
			walletLog.setAddTime(new Date()); // 时间
			walletLog.setAmount(MoneyUtil.yuan2Fen(walletWithdraw.getMoney2())); // 金额
			walletLog.setmId(member.getId()); // 登录者id
			walletLog.setOrderNo(IDUtils.getOrderNo(Contants.PLAT_ORDER_NO_PRE)); // 订单号
			walletLog.setInOut(1); // 出账
			walletLog.setRemark("提现");// 2018.6.10 zlk
			walletLogMapper.insertSelective(walletLog); // 保存一条充值记录到WalletLog表
			// 保存信息到walletWithdraw
			walletWithdraw.setWalletLogId(walletLog.getId());
			walletWithdrawService.add(walletWithdraw);
			result = new BhResult(200, "申请成功", null);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("#######add#######" + e);
			result = new BhResult(200, "申请失败", null);
		}
		return result;
	}
}
