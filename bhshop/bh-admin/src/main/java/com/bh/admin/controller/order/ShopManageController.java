package com.bh.admin.controller.order;

import com.alibaba.fastjson.JSON;
import com.bh.admin.pojo.order.OrderSku;
import com.bh.admin.pojo.order.ShopOrderRecordVo;
import com.bh.admin.pojo.order.ShopWithdraw;
import com.bh.admin.pojo.order.ShopWithdrawVo;
import com.bh.admin.pojo.user.MBusEntity;
import com.bh.admin.service.ShopManageService;
import com.bh.admin.util.JedisUtil;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.PageBean;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Controller
@RequestMapping("/shopManage")
public class ShopManageController {
	private static final Logger logger = LoggerFactory.getLogger(ShopManageController.class);
	@Autowired
	private ShopManageService shopManageService;
	
	/**
	 * @Description: PC端：平台提现记录导出
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34
	 */
	@RequestMapping(value="/excelExport",method = RequestMethod.GET)  
	public void excelExport(String startTime,String endTime, String isPay,
			String type, String state, String shopId,HttpServletRequest request, HttpServletResponse response) {
		try {
			shopManageService.excelExport(startTime,endTime,isPay,type,state, shopId,request, response);
		} catch (Exception e) {
			logger.error("excelExport" + e.getMessage());
			e.printStackTrace();
		}
	}
	
