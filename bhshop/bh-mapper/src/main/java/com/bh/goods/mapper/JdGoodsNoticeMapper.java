package com.bh.goods.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.goods.pojo.JdGoodsNotice;

public interface JdGoodsNoticeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(JdGoodsNotice record);

    int insertSelective(JdGoodsNotice record);

    JdGoodsNotice selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(JdGoodsNotice record);

    int updateByPrimaryKey(JdGoodsNotice record);
    
    List<JdGoodsNotice> pageList(String name, @Param("type") String type, @Param("isRead") String isRead);
}