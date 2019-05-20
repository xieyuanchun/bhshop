package com.bh.goods.mapper;

import com.bh.goods.pojo.GoodsComment;
import com.bh.goods.pojo.GoodsCommentWithBLOBs;

public interface GoodsCommentMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(GoodsCommentWithBLOBs record);

    int insertSelective(GoodsCommentWithBLOBs record);

    GoodsCommentWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(GoodsCommentWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(GoodsCommentWithBLOBs record);

    int updateByPrimaryKey(GoodsComment record);
}