	/**
	 * @Description: PC端：平台提现记录列表
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	@RequestMapping("/withdrawRecordListByPt")
	@ResponseBody
	public BhResult withdrawRecordListByPt(@RequestBody ShopWithdraw entity,HttpServletRequest request) {
		BhResult r = null;
		try {
			PageBean<ShopWithdrawVo> page  = shopManageService.withdrawRecordListByPt(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("withdrawRecordListByPt"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}	

	/**
	 * @Description: PC端：平台更新提现记录
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34
	 */
	@RequestMapping("/update")
	@ResponseBody
	public BhResult update(@RequestBody ShopWithdraw entity, HttpServletRequest request) {
		BhResult r = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Integer userId = (Integer) mapOne.get("userId");
			if (StringUtils.isBlank(String.valueOf(userId))) {
				userId = 1;
			}
			int row = shopManageService.update(entity,userId);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(row);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("updateState" + e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	/**
	 * @Description: PC端：某个商家交易记录列表
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	@RequestMapping("/shopOrderRecord")
	@ResponseBody
	public BhResult shopOrderRecord(@RequestBody OrderSku orderSku,HttpServletRequest request) {
		BhResult r = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = (Integer) sId;
			if (shopId == null) {
				shopId = 1;
			}
			orderSku.setShopId(shopId);
			PageBean<ShopOrderRecordVo> page  = shopManageService.shopOrderRecord(orderSku);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("shopOrderRecord"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}	
	/**
	 * @Description: PC端：计算交易金额、待结算金额
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	@RequestMapping("/countMoney")
	@ResponseBody
	public BhResult countMoney(@RequestBody Map<String, String> map,HttpServletRequest request) {
		BhResult r = null;
		try {
			String startTime = map.get("startTime");// 查询条件：起始时间
			String endTime = map.get("endTime");// 查询条件：结束时间
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = (Integer) sId;
			if (shopId == null) {
				shopId = 1;
			}
			Map<Object,Object> returnMap=shopManageService.countMoney(startTime,endTime,shopId);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(returnMap);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("countMoney"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	
	/**
	 * @Description: 微信端：计算交易金额、待结算金额
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	@RequestMapping("/mCountMoney")
	@ResponseBody
	public BhResult mCountMoney(@RequestBody Map<String, String> map,HttpServletRequest request) {
		BhResult r = null;
		try {
			String startTime = map.get("startTime");// 查询条件：起始时间
			String endTime = map.get("endTime");// 查询条件：结束时间
			MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
			if (user != null) {
				Long shopId = user.getShopId();
				if (shopId == null) {
					shopId = (long) 1;
				}
				Map<Object,Object> returnMap=shopManageService.mCountMoney(startTime,endTime,shopId.intValue());
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(returnMap);
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("mCountMoney"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	
	
	
	/**
	 * @Description: PC端：可提现金额、已提现金额、锁定金额 
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	@RequestMapping("/countWithdraMoney")
	@ResponseBody
	public BhResult countWithdraMoney(HttpServletRequest request) {
		BhResult r = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = (Integer) sId;
			if (shopId == null) {
				shopId = 1;
			}
			Map<Object,Object> returnMap=shopManageService.countWithdraMoney(shopId);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(returnMap);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("countMoney"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	/**
	 * @Description: 微信端：可提现金额、已提现金额、锁定金额 
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	@RequestMapping("/mCountWithdraMoney")
	@ResponseBody
	public BhResult mCountMoney(HttpServletRequest request) {
		BhResult r = null;
		try {
			MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
			if (user != null) {
				Long shopId = user.getShopId();
				if (shopId == null) {
					shopId = (long) 1;
				}
				Map<Object,Object> returnMap=shopManageService.mCountWithdraMoney(shopId.intValue());
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(returnMap);
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("mCountMoney"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}	
	/**
	 * @Description: PC端：判断本日是否已经提现
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	@RequestMapping("/isWithdrawByToday")
	@ResponseBody
	public BhResult isWithdrawByToday(HttpServletRequest request) {
		BhResult r = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = (Integer) sId;
			if (shopId == null) {
				shopId = 1;
			}
			boolean date=shopManageService.isWithdrawByToday(shopId);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(date);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("isWithdrawByToday"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	/**
	 * @Description: 微信端：判断本日是否已经提现
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34
	 */
	@RequestMapping("/mIsWithdrawByToday")
	@ResponseBody
	public BhResult mIsWithdrawByToday(HttpServletRequest request) {
		BhResult r = null;
		try {
			MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
			if (user != null) {
				Long shopId = user.getShopId();
				if (shopId == null) {
					shopId = (long) 1;
				}
				boolean date = shopManageService.mIsWithdrawByToday(shopId.intValue());
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(date);
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("mIsWithdrawByToday" + e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	
	
	/**
	 * @Description: PC端：提现提交操作 
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	@RequestMapping("/withdraw")
	@ResponseBody
	public BhResult withdraw(@RequestBody ShopWithdraw shopWithdraw,HttpServletRequest request) {
		BhResult r = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = (Integer) sId;
			if (shopId == null) {
				shopId = 1;
			}
			shopWithdraw.setShopId(shopId);
			int row=shopManageService.withdraw(shopWithdraw);
			if(row==1){
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(row);
			}else if(row==-1){
				r = BhResult.build(400,"每天只可以操作提现一次");
			}else if(row==-2){
				r = BhResult.build(400,"可提现金额不足");
			}else if(row==-3){
				r = BhResult.build(400,"银行卡信息不匹配");
			}else if(row==-4){
				r = BhResult.build(400,"可提现金额为0");
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("withdraw"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	/**
	 * @Description: 微信端：提现提交操作 
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	@RequestMapping("/mWithdraw")
	@ResponseBody
	public BhResult mWithdraw(@RequestBody ShopWithdraw shopWithdraw,HttpServletRequest request) {
		BhResult r = null;
		try {			
			MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
			if (user != null) {
				Long shopId = user.getShopId();
				if (shopId == null) {
					shopId = (long) 1;
				}
				shopWithdraw.setShopId(shopId.intValue());
				int row=shopManageService.mWithdraw(shopWithdraw);
				if(row==1){
					r = new BhResult();
					r.setStatus(BhResultEnum.SUCCESS.getStatus());
					r.setMsg(BhResultEnum.SUCCESS.getMsg());
					r.setData(row);
				}else if(row==-1){
					r = BhResult.build(400,"每天只可以操作提现一次");
				}else if(row==-2){
					r = BhResult.build(400,"可提现金额不足");
				}else if(row==-3){
					r = BhResult.build(400,"银行卡信息不匹配");
				}else if(row==-4){
					r = BhResult.build(400,"可提现金额为0");
				}
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("mWithdraw"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	/**
	 * @Description: PC端：某商家提现记录列表
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	@RequestMapping("/withdrawRecordListByShop")
	@ResponseBody
	public BhResult withdrawRecordListByShop(@RequestBody ShopWithdraw entity,HttpServletRequest request) {
		BhResult r = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = (Integer) sId;
			if (shopId == null) {
				shopId = 1;
			}
			entity.setShopId(shopId);
			PageBean<ShopWithdrawVo> page  = shopManageService.withdrawRecordListByShop(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("withdrawRecordList"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	/**
	 * @Description: 微信端：某商家提现记录列表
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	@RequestMapping("/mWithdrawRecordListByShop")
	@ResponseBody
	public BhResult mWithdrawRecordListByShop(@RequestBody ShopWithdraw entity,HttpServletRequest request) {
		BhResult r = null;
		try {
			MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
			if (user != null) {
				Long shopId = user.getShopId();
				if (shopId == null) {
					shopId = (long) 1;
				}
				entity.setShopId(shopId.intValue());
				PageBean<ShopWithdrawVo> page  = shopManageService.mWithdrawRecordListByShop(entity);
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(page);
			}else{
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("mWithdrawRecordList"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}	
	/**
	 * @Description: PC端：某商家入驻银行卡信息
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	@RequestMapping("/lastWithdrawRecord")
	@ResponseBody
	public BhResult lastWithdrawRecord(@RequestBody ShopWithdraw entity,HttpServletRequest request) {
		BhResult r = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = (Integer) sId;
			if (shopId == null) {
				shopId = 1;
			}
			entity.setShopId(shopId);
			Map<Object,Object>  map= shopManageService.lastWithdrawRecord(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(map);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("lastWithdrawRecord"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	/**
	 * @Description: 微信端：某商家最新一次提现记录展示
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	@RequestMapping("/mLastWithdrawRecord")
	@ResponseBody
	public BhResult mLastWithdrawRecord(@RequestBody ShopWithdraw entity,HttpServletRequest request) {
		BhResult r = null;
		try {
			MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
			if (user != null) {
				Long shopId = user.getShopId();
				if (shopId == null) {
					shopId = (long) 1;
				}
				entity.setShopId(shopId.intValue());
				Map<Object,Object>  map= shopManageService.mLastWithdrawRecord(entity);
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(map);
			}else{
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("mLastWithdrawRecord"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	
	
	/**
	 * @Description: PC端：某商家最新一次提现记录展示
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	@RequestMapping("/lastWithdrawInfo")
	@ResponseBody
	public BhResult lastWithdrawInfo(@RequestBody ShopWithdraw entity,HttpServletRequest request) {
		BhResult r = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = (Integer) sId;
			if (shopId == null) {
				shopId = 1;
			}
			entity.setShopId(shopId);
			Map<Object,Object>  map= shopManageService.lastWithdrawInfo(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(map);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("lastWithdrawInfo"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	/**
	 * @Description: 微信端：某商家最新一次提现记录展示
	 * @author xieyc
	 * @date 2018年8月15日 上午10:53:34 
	 */
	@RequestMapping("/mLastWithdrawInfo")
	@ResponseBody
	public BhResult mLastWithdrawInfo(@RequestBody ShopWithdraw entity,HttpServletRequest request) {
		BhResult r = null;
		try {
			MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
			if (user != null) {
				Long shopId = user.getShopId();
				if (shopId == null) {
					shopId = (long) 1;
				}
				entity.setShopId(shopId.intValue());
				Map<Object,Object>  map= shopManageService.mLastWithdrawInfo(entity);
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(map);
			}else{
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("mLastWithdrawInfo"+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	
	
	
	
	
}

