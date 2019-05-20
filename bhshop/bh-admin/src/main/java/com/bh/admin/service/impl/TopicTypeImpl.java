package com.bh.admin.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bh.admin.service.TopicTypeService;
import com.bh.admin.mapper.goods.TopicMapper;
import com.bh.admin.mapper.goods.TopicTypeMapper;
import com.bh.admin.pojo.goods.Topic;
import com.bh.admin.pojo.goods.TopicType;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;
@Service
public class TopicTypeImpl implements TopicTypeService{
	@Autowired	
	private TopicTypeMapper topicTypeMapper;//活动类型
	@Autowired	
	private TopicMapper topicMapper;//专题

	/**
	 * @Description: 根据条件查询活动类型列表
	 * @author xieyc
	 * @date 2018年1月10日 下午5:42:45 
	 */
	public PageBean<TopicType> getTopicTypeList(String currentPage, String pageSize, String strId, String name,
			String strStatus) {
		Byte id=null;
		Byte status=null;
		if(!StringUtils.isEmptyOrWhitespaceOnly(strId)){
			id=(byte)Integer.parseInt(strId);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(strStatus)){
			status=(byte)Integer.parseInt(strStatus);
		}
		
		PageHelper.startPage(Integer.parseInt(currentPage), Integer.parseInt(pageSize),true);
		List<TopicType> listTopicType = topicTypeMapper.getTopicTypeList(name,id,status);
		PageBean<TopicType> pageTopicType = new PageBean<>(listTopicType);
		return pageTopicType;
	}

	/**
	 * @Description: 添加活动类型
	 * @author xieyc
	 * @date 2018年1月10日 下午5:42:45 
	 */
	public int addTopicType(TopicType topicType) {
		return topicTypeMapper.insertSelective(topicType);
	}

	/**
	 * @Description: 更新活动类型
	 * @author xieyc
	 * @date 2018年1月10日 下午5:42:45 
	 */
	public int updateTopicType(TopicType topicType) {
		return topicTypeMapper.updateByPrimaryKeySelective(topicType);
	}

	/**
	 * @Description:删除活动类型
	 * @author xieyc
	 * @date 2018年1月10日 下午12:01:40 
	 */
	public Integer deleteTopicType(String id) {
		Integer returnRow=0;
		if (!StringUtils.isEmptyOrWhitespaceOnly(id)) {
		  List<Topic>topticList=topicMapper.getTopicByTypeId(Integer.valueOf(id));//查看是否被引用
		  if(topticList!=null&&topticList.size()>0){
			  returnRow=-1;
		  }else{
			  returnRow=topicTypeMapper.deleteByPrimaryKey(Integer.valueOf(id));
		  }
		}
		return  returnRow ;
	}
	/**
	 * 专题活动添加获取所有活动分类
	 */
	@Override
	public List<TopicType> listAll(TopicType entity) {
		return topicTypeMapper.getAll(entity);
	}

	/**
	 * @Description: 根据条件查询活动类型列表
	 * @author xieyc
	 * @date 2018年1月10日 下午5:42:45 
	 */
	public TopicType getTopicTypeById(String id) {
		if (!StringUtils.isEmptyOrWhitespaceOnly(id)) {
			return topicTypeMapper.selectByPrimaryKey(Integer.valueOf(id));
		}else{
			return null;
		}
	}	
}
