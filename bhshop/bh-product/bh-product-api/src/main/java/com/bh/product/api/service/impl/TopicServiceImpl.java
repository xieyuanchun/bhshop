package com.bh.product.api.service.impl;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.bh.goods.mapper.TopicGoodsMapper;
import com.bh.goods.mapper.TopicMapper;
import com.bh.goods.mapper.TopicTypeMapper;
import com.bh.goods.pojo.Topic;
import com.bh.goods.pojo.TopicGoods;
import com.bh.goods.pojo.TopicType;
import com.bh.product.api.service.TopicService;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
@Service
public class TopicServiceImpl implements TopicService{
    @Autowired
    private TopicMapper topicMapper;
    @Autowired
    private TopicTypeMapper topicTypeMapper;
    @Autowired
    private TopicGoodsMapper topicGoodsMapper;
    
	@Value(value = "${pageSize}")
	private String pageSize;
	@Override
	public int add(Topic entity) {
		TopicType type = topicTypeMapper.selectByPrimaryKey(entity.getTypeid());
		if(type!=null){
			entity.setType(type.getTypeid());
		}
		return  topicMapper.insertSelective(entity);
	}
	
	
	/*public int add(Topic entity) {
		//row的值为0时未进行插入操作,值为1时插入成功,值为2时同个时间段内已存在一个活动
		int row=0;
		Topic topic1 = getTopicTimeState(entity);
		//appTimeStatus报名时间状态:0代表没有活动时间，1代表活动未开始，2代表活动进行中，3代表活动结束
		//topicTimeStatus   活动:0代表没有活动时间，1代表活动未开始，2代表活动进行中，3代表活动结束
		TopicType type = topicTypeMapper.selectByPrimaryKey(entity.getTypeid());
		if(type!=null){
			entity.setType(type.getTypeid());
		}
		if (topic1.getTopicTimeStatus().equals(2)) {
			Topic topic = new Topic();
			List<Topic> topicList = topicMapper.selectTopicByTime(topic);
			//如果查询到的列表已有活动中的topic
			if (topicList.size()>0) {
				row = 2;
			}else{
				row = topicMapper.insertSelective(entity);
			}
		}else{
			row = topicMapper.insertSelective(entity);
		}
		return row;
	}*/

	/*@Override
	public int update(Topic entity) {
		int row=0;
		
		Topic topic = getTopicTimeState(entity);
		//appTimeStatus报名时间状态:0代表没有活动时间，1代表活动未开始，2代表活动进行中，3代表活动结束
		//topicTimeStatus   活动:0代表没有活动时间，1代表活动未开始，2代表活动进行中，3代表活动结束
		if (topic.getTopicTimeStatus().equals(2)) {
			Topic record = new Topic();
			record.setType(entity.getType());
			List<Topic>  topicList = topicMapper.selectTopicByTime(record);
			if (topicList.size() > 0) {
				Topic record1 = new Topic();
				record1.setId(entity.getId());
				List<Topic>  topicList1 = topicMapper.selectTopicByTime(record1);
				if (topicList1.size()>0) {
					Topic topic = topicMapper.selectByPrimaryKey(entity.getId());
					long a = new Date().getTime();
					long b = topic.getEndTime().getTime();
					long c = topic.getStartTime().getTime();
					if(b>a && a>c){
						return 999;
					}
					row = topicMapper.updateByPrimaryKeySelective(entity);
				}else{
					row = 2;
				}
			}else{
				row = topicMapper.updateByPrimaryKeySelective(entity);
			}
		}else{
			row = topicMapper.updateByPrimaryKeySelective(entity);
		}	
		return row;
	}*/
	
	@Override
	public int update(Topic entity) {
		return topicMapper.updateByPrimaryKeySelective(entity);
	}
	
	

	@Override
	public Topic get(Integer id) {
		Topic topic = topicMapper.selectByPrimaryKey(id);
		TopicType type = topicTypeMapper.selectByPrimaryKey(topic.getTypeid());
		topic.setTypeName(type.getName());
		return topic;
	}

