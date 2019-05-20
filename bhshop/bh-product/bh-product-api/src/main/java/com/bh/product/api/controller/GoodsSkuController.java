package com.bh.product.api.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsBrand;
import com.bh.goods.pojo.GoodsSku;
import com.bh.product.api.service.GoodsBrandService;
import com.bh.product.api.service.GoodsSkuService;
import com.bh.product.api.util.JedisUtil;
import com.bh.result.BhResult;
import com.bh.utils.JsonUtils;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.mysql.jdbc.StringUtils;


import net.sf.json.JSONArray;

@Controller
@RequestMapping("/goodsSku")
public class GoodsSkuController {
	
	@Autowired
	private GoodsSkuService service;
	
	@Value(value = "${pageSize}")
	private String pageSize;
	
	/**
	 * SCJ-20171010-01
	 * 根据商品id查询所有sku
	 * @param reid
	 * @return
	 */
	@RequestMapping("/getListByGoodsId")
	@ResponseBody
	public BhResult getListByGoodsId(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String goodsId = map.get("goodsId");
			List<GoodsSku> page = service.getListByGoodsId(goodsId);
			if(page!=null){
				result = new BhResult(page);
			}else{
				result = BhResult.build(400, "暂无数据！");
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20171009-01
	 * 新增goodsSku
	 * @param map
	 * @return
	 */
	@RequestMapping("/addGoodsSku")
	@ResponseBody
	public BhResult addGoodsSku(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String goodsId = map.get("goodsId"); //商品id
			String value = map.get("value"); //规格属性
			String storeNums = map.get("storeNums"); //库存
			String marketPrice = map.get("marketPrice"); //市场价单位分
			String sellPrice = map.get("sellPrice"); //销售价单位分
			String weight = map.get("weight"); //重量
			String minimum = map.get("minimum"); //起订量
			int row = service.addGoodsSku(goodsId, value, storeNums, marketPrice, sellPrice, weight, minimum);
			if(row==0){
				result = BhResult.build(400, "sku新增失败！", null);
			}else{
				result = BhResult.build(200, "sku新增成功！", null);
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库添加失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * SCJ-20171012-03
	 * 批量新增goodsSku
	 * @param map
	 * @return
	 */
	@RequestMapping("/batchAddGoodsSku")
	@ResponseBody
	public BhResult batchAddGoodsSku(@RequestBody Map<String, Object> map){
		BhResult result = null;
		try {
			String goodsId = (String) map.get("goodsId");
			JSONArray list = JSONArray.fromObject(map.get("list"));
			int row = service.batchAddGoodsSku(goodsId, list);
			if(row==0){
				result = BhResult.build(400, "sku新增失败！", null);
			}else{
				result = BhResult.build(200, "sku新增成功！", null);
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库添加失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20180206-02
	 * 批量修改goodsSku
	 * @param map
	 * @return
	 */
	@RequestMapping("/batchUpdateGoodsSku")
	@ResponseBody
	public BhResult batchUpdateGoodsSku(@RequestBody Map<String, Object> map){
		BhResult r = null;
		try {
			JSONArray list = JSONArray.fromObject(map.get("list"));
			service.batchUpdateGoodsSku(list);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return r;
		}
	}
	
	/**
	 * SCJ-20171010-02
	 * 修改goodsSku
	 * @param map
	 * @return
	 */
	@RequestMapping("/editGoodsSku")
	@ResponseBody
	public BhResult editGoodsSku(@RequestBody Map<String, String> map,HttpServletRequest request){
		BhResult result = null;
		try {
			String id = map.get("id"); //商品id
			String value = map.get("value"); //规格属性
			String storeNums = map.get("storeNums"); //库存
			String marketPrice = map.get("marketPrice"); //市场价单位分
			String sellPrice = map.get("sellPrice"); //销售价单位分
			String weight = map.get("weight"); //重量
			String minimum = map.get("minimum"); //起订量
			String teamPrice = map.get("teamPrice"); //团购价
			String jdPrice = map.get("jdPrice"); //京东价格
			String jdBuyPrice = map.get("jdBuyPrice"); //客户购买价格
			String jdOldBuyPrice = map.get("jdOldBuyPrice"); //客户购买价格(旧)
			String jdProtocolPrice = map.get("jdProtocolPrice"); //京东价格
			String jdSupport = map.get("jdSupport"); //是否支持京东下单，0不支持，1支持
			String jdSkuNo = map.get("jdSkuNo"); //京东商品编码
			String jdUpc = map.get("jdUpc"); //条形码
			String goodsSkuName = map.get("goodsSkuName");
			String score = map.get("score");
			String keyOne = map.get("keyOne");
			String valueOne = map.get("valueOne");
			String keyTwo = map.get("keyTwo");
			String valueTwo = map.get("valueTwo");
			String keyThree = map.get("keyThree");
			String valueThree = map.get("valueThree");
			
			//cheng
			String token = request.getHeader("token");
		    JedisUtil jedisUtil= JedisUtil.getInstance();  
		    JedisUtil.Strings strings=jedisUtil.new Strings();
		    String userJson = strings.get(token);
		    Map mapOne = JSON.parseObject(userJson, Map.class);
		    Integer userId = (Integer) mapOne.get("userId");
			
			int row = service.editGoodsSku(id, value, storeNums, marketPrice, sellPrice, weight, minimum, teamPrice,
					jdPrice, jdBuyPrice, jdOldBuyPrice, jdProtocolPrice, jdSupport, jdSkuNo, jdUpc, goodsSkuName, score,
					keyOne, valueOne, keyTwo, valueTwo, keyThree, valueThree,userId);
			if(row==0){
				result = BhResult.build(400, "sku修改失败！", null);
			}else{
				result = BhResult.build(200, "sku修改成功！", null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "数据库访问失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * SCJ-20171010-03
	 * 删除goodsSku
	 * @param map
	 * @return
	 */
	@RequestMapping("/deleteGoodsSku")
	@ResponseBody
	public BhResult deleteGoodsSku(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String id = map.get("id"); //商品id
			int row = service.deleteGoodsSku(id);
			if(row==-1){
				result = BhResult.build(400, "此sku为最后一条记录不允许删除");
			}else if(row == 0){
				result = new BhResult(BhResultEnum.FAIL, null);
			}else{
				result = new BhResult(BhResultEnum.SUCCESS, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * SCJ-20180118-01
	 * 选择商品sku
	 * @param map
	 * @return
	 */
	@RequestMapping("/bargainUse")
	@ResponseBody
	public BhResult bargainUse(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String id = map.get("id"); //商品id
			List<GoodsSku> row = service.bargainUse(Integer.parseInt(id));
			if(row!=null){
				result = new BhResult(BhResultEnum.SUCCESS, row);
			}else{
				result = new BhResult(BhResultEnum.GAIN_FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * cheng-20180327-01
	 * 未上架前修改商品的sku
	 * @param map
	 * @return
	 */
	@RequestMapping("/batchUpdateGoodsSkuBySkuId")
	@ResponseBody
	public BhResult batchUpdateGoodsSkuBySkuId(@RequestBody Map<String, Object> map){
		BhResult r = null;
		try {
			JSONArray list = JSONArray.fromObject(map.get("list"));
			Integer goodsId = (Integer) map.get("goodsId");
			service.batchUpdateGoodsSkuBySkuId(list,String.valueOf(goodsId));
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return r;
		}
	}
}
