package com.bh.bean;

import java.io.Serializable;

public class IDCardEntity implements Serializable{
private static final long serialVersionUID = 1L;
private String address;
private String birth;
private String name;
private String num;
private String sex;
private String nationality;
private boolean success;
public boolean isSuccess() {
	return success;
}
public void setSuccess(boolean success) {
	this.success = success;
}
public String getAddress() {
	return address;
}
public void setAddress(String address) {
	this.address = address;
}
public String getBirth() {
	return birth;
}
public void setBirth(String birth) {
	this.birth = birth;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getNum() {
	return num;
}
public void setNum(String num) {
	this.num = num;
}
public String getSex() {
	return sex;
}
public void setSex(String sex) {
	this.sex = sex;
}
public String getNationality() {
	return nationality;
}
public void setNationality(String nationality) {
	this.nationality = nationality;
}

}
