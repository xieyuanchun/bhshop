package com.bh.admin.service;

import com.bh.admin.pojo.goods.GoodsDesc;

public interface GoodsDescService {
	
	int selectInsert(int goodsId, String desc) throws Exception;
	int selectDesc(GoodsDesc goodsDesc) throws Exception;
}
