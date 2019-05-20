package com.bh.jd.bean.goods;

import java.util.List;

public class SimilarSku {
	private Integer dim;
	
	private String saleName;
	
	private List<SaleAttr> saleAttrList;

	public Integer getDim() {
		return dim;
	}

	public void setDim(Integer dim) {
		this.dim = dim;
	}

	public String getSaleName() {
		return saleName;
	}

	public void setSaleName(String saleName) {
		this.saleName = saleName;
	}

	public List<SaleAttr> getSaleAttrList() {
		return saleAttrList;
	}

	public void setSaleAttrList(List<SaleAttr> saleAttrList) {
		this.saleAttrList = saleAttrList;
	}

}
