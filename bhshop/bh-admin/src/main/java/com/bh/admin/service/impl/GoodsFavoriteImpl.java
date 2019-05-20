package com.bh.admin.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bh.admin.mapper.goods.GoodsFavoriteMapper;
import com.bh.admin.mapper.goods.GoodsMapper;
import com.bh.admin.mapper.goods.GoodsSkuMapper;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.GoodsFavorite;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.service.GoodsFavoriteService;

@Transactional
@Service
public class GoodsFavoriteImpl implements GoodsFavoriteService{
	@Autowired
	private GoodsFavoriteMapper mapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;
	/**
	 * 新增用户收藏
	 */
	@Override
	public int addFavorite(String goodsId, int mId) throws Exception {
		int row = 0;
		GoodsFavorite favorite = new GoodsFavorite();
		Timestamp now = new Timestamp(new Date().getTime());
		GoodsFavorite goodsFavorite = mapper.findByGoodsIdAndMid(Integer.parseInt(goodsId), mId);//判断是否已存在收藏记录
		if(goodsFavorite!=null){
			row = mapper.deleteByPrimaryKey(goodsFavorite.getId());
		}else{
			Goods goods = goodsMapper.selectByPrimaryKey(Integer.parseInt(goodsId));//商品表收藏数+1
			List<GoodsSku> goodsSku = goodsSkuMapper.selectListByGoodsId(goods.getId());
			favorite.setAddtime(now);
			favorite.setmId(mId);
			favorite.setGoodsId(Integer.parseInt(goodsId));
			if (goodsSku.size()>0) {
				favorite.setSkuId(goodsSku.get(0).getId());
			}
			
			row = mapper.insertSelective(favorite);
			
			
			goods.setFavorite(goods.getFavorite()+1);
			goodsMapper.updateByPrimaryKeySelective(goods);
		}
		
		return row;
	}
	
	/**
	 * 取消收藏
	 */
	@Override
	public int deleteFavorite(String goodsId, int mId) throws Exception {
		int row = 0;
		GoodsFavorite favorite = mapper.findByGoodsIdAndMid(Integer.parseInt(goodsId), mId);
		if(favorite!=null){
			row = mapper.deleteByPrimaryKey(favorite.getId());
			Goods goods = goodsMapper.selectByPrimaryKey(Integer.parseInt(goodsId));
			goods.setFavorite(goods.getFavorite()-1);
			goodsMapper.updateByPrimaryKeySelective(goods);
		}
		return row;
	}
	
	/**
	 * 判断商品是否被用户收藏
	 */
	@Override
	public int isFavorite(String goodsId, Member member) throws Exception {
		int flag = 0;
		if(member!=null){
			GoodsFavorite favorite = mapper.findByGoodsIdAndMid(Integer.parseInt(goodsId), member.getId());
			if(favorite!=null){
				flag = 1;
			}else{
				flag = -1;
			}
		}else{
			flag = 100;
		}
		return flag;
	}

}
