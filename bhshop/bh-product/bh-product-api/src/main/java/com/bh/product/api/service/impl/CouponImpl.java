package com.bh.product.api.service.impl;

import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.config.Contants;
import com.bh.goods.mapper.CouponMapper;
import com.bh.goods.pojo.Coupon;
import com.bh.goods.pojo.CouponLog;
import com.bh.product.api.service.CouponService;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
@Service
public class CouponImpl implements CouponService{

	
	@Autowired
	private CouponMapper couponMapper;
	
	@Override
	public int addCoupon(Coupon coupon) {
		// TODO Auto-generated method stub
		return couponMapper.insertSelective(coupon);
	}

	@Override
	public int updateCoupon(Coupon coupon) {
		// TODO Auto-generated method stub
		return couponMapper.updateByPrimaryKeySelective(coupon);
	}

	@Override
	public int deleteCoupon(Coupon coupon) {
		// TODO Auto-generated method stub
		return couponMapper.deleteByPrimaryKey(coupon.getId());
	}

	//PC端优惠劵分页显示
	@Override
	public Map<String, Object> listCoupon(Coupon coupon) {
		// TODO Auto-generated method stub
		PageHelper.startPage(Integer.parseInt(coupon.getCurrentPage()), Contants.PAGE_SIZE, true);
		List<Coupon> list = couponMapper.listPage(coupon);
		for(int i=0;i<list.size();i++){
			if(list.get(i).getStock()!=null&&list.get(i).getSended()!=null){
//				double stock = list.get(i).getStock();
//				double sended = list.get(i).getSended();
//				double num = stock/sended;
				
//				String str = String.valueOf(num*100);//浮点变量a转换为字符串str
//		        int idx = str.lastIndexOf(".");//查找小数点的位置
//		        String strNum = str.substring(0,idx);//截取从字符串开始到小数点位置的字符串，就是整数部分	
//				list.get(i).setPercentage(strNum+"%");//剩余百分比
				
				
			    double amount = list.get(i).getAmount();
			    list.get(i).setAmount2(amount/100+""); //把金额分转换成元
			
		    	double needAmount = list.get(i).getNeedAmount();
			    list.get(i).setNeedAmount2(needAmount/100+"");//把金额分转换成元
			}
		}
		PageBean<Coupon> pageBean = new PageBean<>(list);
		Map<String,Object> map=new HashMap<String,Object>();
		
		map.put("Coupon", pageBean);
		return map;
	}

	@Override
	public Coupon selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return couponMapper.selectByPrimaryKey(id);
	}

	//手机端分页显示可领取
	@Override
	public Map<String, Object> moblieListCoupon(Coupon coupon) {
		PageHelper.startPage(Integer.parseInt(coupon.getCurrentPage()), Contants.PAGE_SIZE, true);
		//前端传status=1过来表示获取已启用的优惠劵
		coupon.setStartTime(new Date());//当前的时间必须大于开始领取时间
		coupon.setEndTime(new Date());//当前的时间必须小于开始领取时间
		
		List<Coupon> list = couponMapper.moblieListPage(coupon);
		for(int i=0;i<list.size();i++){
			if(list.get(i).getStock()!=null&&list.get(i).getSended()!=null){
				double stock = list.get(i).getStock();
				double sended = list.get(i).getSended();
				double num = stock/sended;
				
//				String str = String.valueOf(num*100);//浮点变量a转换为字符串str
//		        int idx = str.lastIndexOf(".");//查找小数点的位置
//		        String strNum = str.substring(0,idx);//截取从字符串开始到小数点位置的字符串，就是整数部分	
//				list.get(i).setPercentage(strNum+"%");//剩余百分比
				NumberFormat nf = NumberFormat.getPercentInstance();
		        nf.setMinimumFractionDigits(0);
		        list.get(i).setPercentage(nf.format(num));//剩余百分比
		        
			    double amount = list.get(i).getAmount();
			    list.get(i).setAmount2(amount/100+""); //把金额分转换成元
			
		    	double needAmount = list.get(i).getNeedAmount();
			    list.get(i).setNeedAmount2(needAmount/100+"");//把金额分转换成元
			    
			    if(list.get(i).getStock()>0){
			    	list.get(i).setLeadEnd("0");//可领取
			    }else{
			    	list.get(i).setLeadEnd("1");//已领完
			    }
			}
		}
		PageBean<Coupon> pageBean = new PageBean<>(list);
		Map<String,Object> map=new HashMap<String,Object>();
		
		map.put("Coupon", pageBean);
		return map;
	}

}
