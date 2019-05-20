package com.order.vo;

/**  
* 类说明   
*  
* @author homeway
* @email homeway.1984@163.com
* @date 2018年1月20日  新建  
*/
public class UnionPaySubOrdersVO {
	
	

	public String merOrderId;//子订单号
	public String totalAmount;//子订单金额
	public String mid;//子商户号
	public String orderDecs;//子订单描述
	
	public String getMerOrderId() {
		return merOrderId;
	}
	public void setMerOrderId(String merOrderId) {
		this.merOrderId = merOrderId;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
	public String getOrderDecs() {
		return orderDecs;
	}
	public void setOrderDecs(String orderDecs) {
		this.orderDecs = orderDecs;
	}

	
}
  