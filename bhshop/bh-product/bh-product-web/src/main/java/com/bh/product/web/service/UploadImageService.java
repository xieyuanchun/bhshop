package com.bh.product.web.service;

import java.util.Map;

import com.bh.goods.pojo.GoodsImage;
import com.bh.result.BhResult;

public interface UploadImageService {
	
	BhResult adduserid(Map<String, String> map);
	
	int selectInsert(GoodsImage goodsImage) throws Exception;
	
}
