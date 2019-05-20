package com.bh.user.api.service.impl;

import java.math.BigDecimal;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.bh.config.Contants;
import com.bh.order.mapper.OrderSkuMapper;
import com.bh.order.pojo.OrderSku;
import com.bh.user.api.service.WalletLogService;
import com.bh.user.mapper.WalletLogMapper;
import com.bh.user.pojo.Wallet;
import com.bh.user.pojo.WalletLog;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
@Service
public class WalletLogServiceImpl implements WalletLogService{

	@Autowired
	private OrderSkuMapper orderSkuMapper;
	@Autowired
	private WalletLogMapper walletLogMapper;
	@Value(value = "${pageSize}")
	private String pageSize;
	//森惠-钱包-充值功能  zlk WalletLog
	@Override
	public int addWalletLog(WalletLog record) {
		// TODO Auto-generated method stub
		return walletLogMapper.insertSelective(record);
	}
	
	//更新WalletLog 状态zlk WalletLog
	@Override
	public int updateByOrderNo(WalletLog record) {
		// TODO Auto-generated method stub
		return walletLogMapper.updateByOrderNo(record);
	}
	
	//根据用户id获取信息，登陆者为null获取所有数据
	@Override
	public List<WalletLog> getByMid(WalletLog walletLog) {
		// TODO Auto-generated method stub
		return walletLogMapper.getByMid(walletLog);
	}
    
	//列表管理
	@Override
	public PageBean<WalletLog> listPage(WalletLog entity) {
		// TODO Auto-generated method stub
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Integer.parseInt(pageSize), true);
		List<WalletLog> list =  walletLogMapper.listPage(entity);
		PageBean<WalletLog> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	//个人钱包记录。操作类型 ：拍卖支付保证金  ，拍卖退保证金，充值，提现，下单，订单退款
	@Override
	public PageBean<WalletLog> WalletRecord(WalletLog entity) {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPage()), Integer.parseInt(pageSize), true);
		List<WalletLog> list =  walletLogMapper.WalletRecord(entity);
		if(list.size()>0){
		//测试环境	
		final String BondImage="https://bhshoptest3.oss-cn-shenzhen.aliyuncs.com/goods/c550420b-d89f-425e-b1cc-9947d1c44c79.png";
		final String rechargeImage="https://bhshoptest3.oss-cn-shenzhen.aliyuncs.com/goods/bb1699fe-470b-4678-9e57-55feade5a606.png";
		//正式环境	
		final String BondImages="https://bhs-oss.bh2015.com/goods/ff401ff4-e6b1-47cf-b565-5a7cfc7a86ee.png";
		final String rechargeImages="https://bhs-oss.bh2015.com/goods/353cdb78-546f-48e2-a465-8b6aaf8d26bd.png";
		        
		     for(WalletLog log : list){
				//double realMoney = (double)log.getAmount()/10/10;
				//log.setRealMoney(realMoney);	
		    	BigDecimal b = new BigDecimal(log.getAmount());
		    	log.setRealMoney(Double.valueOf(b.divide(new BigDecimal(100)).toString()));	
				if(log.getInOut()==0){//进账记录
					if(StringUtils.isNotBlank(log.getRemark())) {
						if(log.getRemark().equals("拍卖退保证金")) {
							log.setDesc("拍卖退保证金");
							log.setImage(Contants.bondImage);
						}else if(log.getRemark().equals("订单退款")||log.getRemark().equals("拼团失败退款")){
							log.setDesc(log.getRemark());
							//获取商品图片
							if(log.getOrderId()!=0) {
								List<OrderSku> OrderSkuList= orderSkuMapper.getSkuListByOrderId(log.getOrderId());
								if(OrderSkuList.size()>0) {
									log.setImage(OrderSkuList.get(0).getSkuImage());
								}else {
									log.setImage("");
								}
							}
						}else if(log.getRemark().equals("充值")){
							log.setDesc("充值");
							log.setImage(Contants.rechargeImage);
						}
					}else {
						log.setDesc("充值");
						log.setImage(Contants.rechargeImage);
					}
					
				}else{//出账记录
					if(StringUtils.isNotBlank(log.getRemark())) {
					     if(log.getRemark().equals("拍卖支付保证金")) {
						    log.setDesc("拍卖支付保证金");
						    log.setImage(Contants.bondImage);
					     }else if(log.getRemark().equals("下单")){
						    log.setDesc("下单");
						    //获取商品图片
							if(log.getOrderId()!=0) {
								List<OrderSku> OrderSkuList= orderSkuMapper.getSkuListByOrderId(log.getOrderId());
								if(OrderSkuList.size()>0) {
									log.setImage(OrderSkuList.get(0).getSkuImage());
								}else {
									log.setImage("");
								}
							}
					     }else if(log.getRemark().equals("提现")){
					    	log.setDesc("提现");
					    	log.setImage(Contants.rechargeImage);
					     }
					}else {
						log.setDesc("提现");
						log.setImage(Contants.rechargeImage);
					}
				}
			}
		}
		PageBean<WalletLog> pageBean = new PageBean<>(list);
		return pageBean;
	}

}
