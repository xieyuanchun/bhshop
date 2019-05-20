package com.bh.product.api.service;

import com.bh.goods.pojo.GoodsImage;

public interface GoodsImageService {
	
	int selectInsert(String goodsId, String url) throws Exception;
	
	GoodsImage selectByGoodsid(Integer goodsId) throws Exception;
	
	int updateImage(GoodsImage goodsImage) throws Exception;
}
