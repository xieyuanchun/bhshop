package com.bh.user.api.service;

import com.bh.user.pojo.MemberBalanceLog;
import com.bh.utils.PageBean;

public interface MemberBalanceLogService {
	//个人钱包明细
	PageBean<MemberBalanceLog> listPage(MemberBalanceLog entity) throws Exception;
}
