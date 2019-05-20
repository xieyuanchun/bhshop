package com.bh.admin.service;

import com.bh.result.BhResult;
import com.bh.admin.pojo.user.MBusEntity;

public interface MBusMSGService {
	
	MBusEntity queryByUserName(String username) throws Exception;
	
	BhResult checkMsg(String username,String phone) throws Exception;
	
	BhResult updatePwd(String username,String phone,String pwd) throws Exception;

}
