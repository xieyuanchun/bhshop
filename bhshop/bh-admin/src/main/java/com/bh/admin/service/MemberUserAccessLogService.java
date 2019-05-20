package com.bh.admin.service;

import java.util.List;

import com.bh.admin.pojo.goods.MemberUserAccessLog;
import com.bh.admin.pojo.user.Member;
import com.bh.utils.PageBean;

public interface MemberUserAccessLogService {

	/*浏览记录分页列表*/
	PageBean<MemberUserAccessLog> getPageBymId(Member member, String currentPage, int pageSize) throws Exception;
}
