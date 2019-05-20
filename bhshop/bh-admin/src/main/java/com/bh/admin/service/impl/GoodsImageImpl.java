package com.bh.admin.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bh.admin.mapper.goods.GoodsImageMapper;
import com.bh.admin.pojo.goods.GoodsImage;
import com.bh.admin.service.GoodsImageService;



@Service
@Transactional
public class GoodsImageImpl implements GoodsImageService{
	private GoodsImageMapper mapper;

	@Override
	public int selectInsert(String goodsId, String url) throws Exception {
		GoodsImage goodsImage = new GoodsImage();
		goodsImage.setGoodsId(Integer.parseInt(goodsId));
		goodsImage.setUrl(url);
		return mapper.insertGoodsImage(goodsImage);
	}

	@Override
	public GoodsImage selectByGoodsid(Integer goodsId) throws Exception {
		return mapper.selectByGoodsId(goodsId);
	}

	@Override
	public int updateImage(GoodsImage goodsImage) throws Exception {
		return mapper.updateByPrimaryKey(goodsImage);
	}

}
