package com.order.job;

import com.bh.config.Contants;
import com.bh.goods.mapper.GoodsCommentMapper;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.pojo.CommentSimple;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsComment;
import com.bh.goods.pojo.GoodsCommentWithBLOBs;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderSendInfoMapper;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderSendInfo;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.utils.JsonUtils;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHander;
import com.xxl.job.core.log.XxlJobLogger;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 任务Handler的一个Demo（Bean模式） 开发步骤：
 * 1、新建一个继承com.xxl.job.core.handler.IJobHandler的Java类；
 * 2、该类被Spring容器扫描为Bean实例，如加“@Component”注解； 3、添加
 * “@JobHander(value="自定义jobhandler名称")”注解，注解的value值为自定义的JobHandler名称，
 * 该名称对应的是调度中心新建任务的JobHandler属性的值。 4、执行日志：需要通过 "XxlJobLogger.log" 打印执行日志；
 *
 *
 *
 * @author xieyc
 * @Description:系统自动评论：商品-订单确认收，且7天没有发生售后行为，+3天则自动好评；如果产生售后，则不再自动好评
 * @date 2018年7月17日 下午3:06:51
 */
@JobHander(value = "systemCommentGoodsJob")
@Component
public class SystemCommentGoodsJob extends IJobHandler {
	private final static String ENCODE = "UTF-8";
	@Autowired
	private OrderSkuMapper orderSkuMapper;
	@Autowired
	private OrderShopMapper orderShopMapper;
	@Autowired
	private OrderSendInfoMapper orderSendInfoMapper;
	@Autowired
	private GoodsCommentMapper goodsCommentMapper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	
	@Override
	public synchronized ReturnT<String> execute(String... params) throws Exception {
		XxlJobLogger.log("XXL-JOB, Hello World.");
		try {
			List<OrderSku> list = orderSkuMapper.systemCommentGoods();
			for (OrderSku orderSku : list) {
				Order order=orderMapper.selectByPrimaryKey(orderSku.getOrderId());
				String json="[{\"id\":"+orderSku.getId()+",\"star\":5,\"remark\":\"此用户未填写评论内容\",\"images\":[],\"add\":0,\"packLevel\":5,\"speedLevel\":5,\"sServiceLevel\":5,\"notname\":0}]";
				List<CommentSimple> commentSimple = JsonUtils.jsonToList(json, CommentSimple.class);
				this.insertGoodsComment(commentSimple,order.getmId());
			}
		} catch (Exception e) {
			XxlJobLogger.log("系统自动评论  error=" + e.getMessage());
			e.printStackTrace();
		}
		return ReturnT.SUCCESS;
	}
	/**
	 * @Description: 添加商品评价
	 * @author xieyc
	 * @date 2018年8月16日 下午3:49:24 
	 */
	public int insertGoodsComment(List<CommentSimple> commentSimples, Integer mId) throws Exception {
		int row = 0;
		for (int i = 0; i < commentSimples.size(); i++) {

			GoodsCommentWithBLOBs goodsCommentWithBLOB = new GoodsCommentWithBLOBs();
			OrderSendInfo orderSendInfo = new OrderSendInfo();

			Integer id = commentSimples.get(i).getId();
			Integer start = commentSimples.get(i).getStar();
			String remark = commentSimples.get(i).getRemark();
			String pic[] = commentSimples.get(i).getImages();
			Integer add = commentSimples.get(i).getAdd();
			Integer speedLevel = commentSimples.get(i).getSpeedLevel();
			Integer sServiceLevel = commentSimples.get(i).getsServiceLevel();
			Integer packLevel = commentSimples.get(i).getPackLevel();
			Integer notname = commentSimples.get(i).getNotname();

			OrderSku orderSku = orderSkuMapper.selectByPrimaryKey(id);
			OrderSendInfo orderSendInfo2 = new OrderSendInfo();
			orderSendInfo2.setmId(mId);
			orderSendInfo2.setOrderShopId(orderSku.getOrderShopId());
			List<OrderSendInfo> sendInfo = orderSendInfoMapper.getSendInfo(orderSendInfo2);

			if (start != null) {
				goodsCommentWithBLOB.setStar(start);
				switch (start) {
				case 5:

				case 4:
					Integer i2 = 1;
					byte le = (byte) i2.intValue();
					goodsCommentWithBLOB.setLevel(le);
					break;
				case 3:

				case 2:
					Integer i3 = 2;
					byte le1 = (byte) i3.intValue();
					goodsCommentWithBLOB.setLevel(le1);
					break;
				case 1:

				case 0:
					Integer i4 = 3;
					byte le2 = (byte) i4.intValue();
					goodsCommentWithBLOB.setLevel(le2);
					break;

				default:
					break;
				}
			}
			if (StringUtils.isNotEmpty(remark)) {
				String remark2 = URLEncoder.encode(remark, "utf-8");

				goodsCommentWithBLOB.setDescription(remark2);
			}
			if (pic.length > 0) {

				String s = org.apache.commons.lang.StringUtils.join(pic, ',');

				// String pics = Arrays.toString(pic);
				goodsCommentWithBLOB.setImage(s);
			}
			if (add == null) {
				add = 0;
			}
			if (speedLevel != null) {
				orderSendInfo.setSpeedLevel(speedLevel);
			}
			if (sServiceLevel != null) {
				orderSendInfo.setsServiceLevel(sServiceLevel);
			}
			if (packLevel != null) {
				orderSendInfo.setPackLevel(packLevel);
			}
			if (notname != null) {
				goodsCommentWithBLOB.setNotname(notname);
			}

			goodsCommentWithBLOB.setAddtime(new Date());
			goodsCommentWithBLOB.setGoodsId(orderSku.getGoodsId());
			goodsCommentWithBLOB.setShopId(orderSku.getShopId());
			goodsCommentWithBLOB.setmId(mId);
			goodsCommentWithBLOB.setOrderId(orderSku.getOrderId());
			goodsCommentWithBLOB.setOrderSkuId(id);
			goodsCommentWithBLOB.setSkuValue(orderSku.getSkuValue());

			orderSendInfo.setOcTime(new Date());

			goodsCommentWithBLOB.setIsDel(0);
			GoodsComment goodsComment = new GoodsComment();
			goodsComment.setmId(mId);
			goodsComment.setIsAddEvaluate(add);
			goodsComment.setOrderSkuId(id);
			GoodsComment g1 = goodsCommentMapper.selectByParams(goodsComment);

			if (add == 0) {
				OrderSku orderSku2 = new OrderSku();
				orderSku2 = orderSkuMapper.selectByPrimaryKey(id);

				OrderSku record1 = new OrderSku();
				record1.setOrderShopId(orderSku2.getOrderShopId());
				OrderSku record2 = new OrderSku();
				record2.setOrderId(goodsCommentWithBLOB.getOrderId());
				List<OrderSku> t = orderSkuMapper.selectOrderSkuByParams2(record1);// 如果该商家下的所有商品已评价，则将order_shop的表的status=7
				List<OrderSku> t1 = orderSkuMapper.selectOrderSkuByParams2(record2);// 如果该orderId下面的所有商品都已评价，则ordermain
																					// 表订单状态status·变成已完成3，用户评价的时候控制

				goodsCommentWithBLOB.setIsAddEvaluate(0);
				goodsCommentWithBLOB.setReid(0);
				if (g1 != null) {
					goodsComment.setId(g1.getId());
					goodsCommentWithBLOB.setId(g1.getId());
					row = goodsCommentMapper.updateByPrimaryKeySelective(goodsCommentWithBLOB);
				} else {
					row = goodsCommentMapper.insertSelective(goodsCommentWithBLOB);
				}
				try {
					if (row == 1) {
						if (t.size() > 1) {
							// 多个商品
							List<OrderSku> tt = orderSkuMapper.selectOrderSkuByOrderShopId(orderSku2.getOrderShopId());// 如果该商家下的所有商品已评价，则将order_shop的表的status=7
							if (tt.size() > 0) {
								int size = tt.size();
								int tSize = t.size() - 1;
								// 如果多个商品已评论完，且现在评论的商品是最后一个商品，则更改orderShop和orderMain的状态
								if (size == tSize) {
									OrderShop orderShop = new OrderShop();// 设置orderShop的状态为：7
									orderShop.setId(orderSku2.getOrderShopId());
									orderShop.setStatus(Contants.IS_COMPLISH);
									orderShopMapper.updateByPrimaryKeySelective(orderShop);

									Order order = new Order();
									order.setId(goodsCommentWithBLOB.getOrderId());
									order.setStatus(3);
									order.setCompletetime(new Date());
									orderMapper.updateByPrimaryKeySelective(order);
								}
							}

						} else if (t.size() == 1) {
							OrderShop orderShop = new OrderShop();// 设置orderShop的状态为：7
							orderShop.setId(orderSku2.getOrderShopId());
							orderShop.setStatus(Contants.IS_COMPLISH);
							orderShopMapper.updateByPrimaryKeySelective(orderShop);
						}
						/********
						 * ordermain
						 * 表订单状态status·变成已完成3，用户评价的时候，要判断这个订单下面的所有商家订单是否全部已评价，
						 * 如果全部已评，status改成3
						 *********/
						if (t1.size() == 1) {
							// 如果只有一个商品
							Order order = new Order();
							order.setId(goodsCommentWithBLOB.getOrderId());
							order.setStatus(3);
							order.setCompletetime(new Date());
							orderMapper.updateByPrimaryKeySelective(order);
						}
						OrderSku record = new OrderSku();
						record.setId(id);
						record.setIsRefund(1);
						record.setCommentOwner(1);
						orderSkuMapper.updateByPrimaryKeySelective(record);
						Integer goodId = goodsCommentWithBLOB.getGoodsId();// 前端评论的时候，每新增一条评论，就要给商品的评论次数＋1
						Goods good = goodsMapper.selectByPrimaryKey(goodId);
						Goods goods = new Goods();
						goods.setId(goodId);
						if (good != null) {
							goods.setComments(good.getComments() + 1);
							goodsMapper.updateByPrimaryKeySelective(goods);
						}
						if (sendInfo.size() > 0) {
							orderSendInfo.setId(sendInfo.get(0).getId());
							orderSendInfoMapper.updateByPrimaryKeySelective(orderSendInfo);
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
			if (add == 1) {
				goodsCommentWithBLOB.setIsAddEvaluate(1);
				GoodsCommentWithBLOBs goodsCommentWithBLOBs = new GoodsCommentWithBLOBs();
				goodsCommentWithBLOBs.setmId(mId);
				goodsCommentWithBLOBs.setIsAddEvaluate(0);
				goodsCommentWithBLOBs.setOrderSkuId(id);
				GoodsComment g = goodsCommentMapper.selectByParams(goodsCommentWithBLOBs);
				goodsCommentWithBLOB.setReid(g.getId());

				if (g1 != null) {
					row = goodsCommentMapper.updateByPrimaryKeySelective(goodsComment);
				} else {
					row = goodsCommentMapper.insertSelective(goodsCommentWithBLOB);
				}
			}
		}
		return row;
	}

}
