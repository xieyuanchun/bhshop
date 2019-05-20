package com.bh.product.api.controller;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.Goods;
import com.bh.jd.api.JDGoodsApi;
import com.bh.jd.bean.JdResult;
import com.bh.jd.bean.goods.CategoryList;
import com.bh.jd.bean.goods.CategoryVo;
import com.bh.jd.bean.goods.SearchResult;
import com.bh.jd.bean.goods.SellPriceResult;
import com.bh.jd.bean.goods.SimilarSku;
import com.bh.jd.bean.goods.Sku;
import com.bh.product.api.service.JDGoodsService;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;

import net.sf.json.JSONArray;
@Controller
@RequestMapping("/api")
public class JDGoodsController {
	
	@Autowired
	private JDGoodsService jdGoodsService;
	/**
	 * 3.1 获取商品池编号接口
	 * @param map
	 * @return
	 */
	@RequestMapping("/product/getPageNum")
	@ResponseBody
	public BhResult getPageNum(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			
			result = new BhResult(BhResultEnum.SUCCESS, JDGoodsApi.getPageNum());
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 3.2 获取池内商品编号接口
	 * @param map
	 * @return
	 */
	@RequestMapping("/product/getSku")
	@ResponseBody
	public BhResult getSku(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String pageNum = map.get("pageNum");
			result = new BhResult(BhResultEnum.SUCCESS, JDGoodsApi.getSku(pageNum));
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	

	/**
	 * 3.3 获取池内商品编号接口-品类商品池
	 * @param map
	 * @return
	 */
	@RequestMapping("/product/getSkuByPage")
	@ResponseBody
	public BhResult getSkuByPage(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String pageNum = map.get("pageNum");
			String pageNo = map.get("pageNo");
			if (StringUtils.isEmpty(pageNo)) {
				result = new BhResult(BhResultEnum.FAIL, "参数不能为空");
			}if (StringUtils.isEmpty(pageNum)) {
				result = new BhResult(BhResultEnum.FAIL, "参数不能为空");
			}else{
				result = new BhResult(BhResultEnum.SUCCESS, JDGoodsApi.getSkuByPage(pageNum, pageNo));
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 3.4 获取商品详细信息接口
	 * @param map
	 * @return
	 */
	@RequestMapping("/product/getDetail")
	@ResponseBody
	public BhResult getDetail(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String sku = map.get("sku");
			String isShow = map.get("isShow");
			result = new BhResult(BhResultEnum.SUCCESS, JDGoodsApi.getDetail(sku, isShow));
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * 3.12 商品搜索接口
	 * @param map
	 * @return
	 */
	@RequestMapping("/search/search")
	@ResponseBody
	public BhResult search(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String keyword = map.get("keyword");
			String catId = map.get("catId");
			String pageIndex = map.get("pageIndex");
			String pageSize = map.get("pageSize");
			String min = map.get("min");
			String max = map.get("max");
			String brands = map.get("brands");
			SearchResult s = jdGoodsService.search(keyword, catId, pageIndex, pageSize, min, max, brands);
			result = new BhResult(BhResultEnum.SUCCESS, s);
		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * 3.15 查询分类信息接口
	 * @param map
	 * @return
	 */
	@RequestMapping("/product/getCategory")
	@ResponseBody
	public BhResult getCategory(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			//参数："1", "10", "670", "1"
			String cid = map.get("cid");
		    result = new BhResult();
		    CategoryVo vo  =jdGoodsService.getCategory(cid);
			result = new BhResult(200, "查询成功", vo);
		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	
	/**
	 * 3.16 查询分类信息接口
	 * @param map
	 * @return
	 */
	@RequestMapping("/product/getCategorys")
	@ResponseBody
	public BhResult getCategorys(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			//参数："1", "10", "670", "1"
			//String pageNo, String pageSize, String parentId, String catClass
			String pageNo = map.get("pageNo");
			String pageSize= map.get("pageSize");
			String parentId = map.get("parentId");
			String catClass = map.get("catClass");
		    result = new BhResult();
		     CategoryList map1 =jdGoodsService.getCategorys(pageNo,pageSize,parentId,catClass);
			result = new BhResult(200, "查询成功", map1);
		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	
	
	/**
	 * 3.16 查询分类信息接口
	 * @param map
	 * @return
	 */
	@RequestMapping("/product/getCategorys1")
	@ResponseBody
	public BhResult getCategorys1(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			//参数："1", "10", "670", "1"
			//String pageNo, String pageSize, String parentId, String catClass
			String pageNo = map.get("pageNo");
			String pageSize= map.get("pageSize");
		    result = new BhResult();
		    List<CategoryVo> list =null; //=jdGoodsService.getCategorys1(pageNo,pageSize);
		    
			result = new BhResult(200, "11", list);
		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 3.17 同类商品查询
	 * @param map
	 * @return
	 */
	@RequestMapping("/product/getSimilarSku")
	@ResponseBody
	public BhResult getSimilarSku(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String sku = map.get("sku");
		    result = new BhResult(BhResultEnum.SUCCESS, JDGoodsApi.getSimilarSku(sku));
		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20171220-01
	 * 京东商品摘取
	 * @param map
	 * @return
	 */
	@RequestMapping("/product/getJdGoods")
	@ResponseBody
	public BhResult getJdGoods(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String sku = map.get("sku");
			if(JDGoodsApi.getSellPrice(sku)==null || JDGoodsApi.getSellPrice(sku).isEmpty()){
				result = new BhResult(BhResultEnum.GOODS_NOT_EXIT, null);
				return result;
			}
			if(JDGoodsApi.skuState(sku).equals("0")){
				result = new BhResult(BhResultEnum.GOODS_IS_DOWN, null);
				return result;
			}
		    List<Goods> goods  =jdGoodsService.getJdGoods(sku);
		    if(goods!=null){
		    	result = new BhResult(BhResultEnum.SUCCESS, goods);
		    }else{
		    	result = new BhResult(BhResultEnum.IS_GET, null);
		    }
		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20171225-01
	 * 京东商品价格变更
	 * @param map
	 * @return
	 */
	@RequestMapping("/product/getPriceChange")
	@ResponseBody
	public BhResult getPriceChange(){
		BhResult result = null;
		try {
		    result = new BhResult(BhResultEnum.SUCCESS, jdGoodsService.getPriceChange());
		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20180109-01
	 * 京东商品价格变更消息提示
	 * @param map
	 * @return
	 */
	@RequestMapping("/product/notice")
	@ResponseBody
	public BhResult notice(){
		BhResult result = null;
		try {
		    result = new BhResult(BhResultEnum.SUCCESS, jdGoodsService.notice());
		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20180109-02
	 * 价格变更短息发送
	 * @param map
	 * @return
	 */
	@RequestMapping("/product/sendMessage")
	@ResponseBody
	public BhResult sendMessage(){
		BhResult result = null;
		try {
			jdGoodsService.sendMessage();
			result = new BhResult(BhResultEnum.SUCCESS, null);
		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20171228-01
	 * 获取商品组合sku属性值
	 * @param map
	 * @return
	 */
	@RequestMapping("/product/getSkuValue")
	@ResponseBody
	public BhResult getSkuValue(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String sku = map.get("sku");
			List<String> str = jdGoodsService.getSkuValue(sku);
			if(str!=null){
				result = new BhResult(BhResultEnum.SUCCESS, str);
			}else{
				result = new BhResult(BhResultEnum.GAIN_FAIL, str);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * SCJ-20180102-01
	 * 京东规格参数copy
	 * @param map
	 * @return
	 */
	@RequestMapping("/product/getParam")
	@ResponseBody
	public BhResult getParam(@RequestBody Map<String, Object> map){
		BhResult result = null;
		try {
			JSONArray list = JSONArray.fromObject(map.get("list"));
			String goodsId = (String)map.get("goodsId");
			int row = jdGoodsService.insertJdParam(list, goodsId);
			if(row ==1){
				result = new BhResult(BhResultEnum.SUCCESS, null);
			}else{
				result = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * 3.4 根据skuId获取商品详细信息接口(分类id/品牌id/品牌名字)
	 * @param map
	 * @return
	 */
	@RequestMapping("/product/getDetailBySkuId")
	@ResponseBody
	public BhResult getDetailBySkuId(@RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			//调用京东获取商品详细信息接口
			String sku = map.get("sku");
			String isShow = map.get("isShow");
			JdResult<Sku> result2 = JDGoodsApi.getDetailBySkuID(sku, isShow);
			if(result2==null){
			}
//	        String  retJsonStr2 = null;
//			try {
//				retJsonStr2 = new String(result2.getResult().getBrandName().getBytes("GBK"),"UTF-8");
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
			//调用京东商品搜索接口
			if (result2.getSuccess()) {
				String keyword = "";
				String catId = "";
				String pageIndex = map.get("pageIndex");
				String pageSize = "30";
				String min = "";
				String max = "";
				String brands = result2.getResult().getBrandName();
				SearchResult s = jdGoodsService.search(keyword, catId, pageIndex, pageSize, min, max, brands);
				result = new BhResult(BhResultEnum.SUCCESS, s);
			}else {
				result = new BhResult(400,result2.getResultMessage(), null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * SCJ-20180316-03
	 * jd数据导入
	 * @param map
	 * @return
	 */
	@RequestMapping("/batchAddJdGoods")
	@ResponseBody
	public BhResult batchAddJdGoods(){
		BhResult r = null;
		try {
			jdGoodsService.batchAddJdGoods();
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	/**
	 * scj
	 * 测试京东param数据迁移
	 */
	@RequestMapping("/insertModelValue")
	@ResponseBody
	public BhResult insertModelValue(){
		BhResult r = null;
		try {
			String htmlStr = "<table width='100%' cellspacing='1' cellpadding='0' border='0' class='Ptable'><tbody><tr><th colspan='2' class='tdTitle'>主体</th></tr><tr></tr><tr><td class='tdTitle'>品牌</td><td>雷蛇 Razer</td></tr><tr><td class='tdTitle'>类型</td><td>有线鼠标</td></tr><tr><td class='tdTitle'>型号</td><td>地狱狂蛇镜面特别版</td></tr><tr><td class='tdTitle'>颜色</td><td>黑色</td></tr><tr><th colspan='2' class='tdTitle'>规格</th></tr><tr></tr><tr><td class='tdTitle'>传输方式</td><td>线缆</td></tr><tr><td class='tdTitle'>人体工学</td><td>否</td></tr><tr><td class='tdTitle'>鼠标工作方式</td><td>光电</td></tr><tr><td class='tdTitle'>鼠标分辨率</td><td>3500dpi</td></tr><tr><td class='tdTitle'>接口</td><td>USB</td></tr><tr><td class='tdTitle'>鼠标尺寸</td><td>115(L) x 63(W) x 40(H) mm</td></tr><tr><td class='tdTitle'>重量</td><td>0.09kg</td></tr></tbody></table>";
			jdGoodsService.insertModelValue(htmlStr, 1, "京东分类名称", "1");
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	/**
	 * scj
	 * 测试京东param数据迁移
	 */
	@RequestMapping("/insertItemModelValue")
	@ResponseBody
	public BhResult insertItemModelValue(){
		BhResult r = null;
		try {
			String htmlStr = "<table width='100%' cellspacing='1' cellpadding='0' border='0' class='Ptable'><tbody><tr><th colspan='2' class='tdTitle'>主体</th></tr><tr></tr><tr><td class='tdTitle'>品牌</td><td>雷蛇 Razer</td></tr><tr><td class='tdTitle'>类型</td><td>有线鼠标</td></tr><tr><td class='tdTitle'>型号</td><td>地狱狂蛇镜面特别版</td></tr><tr><td class='tdTitle'>颜色</td><td>黑色</td></tr><tr><th colspan='2' class='tdTitle'>规格</th></tr><tr></tr><tr><td class='tdTitle'>传输方式</td><td>线缆</td></tr><tr><td class='tdTitle'>人体工学</td><td>否</td></tr><tr><td class='tdTitle'>鼠标工作方式</td><td>光电</td></tr><tr><td class='tdTitle'>鼠标分辨率</td><td>3500dpi</td></tr><tr><td class='tdTitle'>接口</td><td>USB</td></tr><tr><td class='tdTitle'>鼠标尺寸</td><td>115(L) x 63(W) x 40(H) mm</td></tr><tr><td class='tdTitle'>重量</td><td>0.09kg</td></tr></tbody></table>";
			int goodsId = 7217;
			jdGoodsService.insertItemModelVaue(htmlStr, goodsId);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	
	/**
	 * SCJ-20180327-01
	 * 京东商品sku搜索
	 * @param map
	 * @return
	 */
	@RequestMapping("/jdSkuNoSearch")
	@ResponseBody
	public BhResult jdSkuNoSearch(@RequestBody Map<String, String> map){
		BhResult r = null;
		try {
			String skuNo = map.get("skuNo");
			SearchResult s = jdGoodsService.jdSkuNoSearch(skuNo);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(s);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
	
	/**
	 * SCJ-20180329-01
	 * 复制sku判断是否已存在
	 * @param map
	 * @return
	 */
	@RequestMapping("/checkSkuNoExist")
	@ResponseBody
	public BhResult checkSkuNoExist(@RequestBody Map<String, String> map){
		BhResult r = null;
		try {
			String goodsId = map.get("goodsId");
			String jdSkuNo = map.get("jdSkuNo");
			Map<String, Object> b = jdGoodsService.batchAddJdGoods(goodsId, jdSkuNo);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(b);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}
}
