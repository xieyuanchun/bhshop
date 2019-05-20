package com.bh.product.api.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.config.Contants;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.mapper.TopicDauctionLogMapper;
import com.bh.goods.pojo.GoodsSku;
import com.bh.goods.pojo.TopicDauctionLog;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderSku;
import com.bh.product.api.service.TopicDauctionLogService;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.pojo.Member;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
@Service
public class TopicDauctionLogImpl implements TopicDauctionLogService{
	@Autowired
	private TopicDauctionLogMapper mapper;
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private OrderSkuMapper skuMapper;
	@Autowired
	private OrderMapper orderMapper;
	
	@Override
	public int add(TopicDauctionLog entity) throws Exception {
		entity.setAddTime(new Date());
		return mapper.insertSelective(entity);
	}

	@Override
	public int edit(TopicDauctionLog entity) throws Exception {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	public int delete(TopicDauctionLog entity) throws Exception {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(entity.getId());
	}

	@Override
	public TopicDauctionLog get(TopicDauctionLog entity) throws Exception {
		// TODO Auto-generated method stub
		return mapper.selectByPrimaryKey(entity.getId());
	}

	@Override
	public PageBean<TopicDauctionLog> listPage(TopicDauctionLog entity) throws Exception {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Contants.PAGE_SIZE, true);
		List<TopicDauctionLog> list = mapper.listPage(entity);
		if(list.size()>0){
			for(TopicDauctionLog t : list){
				Member member = memberMapper.selectByPrimaryKey(t.getmId());
				if(member!=null){
					t.setMemberName(member.getUsername());
				}
				
				Order order = orderMapper.getOrderByOrderNo(t.getOrderNo());
				if(order!=null){
					List<OrderSku> sku = skuMapper.getOrderSkuByOrderId(order.getId());
					if(sku!=null && sku.size()>0){
						t.setGoodsName(sku.get(0).getGoodsName());
						t.setGoodsImage(sku.get(0).getSkuImage());
					}
				}
				t.setRealPrice((double)t.getPrice()/100);
			}
		}
		PageBean<TopicDauctionLog> pageBean = new PageBean<>(list);
		return pageBean;
	}

}
