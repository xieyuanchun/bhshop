package com.bh.admin.controller.order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bh.admin.pojo.user.WalletWithdraw;
import com.bh.admin.service.WalletWithdrawService;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;

import com.bh.utils.PageBean;

@RestController
@RequestMapping("/walletWithdraw")
public class WalletWithdrawController {

	@Autowired
	WalletWithdrawService walletWithdrawService;

	// PC端 提现记录 列表
	@RequestMapping("/listPage")
	@ResponseBody
	public BhResult listPage(@RequestBody WalletWithdraw entity) {
		BhResult r = null;
		try {
			PageBean<WalletWithdraw> page = walletWithdrawService.listPage(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return r;
		}
	}

	// PC端 更新，更改状态
	@RequestMapping("/update")
	@ResponseBody
	public BhResult update(@RequestBody WalletWithdraw entity) {
		BhResult r = null;
		try {
			walletWithdrawService.update(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}
}
