package com.bh.jd.bean.goods;

import java.io.Serializable;
import java.util.List;

public class CategoryAggregate implements Serializable{
	private static final long serialVersionUID = 1L;
	private List<Category> firstCategory;//一级类目分类
	private List<Category> secondCategory;//二级类目分类
	private List<Category> thridCategory;//三级类目分类
	public List<Category> getFirstCategory() {
		return firstCategory;
	}
	public void setFirstCategory(List<Category> firstCategory) {
		this.firstCategory = firstCategory;
	}
	public List<Category> getSecondCategory() {
		return secondCategory;
	}
	public void setSecondCategory(List<Category> secondCategory) {
		this.secondCategory = secondCategory;
	}
	public List<Category> getThridCategory() {
		return thridCategory;
	}
	public void setThridCategory(List<Category> thridCategory) {
		this.thridCategory = thridCategory;
	}


}
