package com.bh.admin.service.impl;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bh.admin.mapper.goods.CouponLogMapper;
import com.bh.admin.mapper.goods.CouponMapper;
import com.bh.admin.mapper.goods.GoodsCategoryMapper;
import com.bh.admin.mapper.order.OrderMapper;
import com.bh.admin.mapper.order.OrderShopMapper;
import com.bh.admin.pojo.goods.Coupon;
import com.bh.admin.pojo.goods.CouponLog;
import com.bh.admin.pojo.goods.GoodsCategory;
import com.bh.admin.pojo.goods.ReturnCouponLog;
import com.bh.admin.pojo.order.Order;
import com.bh.admin.pojo.order.OrderShop;
import com.bh.admin.service.CouponLogService;
import com.bh.config.Contants;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;


@Service
public class CouponLogImpl implements CouponLogService{

	@Autowired
	private CouponLogMapper couponLogMapper;
	@Autowired
	private CouponMapper couponMapper;
	@Autowired
	private GoodsCategoryMapper goodsCategoryMapper;
	@Autowired
	private OrderShopMapper orderShopMapper;
	
	@Override
	public int addCouponLog(CouponLog couponLog) {
		// TODO Auto-generated method stub
		return couponLogMapper.insertSelective(couponLog);
	}

	/**
	 * 我的移动端的优惠劵列表
	 */
	public Map<String, Object> listCouponLog(CouponLog couponLog) {
		// TODO Auto-generated method stub
		PageHelper.startPage(Integer.parseInt(couponLog.getCurrentPage()), Contants.PAGE_SIZE, true);
		Map<String, Object> map = new HashMap<String, Object>();
		// statusLog 前端传进来的0未使用, 1已使用,2、未领取、 3已过期
		if (couponLog.getStatusLog().equals("0")) {// 未使用
			couponLog.setStatus(0);// 未使用
			couponLog.setExpireTime(new Date());// 当前时间
			List<CouponLog> list = couponLogMapper.notUseListPage(couponLog);
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getStock() != null && list.get(i).getSended() != null) {
					double stock = list.get(i).getStock();
					double sended = list.get(i).getSended();
					double num = stock / sended;
					System.out.println(stock);
					
					NumberFormat nf = NumberFormat.getPercentInstance();
					nf.setMinimumFractionDigits(0);
					list.get(i).setPercentage(nf.format(num));// 剩余百分比
					double needAmount = list.get(i).getNeed_amount();// 把分转成元
					double needAmount2 = needAmount / 100;
					list.get(i).setNeedAmount(needAmount2 + "");
					double amount = list.get(i).getAmount();// 把分转成元 ，优惠金额
					list.get(i).setAmount2(amount / 100 + "");
				}
			}
			PageBean<CouponLog> pageBean = new PageBean<>(list);
			map.put("CouponLogs", pageBean);
			map.put("num", list.size());
		} else if (couponLog.getStatusLog().equals("1")) {// 已使用
			couponLog.setStatus(1);// 已使用
			List<CouponLog> list = couponLogMapper.listPage(couponLog);
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getStock() != null && list.get(i).getSended() != null) {
					double stock = list.get(i).getStock();
					double sended = list.get(i).getSended();
					double num = stock / sended;
					NumberFormat nf = NumberFormat.getPercentInstance();
					nf.setMinimumFractionDigits(0);
					list.get(i).setPercentage(nf.format(num));// 剩余百分比
					double needAmount = list.get(i).getNeed_amount();// 把分转成元
					double needAmount2 = needAmount / 100;
					list.get(i).setNeedAmount(needAmount2 + "");
					double amount = list.get(i).getAmount();// 把分转成元 ，优惠金额
					list.get(i).setAmount2(amount / 100 + "");
				}
			}
			PageBean<CouponLog> pageBean = new PageBean<>(list);
			map.put("CouponLogs", pageBean);
			map.put("num", list.size());
		} else if (couponLog.getStatusLog().equals("2")) {// 未使用
			couponLog.setStatus(2);// 未使用
			List<CouponLog> list = couponLogMapper.listPage(couponLog);
			System.out.println(list.size());
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getStock() != null && list.get(i).getSended() != null) {
					double stock = list.get(i).getStock();
					double sended = list.get(i).getSended();
					double num = stock / sended;
					NumberFormat nf = NumberFormat.getPercentInstance();
					nf.setMinimumFractionDigits(0);
					list.get(i).setPercentage(nf.format(num));// 剩余百分比
					double needAmount = list.get(i).getNeed_amount();// 把分转成元
					double needAmount2 = needAmount / 100;
					list.get(i).setNeedAmount(needAmount2 + "");
					double amount = list.get(i).getAmount();// 把分转成元 ，优惠金额
					list.get(i).setAmount2(amount / 100 + "");
				}
			}
			PageBean<CouponLog> pageBean = new PageBean<>(list);
			map.put("CouponLogs", pageBean);
			map.put("num", list.size());
		} else if (couponLog.getStatusLog().equals("3")) {// 已过期
			couponLog.setExpireTime(new Date());// 当前时间与数据库时间比较
			List<CouponLog> list = couponLogMapper.listPage(couponLog);
			for (int i = 0; i < list.size(); i++) {
				if (list.get(i).getStock() != null && list.get(i).getSended() != null) {
					double stock = list.get(i).getStock();
					double sended = list.get(i).getSended();
					double num = stock / sended;
					NumberFormat nf = NumberFormat.getPercentInstance();
					nf.setMinimumFractionDigits(0);
					list.get(i).setPercentage(nf.format(num));// 剩余百分比
					double needAmount = list.get(i).getNeed_amount();// 把分转成元
					double needAmount2 = needAmount / 100;
					list.get(i).setNeedAmount(needAmount2 + "");
					double amount = list.get(i).getAmount();// 把分转成元 ，优惠金额
					list.get(i).setAmount2(amount / 100 + "");
				}
			}
			PageBean<CouponLog> pageBean = new PageBean<>(list);
			map.put("CouponLogs", pageBean);
			map.put("num", list.size());
		}
		return map;
	}

	/**
	 * 我的移动端，当前可以使用的优惠劵列表
	 */
	@Override
	public Map<String, Object> goodListCouponLog(CouponLog couponLog) {
		// TODO Auto-generated method stub
		PageHelper.startPage(Integer.parseInt(couponLog.getCurrentPage()), Contants.PAGE_SIZE, true);
		Map<String, Object> map = new HashMap<String, Object>();
		couponLog.setExpireTime(new Date());// 当前时间
		List<CouponLog> list = couponLogMapper.goodListPage(couponLog);// 商家的优惠劵
		for (int i = 0; i < list.size(); i++) {
			if (list.get(i).getStock() != null && list.get(i).getSended() != null) {
				double stock = list.get(i).getStock();
				double sended = list.get(i).getSended();
				double num = stock / sended;
				list.get(i).setPercentage(num * 100 + "%");// 剩余百分比

				double needAmount = list.get(i).getNeed_amount();// 把分转成元
																	// ，满多少才能消费
				double needAmount2 = needAmount / 100;
				list.get(i).setNeedAmount(needAmount2 + "");

				double amount = list.get(i).getAmount();// 把分转成元 ，优惠金额
				list.get(i).setAmount2(amount / 100 + "");
			}
		}

		// CouponLog couponLog2 = new CouponLog();
		// couponLog2.setExpireTime(new Date());//当前时间
		// couponLog2.setmId(couponLog.getmId());
		// couponLog2.setCurrentPage(couponLog.getCurrentPage());
		// list = couponLogMapper.goodAllListPage(couponLog2);//平台的优惠劵
		// for(int i=0;i<list.size();i++){
		// if(list.get(i).getStock()!=null&&list.get(i).getSended()!=null){
		// double stock = list.get(i).getStock();
		// double sended = list.get(i).getSended();
		// double num = stock/sended;
		// list.get(i).setPercentage(num*100+"%");//剩余百分比
		//
		// double needAmount = list.get(i).getNeed_amount();//把分转成元 ，满多少才能消费
		// double needAmount2 = needAmount / 100;
		// list.get(i).setNeedAmount(needAmount2+"");
		//
		// double amount = list.get(i).getAmount();//把分转成元 ，优惠金额
		// list.get(i).setAmount2(amount/100+"");
		// }
		// }

		PageBean<CouponLog> pageBean = new PageBean<>(list);
		map.put("CouponLogs", pageBean);
		return map;
	}

	/**
	 * 所有的优惠劵
	 */
	@Override
	public Map<String, Object> allListCouponLog(CouponLog entity) {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Contants.PAGE_SIZE, true);
		Map<String, Object> map = new HashMap<String, Object>();
		if(entity.getStatus()!=null ){//过期
			if(entity.getStatus()==2){
				entity.setExpireTime(null);
			}else{
				entity.setExpireTime(new Date());
			}
		}
		List<CouponLog> listLog = couponLogMapper.allListPage(entity);
		List<ReturnCouponLog> returnList =new ArrayList<ReturnCouponLog>() ;
		
		for (CouponLog couponLog : listLog) {
			ReturnCouponLog returnCouponLog=new ReturnCouponLog();
			Coupon coupon=couponMapper.selectByPrimaryKey(couponLog.getCouponId());
			
			returnCouponLog.setId(couponLog.getId());
			returnCouponLog.setCouponId(couponLog.getCouponId());
			returnCouponLog.setmId(couponLog.getmId());
			returnCouponLog.setNeedAmount((double)coupon.getNeedAmount()/100);
			returnCouponLog.setAmount((double)coupon.getAmount()/100 );//优惠金额
			if(couponLog.getOrderId()>0){
				OrderShop orderShop=orderShopMapper.selectByPrimaryKey(couponLog.getOrderId());//couponLog.getOrderId()商家订单id
				returnCouponLog.setOrderNo(orderShop.getShopOrderNo());//商家订单号
			}
			returnCouponLog.setCreateTime(couponLog.getCreateTime());
			returnCouponLog.setUseTime(couponLog.getUseTime());
			returnCouponLog.setType(coupon.getType());
			returnCouponLog.setRemark(coupon.getRemark());
			returnCouponLog.setCouponType(coupon.getCouponType());
			returnCouponLog.setCouponName(coupon.getTitle());
			returnCouponLog.setGetWay(couponLog.getGetWay());
			if(couponLog.getStatus()!=2){
				long difference = (couponLog.getExpireTime().getTime() - new Date().getTime()) / 86400000;//计算过期时间天数
				if (Math.abs(difference) > 10000) {//永久劵的默认生成的过期时候是领取后的36000天
					returnCouponLog.setExpireTime("永久有效");
				} else {
					SimpleDateFormat sDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //加上时间
					returnCouponLog.setExpireTime(sDateFormat.format(couponLog.getExpireTime()));
				}
				if(new  Date().getTime()>couponLog.getExpireTime().getTime() && couponLog.getStatus()==0){//过期
					returnCouponLog.setCouponStatus(3);
				}else{//已经使用的不管有没有过期		
					returnCouponLog.setCouponStatus(couponLog.getStatus());
				}
			}else{
				returnCouponLog.setCouponStatus(couponLog.getStatus());
			}
			if (coupon.getCatId() == 0) {
				returnCouponLog.setStrApply("适用所有商品");
			} else {
				GoodsCategory goodsCategory = goodsCategoryMapper.selectByPrimaryKey(coupon.getCatId());
				returnCouponLog.setStrApply("适用" + goodsCategory.getName() + "商品");
			}
			returnList.add(returnCouponLog);
		}
		PageBean<CouponLog> pageBean = new PageBean<>(listLog);
		map.put("list", returnList);
		map.put("total", pageBean.getTotal());
		map.put("pageNum", pageBean.getPageNum());
		map.put("pageSize", pageBean.getPageSize());
		map.put("pages", pageBean.getPages());
		map.put("size", pageBean.getSize());
		return map;
	}
	
	/**
	 * @Description: 优惠劵类型处理
	 * @author xieyc
	 * @date 2018年6月6日 下午5:05:19 
	 */
	public String strType(int type){
		String strType=null;
		switch (type) {
		case 1:
			strType="普通券";
			break;
		case 2:
			strType="免邮券";
			break;
		case 3:
			strType="红包券";
			break;

		default:
			strType="错误类型";
			break;
		}
		return strType;
	}
	
	
	/**
	 * 验证用户是否领取过优惠劵
	 */
	@Override
	public List<CouponLog> getByMidAndCouponId(CouponLog couponLog) {
		// TODO Auto-generated method stub
		return couponLogMapper.getByMidAndCouponId(couponLog);
	}

	public static void main(String[] args) {
		
		
		      
		     
	}

}
