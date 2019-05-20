package com.bh.jd.bean.goods;

import java.io.Serializable;

public class Brand implements Serializable{
	private static final long serialVersionUID = 1L;
	private String id; //品牌id
	private String name;//品牌名称
	private String pinyin;//品牌首字母拼音
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPinyin() {
		return pinyin;
	}
	public void setPinyin(String pinyin) {
		this.pinyin = pinyin;
	}
	
	

}
