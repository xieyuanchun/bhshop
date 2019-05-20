package com.bh.product.api.service;

import java.util.HashMap;
import java.util.List;

import com.bh.goods.pojo.GoodsBrand;
import com.bh.goods.pojo.GoodsComment;
import com.bh.user.pojo.PromoteUser;
import com.bh.utils.PageBean;

public interface GoodsCommentService {
	
	/*根据商品id查询所有评论*/
	PageBean<GoodsComment> selectByGoodsId(String goodsId, String shopId, String level, String isAddEvaluate, String fz ,String currentPage, int pageSize) throws Exception;
	
	/*根据不同条件统计所有评论*/
	HashMap<Integer, String> selectCount(String goodsId, String shopId) throws Exception;
	
	/*后台商品评论管理*/
	PageBean<GoodsComment> pageByShopId(GoodsComment entity, int shopId) throws Exception;
	
	/*后台商品评论详情*/
	GoodsComment getCommentDetails(String id) throws Exception;
	
	/*后台新增评价*/
	int insert(GoodsComment entity) throws Exception;
	
	/*后台评价的删除*/
	int delete(GoodsComment entity) throws Exception;
	
	/*评价的显示和隐藏*/
	int changeStatus(GoodsComment entity) throws Exception;
	
	//批量修改排序
	int updateCommentByBatch(List<GoodsComment> comments) throws Exception;
	PageBean<PromoteUser> selectPromoteUser(PromoteUser currentPage) throws Exception;
	
}
