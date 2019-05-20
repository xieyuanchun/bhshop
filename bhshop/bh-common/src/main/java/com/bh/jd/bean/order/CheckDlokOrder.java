package com.bh.jd.bean.order;

import java.util.List;

public class CheckDlokOrder {
    
	//妥投订单信息
	
	
    private List<Orders> orders;
	
	private String total; //订单总数
	
	private String totalPage; //总页码数
	
	private String curPage; //当前页码

	
	
	
	
    
	public List<Orders> getOrders() {
		return orders;
	}

	public void setOrders(List<Orders> orders) {
		this.orders = orders;
	}

	public String getTotal() {
		return total;
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(String totalPage) {
		this.totalPage = totalPage;
	}

	public String getCurPage() {
		return curPage;
	}

	public void setCurPage(String curPage) {
		this.curPage = curPage;
	}
	
	
	
}
