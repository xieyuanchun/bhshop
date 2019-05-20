package com.bh.product.api.service;

import com.bh.goods.pojo.RobotHead;
import com.bh.user.pojo.PromoteUser;
import com.bh.utils.PageBean;

public interface RobotHeadService {

	

	int save(String imgs);

	int update(RobotHead robotHead);

	int delete(String ids);

	PageBean<RobotHead> listPage(RobotHead robotHead);

	PageBean<PromoteUser> list(PromoteUser promoteUser);

	int updateName(PromoteUser promoteUser);
	
	
}
