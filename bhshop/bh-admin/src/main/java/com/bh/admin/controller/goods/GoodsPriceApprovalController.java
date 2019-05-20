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
import com.alibaba.fastjson.JSON;
import com.bh.admin.mapper.goods.GoodsOperLogMapper;
import com.bh.admin.mapper.goods.GoodsPriceApprovalMapper;
import com.bh.admin.mapper.goods.GoodsSkuMapper;
import com.bh.admin.mapper.user.MemberShopMapper;
import com.bh.admin.pojo.goods.GoodsOperLog;
import com.bh.admin.pojo.goods.GoodsPriceApproval;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.goods.SimpleGoodsSku;
import com.bh.admin.pojo.goods.SimplePriceApproval;
import com.bh.admin.service.GoodsPriceApprovalService;
import com.bh.admin.util.JedisUtil;
import com.bh.admin.util.JedisUtil.Strings;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.JsonUtils;
import com.bh.utils.LoggerUtil;
import com.bh.utils.MoneyUtil;
import com.bh.utils.PageBean;

@Controller
@RequestMapping("/priceMsg")
public class GoodsPriceApprovalController {
	@Autowired
	private GoodsPriceApprovalService goodsPriceApprovalService;
	
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;
	@Autowired
	private MemberShopMapper memberShopMapper;
	@Autowired
	private GoodsOperLogMapper goodsOperLogMapper;
	@Autowired
	private GoodsPriceApprovalMapper goodsPriceApprovalMapper;
	
	
	
