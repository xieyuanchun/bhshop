package com.bh.product.api.service.impl;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bh.config.Contants;
import com.bh.goods.mapper.CouponLogMapper;
import com.bh.goods.mapper.CouponMapper;
import com.bh.goods.mapper.GoodsCategoryMapper;
import com.bh.goods.pojo.Coupon;
import com.bh.goods.pojo.CouponLog;
import com.bh.goods.pojo.GoodsCategory;
import com.bh.product.api.service.CouponLogService;
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
	
	@Override
	public int addCouponLog(CouponLog couponLog) {
		// TODO Auto-generated method stub
		return couponLogMapper.insertSelective(couponLog);
	}

	/**
	 * 我的移动端的优惠劵列表
	 */
	@Override
	public Map<String, Object> listCouponLog(CouponLog couponLog) {
		// TODO Auto-generated method stub
		PageHelper.startPage(Integer.parseInt(couponLog.getCurrentPage()), Contants.PAGE_SIZE, true);
		Map<String, Object> map = new HashMap<String, Object>();
		
		List<CouponLog> list=new ArrayList<CouponLog>();
		if (couponLog.getStatusLog().equals("0")) {// 未使用
			couponLog.setStatus(0);// 未使用
			couponLog.setExpireTime(new Date());// 当前时间
			list = couponLogMapper.notUseListPage(couponLog);
		}else if (couponLog.getStatusLog().equals("1")) {// 已使用
			couponLog.setStatus(1);// 已使用
			list = couponLogMapper.listPage(couponLog);
		}else if (couponLog.getStatusLog().equals("2")) {// 未领取
			couponLog.setStatus(2);// 未领取
			list = couponLogMapper.listPage(couponLog);
		}else if (couponLog.getStatusLog().equals("3")) {// 已过期
			couponLog.setStatus(0);//只有未使用的劵才会过期
			couponLog.setExpireTime(new Date());// 当前时间与数据库时间比较
			list = couponLogMapper.listPage(couponLog);
		}
		for (CouponLog log : list) {
			Coupon coupon =couponMapper.selectByPrimaryKey(log.getCouponId());
			log.setCouponType(coupon.getCouponType());//类型
			log.setTypeStr(strType(coupon.getCouponType()));//优惠卷类型
			double amount = (double)coupon.getAmount()/100;// 把分转成元 ，优惠金额
			log.setAmount2(amount+"");//优惠卷金额
			double needAmount = (double)coupon.getNeedAmount()/100;// 把分转成元
			log.setNeedAmount(needAmount + "");//满多少才能消费
			if (coupon.getCatId() == 0) {
				log.setApplyStr("全场通用");
				if(coupon.getCouponType()==2){//免邮卷
					log.setApplyName("任意订单免除邮费");
				}else {
					log.setApplyName("任意商品进行抵扣");
				}
			} else {
				GoodsCategory goodsCategory = goodsCategoryMapper.selectByPrimaryKey(coupon.getCatId());
				log.setApplyStr(goodsCategory.getName());
				log.setApplyName("仅限"+goodsCategory.getName()+"商品使用");
			}
			if(log.getStatus()!=2){//未领取的优惠卷 没有有效期
				long difference = (log.getExpireTime().getTime() - new Date().getTime()) / 86400000;//计算过期时间天数
				if (Math.abs(difference) > 10000) {//永久劵的默认生成的过期时候是领取后的36000天
					log.setEffectiveTime("永久有效");
				} else {
					StringBuffer sb = new StringBuffer();//过期时间的拼凑
					Calendar  calendar= Calendar.getInstance();
					calendar.setTime(log.getExpireTime());
					int year=calendar.get(Calendar.YEAR);//年
					int month=calendar.get(Calendar.MONTH) + 1;//月
					int day=calendar.get(Calendar.DAY_OF_MONTH);//日
					int hour=calendar.get(Calendar.HOUR_OF_DAY);//时
					int minute=calendar.get(Calendar.MINUTE);//分
					sb.append("有效期:");
					sb.append(year+"年");
					if(month>9){
						sb.append(month+"月");
					}else{
						sb.append("0"+month+"月");
					}
					if(day>9){
						sb.append(day+"日 ");
					}else{
						sb.append("0"+day+"日 ");
					}
					if(hour>9){
						sb.append(hour+":");
					}else{
						sb.append("0"+hour+":");
					}
					if(minute>9){
						sb.append(minute);
					}else{
						sb.append("0"+minute);
					}
					log.setEffectiveTime(sb.toString());
				}
			}
		}
		PageBean<CouponLog> pageBean = new PageBean<>(list);
		map.put("CouponLogs", pageBean);
		map.put("num", list.size());
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
	 * 我的移动端，当前可以使用的优惠劵列表
	 */
	@Override
	public Map<String, Object> goodListCouponLog(CouponLog couponLog) {
		// TODO Auto-generated method stub
		PageHelper.startPage(Integer.parseInt(couponLog.getCurrentPage()), Contants.PAGE_SIZE, true);
		Map<String,Object> map=new HashMap<String,Object>();
		couponLog.setExpireTime(new Date());//当前时间
		List<CouponLog> list = couponLogMapper.goodListPage(couponLog);//商家的优惠劵
		for(int i=0;i<list.size();i++){
			if(list.get(i).getStock()!=null&&list.get(i).getSended()!=null){
			double stock = list.get(i).getStock();
			double sended = list.get(i).getSended();
			double num = stock/sended;
			list.get(i).setPercentage(num*100+"%");//剩余百分比
			
			double needAmount = list.get(i).getNeed_amount();//把分转成元 ，满多少才能消费
			double needAmount2 = needAmount / 100;
			list.get(i).setNeedAmount(needAmount2+"");
			
			double amount = list.get(i).getAmount();//把分转成元 ，优惠金额
			list.get(i).setAmount2(amount/100+"");
			}
		}
		
//		CouponLog couponLog2 = new CouponLog();
//		couponLog2.setExpireTime(new Date());//当前时间
//		couponLog2.setmId(couponLog.getmId());
//		couponLog2.setCurrentPage(couponLog.getCurrentPage());
//		list = couponLogMapper.goodAllListPage(couponLog2);//平台的优惠劵
//		for(int i=0;i<list.size();i++){
//			if(list.get(i).getStock()!=null&&list.get(i).getSended()!=null){
//			double stock = list.get(i).getStock();
//			double sended = list.get(i).getSended();
//			double num = stock/sended;
//			list.get(i).setPercentage(num*100+"%");//剩余百分比
//			
//			double needAmount = list.get(i).getNeed_amount();//把分转成元 ，满多少才能消费
//			double needAmount2 = needAmount / 100;
//			list.get(i).setNeedAmount(needAmount2+"");
//			
//			double amount = list.get(i).getAmount();//把分转成元 ，优惠金额
//			list.get(i).setAmount2(amount/100+"");
//			}
//		}

		PageBean<CouponLog> pageBean = new PageBean<>(list);
		map.put("CouponLogs", pageBean);
		return map;
	}
	
	/**
	 * 所有的优惠劵
	 */
	@Override
	public Map<String, Object> allListCouponLog(CouponLog couponLog) {
		// TODO Auto-generated method stub
		PageHelper.startPage(Integer.parseInt(couponLog.getCurrentPage()), Contants.PAGE_SIZE, true);
		Map<String,Object> map=new HashMap<String,Object>();
	
		List<CouponLog> list = couponLogMapper.allListPage(couponLog);
		for(int i=0;i<list.size();i++){
			if(list.get(i).getStock()!=null&&list.get(i).getSended()!=null){
			double stock = list.get(i).getStock();
			double sended = list.get(i).getSended();
			double num = stock/sended;
			list.get(i).setPercentage(num*100+"%");//剩余百分比
			
			double needAmount = list.get(i).getNeed_amount();//把分转成元 ，满多少才能消费
			double needAmount2 = needAmount / 100;
			list.get(i).setNeedAmount(needAmount2+"");
			
			double amount = list.get(i).getAmount();//把分转成元 ，优惠金额
			list.get(i).setAmount2(amount/100+"");
			}
		}
		PageBean<CouponLog> pageBean = new PageBean<>(list);
		map.put("CouponLogs", pageBean);
		return map;
	}
	
	/**
	 * 验证用户是否领取过优惠劵
	 */
	@Override
	public List<CouponLog> getByMidAndCouponId(CouponLog couponLog) {
		// TODO Auto-generated method stub
		return couponLogMapper.getByMidAndCouponId(couponLog);
	}

	/**
	 * @Description: 每种优惠卷类型统计
	 * @author xieyc
	 * @date 2018年6月8日 下午8:58:01 
	 */
	public Map<String, Object> couponNum(Integer mId) {
		Map<String, Object> map = new HashMap<String, Object>();
		CouponLog couponLog1 = new CouponLog();
		couponLog1.setmId(mId);
		couponLog1.setStatus(0);// 未使用
		couponLog1.setExpireTime(new Date());// 当前时间
		List<CouponLog> list1 = couponLogMapper.notUseListPage(couponLog1);
		map.put("0", list1.size());

		CouponLog couponLog2 = new CouponLog();
		couponLog2.setmId(mId);
		couponLog2.setStatus(1);// 已使用
		List<CouponLog> list2 = couponLogMapper.listPage(couponLog2);
		map.put("1", list2.size());

		CouponLog couponLog3 = new CouponLog();
		couponLog3.setmId(mId);
		couponLog3.setStatus(2);// 未领取
		List<CouponLog> list3 = couponLogMapper.listPage(couponLog3);
		map.put("2", list3.size());

		CouponLog couponLog4 = new CouponLog();
		couponLog4.setStatus(0);//只有未使用的才有过期时间
		couponLog4.setmId(mId);
		couponLog4.setExpireTime(new Date());// 当前时间与数据库时间比较
		List<CouponLog> list4 = couponLogMapper.listPage(couponLog4);
		map.put("3", list4.size());

		return map;
	}


}
