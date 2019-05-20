package com.bh.admin.mapper.goods;

import java.util.List;

import com.bh.admin.pojo.goods.Topic;

public interface TopicMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Topic record);

    int insertSelective(Topic record);

    Topic selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Topic record);

    int updateByPrimaryKeyWithBLOBs(Topic record);

    int updateByPrimaryKey(Topic record);
    
    List<Topic> listPage(Topic record);

	List<Topic> getTopicByTypeId(Integer typeid);//根据typeid查询列表
	
	List<Topic> getTopicByType(Topic record);
	
	List<Topic> getByGoodsIdAndStatus(Integer goodsId);
	
	//获取所有砍价活动
	List<Topic> getBargainTopic();
	
	//cheng
	String selectStartTime();
	String selectendtime();
	List<Topic> selectImage();
	List<Topic> selectTopicByTime(Topic record);
	
	List<Topic> getTopicDauction();
}
