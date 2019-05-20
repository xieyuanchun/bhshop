package com.bh.user.api.controller;


import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bh.enums.BhResultEnum;
import com.bh.pojo.ShopMsg;
import com.bh.result.BhResult;
import com.bh.user.api.service.MemberShopInfoService;
import com.bh.user.pojo.MemberShopInfo;

@Controller
@RequestMapping("/memberShopInfo")
public class MemberShopInfoController {
	private static final Logger logger = LoggerFactory.getLogger(MemberShopInfoController.class);
	@Autowired
	private MemberShopInfoService  memberShopInfoService;
	/**
	 * @Description: 个人申请入驻信息保存于修改
	 * @author xieyc
	 * @date 2018年7月6日 下午2:06:15 
	 */
	@RequestMapping("/saveOrUpdateByPerson")
	@ResponseBody
	public BhResult saveOrUpdateByPerson(@RequestBody MemberShopInfo entity) {
		BhResult r = null;
		try {
			int row=memberShopInfoService.saveOrUpdateByPerson(entity);
			if(row==1){
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(row);
			}else if(row==-1){
				r = BhResult.build(400,"银行卡信息不匹配");
			}else if(row==-2){
				r = BhResult.build(400,"后台登入用户名已经存在,请换个联系人手机号码注册！");
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("个人申请入驻信息保存于修改"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	/**
	 * @Description: 企业申请入驻 ==>企业信息保存于修改
	 * @author xieyc
	 * @date 2018年7月6日 下午2:06:15 
	 */
	@RequestMapping("/saveOrUpdateByBusiness")
	@ResponseBody
	public BhResult saveOrUpdateByBusiness(@RequestBody MemberShopInfo entity) {
		BhResult r = null;
		try {
			int row=memberShopInfoService.saveOrUpdateByBusiness(entity);
			if(row==1){
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(row);
			}else if(row==-1){
				r = BhResult.build(400,"企业信息不存在");
			}else if(row==-2){
				r = BhResult.build(400,"后台登入用户名已经存在,请换个联系人手机号码注册！");
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("企业申请入驻 ==>企业信息保存于修改"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	/**
	 * @Description: 企业申请入驻==》银行卡信息保存与修改
	 * @author xieyc
	 * @date 2018年7月6日 下午2:06:15 
	 */
	@RequestMapping("/updateByBusiness")
	@ResponseBody
	public BhResult updateByBusiness(@RequestBody MemberShopInfo entity) {
		BhResult r = null;
		try {
			int row=memberShopInfoService.updateByBusiness(entity);
			if(row==1){
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(row);
			}else if(row==-1){
				r = BhResult.build(400,"银行卡信息不匹配");
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("企业申请入驻==》银行卡信息保存与修改"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	/**
	 * @Description: 获取申请详情
	 * @author xieyc
	 * @date 2018年7月6日 下午2:06:15
	 */
	@RequestMapping("/getApplyDetails")
	@ResponseBody
	public BhResult getApplyDetails(@RequestBody Map<String, String> map) {
		BhResult r = null;
		try {
			Map<Object, Object> returnMap = memberShopInfoService.getApplyDetails(map.get("openid"),map.get("page"),map.get("applyType"));
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(returnMap);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error(" 获取申请详情"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	/**
	 * @Description: 通过token查询目前已经提交资料到哪一步
	 * @author xieyc
	 * @date 2018年8月1日 上午11:15:43 
	 */
	@RequestMapping("/selectStep")
	@ResponseBody
	public BhResult selectStep1(@RequestBody Map<String, String> map) {	
		BhResult r = null;
		try {
			ShopMsg shopMsg=memberShopInfoService.selectStep(map.get("openid"));
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(shopMsg);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("通过token查询目前已经提交资料到哪一步"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	
	/**
	 * @Description: 获取某个微信的申请类型
	 * @author xieyc
	 * @date 2018年7月6日 下午2:06:15
	 */
	@RequestMapping("/getApplyType")
	@ResponseBody
	public BhResult getApplyType(@RequestBody Map<String, String> map) {
		BhResult r = null;
		try {
			int num = memberShopInfoService.getApplyType(map.get("openid"));
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(num);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("获取某个微信的申请类型"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
		 
		
	
    
}
