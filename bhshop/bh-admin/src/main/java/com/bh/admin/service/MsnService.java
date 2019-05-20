package com.bh.admin.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.bh.admin.pojo.order.MsnApply;
import com.bh.utils.PageBean;
import com.bh.admin.pojo.order.MyMsnApply;
import com.bh.admin.pojo.order.Phone;

public interface MsnService {
    
	 Map getMsnShopInfo(Integer shopId,String memberType);
	 
	 String saveMsnApply(Integer shopId,String template,String groupNum,String memberType,String isFreeNum,String money,String apymsnId);
	 
	 String msaveMsnApply(Integer shopId,String template,String groupNum,String memberType,String isFreeNum,String money,String apymsnId);

	 MsnApply checkIsPaySeccuss(String orderNo);

	 Boolean audit(Integer shopId,Integer userId,String apymsnId,String reviewResult,String reviewResultRemkar);
	 
	 MyMsnApply viewInfo(Integer apymsnId);
	 
	 PageBean<MyMsnApply> getShopListInfo (Integer shopId,String startTime,String endTime,String payStatus,String reviewResult,String currentPage);
	 
	 PageBean<MyMsnApply> getListInfo (String startTime,String endTime,String payStatus,String reviewResult,String currentPage);

	 List<Phone> excelExportPhone(Integer apymsnId) throws Exception;
	 
	 PageBean<MyMsnApply> mgetShopListInfo (Integer shopId,String currentPage);
	 
	 int msgPayFailJob();
	 
	 int beginMonthJob();
}
