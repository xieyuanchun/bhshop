package com.bh.admin.service.impl;

import com.bh.admin.mapper.goods.GoodsDescMapper;
import com.bh.admin.pojo.goods.GoodsDesc;
import com.bh.admin.service.GoodsDescService;


public class GoodsDescImpl implements GoodsDescService{
	private GoodsDescMapper mapper;

	@Override
	public int selectInsert(int goodsId, String description) throws Exception {
		GoodsDesc goodsDesc = new  GoodsDesc();
		goodsDesc.setGoodsId(goodsId);
		goodsDesc.setDescription(description);
		return mapper.insertGoodsDesc(goodsDesc);
	}

	@Override
	public int selectDesc(GoodsDesc goodsDesc) {
		return mapper.insertGoodsDesc(goodsDesc);
	}

}
