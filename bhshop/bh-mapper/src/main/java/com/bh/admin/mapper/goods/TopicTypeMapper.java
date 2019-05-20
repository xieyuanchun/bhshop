
package com.bh.admin.mapper.goods;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.admin.pojo.goods.TopicType;


public interface TopicTypeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(TopicType record);

    int insertSelective(TopicType record);

    TopicType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(TopicType record);

    int updateByPrimaryKeyWithBLOBs(TopicType record);

    int updateByPrimaryKey(TopicType record);
    
    

	List<TopicType> getTopicTypeList(@Param("name") String name, @Param("id") Byte id, @Param("status") Byte status);//根据条件查询活动类型列表

	
	
	List<TopicType> getAll(TopicType entity);
}





