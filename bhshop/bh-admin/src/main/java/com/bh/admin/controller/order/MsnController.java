package com.bh.admin.controller.order;

import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bh.admin.mapper.order.MsnApplyMapper;
import com.bh.admin.mapper.user.MemberShopMapper;
import com.bh.admin.pojo.goods.ExportGoods;
import com.bh.admin.pojo.goods.GoodsOperLog;
import com.bh.admin.pojo.order.MsnApply;
import com.bh.admin.pojo.order.MyMsnApply;
import com.bh.admin.pojo.order.Phone;
import com.bh.admin.pojo.user.MBusEntity;
import com.bh.admin.pojo.user.MemberShop;
import com.bh.admin.service.MsnService;
import com.bh.admin.util.ExcelFileGenerator;
import com.bh.admin.util.GoodsExcelUtil;
import com.bh.admin.util.JedisUtil;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.PageBean;
import com.mysql.jdbc.StringUtils;

/**
 * @author fanjh
 * @Description: 短信群发
 * @date 2018年9月11日 下午2:00:25
 */
@Controller
@RequestMapping("/msn")
public class MsnController {

	private static final Logger logger = LoggerFactory.getLogger(MsnController.class);
	@Autowired
	private MsnService msnService;
	@Autowired
	private MsnApplyMapper msnApplyMapper;
	@Autowired
	private MemberShopMapper memberShopMapper;

