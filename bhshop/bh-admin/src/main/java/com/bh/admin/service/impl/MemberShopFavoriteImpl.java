package com.bh.admin.service.impl;

import java.sql.Timestamp;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bh.admin.mapper.user.MemberShopFavoriteMapper;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.pojo.user.MemberShopFavorite;
import com.bh.admin.service.MemberShopFavoriteService;

@Transactional
@Service
public class MemberShopFavoriteImpl implements MemberShopFavoriteService{
	@Autowired
	private MemberShopFavoriteMapper mapper;
	
	/**
	 * 新增用户收藏
	 */
	@Override
	public int addFavorite(String shopId, int mId) throws Exception {
		int row = 0;
		MemberShopFavorite favorite = new MemberShopFavorite();
		Timestamp now = new Timestamp(new Date().getTime());
		MemberShopFavorite goodsFavorite = mapper.findByShopIdAndMid(Integer.parseInt(shopId), mId);//判断是否已存在收藏记录
		if(goodsFavorite!=null){
			row = mapper.deleteByPrimaryKey(goodsFavorite.getId());
		}else{
			favorite.setAddtime(now);
			favorite.setmId(mId);
			favorite.setShopId(Integer.parseInt(shopId));
			row = mapper.insertSelective(favorite);
		}
		
		return row;
	}
	
	/**
	 * 取消收藏
	 */
	@Override
	public int deleteFavorite(String shopId, int mId) throws Exception {
		int row = 0;
		MemberShopFavorite favorite = mapper.findByShopIdAndMid(Integer.parseInt(shopId), mId);
		if(favorite!=null){
			row = mapper.deleteByPrimaryKey(favorite.getId());
		}
		return row;
	}
	
	/**
	 * 判断商品是否被用户收藏
	 */
	@Override
	public int isFavorite(String shopId, Member member) throws Exception {
		int flag = 0;
		if(member!=null){
			MemberShopFavorite favorite = mapper.findByShopIdAndMid(Integer.parseInt(shopId), member.getId());
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
