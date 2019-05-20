package com.bh.admin.controller.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.admin.pojo.order.OrderSeed;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.pojo.user.MemberUser;
import com.bh.admin.pojo.user.MemerScoreLog;
import com.bh.admin.pojo.user.SeedModel;
import com.bh.admin.service.SeedModelService;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;

import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;

@Controller
/*@RequestMapping("/seed")*/
public class SeedModelControllder {
	@Autowired
	private SeedModelService seedModelService;

	/**
	 * CHENG-201712-05-01
	 * 判断用户是否已经购买了种子  --移动端接口
	 * @param map()
	 * @return
	 */
	@RequestMapping("/seed/isBuy")
	@ResponseBody
	public BhResult isBuy(@RequestBody Map<String, String> map ,HttpServletRequest request){
		BhResult result = null;
		try {
			String mId = map.get("mId");
		
			if (StringUtils.isEmpty(mId)) {
				result = new BhResult(400, "用户的id不能为空", null);
			}else {
				MemberUser memberUser = new MemberUser();
				memberUser.setmId(Integer.parseInt(mId));
				result = seedModelService.isBuy(memberUser);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * CHENG-201712-05-01
	 * 已累计签到的天数接口   --移动端接口
	 * @param map()
	 * @return
	 */
	@RequestMapping("/seed/attendancedays")
	@ResponseBody
	public BhResult attendanceDays(@RequestBody Map<String, String> map ,HttpServletRequest request){
		BhResult result = null;
		try {
			String mId = map.get("mId");
			if (StringUtils.isNotEmpty(mId)) {
				MemberUser user = seedModelService.selectMemberUserBymId(Integer.parseInt(mId));
				//根据mId查询用户,如果用户不存在
				if (user == null) {
					result = new BhResult(400, "抱歉,该用户不存在", null);
				}else{
					MemberUser memberUser1 = new MemberUser();
					memberUser1.setmId(Integer.parseInt(mId));
					BhResult bhResult = seedModelService.isBuy(memberUser1);
					//200  未购买过种子
					if (bhResult.getStatus() == 200) {
						result = new BhResult(400, "用户未购买过种子", null);
					}else if (bhResult.getStatus() == 400) {
						MemberUser memberUser = new MemberUser();
						memberUser.setmId(Integer.parseInt(mId));
						List<SeedModel> del = new ArrayList<>();
						del = seedModelService.attendanceDays(memberUser);
						result = new BhResult(BhResultEnum.SUCCESS, del);
					}
				}
			}else{
				result = new BhResult(400, "用户的id不能为空", null);
			}
			
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * CHENG-201712-06-01
	 * 签到的接口              --移动端接口
	 * @param map()
	 * @return
	 */
	@RequestMapping("/seed/attendances")
	@ResponseBody
	public BhResult attendances(@RequestBody Map<String, String> map ,HttpServletRequest request){
		BhResult bhResult = null;
		try {
			String mId = map.get("mId");
			String orderseedId = map.get("orderseedId");
			if (StringUtils.isEmpty(mId)) {
				bhResult = new BhResult(400, "mId参数不能为空", null);
			}if (StringUtils.isEmpty(orderseedId)) {
				bhResult = new BhResult(400, "orderseedId参数不能为空", null);
			}
			else{
				MemberUser user = seedModelService.selectMemberUserBymId(Integer.parseInt(mId));
				if (user == null) {
					bhResult = new BhResult(400, "抱歉,该用户不存在", null);
				}else{
					MemerScoreLog log = new MemerScoreLog();
					log.setmId(Integer.parseInt(mId));
					log.setOrderseedId(Integer.parseInt(orderseedId));
					int row =0;
					row = seedModelService.attendances(log);
					//row=0签到失败,可能sql语句操作失败,1签到成功,2今天已经签到过了
					if (row ==0) {
						bhResult = new BhResult(BhResultEnum.FAIL, null);
					}else if (row ==1) {
						bhResult = new BhResult(200,"签到成功", null);
					}else if (row ==2) {
						bhResult = new BhResult(400,"今天已签到过了哦,明天再来吧!", null);
					}
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
	 * CHENG-201712-06-01
	 * 种子列表的显示-用户        --移动端接口
	 * @param map()
	 * @return
	 */
	@RequestMapping("/seed/seedModelList")
	@ResponseBody
	public BhResult seedModelList(@RequestBody Map<String, String> map ,HttpServletRequest request){
		BhResult result = null;
		try {
			SeedModel seedModel = new SeedModel();
		    String seedName = map.get("seedName");//种子名称
		    //设置分页,page起始页，size每页的大小
		    String p = map.get("page");
			String r = map.get("size");
		    String id = map.get("id");
		    //0代表种子，1代表土地，2代表鱼苗,3代表工具
		    String type = map.get("type");
			if (StringUtils.isNotEmpty(seedName)) {
				seedModel.setSeedName(seedName);
			}if (StringUtils.isNotEmpty(id)) {
				seedModel.setId(Integer.parseInt(id));
			}
			
			if (StringUtils.isEmpty(type)) {
				result = new BhResult(400, "参数type不能为空,0代表种子，1代表土地，2代表鱼苗,3代表工具", null);
			}else{
				Integer page = 0;
				Integer rows = 0;
				if (p == null || p.equals("")) {
						page = Contants.PAGE;
				} else {
						page = Integer.parseInt(p);
				}if (r == null || r.equals("")) {
						rows = Contants.SIZE;
				}else{
						rows = Integer.parseInt(r);
				}
				seedModel.setType(Integer.parseInt(type));
				seedModel.setStatus(2);
				PageBean<SeedModel> list = new PageBean<>();
				list = seedModelService.seedModelList(seedModel,page,rows);
				result = new BhResult(BhResultEnum.SUCCESS, list);
				
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * CHENG-201712-07-01
	 * 领取余额收益接口
	 * @param map()
	 * @return
	 */
	@RequestMapping("/seed/getbalance")
	@ResponseBody
	public BhResult getBalance(@RequestBody MemerScoreLog log,HttpServletRequest request){
		BhResult result = null;
		try {
			if (log.getmId() == null) {
				result = new BhResult(400,"mId参数不能为空", null);
			}
			if (log.getOrderseedId() == null) {
				result = new BhResult(400,"orderseedId参数不能为空", null);
			}
			else{
				MemberUser user = seedModelService.selectMemberUserBymId(log.getmId());
				if (user == null) {
					result = new BhResult(400, "抱歉,该用户不存在", null);
				}else{
					BhResult bhResult = seedModelService.getBalance(log);
					if (bhResult.getStatus().equals(201)) {
						result = new BhResult(200,"领取成功",bhResult.getData());
					}else if (bhResult.getStatus().equals(202)) {
						result = new BhResult(400,"你未达到领取天数", null);
					}
				}
			}
		
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	
	
	
	
	/**
	 * CHENG-201712-06-01
	 * 添加种子模型--商城后台
	 * @param map()
	 * @return
	 */
	@RequestMapping("/addSeedModel")
	@ResponseBody
	public BhResult addSeedModel(@RequestBody SeedModel seedModel ,HttpServletRequest request){
		BhResult result = null;
		try {
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY);
			if (StringUtils.isEmpty(seedModel.getType()+"")) {
				result = new BhResult(400, "type参数不能为空,0代表种子,1代表土地,2代表鱼苗,3代表工具", null);
			}else{
				if (StringUtils.isEmpty(seedModel.getSeedName())) {
					result = new BhResult(400, "名称不能为空", null);
				}else{
					int row =0;
					if (seedModel.getType().equals(0)) {
						if (StringUtils.isEmpty(seedModel.getSmallImage())) {
							result = new BhResult(400, "阶段图不能为空", null);
						}else{
							row = seedModelService.insertSeedModel(seedModel);
							if (row ==0) {
								result = new BhResult(BhResultEnum.FAIL, null);
							}else if (row ==1) {
								result = new BhResult(200,"添加成功", null);
							}else if (row == 2) {
								result = new BhResult(400,"名称已经存在了,请使用其他的新名称", null);
							}
						}
					}else{
						row = seedModelService.insertSeedModel(seedModel);
						if (row ==0) {
							result = new BhResult(BhResultEnum.FAIL, null);
						}else if (row ==1) {
							result = new BhResult(200,"添加成功", null);
						}else if (row == 2) {
							result = new BhResult(400,"名称已经存在了,请使用其他的新名称", null);
						}
					}
				}
				
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * CHENG-201712-06-01
	 * 修改种子模型-商城后台
	 * @param map()
	 * @return
	 */
	@RequestMapping("/updateSeedModel")
	@ResponseBody
	public BhResult updateSeedModel(@RequestBody SeedModel seedModel ,HttpServletRequest request){
		BhResult result = null;
		try {
			if (StringUtils.isEmpty(seedModel.getId()+"")) {
				result = new BhResult(400,"id不能为空", null);
			}else{
				int row =0;
				row = seedModelService.updateSeedModel(seedModel);
				if (row ==0) {
					result = new BhResult(BhResultEnum.FAIL, null);
				}else if (row ==1) {
					result = new BhResult(200,"更新成功", null);
				}else if (row == 2) {
					result = new BhResult(400,"名称已经存在了,请使用其他的新名称", null);
				}
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * CHENG-201712-06-01
	 * 种子列表的显示-商城后台
	 * @param map()
	 * @return
	 */
	@RequestMapping("/seedModelList")
	@ResponseBody
	public BhResult seedModelList1(@RequestBody Map<String, String> map ,HttpServletRequest request){
		BhResult result = null;
		try {
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY);
			SeedModel seedModel = new SeedModel();
		    String seedName = map.get("seedName");//种子名称
		    String id = map.get("id");
		    String status = map.get("status");
		    
			String p = map.get("page");
			String r = map.get("size");
			String type = map.get("type");
			
				Integer page = 0;
				Integer rows = 0;
				if (p == null || p.equals("")) {
					page = Contants.PAGE;
				} else {
					page = Integer.parseInt(p);
				}if (r == null || r.equals("")) {
					rows = 10;
				}else{
					rows = Integer.parseInt(r);
				}
				
			if (StringUtils.isNotEmpty(seedName)) {
				seedModel.setSeedName(seedName);
			}if (StringUtils.isNotEmpty(id)) {
				seedModel.setId(Integer.parseInt(id));
			}if (StringUtils.isNotEmpty(status)) {
				seedModel.setStatus(Integer.parseInt(status));
			}if (StringUtils.isNotEmpty(type)) {
				seedModel.setType(Integer.parseInt(type));
			}
			
			PageBean<SeedModel> list = new PageBean<>();
			list = seedModelService.seedModelListByPc(seedModel,page,rows);
			result = new BhResult(BhResultEnum.SUCCESS, list);
			
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * CHENG-201712-06-01
	 * 删除种子模型-商城后台
	 * @param map()
	 * @return
	 */
	@RequestMapping("/deleteSeedModel")
	@ResponseBody
	public BhResult deleteSeedModel(@RequestBody Map<String, String> map ,HttpServletRequest request){
		BhResult result = null;
		try {
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY);
			SeedModel seedModel = new SeedModel();
		    String id = map.get("id");
			if (StringUtils.isEmpty(id)) {
				result = new BhResult(400,"参数不能为空", null);
			}else{
				seedModel.setId(Integer.parseInt(id));
				seedModel.setStatus(-3);
				int row = seedModelService.deleteSeedModel(seedModel);
				if (row ==1) {
					result = new BhResult(BhResultEnum.SUCCESS, null);
				}else{
					result = new BhResult(BhResultEnum.FAIL, null);
				}
			}
			
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * CHENG-201712-06-01
	 * 商城种子模型回收站
	 * @param map()
	 * @return
	 */
	@RequestMapping("/deleteSeedList")
	@ResponseBody
	public BhResult deleteSeedList(@RequestBody SeedModel seedModel ,HttpServletRequest request){
		BhResult result = null;
		try {
			if (StringUtils.isEmpty(seedModel.getPage())) {
					seedModel.setPage(String.valueOf(Contants.PAGE));
			} 
				
			PageBean<SeedModel> list = new PageBean<>();
			list = seedModelService.deleteSeedList(seedModel,Integer.parseInt(seedModel.getPage()),Contants.SIZE);
			result = new BhResult(BhResultEnum.SUCCESS, list);
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * CHENG-201712-06-01
	 * 商城后台--展示用户的银行卡
	 * @param map()
	 * @return
	 */
	@RequestMapping("/showuserbankcard")
	@ResponseBody
	public BhResult showUserBankCard(@RequestBody Map<String, String> map ,HttpServletRequest request){
		BhResult result = null;
		try {
			//Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY);
		    
			String p = map.get("page");
			String r = map.get("size");
			String mId = map.get("id");
			String username = map.get("username");
			String phone = map.get("phone");
			
		
			//MemberBankCard card
				Integer page = 0;
				Integer rows = 0;
				if (p == null || p.equals("")) {
					page = Contants.PAGE;
				} else {
					page = Integer.parseInt(p);
				}if (r == null || r.equals("")) {
					rows = 10;
				}else{
					rows = Integer.parseInt(r);
				}
				
			MemberUser memberUser = new MemberUser();
			if(StringUtils.isNotBlank(mId)){
				memberUser.setmId(Integer.valueOf(mId));
			}
			memberUser.setFullName(username);
			memberUser.setPhone(phone);
			PageBean<MemberUser> list = new PageBean<>();
			list = seedModelService.selectMemberUserCard(memberUser,page,rows);
			result = new BhResult(BhResultEnum.SUCCESS, list);
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * CHENG-201712-07-01
	 * 根据种子订单获得该用户的信息
	 * @param map()
	 * @return
	 */
	@RequestMapping("/seed/getusermsg")
	@ResponseBody
	public BhResult getUsermsgByOrderNo(@RequestBody OrderSeed orderSeed,HttpServletRequest request){
		BhResult result = null;
		try {
			if (orderSeed.getOrderNo() == null) {
				result = new BhResult(400,"orderNo参数不能为空", null);
			}else{
				result = seedModelService.getUsermsgByOrderNo(orderSeed.getOrderNo());
					
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	/**
	 * CHENG-201712-06-01
	 * 添加土地--商城后台
	 * @param map()
	 * @return
	 */
	@RequestMapping("/addLand")
	@ResponseBody
	public BhResult addLand(@RequestBody SeedModel seedModel ,HttpServletRequest request){
		BhResult result = null;
		try {
			if (StringUtils.isEmpty(seedModel.getType()+"")) {
				result = new BhResult(400, "type参数不能为空,0代表种子,1代表土地,2代表鱼苗,3代表工具", null);
			}else{
				if (StringUtils.isEmpty(seedModel.getSeedName())) {
					result = new BhResult(400, "名称不能为空", null);
				}else{
					int row =0;
					if (seedModel.getType().equals(0)) {
						if (StringUtils.isEmpty(seedModel.getSmallImage())) {
							result = new BhResult(400, "阶段图不能为空", null);
						}else{
							row = seedModelService.insertSeedModel(seedModel);
							if (row ==0) {
								result = new BhResult(BhResultEnum.FAIL, null);
							}else if (row ==1) {
								result = new BhResult(200,"添加成功", null);
							}else if (row == 2) {
								result = new BhResult(400,"名称已经存在了,请使用其他的新名称", null);
							}
						}
					}else{
						seedModel.setType(1);
						row = seedModelService.insertSeedModel(seedModel);
						if (row ==0) {
							result = new BhResult(BhResultEnum.FAIL, null);
						}else if (row ==1) {
							result = new BhResult(200,"添加成功", null);
						}else if (row == 2) {
							result = new BhResult(400,"名称已经存在了,请使用其他的新名称", null);
						}
					}
				}
				
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	
	
	
	
	
	
	
	
	
}
