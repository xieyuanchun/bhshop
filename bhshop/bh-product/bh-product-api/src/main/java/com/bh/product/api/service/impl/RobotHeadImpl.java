package com.bh.product.api.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bh.goods.mapper.RobotHeadMapper;
import com.bh.goods.pojo.RobotHead;
import com.bh.product.api.service.RobotHeadService;
import com.bh.user.mapper.PromoteUserMapper;
import com.bh.user.pojo.PromoteUser;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;

@Service
public class RobotHeadImpl implements  RobotHeadService{
	@Autowired
	private RobotHeadMapper robotHeadMapper;
	@Autowired
	private PromoteUserMapper promoteUserMapper;
	
	@Override
	public int save(String imgs) {
		int row=0;
		String []headImg=imgs.split(",");
		for (String img : headImg) {
			RobotHead robotHead=new RobotHead();
			robotHead.setImg(img);
			row=robotHeadMapper.insert(robotHead);   
		}
		return row;
	}

	@Override
	public int update(RobotHead robotHead) {
		return robotHeadMapper.updateByPrimaryKey(robotHead);
	}

	@Override
	public int delete(String ids) {
		int row=0;
		String []headIds=ids.split(",");
		for (String id : headIds) {
			row=robotHeadMapper.deleteByPrimaryKey(Integer.valueOf(id));
		}
		return row;
	}

	public PageBean<RobotHead> listPage(RobotHead robotHead) {
		PageHelper.startPage(robotHead.getCurrentPage(), robotHead.getPageSize(), true);
		List<RobotHead> list = robotHeadMapper.getALL();
		PageBean<RobotHead> page = new PageBean<>(list);
		return page;
	}

	@Override
	public PageBean<PromoteUser> list(PromoteUser promoteUser) {
		PageHelper.startPage(Integer.valueOf(promoteUser.getCurrentPage()),promoteUser.getPageSize(), true);
		List<PromoteUser> list = promoteUserMapper.selectAllPUser(promoteUser);
		PageBean<PromoteUser> page = new PageBean<>(list);
		return page;
	}

	@Override
	public int updateName(PromoteUser promoteUser) {
		return promoteUserMapper.updateByPrimaryKeySelective(promoteUser);
	}



	
	
	
	
}
