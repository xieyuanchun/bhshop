package com.bh.user.pojo;

import java.util.List;

import com.bh.order.pojo.Order;
import com.bh.utils.PageBean;

public class MemberUserDetail {
	private String username;

	private String fullName;

	private String phone;

	private String email;

	private Integer status;
	private Integer sex;

	private Integer orderNum;// 订单总数

	private double totalAbility;// 总消费金额

	private Integer thisMonthOrderNum;// 本月消费订单笔数

	private double thisMonthTotalAbility;// 本月消费金额(元)

	private List<Order> order;

	private List<MemberUserAddress> memberUserAddressList;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}

	public double getTotalAbility() {
		return totalAbility;
	}

	public void setTotalAbility(double totalAbility) {
		this.totalAbility = totalAbility;
	}

	public Integer getThisMonthOrderNum() {
		return thisMonthOrderNum;
	}

	public void setThisMonthOrderNum(Integer thisMonthOrderNum) {
		this.thisMonthOrderNum = thisMonthOrderNum;
	}

	public double getThisMonthTotalAbility() {
		return thisMonthTotalAbility;
	}

	public void setThisMonthTotalAbility(double thisMonthTotalAbility) {
		this.thisMonthTotalAbility = thisMonthTotalAbility;
	}

	public List<MemberUserAddress> getMemberUserAddressList() {
		return memberUserAddressList;
	}

	public void setMemberUserAddressList(List<MemberUserAddress> memberUserAddressList) {
		this.memberUserAddressList = memberUserAddressList;
	}

	public List<Order> getOrder() {
		return order;
	}

	public void setOrder(List<Order> order) {
		this.order = order;
	}

	public Integer getSex() {
		return sex;
	}

	public void setSex(Integer sex) {
		this.sex = sex;
	}
	
}
