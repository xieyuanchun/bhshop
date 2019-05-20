package com.bh.user.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.config.Contants;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderSeedMapper;
import com.bh.order.mapper.OrderSendInfoMapper;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderSeed;
import com.bh.order.pojo.OrderSendInfo;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.user.api.service.MemberBalanceLogService;
import com.bh.user.mapper.MemberBalanceLogMapper;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.mapper.SeedModelMapper;
import com.bh.user.pojo.MemberBalanceLog;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.SeedModel;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;

@Service
public class MemberBalanceLogImpl implements MemberBalanceLogService{
	@Autowired
	private MemberBalanceLogMapper mapper;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderSeedMapper orderSeedMapper;
	@Autowired
	private SeedModelMapper seedModelMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private OrderSendInfoMapper sendInfoMapper;
	@Autowired
	private MemberShopMapper memberShopMapper;
	@Autowired
	private OrderSkuMapper orderSkuMapper;
	@Autowired
	private OrderShopMapper orderShopMapper;
	
	/**
	 * 个人钱包明细
	 */
	@Override
	public PageBean<MemberBalanceLog> listPage(MemberBalanceLog entity) throws Exception {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Contants.PAGE_SIZE, true);
		List<MemberBalanceLog> list = mapper.listPage(entity);
		if(list.size()>0){
			for(MemberBalanceLog log : list){
				double realMoney = (double)log.getMoney()/100;
				log.setRealMoney(realMoney);
				if(log.getTargetType()==1){//种子收支
					OrderSeed orderSeed = orderSeedMapper.selectByPrimaryKey(log.getTargetId());
					if(orderSeed!=null){
						SeedModel seedModel = seedModelMapper.selectByPrimaryKey(orderSeed.getSmId());
						if(seedModel!=null){
							log.setName("滨惠农场");
							log.setImage(seedModel.getImage());
						}
					}
				}
				if(log.getTargetType()==2){//购买商品支出
					Order order = orderMapper.selectByPrimaryKey(log.getTargetId());
					if(order!=null){
						List<OrderSku> orderSku = orderSkuMapper.getOrderSkuByOrderId(order.getId());
						if(orderSku.size()>0){
							log.setName(orderSku.get(0).getGoodsName());
							log.setImage(orderSku.get(0).getSkuImage());
						}
					}
				}
				if(log.getTargetType()==3){//配送收入
					OrderSendInfo sendInfo = sendInfoMapper.selectByPrimaryKey(log.getTargetId());
					if(sendInfo!=null){
						OrderShop orderShop = orderShopMapper.selectByPrimaryKey(sendInfo.getOrderShopId());
						if(orderShop!=null){
							MemberShop shop = memberShopMapper.selectByPrimaryKey(orderShop.getShopId());
							if(shop!=null){
								log.setName(shop.getShopName());
								log.setImage(shop.getLogo());
							}
						}
					}
				}
			}
		}
		PageBean<MemberBalanceLog> pageBean = new  PageBean<>(list);
		return pageBean;
	}

}
