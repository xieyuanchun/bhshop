package com.bh.admin.mapper.goods;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.admin.pojo.goods.TopicCate;

public interface TopicCateMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TopicCate record);

    int insertSelective(TopicCate record);

    TopicCate selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TopicCate record);

    int updateByPrimaryKeyWithBLOBs(TopicCate record);

    int updateByPrimaryKey(TopicCate record);
    
    
    
    List<TopicCate> selectByParentId(@Param("parentid") Integer parentid,@Param("isrec") Integer isrec);
    
    List<TopicCate> listPage(TopicCate record);
    
    List<TopicCate> selectByPIdOwner(Integer parentId, Integer id);
    
    List<TopicCate> selectAll();
}