package com.bh.admin.controller.goods;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.admin.pojo.goods.AuctionGoodsInfo;
import com.bh.admin.pojo.goods.HollandDauction;
import com.bh.admin.pojo.goods.HollandDauctionLog;
import com.bh.admin.service.HollandDauctionService;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.PageBean;

@Controller
@RequestMapping("/hollandDauction")
public class HollandDauctionController {
	private static final Logger logger = LoggerFactory.getLogger(HollandDauctionController.class);
	@Autowired
	private HollandDauctionService hollandDauctionService;
	/**
	 * @Description: 拍卖时获取商品信息接口
	 * @author xieyc
	 * @date 2018年5月25日 下午12:00:54 
	 */
	@RequestMapping("/getBhShopGoodsInfo")
	@ResponseBody
	public AuctionGoodsInfo getBhShopGoodsInfo(@RequestBody Map<String,String> map) {
		AuctionGoodsInfo auctionGoodsInfo=null;
		try {
			String goodsId=map.get("goodsId");
			auctionGoodsInfo=hollandDauctionService.getBhShopGoodsInfo(Integer.valueOf(goodsId));
		} catch (Exception e) {
			logger.error("拍卖时获取商品信息接口"+e.getMessage());
			e.printStackTrace();
		}
		return auctionGoodsInfo;
	}
	@RequestMapping("/test")
	@ResponseBody
	public BhResult test(HttpServletRequest request,String a) {
		BhResult r = null;
		try {
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(a);
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
		}
		return r;
	}
	
	/**
	 * @Description: 荷兰式拍卖配置保存或更新
	 * @author xieyc
	 * @date 2018年5月21日 下午4:04:24 
	 * @param   
	 * @return  
	 */
	@RequestMapping("/saveOrUpdate")
	@ResponseBody
	public BhResult insert(@RequestBody  Map<String, String> map) {
		BhResult r = null;
		try {
			
			String goodsId=map.get("goodsId");
			String lowPrice=map.get("lowPrice");
			String scopePrice=map.get("scopePrice");
			String dauctionPrice=map.get("dauctionPrice");
			String storeNums=map.get("storeNums");
			String timeSection=map.get("timeSection");
			HollandDauction entity=new HollandDauction();
			entity.setGoodsId(Integer.valueOf(goodsId));//商品名字
			entity.setLowPrice((int)(Double.parseDouble(lowPrice)*10*10));//最低价
			entity.setDauctionPrice((int)(Double.parseDouble(dauctionPrice)*10*10));//拍卖价
			entity.setScopePrice((int)(Double.parseDouble(scopePrice)*10*10));//将价值
			entity.setStoreNums(Integer.valueOf(storeNums));//拍卖库存
			entity.setTimeSection(Integer.valueOf(timeSection));//时间区间
			
			int row = hollandDauctionService.saveOrUpdate(entity);
			if(row==1){
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(row);
			}else if(row==-1){
				r = BhResult.build(400, "该商品的实际库存小于拍卖库存,请重置拍卖库存");
			}else if(row==0){
				r = BhResult.build(400, "拍卖库存只允许改大不允许该小");
			}else if(row==2){
				r = BhResult.build(400, "拍卖库存要设置成大于0");
			}
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
		}
		return r;
	}
	
	/**
	 * @Description: 拍卖配置详情
	 * @author xieyc
	 * @date 2018年5月21日 下午5:47:37 
	 */
	@RequestMapping("/hollandDauctionDetail")
	@ResponseBody
	public BhResult hollandDauctionDetail(@RequestBody Map<String, String> map) {
		BhResult r = null;
		try {
			String goodsId = map.get("goodsId");
			HollandDauction hollandDauction= hollandDauctionService.hollandDauctionDetail(Integer.parseInt(goodsId));
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(hollandDauction);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
	}

	/**
	 * @Description: pc端拍卖日志列表记录
	 * @author xieyc
	 * @date 2018年5月21日 下午7:19:40 
	 */
	@RequestMapping("/hollandDauctionLogList")
	@ResponseBody
	public BhResult hollandDauctionLogList(@RequestBody HollandDauctionLog entity) {
		BhResult r = null;
		try {
			PageBean<HollandDauctionLog> page  = hollandDauctionService.hollandDauctionLogList(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}
		return r;
	}
}
