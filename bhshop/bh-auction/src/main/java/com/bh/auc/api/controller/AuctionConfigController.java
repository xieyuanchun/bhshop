package com.bh.auc.api.controller;

import com.bh.auc.api.service.AuctionConfigService;
import com.bh.auc.pojo.AuctionConfig;
import com.bh.auc.pojo.AuctionHistory;
import com.bh.auc.pojo.BhShopGoodsInfo;
import com.bh.auc.vo.AuctionHistoryVo;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.MoneyUtil;
import com.bh.utils.PageBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@Controller
@RequestMapping("/auctionConfig")
public class AuctionConfigController {
	private static final Logger logger = LoggerFactory.getLogger(AuctionConfigController.class);
	@Autowired
	private AuctionConfigService auctionConfigService;


	/**
	 * @Description:测试
	 * @author xieyc
	 * @date 2018年5月21日 下午5:47:37
	 */
	@RequestMapping("/test")
	@ResponseBody
	public BhResult test() {
		BhResult r = null;
		try {
			auctionConfigService.test();
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(null);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * @Description: 测试获取滨惠商城商品信息
	 * @author xieyc
	 * @date 2018年5月21日 下午5:47:37
	 */
	@RequestMapping("/getBhShopGoodsInfo")
	@ResponseBody
	public BhResult getBhShopGoodsInfo(@RequestBody Map<String, String> map) {
		BhResult r = null;
		try {
			String goodsId = map.get("goodsId");// 商品id
			BhShopGoodsInfo bhShopGoodsInfo = auctionConfigService.getBhShopGoodsInfo(Integer.valueOf(goodsId));
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(bhShopGoodsInfo);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}


	/**
	 * @Description: 去拍卖接口（将商品设置成拍卖商品）
	 * @author xieyc
	 * @date 2018年5月21日 下午5:47:37
	 */
	@RequestMapping("/goAuction")
	@ResponseBody
	public BhResult goAuction(@RequestBody Map<String, String> map) {
		BhResult r = null;
		try {
			String goodsId = map.get("goodsId");//商品id
			String storeNum=map.get("storeNum");//要送去的拍卖库存
			String sysCode=map.get("sysCode");//商品系统来源标识
			int row= auctionConfigService.goAuction(goodsId,storeNum,sysCode);
			if(row==1){
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(row);
			}else if(row==-2){
				r = BhResult.build(400, "拍卖库存要设置大于0");
			}else if(row==-1){
				r = BhResult.build(400, "库存不足");
			}else if(row==-3){
				r = BhResult.build(400, "请先下架拍卖商品,在设置拍卖库存");
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	/**
	 * @Description: 设置拍卖配置
	 * @author xieyc
	 * @date 2018年5月21日 下午4:04:24
	 */
	@RequestMapping("/setAuctionConfig")
	@ResponseBody
	public BhResult insert(@RequestBody Map<String, String> map) {
		BhResult r = null;
		try {
			String goodsId=map.get("goodsId");
			String lowPrice=map.get("lowPrice");
			String scopePrice=map.get("scopePrice");
			String actPrice=map.get("actPrice");
			String timeSection=map.get("timeSection");
			String cashDeposit=map.get("cashDeposit");
			String highPrice=map.get("highPrice");

			AuctionConfig entity=new AuctionConfig();
			entity.setGoodsId(Integer.valueOf(goodsId));//商品名字
			entity.setLowPrice(MoneyUtil.yuan2Fen(lowPrice));//最低价
			entity.setActPrice(MoneyUtil.yuan2Fen(actPrice));//拍卖价(起拍价格)
			entity.setScopePrice(MoneyUtil.yuan2Fen(scopePrice));//将价值
			entity.setTimeSection(Integer.valueOf(timeSection));//时间区间
			entity.setCashDeposit(MoneyUtil.yuan2Fen(cashDeposit));//保证金
			entity.setHighPrice(MoneyUtil.yuan2Fen(highPrice));//最高价

			int row = auctionConfigService.setAuctionConfig(entity);
			if(row==1){
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(row);
			}else if(row==-1){
				r = BhResult.build(400, "参数配置不正确");
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	/**
	 * @Description: 拍卖配置详情
	 * @author xieyc
	 * @date 2018年5月21日 下午5:47:37
	 */
	@RequestMapping("/auctionConfigDetail")
	@ResponseBody
	public BhResult auctionConfigDetail(@RequestBody Map<String, String> map) {
		BhResult r = null;
		try {
			String goodsId = map.get("goodsId");
			AuctionConfig auctionConfig= auctionConfigService.auctionConfigDetail(Integer.parseInt(goodsId));
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(auctionConfig);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * @Description: 拍卖商品列表
	 * @author xieyc
	 * @date 2018年5月21日 下午7:19:40
	 */
	@RequestMapping("/auctionGoodList")
	@ResponseBody
	public BhResult auctionGoodList(@RequestBody AuctionConfig entity) {
		BhResult r = null;
		try {
			PageBean<AuctionConfig> page  = auctionConfigService.auctionGoodList(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	/**
	 * @Description: 上架或下降拍卖商品
	 * @author xieyc
	 * @date 2018年5月21日 下午5:47:37
	 */
	@RequestMapping("/upDownAuctionGoods")
	@ResponseBody
	public BhResult upDownAuctionGoods(@RequestBody Map<String, String> map) {
		BhResult r = null;
		try {
			String upDownStatus = map.get("upDownStatus");
			String id = map.get("id");
			int row= auctionConfigService.upDownAuctionGoods(Integer.parseInt(id),Integer.parseInt(upDownStatus));

			if(row==1){
				r = new BhResult();
				r.setStatus(BhResultEnum.SUCCESS.getStatus());
				r.setMsg(BhResultEnum.SUCCESS.getMsg());
				r.setData(row);
			}else if(row==-1){
				r = BhResult.build(400, "请在设置完拍卖参数或设置正确的参数后再上架");
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * @Description: 每期的拍卖记录列表
	 * @author xieyc
	 * @date 2018/7/23 10:49
	 */
	@RequestMapping("/auctionHistoryList")
	@ResponseBody
	public BhResult auctionHistoryList(@RequestBody AuctionHistory entity) {
		BhResult r = null;
		try {
			PageBean<AuctionHistoryVo> page  = auctionConfigService.auctionHistoryList(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("每期的拍卖记录列表："+e.getMessage());
			e.printStackTrace();
		}
		return r;
	}

}
