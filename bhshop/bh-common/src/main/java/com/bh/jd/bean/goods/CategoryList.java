package com.bh.jd.bean.goods;

import java.util.List;

public class CategoryList {
	List<CategoryVo> categorys;
	
	private Integer totalRows;
	
	private Integer pageNo ;
	
	private Integer pageSize;

	public List<CategoryVo> getCategorys() {
		return categorys;
	}

	public void setCategorys(List<CategoryVo> categorys) {
		this.categorys = categorys;
	}

	public Integer getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(Integer totalRows) {
		this.totalRows = totalRows;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
	
}
