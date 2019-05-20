package com.bh.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bh.admin.mapper.user.WhiteMemberMapper;
import com.bh.admin.pojo.user.WhiteMember;
import com.bh.admin.service.WhiteMemberService;
import com.bh.admin.util.JedisUtil;
import com.bh.config.Contants;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;

@Service
public class WhiteMemberImpl implements WhiteMemberService {

	@Autowired
	private WhiteMemberMapper  whiteMemberMapper;
	
	
	
	@Override
	public int add(WhiteMember w) {
		// TODO Auto-generated method stub
		int num = whiteMemberMapper.insertSelective(w);
		
		JedisUtil jedisUtil= JedisUtil.getInstance();  
		JedisUtil.Strings strings=jedisUtil.new Strings();
		
		if(StringUtils.isNotBlank(strings.get("whiteMember"))) {//开启
			String whiteMember = whiteMemberMapper.getAll();
			if(StringUtils.isNotBlank(whiteMember)) {
			    strings.set("whiteMember", whiteMember);
			}else {
			    strings.set("whiteMember", ",");
			}
	    }else {
			strings.set("whiteMember", ""); 
	    }
		
		return num;
	}

	@Override
	public int update(WhiteMember e) {
		// TODO Auto-generated method stub
		int num = whiteMemberMapper.updateByPrimaryKeySelective(e);

		JedisUtil jedisUtil= JedisUtil.getInstance();  
		JedisUtil.Strings strings=jedisUtil.new Strings();
		
		if(StringUtils.isNotBlank(strings.get("whiteMember"))) {//开启
			String whiteMember = whiteMemberMapper.getAll();
			if(StringUtils.isNotBlank(whiteMember)) {
			    strings.set("whiteMember", whiteMember);
			}else {
			    strings.set("whiteMember", ",");
			}
	    }else {
			strings.set("whiteMember", ""); 
	    }
		
		return num;
	}

	@Override
	public Map<String, Object> listPage(WhiteMember w) {
		PageHelper.startPage(Integer.parseInt(w.getCurrentPage()), Contants.PAGE_SIZE, true);
		Map<String, Object> map = new HashMap<String, Object>();
		List<WhiteMember> list = whiteMemberMapper.pageList(w);
	    PageBean<WhiteMember> pageBean = new PageBean<>(list);
	    map.put("WhiteMember", pageBean);
		return map;
	}

	@Override
	public int delete(WhiteMember e) {
		// TODO Auto-generated method stub
		int num = whiteMemberMapper.deleteByPrimaryKey(e.getId());
		
		JedisUtil jedisUtil= JedisUtil.getInstance();  
		JedisUtil.Strings strings=jedisUtil.new Strings();
		if(StringUtils.isNotBlank(strings.get("whiteMember"))) {//开启
		   String whiteMember = whiteMemberMapper.getAll();
		   if(StringUtils.isNotBlank(whiteMember)) {
		      strings.set("whiteMember", whiteMember);
		   }else {
			  strings.set("whiteMember", ",");
		   }
		}else {
		   strings.set("whiteMember", ""); 
		}
		return num;
	}

	@Override
	public List<WhiteMember> getByMid(WhiteMember w) {
		// TODO Auto-generated method stub
		return whiteMemberMapper.getByMid(w);
	}

	@Override
	public String getAll() {
		// TODO Auto-generated method stub
		return whiteMemberMapper.getAll();
	}
    
	
}
