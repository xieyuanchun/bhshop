package com.bh.admin.service;

import com.bh.admin.pojo.goods.RobotHead;
import com.bh.admin.pojo.user.PromoteUser;
import com.bh.utils.PageBean;

public interface RobotHeadService {

	

	int save(String imgs);

	int update(RobotHead robotHead);

	int delete(String ids);

	PageBean<RobotHead> listPage(RobotHead robotHead);

	PageBean<PromoteUser> list(PromoteUser promoteUser);

	int updateName(PromoteUser promoteUser);
	
	
}
