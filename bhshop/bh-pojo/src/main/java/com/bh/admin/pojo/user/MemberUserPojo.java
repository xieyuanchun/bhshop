package com.bh.admin.pojo.user;

public class MemberUserPojo {
	private Member member;
	private String email;
	private Integer sex;
	private String sexName;
	private String year;
	private String month;
	private String day;
	private Integer career;
	private String careerName;//职业1.学生2.在职3.自由职业0.其他',
    private Integer single;
    private String singleName;//个人情况：1.单身2恋爱中。3已婚，0保密',
    private String categoryId;
    private Integer point;//用户滨惠豆的数量
	public Member getMember() {
		return member;
	}
	public void setMember(Member member) {
		this.member = member;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
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
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public Integer getCareer() {
		return career;
	}
	public void setCareer(Integer career) {
		this.career = career;
	}
	public String getCareerName() {
		switch (careerName) {
		
		case "1":return "学生";
		case "2":return "在职";
		case "3":return "自由职业";
		case "0":return "其他";
		default: return "";
		}
	
	}
	public void setCareerName(String careerName) {
		this.careerName = careerName;
	}
	public Integer getSingle() {
		return single;
	}
	public void setSingle(Integer single) {
		this.single = single;
	}
	public String getSingleName() {
        switch (careerName) {
		
		case "1":return "单身";
		case "2":return "恋爱中";
		case "3":return "已婚";
		case "0":return "保密";
		default: return "";
		}
	}
	public void setSingleName(String singleName) {
		this.singleName = singleName;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public Integer getPoint() {
		return point;
	}
	public void setPoint(Integer point) {
		this.point = point;
	}
	
	
	
}
