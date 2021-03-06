
package com.bh.product.api.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.TopicGoods;
import com.bh.goods.pojo.UserAttendance;
import com.bh.order.pojo.OrderSeed;
import com.bh.product.api.service.BhTopicService;
import com.bh.product.api.service.QianDaoService;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberUser;
import com.bh.user.pojo.MemerScoreLog;
import com.bh.user.pojo.MyTime;
import com.bh.user.pojo.SeedModel;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;

@RestController
@RequestMapping("/topic")
public class BhTopicController {

	@Autowired
	private BhTopicService bhTopicService;
	@Autowired
	private QianDaoService qianDaoService;

	// 前端显示列表
	@RequestMapping("/goodsList")
	public BhResult goodsList(@RequestBody TopicGoods entity) {
		BhResult r = null;
		try {

			PageBean<TopicGoods> list = bhTopicService.selectGoodsListByBhBean(entity);
			Map<String, Object> map = new HashMap<>();
			map.put("list", list);
			map.put("waiTime", bhTopicService.waiTime());
			map.put("images", bhTopicService.selectTopicImage());
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(map);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}

	// 查询所有的可用滨惠豆抵消的商品
	@RequestMapping("/discountedList")
	public BhResult discountedList(@RequestBody GoodsSku entity) {
		BhResult r = null;
		try {

			PageBean<GoodsSku> list = bhTopicService.selectDouGoods(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(list);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

	}

	/**
	 * CHENG-201801-22 已累计签到的天数接口
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/attendancedays")
	@ResponseBody
	public BhResult attendanceDays(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			HttpSession session=request.getSession(false);
			if (session!=null) {
				Member member =(Member) session.getAttribute(Contants.USER_INFO_ATTR_KEY); // 获取当前登录用户信息
				if (member != null) {
					MemerScoreLog log = new MemerScoreLog();
					log.setmId(member.getId());// 用户ID
					log.setSmId(0);// 积分规则ID
					UserAttendance attendance = new UserAttendance();
					attendance = bhTopicService.isAttendances(log);
					result = new BhResult(BhResultEnum.SUCCESS, attendance);
				} else {
					result = new BhResult(BhResultEnum.LOGIN_FAIL, null);
				}
			}else{
				return BhResult.build(BhResultEnum.LOGIN_FAIL);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.LOGIN_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * CHENG-201801-22 用户签到的接口
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/attendances")
	@ResponseBody
	public BhResult attendances(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			HttpSession session=request.getSession(false);
			if (session!=null) {
				Member member = (Member) session.getAttribute(Contants.USER_INFO_ATTR_KEY);
				if (member != null) {
					MemerScoreLog log = new MemerScoreLog();
					log.setmId(member.getId());
					int row = 0;
					row = bhTopicService.attendances(log);
					if (row == 0) {
						result = new BhResult(BhResultEnum.FAIL, null);
					} else if (row == 1) {
						result = new BhResult(200, "签到成功", null);
					} else if (row == 2) {
						result = new BhResult(400, "今天已签到过了哦,明天再来吧!", null);
					}
				} else {
					result = new BhResult(BhResultEnum.LOGIN_FAIL, null);
				}
			}else{
				return BhResult.build(BhResultEnum.LOGIN_FAIL);
			}

		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	
	/** 
	 * 根据日期获得所在周的日期  
	 * @param mdate 
	 * @return 
	 */  
	@SuppressWarnings("deprecation")  
	public static List<Date> dateToWeek(Date mdate) {  
	    int b = mdate.getDay();  
	    Date fdate;  
	    List<Date> list = new ArrayList<Date>();  
	    Long fTime = mdate.getTime() - b * 24*3600000;  
	    for(int a = 1; a <= 7; a++) {  
	        fdate = new Date();  
	        fdate.setTime(fTime + (a * 24*3600000)); //一周从周日开始算，则使用此方式  
	        list.add(a-1, fdate);  
	    }  
	    return list;  
	}  
	  
	  
	/** 
	 * 测试 
	 * @param args 
	 */  
	public static void main(String[] args) {  
	    // 定义输出日期格式  
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	    Date currentDate = new Date();  
	    // 比如今天是2015-02-03  
	    List<Date> days = dateToWeek(currentDate);  
	    System.out.println("今天的日期: " + sdf.format(currentDate));  
	    for(Date date : days) {  
	        System.out.println(sdf.format(date));  
	    }  
	}
	
	/**
	 * CHENG-201806-27 滨惠豆明细列表
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/beansdetail")
	@ResponseBody
	public BhResult beansDetail(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			HttpSession session=request.getSession(false);
			if (session==null) {
				return BhResult.build(BhResultEnum.LOGIN_FAIL);
			}else{
				Member member =(Member) session.getAttribute(Contants.USER_INFO_ATTR_KEY); // 获取当前登录用户信息
				if (member != null) {
					String currentPage=map.get("currentPage");
					String pageSize=map.get("pageSize");
					//log的值:如果为0代表查询所有的记录,如果为1代表查询的是获取记录,如果为2代表查询的是使用记录(默认0)
					String log=map.get("log");
					//time的值:如果为2代表查询的是近3个月的记录,如果为0代表查询的是本月的记录,如果为的话代表查询上一个月的记录,
					String time=map.get("time");
					Map<String, Object> myMap = bhTopicService.selectBeanList(member.getId(),currentPage,pageSize,log,time);
					result = new BhResult(BhResultEnum.SUCCESS, myMap);
				} else {
					result = new BhResult(BhResultEnum.LOGIN_FAIL, null);
				}
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.LOGIN_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * CHENG-201806-27 查询时间列表
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/searchtime")
	@ResponseBody
	public BhResult searchtime(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			List<MyTime> list = bhTopicService.searchTime();
			result = new BhResult(BhResultEnum.SUCCESS, list);
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.LOGIN_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * CHENG-201801-22 已累计签到的天数接口
	 * 
	 * @param map()
	 * @return
	 */
	@RequestMapping("/att")
	@ResponseBody
	public BhResult att(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			HttpSession session=request.getSession(false);
			if (session!=null) {
				Member member =(Member) session.getAttribute(Contants.USER_INFO_ATTR_KEY); // 获取当前登录用户信息
				if (member != null) {
					MemerScoreLog log = new MemerScoreLog();
					log.setmId(member.getId());// 用户ID
					log.setSmId(0);// 积分规则ID
					UserAttendance attendance = new UserAttendance();
					attendance = qianDaoService.getAtt(log);
					result = new BhResult(BhResultEnum.SUCCESS, attendance);
				} else {
					result = new BhResult(BhResultEnum.LOGIN_FAIL, null);
				}
			}else{
				return BhResult.build(BhResultEnum.LOGIN_FAIL);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.LOGIN_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}

}
