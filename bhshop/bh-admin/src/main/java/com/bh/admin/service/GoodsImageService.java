package com.bh.admin.service;

import com.bh.admin.pojo.goods.GoodsImage;

public interface GoodsImageService {
	
	int selectInsert(String goodsId, String url) throws Exception;
	
	GoodsImage selectByGoodsid(Integer goodsId) throws Exception;
	
	int updateImage(GoodsImage goodsImage) throws Exception;
}
