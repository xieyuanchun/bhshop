package com.bh.admin.pojo.order;

import java.io.Serializable;
import java.util.Date;

import com.bh.user.pojo.MemberUserAddress;

public class OrderSkuReturn implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 5652849729456419120L;
	private OrderSku orderSku;
    private MemberUserAddress memberUserAddress;
	public OrderSku getOrderSku() {
		return orderSku;
	}
	public void setOrderSku(OrderSku orderSku) {
		this.orderSku = orderSku;
	}
	public MemberUserAddress getMemberUserAddress() {
		return memberUserAddress;
	}
	public void setMemberUserAddress(MemberUserAddress memberUserAddress) {
		this.memberUserAddress = memberUserAddress;
	}
	
    
    
}
