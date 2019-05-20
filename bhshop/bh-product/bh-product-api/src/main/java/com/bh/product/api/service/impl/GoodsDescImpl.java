package com.bh.product.api.service.impl;

import com.bh.goods.mapper.GoodsDescMapper;
import com.bh.goods.pojo.GoodsDesc;
import com.bh.product.api.service.GoodsDescService;

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
