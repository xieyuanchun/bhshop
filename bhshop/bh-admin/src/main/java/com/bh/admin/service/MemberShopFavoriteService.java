package com.bh.admin.service;

import com.bh.admin.pojo.user.Member;

public interface MemberShopFavoriteService {
	
	/*新增用户收藏*/
	int addFavorite(String shopId, int mId) throws Exception;
	
	/*取消收藏*/
	int deleteFavorite(String shopId, int mId) throws Exception;
	
	/*判断是否收藏*/
	int isFavorite(String shopId, Member member) throws Exception;
}
