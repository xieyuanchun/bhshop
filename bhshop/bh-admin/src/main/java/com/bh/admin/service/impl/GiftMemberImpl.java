package com.bh.admin.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.admin.mapper.user.GiftMemberMapper;
import com.bh.admin.pojo.goods.ExchangeGroup;
import com.bh.admin.pojo.user.GiftMember;
import com.bh.admin.service.GiftMemberService;
import com.bh.config.Contants;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;

@Service
public class GiftMemberImpl implements GiftMemberService {

	@Autowired
	private GiftMemberMapper giftMemberMapper;
	
	@Override
	public int update(GiftMember g) {
		// TODO Auto-generated method stub
		return giftMemberMapper.updateByPrimaryKeySelective(g);
	}

	@Override
	public Map<String, Object> listPage(GiftMember g) {
		PageHelper.startPage(Integer.parseInt(g.getCurrentPage()), Contants.PAGE_SIZE, true);
		Map<String, Object> map = new HashMap<String, Object>();
		List<GiftMember> list = giftMemberMapper.pageList(g);
	    PageBean<GiftMember> pageBean = new PageBean<>(list);
	    map.put("GiftMember", pageBean);
		return map;
	}

	@Override
	public int delete(GiftMember g) {
		// TODO Auto-generated method stub
		return giftMemberMapper.deleteByPrimaryKey(g.getId());
	}

	@Override
	public int add(GiftMember e) {
		// TODO Auto-generated method stub
		return giftMemberMapper.insertSelective(e);
	}

	
	
	
}
