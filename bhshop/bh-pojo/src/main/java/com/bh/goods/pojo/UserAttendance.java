package com.bh.goods.pojo;

import java.util.List;

public class UserAttendance {
	private Integer isAttendance;//0未签到，1已签到
	private Integer times;//签到的天数
	private Integer point;//总共有多少个滨惠豆
	private List<OneWeek> oneWeek;
	
	public Integer getIsAttendance() {
		return isAttendance;
	}
	public void setIsAttendance(Integer isAttendance) {
		this.isAttendance = isAttendance;
	}
	public Integer getTimes() {
		return times;
	}
	public void setTimes(Integer times) {
		this.times = times;
	}
	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}
	public List<OneWeek> getOneWeek() {
		return oneWeek;
	}
	public void setOneWeek(List<OneWeek> oneWeek) {
		this.oneWeek = oneWeek;
	}
	
	
	
	
	
}
