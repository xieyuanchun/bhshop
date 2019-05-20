package com.order.user.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bh.order.mapper.RechargePhoneMapper;
import com.bh.order.pojo.RechargePhone;
import com.bh.utils.MoneyUtil;
import com.bh.utils.PageBean;
import com.bh.utils.recharge.HttpUtil;
import com.github.pagehelper.PageHelper;
import com.order.user.service.RechargePhoneService;

import net.sf.json.JSONObject;

@Service
@Transactional
public class RechargePhoneImpl implements RechargePhoneService{

	@Autowired
	private RechargePhoneMapper mapper;
	@Value(value = "${pageSize}")
	private String pageSize;
    public static final String APPKEY = "11e462bfb968f2c4";// 你的appkey
    public static final String URL = "http://api.jisuapi.com/mobilerecharge/orderdetail";
    public static final String orderno = "201601241155888";// 订单号
    public static final String outorderno = "77888855888";// 商家订单
    
	@Override
	public int add(RechargePhone record) {
		// TODO Auto-generated method stub
		return mapper.insertSelective(record);
	}

	@Override
	public int update(RechargePhone record) {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKeySelective(record);
	}

	@Override
	public int delete(RechargePhone record) {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(record.getId());
	}

	@Override
	public PageBean<RechargePhone> listPage(RechargePhone r) {
		// TODO Auto-generated method stub
		PageHelper.startPage(Integer.parseInt(r.getCurrentPage()), Integer.parseInt(pageSize), true);
		List<RechargePhone> list = mapper.listPage(r);
		PageBean<RechargePhone> pageBean = new PageBean<>(list);
		return pageBean;
	}

	@Override
	public List<RechargePhone> getByOrderNo(RechargePhone r) {
		// TODO Auto-generated method stub
		List<RechargePhone> list = mapper.getByOrderNo(r);
		for(RechargePhone rp:list) {
			
			MoneyUtil m = new MoneyUtil();
			String am = m.fen2Yuan(String.valueOf(rp.getAmount()));
			if(am.equals("29.97")) {
				am="30";
			}else if(am.equals("49.95")) {
				am="50";
			}else if(am.equals("99.9")) {
				am="100";
			}else if(am.equals("199.8")) {
				am="200";
			}else if(am.equals("299.7")) {
				am="300";
			}else if(am.equals("499.5")) {
				am="500";
			}
			rp.setAmount2(am);
		}
		return list;
	}

	@Override
	public void getOrderDetails() throws Exception {
		// TODO Auto-generated method stub
		RechargePhone r = new RechargePhone();
		List<RechargePhone> list = mapper.getOrderDetails(r);
		for(RechargePhone rp :list) {
			    String result = null;
		        String url = URL + "?appkey=" + APPKEY + "&outorderno=" + rp.getOrderNo();
		 
		            result = HttpUtil.sendGet(url, "utf-8");
		            JSONObject json = JSONObject.fromObject(result);
		            if (json.getInt("status") != 0) {
		                System.out.println(json.getString("msg"));
		            } else {
		                JSONObject resultarr = (JSONObject) json.opt("result");
		                String orderno = resultarr.getString("orderno");
		                if(StringUtils.isNotBlank(orderno)) {
		                	String rechargestatus = resultarr.getString("rechargestatus");
		                	RechargePhone rp2 = new RechargePhone();
		                	rp2.setId(rp.getId());
		                	rp2.setPaystatus(Integer.valueOf(rechargestatus));
		                	mapper.updateByPrimaryKeySelective(rp2);
		                	
		                }

		                System.out.println("====>"+resultarr);
		            }
		     
		}
	}
	
}
