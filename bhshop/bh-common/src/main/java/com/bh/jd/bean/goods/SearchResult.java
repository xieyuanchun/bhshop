package com.bh.jd.bean.goods;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer resultCount; //搜索结果总记录数量
	private Integer pageCount; //总页数
	private Integer pageSize; //每页大小
	private Integer pageIndex; //当前页
	private BrandAggregate brandAggregate;//品牌分类汇总信息
	private List<Price> priceIntervalAggregate;//价格汇总
	private CategoryAggregate categoryAggregate;//相关分类汇总信息
	private List<HitResult> hitResult;//搜索命中数据json字符串
	private String expandAttrAggregate; 
	public Integer getResultCount() {
		return resultCount;
	}
	public void setResultCount(Integer resultCount) {
		this.resultCount = resultCount;
	}
	public Integer getPageCount() {
		return pageCount;
	}
	public void setPageCount(Integer pageCount) {
		this.pageCount = pageCount;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public Integer getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}
	
	public BrandAggregate getBrandAggregate() {
		return brandAggregate;
	}
	public void setBrandAggregate(BrandAggregate brandAggregate) {
		this.brandAggregate = brandAggregate;
	}
	public List<Price> getPriceIntervalAggregate() {
		return priceIntervalAggregate;
	}
	public void setPriceIntervalAggregate(List<Price> priceIntervalAggregate) {
		this.priceIntervalAggregate = priceIntervalAggregate;
	}
		
	public CategoryAggregate getCategoryAggregate() {
		return categoryAggregate;
	}
	public void setCategoryAggregate(CategoryAggregate categoryAggregate) {
		this.categoryAggregate = categoryAggregate;
	}
	public List<HitResult> getHitResult() {
		return hitResult;
	}
	public void setHitResult(List<HitResult> hitResult) {
		this.hitResult = hitResult;
	}
	public String getExpandAttrAggregate() {
		return expandAttrAggregate;
	}
	public void setExpandAttrAggregate(String expandAttrAggregate) {
		this.expandAttrAggregate = expandAttrAggregate;
	}
	

}
