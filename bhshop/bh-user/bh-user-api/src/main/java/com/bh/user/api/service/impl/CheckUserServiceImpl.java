package com.bh.user.api.service.impl;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.user.api.service.CheckUserService;
import com.bh.user.mapper.MemberSendMapper;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.pojo.MemberSend;
import com.bh.user.pojo.MemberShop;
import com.bh.utils.PageBean;

@Service
public class CheckUserServiceImpl implements CheckUserService{
	
	@Autowired
	private MemberShopMapper memberShopMapper;
	
	@Autowired
	private MemberSendMapper memberSendMapper;

	public PageBean<MemberSend> selectAllSendByStatus(String pSize,String currentPages) throws Exception{
		PageBean<MemberSend> bean = null;
		
		Integer currentPage = Integer.parseInt(currentPages);
		Integer pageSize = Integer.parseInt(pSize);
		Integer pageStart = (currentPage-1)*pageSize;
		List<MemberSend> list = memberSendMapper.selectAllSendByStatus(pageStart, currentPage);
		int totalCount = memberSendMapper.selectAllSendByStatusCount();
		int pages = totalCount/pageSize;
		pages = totalCount % pageSize > 0? (pages +1) : pages; 
		int size = list.size() == pageSize ? pageSize :list.size();
		bean = new PageBean<>(list);
		bean.setPageNum(currentPage);
		bean.setList(list);
		bean.setTotal(totalCount);
		bean.setPages(pages);
		bean.setPageSize(pageSize);
		bean.setSize(size);
		

		return bean;
	}
	
	public PageBean<MemberShop> selectAllShopByStatus(String pSize,String currentPages) throws Exception{
		PageBean<MemberShop> bean =null;
		Integer currentPage = Integer.parseInt(currentPages);
		Integer pageSize = Integer.parseInt(pSize);
		Integer pageStart = (currentPage-1)*pageSize;
		List<MemberShop> list = memberShopMapper.selectStatusByPage(pageStart,pageSize);
		int totalCount = memberShopMapper.selectStatusByPageCount();
		int pages = totalCount/pageSize;
		pages = totalCount % pageSize > 0? (pages +1) : pages; 
		int size = list.size() == pageSize ? pageSize :list.size();
		bean = new PageBean<>(list);
		bean.setPageNum(currentPage);
		bean.setList(list);
		bean.setTotal(totalCount);
		bean.setPages(pages);
		bean.setPageSize(pageSize);
		bean.setSize(size);
		
		return bean;
	}
	

	
	// 更新商家审核的状态
	public int updateShopStatus(MemberShop memberShop) throws Exception {
		int row = memberShopMapper.updateShopStatus(memberShop);
		return row;
	}
	
	//更新配送员的审核状态
	public int updateSendStatus(MemberSend memberSend) throws Exception{
		int row = memberSendMapper.updateSendStatus(memberSend);
		return row;
	}

	
}
