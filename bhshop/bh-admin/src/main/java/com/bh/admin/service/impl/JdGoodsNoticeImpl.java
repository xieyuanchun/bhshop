package com.bh.admin.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.admin.mapper.goods.GoodsMapper;
import com.bh.admin.mapper.goods.JdGoodsNoticeMapper;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.JdGoodsNotice;
import com.bh.admin.service.JdGoodsNoticeService;
import com.bh.config.Contants;
import com.bh.utils.MoneyUtil;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;

@Service
public class JdGoodsNoticeImpl implements JdGoodsNoticeService{
	@Autowired
	private JdGoodsNoticeMapper mapper;
	@Autowired
	private GoodsMapper goodsMapper;
	
	/**
	 * 商品变更消息列表
	 */
	@Override
	public PageBean<JdGoodsNotice> pageList(JdGoodsNotice jdGoodsNotice) throws Exception {
		PageHelper.startPage(jdGoodsNotice.getCurrentPage(), Contants.PAGE_SIZE, true);
		List<JdGoodsNotice> list = mapper.pageList(jdGoodsNotice);
		if(list.size()>0){
			for(JdGoodsNotice entity : list){
				Goods goods = goodsMapper.selectByPrimaryKey(entity.getGoodsId());
				if(goods!=null){
					entity.setSaleType(goods.getSaleType());
					entity.setGoodsStatus(goods.getStatus());
				}
				entity.setRealOldJdPrice(MoneyUtil.IntToDouble(entity.getOldJdPrice()));	
				entity.setRealOldJdProtocolPrice(MoneyUtil.IntToDouble(entity.getOldJdProtocolPrice()));
				entity.setRealOldSellPrice(MoneyUtil.IntToDouble(entity.getOldSellPrice()));
				entity.setRealOldTeamPrice(MoneyUtil.IntToDouble(entity.getOldTeamPrice()));
				entity.setRealNewJdPrice(MoneyUtil.IntToDouble(entity.getNewJdPrice()));
				entity.setRealNewJdProtocolPrice(MoneyUtil.IntToDouble(entity.getNewJdProtocolPrice()));
				entity.setRealNewSellPrice(MoneyUtil.IntToDouble(entity.getNewSellPrice()));
				entity.setRealNewTeamPrice(MoneyUtil.IntToDouble(entity.getNewTeamPrice()));
			}
		}
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
