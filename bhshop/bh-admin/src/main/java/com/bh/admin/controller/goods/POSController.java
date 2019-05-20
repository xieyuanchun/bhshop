package com.bh.admin.controller.goods;


import java.util.Date;
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
import com.bh.result.BhResult;
import com.bh.admin.service.POSService;
import com.bh.admin.service.ShopService;
import com.bh.admin.pojo.user.MemberShop;
import com.bh.admin.pojo.user.POSParam;
import com.bh.admin.pojo.user.TbPos;
import com.bh.utils.PageBean;

@Controller
public class POSController {
	
	@Autowired
	private POSService posService;
	@Autowired
	private ShopService shopService;
	
	
	/**
	 * CHENG-20180305-01 查询用户是否有pos机--针对移动端
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/shop/pos")
	@ResponseBody
	public BhResult POS(@RequestBody POSParam posParam, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			//获得token
			String token = (String) request.getSession().getAttribute("token");
			if (StringUtils.isNotEmpty(token)) {
				MemberShop param = new MemberShop();
				param.setToken(token);
				List<MemberShop> memberShop1 = shopService.selectMemberShop(param);
				//如果member_shop表中未有记录
				if (memberShop1.size() < 1) {
					MemberShop memberShop = new MemberShop();
					memberShop.setToken(token);
					memberShop.setStep(-1);
					memberShop.setAddtime(new Date());
					memberShop.setIsPc(1);
					shopService.insertMemberShopByStepOne(memberShop);
				}
				
				posParam.setToken(token);
				bhResult = posService.insertPosMsg(posParam);
			}else{
				bhResult = new BhResult(400, "token不能为空", null);
			}
		
			
		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return bhResult;
	}
	
	
	
	/**
	 * CHENG-20180305-02 pos列表
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/shop/poslist")
	@ResponseBody
	public BhResult posList(@RequestBody POSParam posParam, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			if (StringUtils.isNotEmpty(posParam.getCurrentPage())) {
				PageBean<TbPos> list = posService.selectPosList(posParam);
				bhResult = new BhResult(200, "查询成功", list);
			}else{
				bhResult = new BhResult(400, "currentPage参数不能为空", null);
			}
		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return bhResult;
	}
	
	/**
	 * CHENG-20180305-02 更新处理操作
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/shop/updateHandleStatus")
	@ResponseBody
	public BhResult updateHandleStatus(@RequestBody POSParam posParam, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			if (posParam.getmId() == null) {
				bhResult = new BhResult(400, "mId参数不能为空", null);
			}else if(posParam.getHandleStatus()==null){
				bhResult = new BhResult(400, "handleStatus参数不能为空", null);
			}else{
				posService.updateHandleStatus(posParam);
				bhResult = new BhResult(200, "操作成功", null);
			}
		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return bhResult;
	}
	
	
	
	/**
	 * CHENG-20180321-保证金的记录
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/shop/promisReco")
	@ResponseBody
	public BhResult promisReco(@RequestBody POSParam posParam, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
				PageBean<MemberShop> list = posService.promisReco(posParam);
				bhResult = new BhResult(200, "操作成功", list);
		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return bhResult;
	}
	
	
	/**
	 * CHENG-20180321-单条记录
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/shop/simpleShop")
	@ResponseBody
	public BhResult promisReco(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			String mId = map.get("mId");
			if (StringUtils.isEmpty(mId)) {
				bhResult = new BhResult(400, "mId参数不能为空", null);
			}else{
				MemberShop memberShop = new MemberShop();
				posService.selectSimpleShop(Integer.parseInt(mId));
				bhResult = new BhResult(200, "操作成功", memberShop);
			}
			
		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return bhResult;
	}
	
	
	/**
	 * CHENG-20180321-免审核上架商家的列表
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/shop/depositReco")
	@ResponseBody
	public BhResult depositReco(@RequestBody MemberShop memberShop, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			PageBean<MemberShop> list = new PageBean<>();
				if (StringUtils.isEmpty(memberShop.getCurrentPage())) {
					memberShop.setCurrentPage("1");
				}
				if (StringUtils.isEmpty(memberShop.getSize())) {
					memberShop.setSize("10");
				}
				if (StringUtils.isEmpty(memberShop.getDepositStatus()+"")) {
					memberShop.setDepositStatus(2);
				}
				else{
					list = posService.depositReco(memberShop);
				}
				bhResult = new BhResult(200, "操作成功", list);
		} catch (Exception e) {
			bhResult = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return bhResult;
	}


}
