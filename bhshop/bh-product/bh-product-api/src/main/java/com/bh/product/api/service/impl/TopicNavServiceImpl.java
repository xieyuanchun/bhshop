package com.bh.product.api.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.bh.goods.mapper.TopicNavMapper;
import com.bh.goods.pojo.TopicNav;
import com.bh.product.api.service.TopicNavService;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;

@Service
public class TopicNavServiceImpl implements TopicNavService{
    @Autowired
    private TopicNavMapper topicNavMapper;
	@Value(value = "${pageSize}")
	private String pageSize;
	
	/**
	 * @Description: 导航增加
	 * @author xieyc
	 * @date 2018年1月26日 下午5:52:38 
	 */
	public int add(TopicNav entity) {
		return topicNavMapper.insertSelective(entity);
	}
	/**
	 * @Description: 导航更新
	 * @author xieyc
	 * @date 2018年1月26日 下午6:00:13 
	 */
	public int update(TopicNav entity) {
		return topicNavMapper.updateByPrimaryKeySelective(entity);
	}
	/**
	 * @Description: 导航获取
	 * @author xieyc
	 * @date 2018年1月26日 下午6:01:33 
	 */
	public TopicNav get(Integer id) {
		if (id!=null) {
			return topicNavMapper.selectByPrimaryKey(id);
		}else{
			return null;
		}
	}
	/**
	 * @Description: 导航列表
	 * @author xieyc
	 * @date 2018年1月26日 下午6:09:52 
	 */
	public PageBean<TopicNav> listPage(TopicNav entity) {
		PageHelper.startPage(entity.getCurrentPag(), Integer.parseInt(pageSize),true);
		List<TopicNav> listTopicNav = topicNavMapper.listPage(entity);
		PageBean<TopicNav> pageTopicNav = new PageBean<>(listTopicNav);
		return pageTopicNav;
	}
	/**
	 * @Description: 导航删除
	 * @author xieyc
	 * @date 2018年1月26日 下午6:05:19 
	 */
	public int delete(Integer id) {
		return  topicNavMapper.deleteByPrimaryKey(id) ;
	}
}
