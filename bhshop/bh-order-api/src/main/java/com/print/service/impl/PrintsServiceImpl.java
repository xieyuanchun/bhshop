package com.print.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.pojo.GoodsSku;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderShop;
import com.bh.order.pojo.OrderSku;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.mapper.MemberUserAddressMapper;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.MemberUserAddress;
import com.print.service.PrintsService;

@Service
@Transactional
public class PrintsServiceImpl implements PrintsService{

	@Autowired
	private OrderShopMapper shopmapper; 
	
	@Autowired
	private OrderSkuMapper skumapper; 
	
	@Autowired
	private OrderMapper ordermapper; 
	
	@Autowired
	private MemberUserAddressMapper muamapper; 
	
	@Autowired
	private GoodsSkuMapper goodsSkuMapper; 
	
	@Autowired
	private MemberShopMapper memberShopMapper; 
	
	@Override
	public OrderShop getOrderById(String id) {
		// TODO Auto-generated method stub
		//return shopmapper.selectByPrimaryKey(Integer.parseInt(id));
		OrderShop orderShop = new OrderShop();
		orderShop.setShopOrderNo(id); //商家订单号
		return shopmapper.getByOrderNo(orderShop);
	}

	@Override
	public List<OrderSku> getByOrderShopId(String id) {
		// TODO Auto-generated method stub
		return skumapper.getByOrderShopId(Integer.parseInt(id));
	}

	@Override
	public Order getOrderMainById(String id) {
		// TODO Auto-generated method stub
		return ordermapper.getOrderByOrderNo(id);
	}

	@Override
	public MemberUserAddress getUserById(Integer id) {
		// TODO Auto-generated method stub
		return muamapper.selectByPrimaryKey(id);
	}

	@Override
	public GoodsSku getGoodsSku(Integer id) {
		// TODO Auto-generated method stub
		return goodsSkuMapper.selectByPrimaryKey(id);
	}

	@Override
	public MemberShop getMemberShopById(Integer id) {
		// TODO Auto-generated method stub
		return memberShopMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKey(OrderShop record) {
		// TODO Auto-generated method stub
		return shopmapper.updateByPrimaryKey(record);
	}

	@Override
	public void updateExpressByOrderNo(Order record) {
		// TODO Auto-generated method stub
		ordermapper.updateExpressByOrderNo(record);
	}

	@Override
	public int updatePrintByPrimaryKey(OrderShop record) {
		// TODO Auto-generated method stub
		return shopmapper.updateByPrimaryKeySelective(record);
	}




	
}
