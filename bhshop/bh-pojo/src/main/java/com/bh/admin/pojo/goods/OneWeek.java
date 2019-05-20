package com.bh.admin.pojo.goods;

public class OneWeek {
	private String day;
	private Integer score;
	private Integer isToday;//0不是，1是今天
	private Integer isAttence;//是否签到0未签到，1已签到
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}
	public Integer getIsToday() {
		return isToday;
	}
	public void setIsToday(Integer isToday) {
		this.isToday = isToday;
	}
	public Integer getIsAttence() {
		return isAttence;
	}
	public void setIsAttence(Integer isAttence) {
		this.isAttence = isAttence;
	}
	
	
	
}