	/**
	 * cheng-20180316
	 * 价格的接收
	 * @param map
	 * @return
	 */
	@RequestMapping("/insertPriceApproval")
	@ResponseBody
	public BhResult insertPriceApproval(@RequestBody Map<String, Object> map,HttpServletRequest request) {
	   BhResult result = null;
	   try {
		 //cheng
		    String token = request.getHeader("token");
		    JedisUtil jedisUtil= JedisUtil.getInstance();  
		    JedisUtil.Strings strings=jedisUtil.new Strings();
		    String userJson = strings.get(token);
		    Map mapOne = JSON.parseObject(userJson, Map.class);
		    Integer userId = (Integer)mapOne.get("userId");
		    if(StringUtils.isBlank(String.valueOf(userId))){
			    userId = 1;
			}
		   
		    Object json = (Object) map.get("json");
		    if (json !=null) {
		    	String o = JsonUtils.objectToJson(json);
				List<SimpleGoodsSku> skuList = JsonUtils.jsonToList(o, SimpleGoodsSku.class);
				GoodsSku goods=goodsSkuMapper.selectByPrimaryKey(Integer.parseInt(skuList.get(0).getId()));
				for (SimpleGoodsSku simpleSku : skuList) {
					   GoodsSku goodsSku = new GoodsSku();
					   if (StringUtils.isEmpty(simpleSku.getId())) {
						result = new BhResult(400, "id不能为空", null);
					   }else{
						   goodsSku.setId(Integer.parseInt(simpleSku.getId()));
						   if (StringUtils.isNotEmpty(simpleSku.getMarketPrice())) {
							   Integer mPrice =(int) (MoneyUtil.yuan2Fen(simpleSku.getMarketPrice()));
							   goodsSku.setMarketPrice(mPrice);
						   }
						   if (StringUtils.isNotEmpty(simpleSku.getSellPrice())) {
							   Integer realPrice =(int) (MoneyUtil.yuan2Fen(simpleSku.getSellPrice()));
							   goodsSku.setSellPrice(realPrice);
						   }
						   if (StringUtils.isNotEmpty(simpleSku.getTeamPrice())) {
							   Integer tPrice =(int) (MoneyUtil.yuan2Fen(simpleSku.getTeamPrice()));
							   goodsSku.setTeamPrice(tPrice);
						   }
						   if (StringUtils.isNotEmpty(simpleSku.getStockPrice())) {
							   Integer stoPrice = (int) (MoneyUtil.yuan2Fen(simpleSku.getStockPrice()));
							   goodsSku.setStockPrice(stoPrice);
						   }
						   if (StringUtils.isNotEmpty(simpleSku.getDeliveryPrice())) {
							   Integer delP = (int) (MoneyUtil.yuan2Fen(simpleSku.getDeliveryPrice()));
							   goodsSku.setDeliveryPrice(delP);
						   }
						   goodsPriceApprovalService.insertGoodsPriceApproval(goodsSku, userId);
				       }
			      }
				GoodsOperLog goodsOperLog = new GoodsOperLog();
				goodsOperLog.setOpType("申请价格变动");
				goodsOperLog.setUserId(userId.toString());
				goodsOperLog.setOrderId("");
				String userName = memberShopMapper.selectUsernameBymId(userId); //查找操作人
				goodsOperLog.setAdminUser(userName);
				goodsOperLogMapper.insertGoodsById(goods.getGoodsId(), goodsOperLog);
		    }
			   result = new BhResult(200, "操作成功", null);
			   
		} catch (Exception e) {
			 e.printStackTrace();
			 result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * cheng-20180316
	 * 价格的审核
	 * @param id(GoodsPriceApproval表的id),status(0未审核  1通过  2未通过, 3价格已变更)
	 * @return
	 */
	@RequestMapping("/velifyStatus")
	@ResponseBody
	public BhResult velifyStatus(@RequestBody GoodsPriceApproval approval,HttpServletRequest request) {
	   BhResult result = null;
	   try {
			String token = request.getHeader("token");
		    JedisUtil jedisUtil= JedisUtil.getInstance();  
		    JedisUtil.Strings strings=jedisUtil.new Strings();
		    String userJson = strings.get(token);
		    Map mapOne = JSON.parseObject(userJson, Map.class);
		    Integer userId = (Integer) mapOne.get("userId");
		    approval.setApprovalId(userId);
		    approval.setApprovalTime(new Date());
		    
		   int row = 1;
		   //状态:0未审核  1通过  2未通过, 3价格已变更
			row = goodsPriceApprovalService.checkVelifyStatus(approval);
		   if (row == 1) {
			   result = new BhResult(200,"操作成功", null);
		   }else{
			   result = new BhResult(400,"更新失败了...", null);
		   }
		} catch (Exception e) {
			 e.printStackTrace();
			 result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * cheng-20180316
	 * 价格的接收列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/selectPriceApprovalList")
	@ResponseBody
	public BhResult selectPriceApprovalList(@RequestBody GoodsPriceApproval vApproval) {
	   BhResult result = null;
	   try {
		   if (StringUtils.isEmpty(vApproval.getCurrentPage())) {
			   vApproval.setCurrentPage("1");
		   } if (StringUtils.isEmpty(vApproval.getSize())) {
			vApproval.setSize("10");
		   }
			 if (vApproval.getGoodsSkuNo()!=null) {
				List<String> gskuList = JsonUtils.stringToList(vApproval.getGoodsSkuNo());
				if (gskuList.size() > 0) {
					vApproval.setList(gskuList);
				}
			}
			   PageBean<GoodsPriceApproval> list = goodsPriceApprovalService.selectPriceApprovalList(vApproval);
			   result = new BhResult(BhResultEnum.SUCCESS, list);
		   
		    
		} catch (Exception e) {
			 e.printStackTrace();
			 result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * cheng-20180316
	 * 价格的接收列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/deletePriceApproval")
	@ResponseBody
	public BhResult selectPriceApprovalList(@RequestBody Map<String, String> map,HttpServletRequest request) {
	   BhResult result = null;
	   try {
		   String token = request.getHeader("token");
		    JedisUtil jedisUtil= JedisUtil.getInstance();  
		    JedisUtil.Strings strings=jedisUtil.new Strings();
		    String userJson = strings.get(token);
		    Map mapOne = JSON.parseObject(userJson, Map.class);
		    Integer userId = (Integer)mapOne.get("userId");
		    if(StringUtils.isBlank(String.valueOf(userId))){
			    userId = 1;
			}
		   String ids = map.get("id");
		   List<String> id = JsonUtils.stringToList(ids);
		   
		   if (StringUtils.isNotEmpty(ids)) {
			   List<String> idList = JsonUtils.stringToList(ids);
			   goodsPriceApprovalService.deletePriceApproval(idList);
		   }
		    GoodsOperLog goodsOperLog = new GoodsOperLog();
			goodsOperLog.setOpType("价格变动删除");
			goodsOperLog.setUserId(userId.toString());
			goodsOperLog.setOrderId("");
			String userName = memberShopMapper.selectUsernameBymId(userId); //查找操作人
			goodsOperLog.setAdminUser(userName);
			GoodsPriceApproval goodsPriceApproval=goodsPriceApprovalMapper.selectByPrimaryKey(Integer.parseInt(id.get(0)));
			goodsOperLogMapper.insertGoodsById(goodsPriceApproval.getGoodsId(), goodsOperLog);
		   result = new BhResult(200,"操作成功", null);
		} catch (Exception e) {
			 e.printStackTrace();
			 result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*******************************************************************************************************/
	/**
	 * cheng-20180316
	 * 价格的接收列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/selectPriceApprovalList1")
	@ResponseBody
	public BhResult selectPrice(@RequestBody SimplePriceApproval vApproval,HttpServletRequest request) {
	   BhResult result = null;
	   try {
		   if (StringUtils.isEmpty(vApproval.getCurrentPage())) {
			   vApproval.setCurrentPage("1");
		   } if (StringUtils.isEmpty(vApproval.getSize())) {
			vApproval.setSize("10");
		   }
			 if (vApproval.getGoodsSkuNo()!=null) {
				List<String> gskuList = JsonUtils.stringToList(vApproval.getGoodsSkuNo());
				if (gskuList.size() > 0) {
					vApproval.setList(gskuList);
				}
			}
			 String token = request.getHeader("token");
				JedisUtil jedisUtil = JedisUtil.getInstance();
				JedisUtil.Strings strings = jedisUtil.new Strings();
				String userJson = strings.get(token);
				Map mapOne = JSON.parseObject(userJson, Map.class);
				Object sId = mapOne.get("shopId");
				Integer shopId = 0;
				if (sId != null) {
					shopId = (Integer) sId;
				}
				vApproval.setShopId(shopId);
			   PageBean<SimplePriceApproval> list = goodsPriceApprovalService.selectPriceApprovalList1(vApproval);
			   result = new BhResult(BhResultEnum.SUCCESS, list);
		   
		    
		} catch (Exception e) {
			 e.printStackTrace();
			 result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * cheng-20180316
	 * 价格的审核
	 * @param id(GoodsPriceApproval表的id),status(0未审核  1通过  2未通过, 3价格已变更)
	 * @return
	 */
	@RequestMapping("/velifyStatus1")
	@ResponseBody
	public BhResult velifyStatus1(@RequestBody Map<String, String> map ,HttpServletRequest request) {
	   BhResult result = null;
	   try {
		   
		    
		   String id = map.get("id");
		   String status = map.get("status");
		   String token = request.getHeader("token");
		    JedisUtil jedisUtil= JedisUtil.getInstance();  
		    JedisUtil.Strings strings=jedisUtil.new Strings();
		    String userJson = strings.get(token);
		    Map mapOne = JSON.parseObject(userJson, Map.class);
		    Integer userId = (Integer) mapOne.get("userId");
		   List<String> ids = JsonUtils.stringToList(id);
		   if (ids.size()>0) {
			  for (String string : ids) {
				    GoodsPriceApproval approval = new GoodsPriceApproval();
				    approval.setApprovalId(userId);
				    approval.setApprovalTime(new Date());
				    approval.setId(Integer.parseInt(string));
				    approval.setStatus(Integer.parseInt(status));
				    int row = 1;
					   //状态:0未审核  1通过  2未通过, 3价格已变更
					row = goodsPriceApprovalService.checkVelifyStatus(approval);
			  }
			    GoodsOperLog goodsOperLog = new GoodsOperLog();
				goodsOperLog.setOpType("平台商品价格变动审核");
				goodsOperLog.setUserId(userId.toString());
				goodsOperLog.setOrderId("");
				String userName = memberShopMapper.selectUsernameBymId(userId); //查找操作人
				goodsOperLog.setAdminUser(userName);
				GoodsPriceApproval goodsPriceApproval=goodsPriceApprovalMapper.selectByPrimaryKey(Integer.parseInt(ids.get(0)));
				goodsOperLogMapper.insertGoodsById(goodsPriceApproval.getGoodsId(), goodsOperLog);
			    result = new BhResult(200,"操作成功", null);
		 }
	} catch (Exception e) {
			 e.printStackTrace();
			 result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * cheng-20180316
	 * 价格的接收列表
	 * @param map
	 * @return
	 */
	@RequestMapping("/deletePriceApproval1")
	@ResponseBody
	public BhResult selectPriceApprovalList1(@RequestBody Map<String, String> map,HttpServletRequest request) {
	   BhResult result = null;
	   try {
		   String ids = map.get("id");
		   if (StringUtils.isNotEmpty(ids)) {
			   List<String> idList = JsonUtils.stringToList(ids);
			   goodsPriceApprovalService.deletePriceApproval(idList);
		   }
		   result = new BhResult(200,"操作成功", null);
		} catch (Exception e) {
			 e.printStackTrace();
			 result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	
}
