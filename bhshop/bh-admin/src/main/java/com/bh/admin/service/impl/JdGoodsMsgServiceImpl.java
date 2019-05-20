package com.bh.admin.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.admin.controller.goods.GoodsController;
import com.bh.admin.mapper.goods.GoodsMapper;
import com.bh.admin.mapper.goods.GoodsSkuMapper;
import com.bh.admin.mapper.goods.JdGoodsMapper;
import com.bh.admin.mapper.goods.JdGoodsMsgMapper;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.goods.JdGoods;
import com.bh.admin.pojo.goods.JdGoodsMsg;
import com.bh.admin.service.JdGoodsMsgService;
import com.bh.jd.api.JDGoodsApi;
import com.bh.jd.bean.goods.AddOrDeleteResult;
import com.bh.jd.bean.goods.Detail;
import com.bh.jd.bean.goods.MessageAddOrDelete;
import com.bh.jd.bean.goods.MessageResult;
import com.bh.jd.bean.goods.SellPriceResult;
import com.bh.jd.bean.goods.UpOrDown;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
@Service
public class JdGoodsMsgServiceImpl implements JdGoodsMsgService{
	private static final Logger logger = LoggerFactory.getLogger(JdGoodsMsgServiceImpl.class);
	@Autowired
	private JdGoodsMsgMapper mapper;
	@Autowired
	private JdGoodsMapper jdGoodsMapper;
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	
	//上下架消息
	@Override
	public int receiveUpOrDownMsg() throws Exception {
		int row = 0;
		int status = 0;
		List<MessageResult> list = JDGoodsApi.getUpOrDownMessage("4");
		if(list.size()>0){
			for(MessageResult result : list){
				JdGoodsMsg entity = new JdGoodsMsg();
				entity.setJdMsgId(result.getId()); //京东消息id
				entity.setIsLook(0);// 是否已阅，0否，1是
				entity.setAddTime(new Date()); //添加时间
				
				entity.setSendTime(result.getTime());
				UpOrDown upOrdown = result.getResult();
				entity.setJdSkuNo(upOrdown.getSkuId()); //京东商品编码
				
				JdGoods jd = new JdGoods();
				jd.setJdSkuNo(upOrdown.getSkuId());
				
				status = upOrdown.getState(); //上下架状态 1上架，0下架
				if(status==0){
					entity.setMsgType(3); //消息类型，2上架，3下架
					entity.setMsgContent("京东商品"+upOrdown.getSkuId()+"已下架");
				}else if(status==1){
					entity.setMsgType(2);
					entity.setMsgContent("京东商品"+upOrdown.getSkuId()+"已上架");
				}else{
					entity.setMsgContent("上下架状态异常");
				}
				
				JdGoodsMsg msg = new JdGoodsMsg();
				msg.setJdMsgId(result.getId());
				List<JdGoodsMsg> msgList = mapper.listPage(msg);
				if(msgList.size()>0){ //消息已存在
					JDGoodsApi.deleteMessage(result.getId().toString());//删除京东消息推送
				}else{
					row = mapper.insertSelective(entity); //插入消息记录
					List<JdGoods> jdGoodsList = jdGoodsMapper.getByJdSkuNo(jd);
					if(jdGoodsList.size()>0){
						for(JdGoods jdGoods : jdGoodsList){
							if(status==0){
								jdGoods.setIsUp(0);
								jdGoods.setIsGet(0);
							}else{
								jdGoods.setIsUp(1);
							}
							row = jdGoodsMapper.updateByPrimaryKeySelective(jdGoods); //更新京东商品表上下架状态
						}
					}
					if(status==0){//当京东商品下架时处理商品逻辑
						try {
							List<GoodsSku> skuList = goodsSkuMapper.isExistByJdSkuNo(upOrdown.getSkuId());
							if(skuList.size()>0){
								long jdSkuNo = 0;
								List<GoodsSku> goodsSkuList = null;
								Goods goods = null;
								for(GoodsSku sku : skuList){
									goodsSkuList = goodsSkuMapper.selectListByGoodsIdAndStatus(sku.getGoodsId());
									if(goodsSkuList.size()==1){
										goods = goodsMapper.selectByPrimaryKey(sku.getGoodsId());
										goods.setStatus(1);
										goods.setIsJd(0);
										row = goodsMapper.updateByPrimaryKeySelective(goods);
										sku.setJdSkuNo(jdSkuNo);
										sku.setJdSupport(0);
										sku.setStatus(1);
										row = goodsSkuMapper.updateByPrimaryKeySelective(sku);
									}else if(goodsSkuList.size()>1){
										sku.setJdSkuNo(jdSkuNo);
										sku.setJdSupport(0);
										sku.setStatus(1);
										row = goodsSkuMapper.updateByPrimaryKeySelective(sku);
									}else{
										logger.info("京东商品上下架sku异常");
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							logger.info("京东商品上下架操作商品上下架异常");
						}
					}
				}
			}
		}
		return row;
	}
	
	//删除新增消息
	@Override
	public int receiveDeleteOrAddMsg() throws Exception {
		int row = 0;
		int status = 0;
		List<MessageAddOrDelete> list = JDGoodsApi.getDeleteOrAddMessage();
		if(list.size()>0){
			for(MessageAddOrDelete result : list){
				JdGoodsMsg msg = new JdGoodsMsg();
				msg.setJdMsgId(result.getId());
				List<JdGoodsMsg> msgList = mapper.listPage(msg);
				if(msgList.size()>0){ //消息已存在
					JDGoodsApi.deleteMessage(result.getId().toString());//删除京东消息推送
				}else{
					JdGoodsMsg entity = new JdGoodsMsg();
					entity.setJdMsgId(result.getId()); //京东消息id
					entity.setIsLook(0);// 是否已阅，0否，1是
					entity.setAddTime(new Date()); //添加时间
					entity.setSendTime(result.getTime());
					
					AddOrDeleteResult addOrDeleteResult = result.getResult();
					entity.setJdSkuNo(addOrDeleteResult.getSkuId()); //京东商品编码
					
					JdGoods jd = new JdGoods();
					jd.setJdSkuNo(addOrDeleteResult.getSkuId());
					jd.setPoolNum(addOrDeleteResult.getPage_num().toString());
					
					status = addOrDeleteResult.getState(); //状态，1添加，2删除
					if(status==1){
						entity.setMsgType(4); //消息类型，4新增，5删除
						entity.setMsgContent("商品池:"+addOrDeleteResult.getPage_num()+"中,新增商品"+addOrDeleteResult.getSkuId());
						//商品入库
						insertJdGoods(addOrDeleteResult.getSkuId().toString(), addOrDeleteResult.getPage_num().toString());
					}else if(status==2){
						entity.setMsgType(5); 
						entity.setMsgContent("商品池:"+addOrDeleteResult.getPage_num()+"中"+addOrDeleteResult.getSkuId()+",已删除");
						
						try {//当京东商品下架时处理商品逻辑
							List<GoodsSku> skuList = goodsSkuMapper.isExistByJdSkuNo(addOrDeleteResult.getSkuId());
							if(skuList.size()>0){
								long jdSkuNo = 0;
								List<GoodsSku> goodsSkuList = null;
								Goods goods = null;
								for(GoodsSku sku : skuList){
									goodsSkuList = goodsSkuMapper.selectListByGoodsIdAndStatus(sku.getGoodsId());
									if(goodsSkuList.size()==1){
										goods = goodsMapper.selectByPrimaryKey(sku.getGoodsId());
										goods.setStatus(1);
										goods.setIsJd(0);
										row = goodsMapper.updateByPrimaryKeySelective(goods);
										sku.setJdSkuNo(jdSkuNo);
										sku.setJdSupport(0);
										sku.setStatus(1);
										row = goodsSkuMapper.updateByPrimaryKeySelective(sku);
									}else if(goodsSkuList.size()>1){
										sku.setJdSkuNo(jdSkuNo);
										sku.setJdSupport(0);
										sku.setStatus(1);
										row = goodsSkuMapper.updateByPrimaryKeySelective(sku);
									}else{
										logger.info("京东商品上下架sku异常");
									}
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
							logger.info("京东商品上下架操作商品上下架异常");
						}
					}else{
						entity.setMsgContent("删除新增状态异常");
					}
					
					row = mapper.insertSelective(entity); //插入消息记录
					List<JdGoods> jdGoodsList = jdGoodsMapper.getByJdSkuNoAndPoolNum(jd);
					if(jdGoodsList.size()>0){
						if(status==2){
							for(JdGoods jdGoods : jdGoodsList){
								row = jdGoodsMapper.deleteByPrimaryKey(jdGoods.getId());
							}
						}
					}
				}
			}
		}
		return row;
	}
	
	private int insertJdGoods(String jdSkuNo, String poolNum){
		int row = 0;
		JdGoods entity = new JdGoods();
		entity.setPoolNum(poolNum);
		entity.setJdSkuNo(Long.valueOf(jdSkuNo));
		List<JdGoods> jd = jdGoodsMapper.getByJdSkuNoAndPoolNum(entity);
		if(jd.size()==0){
			Detail detail = JDGoodsApi.getDetail(jdSkuNo, "false");
			JdGoods jdGoods = new JdGoods();
			jdGoods.setPoolNum(poolNum);//所属商品池编号
			jdGoods.setJdSkuNo(Long.valueOf(jdSkuNo));//京东商品编码
			jdGoods.setGoodsImage(detail.getImagePath()); //商品主图
			jdGoods.setIsUp(detail.getState());//上架状态0下架，1上架
			jdGoods.setBrandName(detail.getBrandName()); //品牌名称
			jdGoods.setCatId(detail.getCategory()); //商品分类id
			jdGoods.setGoodsName(detail.getName());//商品名称
			jdGoods.setIsDelete(0); //是否删除0正常，1已删除
			jdGoods.setAddTime(new Date());//添加时间
			jdGoods.setEditTime(new Date());//编辑时间
			
			List<SellPriceResult> priceList = JDGoodsApi.getPrice(jdSkuNo);
			if(priceList.size()>0){
				jdGoods.setJdPrice((int)(priceList.get(0).getJdPrice()*100)); //京东价
				jdGoods.setStockPrice((int)(priceList.get(0).getPrice()*100)); //进货价
			}
			
			List<GoodsSku> jdSku = goodsSkuMapper.selectByJdSkuNo(Long.valueOf(jdSkuNo)); //判断京东商品是否已存在数据库
			if(jdSku.size()>0){
				jdGoods.setIsGet(1);//是否入库，0否，1是
			}else{
				jdGoods.setIsGet(0);
			}
			row = jdGoodsMapper.insertSelective(jdGoods);
		}
		return row;
	}
	
	//价格变更消息
	@Override
	public int receivePriceChangeMsg() throws Exception {
		int row = 0;
		List<MessageResult> list = JDGoodsApi.getUpOrDownMessage("2");
		if(list != null && list.size()>0){
			for(MessageResult result : list){
				JdGoodsMsg entity = new JdGoodsMsg();
				entity.setJdMsgId(result.getId()); //京东消息id
				entity.setIsLook(0);// 是否已阅，0否，1是
				entity.setAddTime(new Date()); //添加时间
				entity.setSendTime(result.getTime());
				
				UpOrDown upOrdown = result.getResult();
				entity.setJdSkuNo(upOrdown.getSkuId()); //京东商品编码
				
				JdGoods jd = new JdGoods();
				jd.setJdSkuNo(upOrdown.getSkuId());
				entity.setMsgType(1);
				entity.setMsgContent("京东商品:"+upOrdown.getSkuId()+"价格变动");
				
				JdGoodsMsg msg = new JdGoodsMsg();
				msg.setJdMsgId(result.getId());
				List<JdGoodsMsg> msgList = mapper.listPage(msg);
				if(msgList.size()>0){ //消息已存在
					JDGoodsApi.deleteMessage(result.getId().toString());//删除京东消息推送
				}else{
					row = mapper.insertSelective(entity); //插入消息记录
				}
				
				List<JdGoods> jdGoodsList = jdGoodsMapper.getByJdSkuNo(jd);
				List<SellPriceResult> priceList = JDGoodsApi.getPrice(upOrdown.getSkuId().toString());
				if(jdGoodsList.size()>0){
					for(JdGoods jdGoods : jdGoodsList){
						if(priceList.size()>0){
							jdGoods.setJdPrice((int)(priceList.get(0).getJdPrice()*100)); //京东价
							jdGoods.setStockPrice((int)(priceList.get(0).getPrice()*100)); //进货价
							row = jdGoodsMapper.updateByPrimaryKeySelective(jdGoods); //同步价格
						}
					}
				}
			}
		}
		return row;
	}
	
	//参数及介绍变更消息
	@Override
	public int introduceChangeMsg() throws Exception {
		int row = 0;
		List<MessageResult> list = JDGoodsApi.getUpOrDownMessage("16");
		if(list.size()>0){
			for(MessageResult result : list){
				JdGoodsMsg entity = new JdGoodsMsg();
				entity.setJdMsgId(result.getId()); //京东消息id
				entity.setIsLook(0);// 是否已阅，0否，1是
				entity.setAddTime(new Date()); //添加时间
				entity.setSendTime(result.getTime());
				
				UpOrDown upOrdown = result.getResult();
				entity.setJdSkuNo(upOrdown.getSkuId()); //京东商品编码
				
				JdGoods jd = new JdGoods();
				jd.setJdSkuNo(upOrdown.getSkuId());
				entity.setMsgType(6);
				entity.setMsgContent("京东商品:"+upOrdown.getSkuId()+"规格参数及介绍变更");
				
				JdGoodsMsg msg = new JdGoodsMsg();
				msg.setJdMsgId(result.getId());
				List<JdGoodsMsg> msgList = mapper.listPage(msg);
				if(msgList.size()>0){ //消息已存在
					JDGoodsApi.deleteMessage(result.getId().toString());//删除京东消息推送
				}else{
					row = mapper.insertSelective(entity); //插入消息记录
				}
			}
		}
		return row;
	}
	
	//已阅
	@Override
	public int isLook(JdGoodsMsg entity) throws Exception {
		JdGoodsMsg msg = mapper.selectByPrimaryKey(entity.getId());
		msg.setIsLook(1); //已阅
		//JDGoodsApi.deleteMessage(msg.getJdMsgId().toString());//删除京东消息推送
		return mapper.updateByPrimaryKeySelective(msg);
	}
	
	//列表管理
	@Override
	public PageBean<JdGoodsMsg> listPage(JdGoodsMsg entity) throws Exception {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Integer.parseInt(entity.getPageSize()), true);
		List<JdGoodsMsg> list = mapper.listPage(entity);
		PageBean<JdGoodsMsg> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	//删除
	@Override
	public int delete(JdGoodsMsg entity) throws Exception {
		return mapper.deleteByPrimaryKey(entity.getId());
	}
	
	/**
	 * 京东商品下架删除逻辑处理Test 
	 */
	@Override
	public int jdDownDeleteTest(JdGoodsMsg entity) throws Exception {
		int row = 0;
		String[] jdSkuNoStr = entity.getJdSkuNoStr();
		if(jdSkuNoStr.length>0){
			for (int i = 0; i < jdSkuNoStr.length; i++) {
				List<GoodsSku> skuList = goodsSkuMapper.isExistByJdSkuNo(Long.valueOf(jdSkuNoStr[i]));
				if(skuList.size()>0){
					long jdSkuNo = 0;
					List<GoodsSku> goodsSkuList = null;
					Goods goods = null;
					for(GoodsSku sku : skuList){
						goodsSkuList = goodsSkuMapper.selectListByGoodsIdAndStatus(sku.getGoodsId());
						if(goodsSkuList.size()==1){
							goods = goodsMapper.selectByPrimaryKey(sku.getGoodsId());
							goods.setStatus(1);
							goods.setIsJd(0);
							row = goodsMapper.updateByPrimaryKeySelective(goods);
							sku.setJdSkuNo(jdSkuNo);
							sku.setJdSupport(0);
							sku.setStatus(1);
							row = goodsSkuMapper.updateByPrimaryKeySelective(sku);
						}else if(goodsSkuList.size()>1){
							sku.setJdSkuNo(jdSkuNo);
							sku.setJdSupport(0);
							sku.setStatus(1);
							row = goodsSkuMapper.updateByPrimaryKeySelective(sku);
						}else{
							logger.info("京东商品上下架sku异常");
						}
					}
					
					JdGoods jg = new JdGoods();
					jg.setJdSkuNo(Long.valueOf(jdSkuNoStr[i]));
					List<JdGoods> jdGoodsList = jdGoodsMapper.getByJdSkuNo(jg);
					if(jdGoodsList.size()>0){
						for(JdGoods jdGoods : jdGoodsList){
							jdGoods.setIsGet(0);
							row = jdGoodsMapper.updateByPrimaryKeySelective(jdGoods); //更新京东商品表上下架状态
						}
					}
				}
			}
		}
		return row;
	}
}