	@Override
	public PageBean<Topic> listPage(Topic entity) {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Integer.parseInt(pageSize), true);
		List<Topic> list = topicMapper.listPage(entity);
		if(list.size()>0){
			for(Topic topic : list){
				TopicType type = topicTypeMapper.selectByPrimaryKey(topic.getTypeid());
				if(type!=null){
					topic.setTypeName(type.getName());
				}
				int count = topicGoodsMapper.countByActId(topic.getId());
				topic.setNum(count);
				String goodsStrs = getTopicStr(topic);
				if(goodsStrs!=null){
					topic.setGoodsStrs(goodsStrs);
				}
				//cheng  报名:0代表没有活动时间，1代表活动未开始，2代表活动进行中，3代表活动结束
				//获取当前时间
				Date date=Calendar.getInstance().getTime();
				//报名开始时间
				Date applyStime = topic.getApplyStime();
				//报名结束时间
				Date applyEtime =topic.getApplyEtime();
				
				if ((applyEtime!=null) && (applyStime!=null)) {
					//如果现在的时间小于开始的时间--活动就是未开始
					if (date.getTime() < applyStime.getTime()) {
						topic.setAppTimeStatus(1);
					}else if (date.getTime() > applyEtime.getTime()) {
						topic.setAppTimeStatus(3);
					}else  {
						topic.setAppTimeStatus(2);
					}
				}else{
					topic.setAppTimeStatus(0);
				}
			
				//活动开始时间    活动:0代表没有活动时间，1代表活动未开始，2代表活动进行中，3代表活动结束
				Date startTime =topic.getStartTime();
				//活动结束时间
				Date endTime = topic.getEndTime();
				if ((startTime!=null) && (endTime!=null)) {
					if (date.getTime() < startTime.getTime()) {
						topic.setTopicTimeStatus(1);
					}else if (date.getTime() > endTime.getTime()) {
						topic.setTopicTimeStatus(3);
					}else{
						topic.setTopicTimeStatus(2);
					}
				}else{
					topic.setTopicTimeStatus(0);
				}
			}
		}
		PageBean<Topic> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	private String getTopicStr(Topic topic){
		String str = null;
		StringBuffer buffer = new StringBuffer();
		List<TopicGoods> list = topicGoodsMapper.getByActIdAndNotDelete(topic.getId());
		if(list.size()>0){
			for(TopicGoods entity : list){
				buffer.append(entity.getGoodsId()+",");
			}
			str = buffer.toString().substring(0, buffer.toString().length()-1);
		}
		return str;
	}
	
	public Topic getTopicTimeState(Topic topic){
		//获取当前时间
		Date date=Calendar.getInstance().getTime();
		//报名开始时间
		Date applyStime = topic.getApplyStime();
		//报名结束时间
		Date applyEtime =topic.getApplyEtime();
		
		if ((applyEtime!=null) && (applyStime!=null)) {
			//如果现在的时间小于开始的时间--活动就是未开始
			if (date.getTime() < applyStime.getTime()) {
				topic.setAppTimeStatus(1);
			}else if (date.getTime() > applyEtime.getTime()) {
				topic.setAppTimeStatus(3);
			}else  {
				topic.setAppTimeStatus(2);
			}
		}else{
			topic.setAppTimeStatus(0);
		}
		
		
		//活动开始时间    活动:0代表没有活动时间，1代表活动未开始，2代表活动进行中，3代表活动结束
		Date startTime =topic.getStartTime();
		//活动结束时间
		Date endTime = topic.getEndTime();
		if ((startTime!=null) && (endTime!=null)) {
			if (date.getTime() < startTime.getTime()) {
				topic.setTopicTimeStatus(1);
			}else if (date.getTime() > endTime.getTime()) {
				topic.setTopicTimeStatus(3);
			}else{
				topic.setTopicTimeStatus(2);
			}
		}else{
			topic.setTopicTimeStatus(0);
		}
		return topic;
	}

	@Override
	public int delete(Integer id) {
		Topic topic = topicMapper.selectByPrimaryKey(id);
		long a = new Date().getTime();
		long b = topic.getEndTime().getTime();
		long c = topic.getStartTime().getTime();
		if(b>a && a>c){
			return 999;
		}
		topic.setIsDelete(1);
		return topicMapper.updateByPrimaryKeySelective(topic);
	}

	@Override
	public int getValid(Topic entity) {
		Topic retTopic = topicMapper.selectByPrimaryKey(entity.getId());
		long curMill = new Date().getTime();
		long startMill = retTopic.getStartTime().getTime();
		long endMill = retTopic.getEndTime().getTime();
		if(curMill<startMill){
			//活动还没开始
			return 0;
		}else if(curMill>endMill){
			//活动已结束
			return -1;
		}else{
			//活动进行中
			return 1;
		}
	}

	@Override
	public List<Topic> selectListByType(Topic entity) {
		List<Topic> list = topicMapper.getTopicByType(entity);
		if(list.size()>0){
			for(Topic topic : list){
				TopicType type = topicTypeMapper.selectByPrimaryKey(topic.getTypeid());
				if(type!=null){
					topic.setTypeName(type.getName());
				}
			}
		}
		return list;
	}

}
