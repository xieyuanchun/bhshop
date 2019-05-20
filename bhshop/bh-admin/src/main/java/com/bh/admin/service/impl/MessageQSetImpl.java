package com.bh.admin.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bh.admin.mapper.goods.MessageQSetMapper;
import com.bh.admin.pojo.goods.MessageQSet;
import com.bh.admin.service.MessageQSetService;
import com.bh.utils.PageBean;
import com.bh.utils.RegExpValidatorUtils;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;

@Service
public class MessageQSetImpl implements MessageQSetService{
	@Autowired
	private MessageQSetMapper mapper;

	/**
	 * 设置接受消息人
	 */
	@Override
	public int insertQSet(String sendTo, String type, String remark) throws Exception {
		int row = 0;
		
		String[] str = sendTo.split(",");
	    if(Integer.parseInt(type)==1){ //验证邮箱
	    	for(int i=0; i<str.length; i++){
	    		if(RegExpValidatorUtils.isEmail(str[i])){
	    			
		    	}else{
		    		return -1;
		    	}
	    	}
	    }else{//验证手机号
	    	for(int i=0; i<str.length; i++){
		    	if(RegExpValidatorUtils.checkMobile(str[i])){
					
		    	}else{
		    		return -2;
		    	}
	    	}
	    }
		
		MessageQSet messageSet = new MessageQSet();
		if(!StringUtils.isEmptyOrWhitespaceOnly(sendTo)){
			messageSet.setSendTo(sendTo);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(type)){
			messageSet.setType(type);
		}
		if(!StringUtils.isEmptyOrWhitespaceOnly(remark)){
			messageSet.setRemark(remark);
		}
		row = mapper.insertSelective(messageSet);
		return row;
	}
	
	/**
	 * 接收消息账号管理
	 */
	@Override
	public PageBean<MessageQSet> pageList(String currentPage, int pageSize, String type) throws Exception {
		PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
		List<MessageQSet> list = mapper.pageList(type);
		PageBean<MessageQSet> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	/**
	 * 修改接受消息人
	 */
	@Override
	public int updateQSet(String id, String sendTo, String remark, String type) throws Exception {
		int row = 0;
		String[] str = sendTo.split(",");
	    if(Integer.parseInt(type)==1){ //验证邮箱
	    	for(int i=0; i<str.length; i++){
	    		if(RegExpValidatorUtils.isEmail(str[i])){
	    			
		    	}else{
		    		return -1;
		    	}
	    	}
	    }else{//验证手机号
	    	for(int i=0; i<str.length; i++){
		    	if(RegExpValidatorUtils.checkMobile(str[i])){
					
		    	}else{
		    		return -2;
		    	}
	    	}
	    }
		MessageQSet qSet = mapper.selectByPrimaryKey(Integer.parseInt(id));
		if(qSet!=null){
			if(!StringUtils.isEmptyOrWhitespaceOnly(sendTo)){
				qSet.setSendTo(sendTo);
			}
			qSet.setRemark(remark);
			if(!StringUtils.isEmptyOrWhitespaceOnly(type)){
				qSet.setType(type);
			}
			row = mapper.updateByPrimaryKeySelective(qSet);
		}
		return row;
	}
	
	/**
	 * 删除接受消息人
	 */
	@Override
	public int deleteQSet(String id) throws Exception {
		return mapper.deleteByPrimaryKey(Integer.decode(id));
	}
	
}
