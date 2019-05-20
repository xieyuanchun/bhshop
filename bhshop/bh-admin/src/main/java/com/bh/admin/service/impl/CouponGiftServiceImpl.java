package com.bh.admin.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.admin.mapper.goods.CouponAndGiftMapper;
import com.bh.admin.mapper.goods.CouponGiftMapper;
import com.bh.admin.pojo.goods.CouponAndGift;
import com.bh.admin.pojo.goods.CouponGift;
import com.bh.admin.service.CouponGiftService;
import com.bh.config.Contants;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
@Service
public class CouponGiftServiceImpl implements CouponGiftService{
	@Autowired
	private CouponGiftMapper mapper;
	@Autowired
	private CouponAndGiftMapper andGiftMapper;

	@Override
	public int insert(CouponGift entity) throws Exception {
		int row = 0;
		entity.setAddTime(new Date());
		row = mapper.insertSelective(entity);
		
		List<CouponAndGift> couponAndGiftList = entity.getCouponAndGiftList();//插入中间表
		if(couponAndGiftList!=null){
			for(CouponAndGift cag : couponAndGiftList){
				cag.setcGId(entity.getId());
				andGiftMapper.insertSelective(cag);
			}
		}
		return row;
	}

	@Override
	public int edit(CouponGift entity) throws Exception {
		int row = 0;
		CouponAndGift gift = new CouponAndGift();
		gift.setcGId(entity.getId());
		List<CouponAndGift> oldList = andGiftMapper.selectByCGid(gift);
		if(oldList.size()>0){
			for(CouponAndGift cgt : oldList){
				andGiftMapper.deleteByPrimaryKey(cgt.getId());
			}
		}
		List<CouponAndGift> couponAndGiftList = entity.getCouponAndGiftList();
		if(couponAndGiftList!=null){
			for(CouponAndGift cag : couponAndGiftList){
				cag.setcGId(entity.getId());
				andGiftMapper.insertSelective(cag);
			}
		}
		row = mapper.updateByPrimaryKeySelective(entity);
		return row;
	}
	/*public int edit(CouponGift entity) throws Exception {
		int row = 0;
		List<CouponAndGift> couponAndGiftList = entity.getCouponAndGiftList();
		if(couponAndGiftList!=null){
			for(CouponAndGift cag : couponAndGiftList){
				if(cag.getId()!=null){
					CouponAndGift cagEntity = andGiftMapper.selectByPrimaryKey(cag.getId());
					if(cagEntity !=null){//存在更新
						andGiftMapper.updateByPrimaryKeySelective(cag);
					}else{//不存在插入
						cag.setcGId(entity.getId());
						andGiftMapper.insertSelective(cag);
					}
				}else{
					cag.setcGId(entity.getId());
					andGiftMapper.insertSelective(cag);
				}
			}
		}
		row = mapper.updateByPrimaryKeySelective(entity);
		return row;
	}*/

	@Override
	public int delete(CouponGift entity) throws Exception {
		int row = 0;
		row = mapper.deleteByPrimaryKey(entity.getId());
		CouponAndGift cag = new CouponAndGift();
		cag.setcGId(entity.getId());
		List<CouponAndGift> couponAndGiftList = andGiftMapper.selectByCGid(cag);
		if(couponAndGiftList.size()>0){
			for(CouponAndGift g : couponAndGiftList){
				row = andGiftMapper.deleteByPrimaryKey(g.getcId());
			}
		}
		return row;
	}

	@Override
	public CouponGift get(CouponGift entity) throws Exception {
		CouponGift gift = mapper.selectByPrimaryKey(entity.getId());
		CouponAndGift cag = new CouponAndGift();
		cag.setcGId(entity.getId());
		List<CouponAndGift> couponAndGiftList = andGiftMapper.selectByCGid(cag);
		if(couponAndGiftList.size()>0){
			gift.setCouponAndGiftList(couponAndGiftList);
		}
		return gift;
	}

	@Override
	public PageBean<CouponGift> listPage(CouponGift entity) throws Exception {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Contants.PAGE_SIZE, true);
		List<CouponGift> list = mapper.listPage(entity);
		PageBean<CouponGift> pageBean = new PageBean<>(list);
		return pageBean;
	}

	@Override
	public int deleteAndGift(int id) throws Exception {
		// TODO Auto-generated method stub
		return andGiftMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int update(CouponGift entity) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKeySelective(entity);
	}

}
