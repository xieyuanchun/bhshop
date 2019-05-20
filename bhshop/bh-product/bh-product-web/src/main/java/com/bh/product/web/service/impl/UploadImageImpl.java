package com.bh.product.web.service.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.bh.goods.mapper.GoodsImageMapper;
import com.bh.goods.pojo.GoodsImage;

import com.bh.product.web.service.UploadImageService;
import com.bh.result.BhResult;

@Service
public class UploadImageImpl implements UploadImageService {
	@Autowired
	private GoodsImageMapper imageMapper;
	@Override
	public BhResult adduserid(Map<String, String> map) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int selectInsert(GoodsImage goodsImage) throws Exception {
		return imageMapper.insertSelective(goodsImage);
	}
	

}
