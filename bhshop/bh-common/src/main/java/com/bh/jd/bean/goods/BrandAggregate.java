package com.bh.jd.bean.goods;

import java.io.Serializable;
import java.util.List;

public class BrandAggregate implements Serializable{
	private static final long serialVersionUID = 1L;
	private List<Brand> brandList;//品牌列表
	private String[] pinyinAggr;//品牌首字母拼音
	public List<Brand> getBrandList() {
		return brandList;
	}
	public void setBrandList(List<Brand> brandList) {
		this.brandList = brandList;
	}
	public String[] getPinyinAggr() {
		return pinyinAggr;
	}
	public void setPinyinAggr(String[] pinyinAggr) {
		this.pinyinAggr = pinyinAggr;
	}
	
	
	

}
