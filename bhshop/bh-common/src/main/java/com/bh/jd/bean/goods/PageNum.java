package com.bh.jd.bean.goods;

import java.io.Serializable;

public class PageNum implements Serializable{
	private static final long serialVersionUID = 1L;
	private String name;//商品池名
	private String page_num;//池编号
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPage_num() {
		return page_num;
	}
	public void setPage_num(String page_num) {
		this.page_num = page_num;
	}
	

}