	/**
	 * @Description: 进入短信群发的页面
	 * @author fanjh
	 * @date 2018年9月11日 下午2:00:25
	 */
	@RequestMapping("/getMsnShopInfo")
	@ResponseBody
	public BhResult getMsnShopInfo(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = 1;
			if (sId != null) {
				shopId = (Integer) sId;
			}
			String memberType = map.get("custMember");
			Map map1 = msnService.getMsnShopInfo(shopId, memberType);
			if (map1 != null) {
				result = new BhResult(BhResultEnum.SUCCESS, map1);
			} else {
				result = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @Description: 保存群发信息并支付
	 * @author fanjh
	 * @date 2018年9月11日 下午2:00:25
	 */
	@RequestMapping("/saveMsnApply")
	@ResponseBody
	public BhResult saveMsnApply(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = 1;
			if (sId != null) {
				shopId = (Integer) sId;
			}
			String template = map.get("template"); // 内容
			String groupNum = map.get("groupNum"); // 群发人数
			String memberType = map.get("custMember"); // 群发对象
			String isFreeNum = map.get("isFreeNum"); // 是否使用免费次数 0-不使用 1-使用
			String money = map.get("money"); // 支付金额
			String apymsnId = map.get("apymsnId");
			String jsonStr = msnService.saveMsnApply(shopId, template, groupNum, memberType, isFreeNum, money,
					apymsnId);
			if (jsonStr.equals("1")) {
				result = new BhResult(400, "本月次数已用完", null);
			} else if (jsonStr.equals("2")) {
				result = new BhResult(400, "免费次数已用完", null);
			} else {
				result = new BhResult(200, "操作成功", jsonStr);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @Description: 判断是否已经支付
	 * @author fanjh
	 * @date 2018年9月12日 下午2:00:25
	 */
	@RequestMapping("/checkIsPay")
	@ResponseBody
	public BhResult checkIsPay(@RequestBody Map<String, String> map, HttpServletRequest request,
			HttpServletResponse response) {
		BhResult bhResult = null;
		try {
			String orderNo = map.get("orderNo");
			if (StringUtils.isNullOrEmpty(orderNo)) {
				bhResult = new BhResult(400, "orderNo参数不能为空", null);
			} else {
				MsnApply msnApply = msnService.checkIsPaySeccuss(orderNo);
				bhResult = new BhResult(200, "操作成功", msnApply.getPayStatus());
			}
		} catch (Exception e) {
			bhResult = new BhResult(500, "操作失败", null);
			e.printStackTrace();
		}
		return bhResult;
	}

	/**
	 * @Description: 审核
	 * @author fanjh
	 * @date 2018年9月12日 下午2:00:25
	 */
	@RequestMapping("/audit")
	@ResponseBody
	public BhResult audit(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = 1;
			if (sId != null) {
				shopId = (Integer) sId;
			}
			Object uId = mapOne.get("userId");
			Integer userId = 1;
			if (uId != null) {
				userId = (Integer) uId;
			}
			String apymsnId = map.get("apymsnId");
			String reviewResult = map.get("reviewResult");// 商城审核状态 2-审核通过 3-审核拒绝
			String reviewResultRemkar = map.get("reviewResultRemkar");// 审核备注
			Boolean flag = msnService.audit(shopId, userId, apymsnId, reviewResult, reviewResultRemkar);
			if (flag == true) {
				bhResult = new BhResult(BhResultEnum.SUCCESS, flag);
			} else {
				bhResult = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			bhResult = new BhResult(500, "操作失败", null);
			e.printStackTrace();
		}
		return bhResult;
	}

	/**
	 * @Description: 查看信息
	 * @author fanjh
	 * @date 2018年9月12日 下午2:00:25
	 */
	@RequestMapping("/viewInfo")
	@ResponseBody
	public BhResult viewInfo(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult bhResult = null;
		try {
			String apymsnId = map.get("apymsnId");
			MyMsnApply myMsnApply = msnService.viewInfo(Integer.valueOf(apymsnId));
			if (myMsnApply != null) {
				bhResult = new BhResult(BhResultEnum.SUCCESS, myMsnApply);
			} else {
				bhResult = new BhResult(BhResultEnum.FAIL, null);
			}

		} catch (Exception e) {
			e.printStackTrace();
			bhResult = new BhResult(500, "操作失败", null);
		}
		return bhResult;
	}

	/**
	 * 商家查询群发记录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getShopListInfo")
	@ResponseBody
	public BhResult getShopListInfo(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult r = null;
		try {
			String token = request.getHeader("token");
			JedisUtil jedisUtil = JedisUtil.getInstance();
			JedisUtil.Strings strings = jedisUtil.new Strings();
			String userJson = strings.get(token);
			Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
			Integer shopId = 1;
			if (sId != null) {
				shopId = (Integer) sId;
			}
			String currentPage = map.get("currentPage");
			String startTime = map.get("startTime");
			String endTime = map.get("endTime");
			String payStatus = map.get("payStatus");
			String reviewResult = map.get("reviewResult");
			PageBean<MyMsnApply> msnListList = msnService.getShopListInfo(shopId, startTime, endTime, payStatus,
					reviewResult, currentPage);
			if (msnListList != null) {
				r = new BhResult(BhResultEnum.SUCCESS, msnListList);
			} else {
				r = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			r = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * 平台查询群发记录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/getListInfo")
	@ResponseBody
	public BhResult getListInfo(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult r = null;
		try {
			String currentPage = map.get("currentPage");
			String startTime = map.get("startTime");
			String endTime = map.get("endTime");
			String payStatus = map.get("payStatus");
			String reviewResult = map.get("reviewResult");
			PageBean<MyMsnApply> msnListList = msnService.getListInfo(startTime, endTime, payStatus, reviewResult,
					currentPage);
			if (msnListList != null) {
				r = new BhResult(BhResultEnum.SUCCESS, msnListList);
			} else {
				r = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			r = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * @Description: 移动端进入短信群发的页面
	 * @author fanjh
	 * @date 2018年9月11日 下午2:00:25
	 */
	@RequestMapping("/mgetMsnShopInfo")
	@ResponseBody
	public BhResult mgetMsnShopInfo(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
			if (user == null) {
				return new BhResult(100, "您还未登录,请重新登录", null);
			} else {
				if (user.getShopId() == null) {
					user.setShopId((long) 1);
				}
				long l1 = user.getShopId();
				Integer shopId = (int) l1;
				String memberType = map.get("custMember");
				Map map1 = msnService.getMsnShopInfo(shopId, memberType);
				if (map1 != null) {
					result = new BhResult(BhResultEnum.SUCCESS, map1);
				} else {
					result = new BhResult(BhResultEnum.FAIL, null);
				}
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * @Description: 移动端保存群发信息并支付
	 * @author fanjh
	 * @date 2018年9月11日 下午2:00:25
	 */
	@RequestMapping("/msaveMsnApply")
	@ResponseBody
	public BhResult msaveMsnApply(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult result = null;
		try {
			MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
			if (user == null) {
				return new BhResult(100, "您还未登录,请重新登录", null);
			} else {
				if (user.getShopId() == null) {
					user.setShopId((long) 1);
				}
				long l1 = user.getShopId();
				Integer shopId = (int) l1;
				String template = map.get("template"); // 内容
				String groupNum = map.get("groupNum"); // 群发人数
				String memberType = map.get("custMember"); // 群发对象
				String isFreeNum = map.get("isFreeNum"); // 是否使用免费次数 0-不使用 1-使用
				String money = map.get("money"); // 支付金额
				String apymsnId = map.get("apymsnId");
				String jsonStr = msnService.saveMsnApply(shopId, template, groupNum, memberType, isFreeNum, money,
						apymsnId);
				if (jsonStr != null) {
					result = new BhResult(BhResultEnum.SUCCESS, jsonStr);
				} else {
					result = new BhResult(BhResultEnum.FAIL, null);
				}
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 移动端商家查询群发记录
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/mgetShopListInfo")
	@ResponseBody
	public BhResult mgetShopListInfo(@RequestBody Map<String, String> map, HttpServletRequest request) {
		BhResult r = null;
		try {
			MBusEntity user = (MBusEntity) request.getSession().getAttribute(Contants.MUSER);
			if (user == null) {
				return new BhResult(100, "您还未登录,请重新登录", null);
			} else {
				if (user.getShopId() == null) {
					user.setShopId((long) 1);
				}
				long l1 = user.getShopId();
				Integer shopId = (int) l1;
				String currentPage = map.get("currentPage");
				PageBean<MyMsnApply> msnListList = msnService.mgetShopListInfo(shopId, currentPage);
				if (msnListList != null) {
					r = new BhResult(BhResultEnum.SUCCESS, msnListList);
				} else {
					r = new BhResult(BhResultEnum.FAIL, null);
				}
			}
		} catch (Exception e) {
			r = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * <p>
	 * Description: 导出手机号码excel
	 * </p>
	 * 
	 * @author fanjh
	 * @date 2018年9月13日
	 */
	/*@RequestMapping(value = "/excelExportPhone", method = RequestMethod.GET)
	public void excelExportPhone(String apymsnId, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		try {
			MsnApply msnApply = msnApplyMapper.selectByPrimaryKey(Integer.valueOf(apymsnId));
			MemberShop memberShop = memberShopMapper.selectByPrimaryKey(msnApply.getShopId());
			ArrayList<String> fieldName = new ArrayList<>();
			fieldName.add("手机号码"); // 手机号码
			ArrayList<ArrayList<String>> fieldData = msnService.excelExportPhone(Integer.valueOf(apymsnId));
			ExcelFileGenerator generator = new ExcelFileGenerator(fieldName, fieldData);
			OutputStream os = response.getOutputStream();
			Date date = new Date();
			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String filename = apymsnId + "_" + memberShop.getShopName() + "（" + df.format(date).replace(":", "_")
					+ "）.xls";
			filename = ExcelFileGenerator.processFileName(request, filename);
			// 可以不加，但是保证response缓冲区没有任何数据，开发时建议加上
			response.reset();
			response.setContentType("application/vnd.ms-excel;charset=utf-8");
			response.setHeader("Content-disposition", "attachment;filename=" + filename);
			response.setBufferSize(1024);
			*//** 将生成的excel报表，写到os中 *//*
			generator.expordExcel(os);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}*/
	
	
	 @RequestMapping(value="/excelExportPhone",method = RequestMethod.GET)  
	   public void excelExportTwo(String apymsnId, HttpServletRequest request, HttpServletResponse response) throws Exception{  
		   try {
			   MsnApply msnApply = msnApplyMapper.selectByPrimaryKey(Integer.valueOf(apymsnId));
			   MemberShop memberShop = memberShopMapper.selectByPrimaryKey(msnApply.getShopId());
			   List<Phone> list =msnService.excelExportPhone(Integer.valueOf(apymsnId));
			   Date date = new Date();
			   SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		       String fileName = apymsnId + "_" + memberShop.getShopName() + "（" + df.format(date).replace(":", "_") +"）";  
		       //fileName = ExcelFileGenerator.processFileName(request,fileName);  
			   String[] colTitle = GoodsExcelUtil.TdTitle1();
			   String path = "C:\\Users\\pc\\Desktop";
			   GoodsExcelUtil.writer1(path, fileName, "xlsx", list, colTitle);
		  } catch (Exception e) {
			  e.printStackTrace();
			// TODO: handle exception
		  }
	   }
	

}
