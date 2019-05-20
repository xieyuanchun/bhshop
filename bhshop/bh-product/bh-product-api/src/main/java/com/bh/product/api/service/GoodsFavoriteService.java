package com.bh.product.api.service;

import com.bh.user.pojo.Member;

public interface GoodsFavoriteService {
	
	/*新增用户收藏*/
	int addFavorite(String goodsId, int mId) throws Exception;
	
	/*取消收藏*/
	int deleteFavorite(String goodsId, int mId) throws Exception;
	
	/*判断是否收藏*/
	int isFavorite(String goodsId, Member member) throws Exception;
}
