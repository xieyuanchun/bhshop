package com.bh.goods.pojo;

public class OneWeek {
	private String day;
	private Integer score;
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
	
	public Integer getIsAttence() {
		return isAttence;
	}
	public void setIsAttence(Integer isAttence) {
		this.isAttence = isAttence;
	}
	
	
	
}
