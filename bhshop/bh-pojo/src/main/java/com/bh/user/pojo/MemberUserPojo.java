package com.bh.user.pojo;


import java.util.Date;
import java.util.Map;

public class MemberUserPojo {
	private Map<String, Object> member;
	private String sexName;//性别
    private Integer point;//用户滨惠豆的数量
    private Date registerDate;//注册时间
	
	public Map<String, Object> getMember() {
		return member;
	}
	public void setMember(Map<String, Object> member) {
		this.member = member;
	}
	public String getSexName() {
		switch (sexName) {
		case "0":return "保密";
		case "1":return "男";
		case "2":return "女";
		default: return "";
		}
	}
	public void setSexName(String sexName) {
		this.sexName = sexName;
	}
	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}
	public Date getRegisterDate() {
		return registerDate;
	}
	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}
	
	
	
}
