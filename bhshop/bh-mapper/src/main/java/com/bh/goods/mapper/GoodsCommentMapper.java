package com.bh.goods.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsComment;
import com.bh.goods.pojo.GoodsCommentWithBLOBs;
import com.bh.goods.pojo.MyGoodsComment;
import com.bh.goods.pojo.MyWaitcommentlist;

public interface GoodsCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsComment record);

    int insertSelective(GoodsComment record);

    GoodsComment selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsComment record);

    int updateByPrimaryKeyWithBLOBs(GoodsComment record);

    int updateByPrimaryKey(GoodsComment record);
    
    
    
    
    
    List<GoodsComment> getListByGoodsId(Integer goodsId, Integer shopId);
    
    int countByGoodsId(Integer goodsId, Integer shopId);
    
    List<GoodsComment> getListByLevel(Integer goodsId, Integer shopId, @Param("level") String level);
    
    int countByLevel(Integer goodsId, Integer shopId, @Param("level") Integer level);
    
    List<GoodsComment> getListByEvalute(Integer goodsId, Integer shopId);
    
    int countByEvalute(Integer goodsId, Integer shopId);
    
    List<GoodsComment> getListByImage(Integer goodsId, Integer shopId);
    
    int countByImage(Integer goodsId, Integer shopId);
    
    GoodsComment getListByReid(Integer reid);
    
    /*批量查询*/
    List<GoodsComment> batchSelect(List<String> list);
    
    /*后台评价管理*/
    List<GoodsComment> pageByShopId(GoodsComment record);
    
    GoodsComment getByReid(Integer reid);
    
    //chengfengyun2017-11-10
    /*******通过产生查询*********/
    GoodsComment selectByParams(GoodsComment goodsComment);
    //评论商品的列表
    List<MyGoodsComment> selectGoodsComment(@Param("mId")Integer mId,@Param("currentPageIndex")Integer currentPageIndex,@Param("pageSize")Integer pageSize);
    List<GoodsComment> selectCommentsBySkuId(GoodsComment comment);
    List<GoodsComment> selectCommentsByIds(@Param("ids")List<String> ids);

	int countStarAvg(int shopId);

	int countStar(Integer shopId, Integer id);
    
	List<MyWaitcommentlist> selectMyWaitCommentList(@Param("mId")Integer mId);
    
}