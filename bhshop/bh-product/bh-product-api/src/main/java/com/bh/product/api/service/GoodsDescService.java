package com.bh.product.api.service;

import com.bh.goods.pojo.GoodsDesc;

public interface GoodsDescService {
	
	int selectInsert(int goodsId, String desc) throws Exception;
	int selectDesc(GoodsDesc goodsDesc) throws Exception;
}
