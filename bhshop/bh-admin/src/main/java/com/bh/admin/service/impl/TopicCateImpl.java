package com.bh.admin.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bh.admin.mapper.goods.TopicCateMapper;
import com.bh.admin.pojo.goods.TopicCate;
import com.bh.admin.service.TopicCateService;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;

@Transactional
@Service
public class TopicCateImpl implements TopicCateService{
	@Autowired
	private TopicCateMapper mapper;
	@Value(value = "${pageSize}")
	private String pageSize;

	/**
	 * 新增专题分类
	 */
	@Override
	public int add(TopicCate entity) throws Exception {
		int row = mapper.insertSelective(entity);
		
		TopicCate parent = mapper.selectByPrimaryKey(entity.getParentid());//上一级
		if(parent!=null){
			if(parent.getChild()==false){
				parent.setChild(true);
				parent.setArrchildid(entity.getId()+"");
			}else{
				parent.setArrchildid(parent.getArrchildid()+","+entity.getId());
			}
			row = mapper.updateByPrimaryKeySelective(parent);
		}
		return row;
	}
	
	/**
	 * 新增时修改上级信息(三级)
	 * @param entity
	 * @param parentId
	 * @return
	 */
	private int insertChangeParent(TopicCate entity, int parentId){
		int row = 0;
		TopicCate parent = mapper.selectByPrimaryKey(parentId);//上一级
		if(parent!=null){
			if(parent.getChild()==false){
				parent.setChild(true);
				parent.setArrchildid(entity.getId()+"");
			}else{
				parent.setArrchildid(parent.getArrchildid()+","+entity.getId());
			}
			row = mapper.updateByPrimaryKeySelective(parent);
			
			String[] pIdStr = parent.getArrparentid().split(",");
			for(int i=0; i<pIdStr.length; i++){
				TopicCate upParent = mapper.selectByPrimaryKey(Integer.parseInt(pIdStr[i]));
				upParent.setArrchildid(upParent.getArrchildid()+","+entity.getId());
				row = mapper.updateByPrimaryKeySelective(upParent);
			}
		}
		return row;
	}
	
	/**
	 * 修改专题分类
	 */
	@Override
	public int update(TopicCate entity) throws Exception {
		return mapper.updateByPrimaryKeySelective(entity);
	}
	
	/**
	 * 删除专题分类
	 */
	@Override
	public int delete(TopicCate entity) throws Exception {
		int row = 0;
		TopicCate topicCate = mapper.selectByPrimaryKey(entity.getId());
		if(topicCate.getChild()){
			row = 1000;
		}else{
			topicCate.setIsDelete(1);
			row = mapper.updateByPrimaryKeySelective(topicCate);
		}
		
		List<TopicCate> childList = mapper.selectByPIdOwner(topicCate.getParentid(), entity.getId());
		if(childList.size()==0){
			TopicCate parent = mapper.selectByPrimaryKey(topicCate.getParentid());
			if(parent!=null){
				parent.setChild(false);
				row = mapper.updateByPrimaryKeySelective(parent);
			}
		}
		return row;
	}
	
	/**
	 * 专题分类列表
	 */
	@Override
	public PageBean<TopicCate> listPage(TopicCate entity) throws Exception {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Integer.parseInt(pageSize), true);
		List<TopicCate> list = mapper.listPage(entity);
		if(entity.getParentid()!=null){
			if(list.size()>0){
				getNext(list);
			}
		}
		
		PageBean<TopicCate> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	private List<TopicCate> getNext(List<TopicCate> list){
		for(TopicCate entity : list){
			if(entity.getChild()){
				List<TopicCate> nextList = mapper.selectByParentId(entity.getId(),null);
				entity.setChildList(nextList);
				
				for(TopicCate topic : nextList){
					if(topic.getChild()){
						getNext(nextList);
					}
				}
			}
		}
		return list;
	}
	
	/**
	 * 获取专题分类
	 */
	@Override
	public TopicCate get(TopicCate entity) throws Exception {
		return mapper.selectByPrimaryKey(entity.getId());
	}

	/**
	 * 加载所有分类
	 */
	@Override
	public List<TopicCate> listAll() throws Exception {
		List<TopicCate> list  = mapper.selectAll();
		TopicCate entity = new TopicCate();
		entity.setId(0);
		entity.setSeries(0);
		entity.setCatname("第一级");
		list.add(0, entity);
		return list;
	}
	
	/**
	 * 专题活动添加所有分类
	 */
	@Override
	public List<TopicCate> getLinkedAll() throws Exception {
		List<TopicCate> list = mapper.selectByParentId(0,1); //第一级列表
		if(list.size()>0){
			getNext(list);
		}
		return list;
	}
	

}
