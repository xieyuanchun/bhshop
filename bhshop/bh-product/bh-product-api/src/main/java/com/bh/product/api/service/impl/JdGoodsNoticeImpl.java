package com.bh.product.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.goods.mapper.JdGoodsNoticeMapper;
import com.bh.goods.pojo.JdGoodsNotice;
import com.bh.product.api.service.JdGoodsNoticeService;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;

@Service
public class JdGoodsNoticeImpl implements JdGoodsNoticeService{
	@Autowired
	private JdGoodsNoticeMapper mapper;
	
	/**
	 * 商品变更消息列表
	 */
	@Override
	public PageBean<JdGoodsNotice> pageList(String currentPage, int pageSize, String name, String type, String isRead) throws Exception {
		PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
		List<JdGoodsNotice> list = mapper.pageList(name, type, isRead);
		PageBean<JdGoodsNotice> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	/**
	 * 已阅
	 */
	@Override
	public int changeReadStatus(String id) throws Exception {
		int row = 0;
		JdGoodsNotice notice = mapper.selectByPrimaryKey(Integer.parseInt(id));
		if(notice!=null){
			notice.setIsRead(1); //	'是否已读(0未读 1已读)',
			row = mapper.updateByPrimaryKeySelective(notice);
		}
		return row;
	}
}
