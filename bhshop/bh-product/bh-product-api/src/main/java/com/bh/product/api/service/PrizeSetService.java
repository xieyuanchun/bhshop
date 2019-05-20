package com.bh.product.api.service;

import java.util.List;

import com.bh.goods.pojo.PrizeSet;
import com.bh.utils.PageBean;

public interface PrizeSetService {
	//新增
	int insert(PrizeSet entity) throws Exception;
	//编辑
	int edit(PrizeSet entity) throws Exception;
	//删除
	int delete(PrizeSet entity) throws Exception;
	//获取
	PrizeSet get(PrizeSet entity) throws Exception;
	//列表
	PageBean<PrizeSet> listPage(PrizeSet entity) throws Exception;
	//中奖算法
	String priceIsGet(PrizeSet entity, int mId) throws Exception;
	 //zlk 中奖设置和当前的商品列表
	PageBean<PrizeSet> getGoodAndPrize(PrizeSet record) throws Exception;
} 
