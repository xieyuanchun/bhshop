package com.bh.admin.service;

import java.util.List;
import com.bh.admin.pojo.goods.GoodsPriceApproval;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.goods.SimplePriceApproval;
import com.bh.utils.PageBean;

public interface GoodsPriceApprovalService {
	//如果价格改变，则插入价格改变表
	void insertGoodsPriceApproval(GoodsSku goodsSku,Integer userId) throws Exception;
	//价格的审核
	int checkVelifyStatus(GoodsPriceApproval vApproval) throws Exception;
	
	//价格的审核
	int velifyStatus(GoodsPriceApproval val) throws Exception;

	int velifyStatus2(GoodsPriceApproval val) throws Exception;
	
	PageBean<GoodsPriceApproval> selectPriceApprovalList(GoodsPriceApproval val) throws Exception;
	List<GoodsPriceApproval> selectByStatus(GoodsPriceApproval val) throws Exception;	
	int deletePriceApproval(List<String> ids) throws Exception;

	
	
	//2018-4-16
	PageBean<SimplePriceApproval> selectPriceApprovalList1(SimplePriceApproval appro) throws Exception;

}
