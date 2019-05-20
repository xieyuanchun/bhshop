package com.bh.product.api.service;

import java.util.List;

import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.MemberUserAccessLog;
import com.bh.user.pojo.Member;
import com.bh.utils.PageBean;

public interface MemberUserAccessLogService {

	/*浏览记录分页列表*/
	PageBean<MemberUserAccessLog> getPageBymId(Member member, String currentPage, int pageSize) throws Exception;
}
