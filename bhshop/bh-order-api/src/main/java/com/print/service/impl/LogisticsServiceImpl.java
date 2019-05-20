package com.print.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bh.config.Contants;
import com.bh.jd.api.JDStockApi;
import com.bh.jd.bean.JdResult;
import com.bh.jd.bean.order.OrderStock;
import com.bh.order.mapper.OrderShopMapper;
import com.bh.order.pojo.OrderShop;
import com.print.service.LogisticsService;
@Service
@Transactional
public class LogisticsServiceImpl implements LogisticsService {

	@Autowired
	private OrderShopMapper orderShopMapper;

	@Override
	public void getLogistics() {
		// TODO Auto-generated method stub
		try{
		   List<OrderShop> list= orderShopMapper.getByStatus();

		   for(int i=0;i<list.size();i++){
		     if(!list.get(i).getJdorderid().equals("0")&&!list.get(i).getJdorderid().equals("-1")){
		       String ret = JDStockApi.SelectJdOrder(list.get(i).getJdorderid());
               JSONObject obj = JSONObject.fromObject(ret);
               if(obj.get("success").toString().equals("true")){
		        JSONObject obj2 = JSONObject.fromObject(obj.get("result"));
		        String type = String.valueOf(obj2.get("type"));
		        if(type.equals("1")){ //父订单
		        	JSONArray ja = JSONArray.fromObject(obj2.get("cOrder"));
		        	for(int j=0;j<ja.size();j++){
		        		JSONObject obj3 = JSONObject.fromObject(ja.get(j));
		        		String state = String.valueOf(obj3.get("state"));
		        		if(state.equals("1")){
					         OrderShop orderShop = new OrderShop();
					         orderShop.setReceivedtime(new Date());
					         orderShop.setId(list.get(i).getId());
					         orderShop.setdState(Contants.CATE_FOUR);
					         orderShopMapper.updateByPrimaryKeySelective(orderShop);
				        }
		        	}
		        }else if(type.equals("2")){ //子订单		        	
		            String state = String.valueOf(obj2.get("state"));
		            if(state.equals("1")){ //1是妥投
			            OrderShop orderShop = new OrderShop();
			            orderShop.setReceivedtime(new Date());
			            orderShop.setId(list.get(i).getId());
			            orderShop.setdState(Contants.CATE_FOUR);
			            orderShopMapper.updateByPrimaryKeySelective(orderShop);
		            }else if(state.equals("0")){ //0 是新建
		            	OrderShop orderShop = new OrderShop();
			            orderShop.setId(list.get(i).getId());
			            orderShop.setdState(Contants.CATE_THREE);
			            orderShopMapper.updateByPrimaryKeySelective(orderShop);
		            }else if(state.equals("2")){ //2是拒收
		            	OrderShop orderShop = new OrderShop();
			            orderShop.setId(list.get(i).getId());
			            orderShop.setdState(5);
			            orderShopMapper.updateByPrimaryKeySelective(orderShop);
		            }
		        }
               }
		     }
		   }
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}



}
