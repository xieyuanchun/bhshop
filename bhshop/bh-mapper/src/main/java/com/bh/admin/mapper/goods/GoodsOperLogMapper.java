package com.bh.admin.mapper.goods;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.admin.pojo.goods.GoodsOperLog;

public interface GoodsOperLogMapper {
	 int deleteByPrimaryKey(Integer id);

	    int insert(GoodsOperLog record);

	    int insertSelective(GoodsOperLog record);

	    GoodsOperLog selectByPrimaryKey(Integer id);

	    int updateByPrimaryKeySelective(GoodsOperLog record);

	    int updateByPrimaryKey(GoodsOperLog record);
	    
	    List<GoodsOperLog> selectAlloperation(Integer currentPage, Integer pageSize,@Param("adminUser")String adminUser,@Param("opType")String opType,@Param("goodId")String goodId,@Param("orderId")String orderId);
	    
	    int selectAlloperationCount(@Param("adminUser")String adminUser,@Param("opType")String opType,@Param("goodId")String goodId,@Param("orderId")String orderId);
    
    int insertGoodsById(@Param("id")Integer id,@Param("goodsOperLog")GoodsOperLog goodsOperLog);
}