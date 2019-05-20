package com.bh.utils;

import java.io.Serializable;
import java.util.List;
/**
 * 分页参数
 * @author xxj
 *
 * @param <T>
 */
public class PageParams<T> implements Serializable{
	private static final long serialVersionUID = 1L;
	//当前页
	private int curPage = 1;
	//一页记录数
	private int pageSize = 10;
	//结果集
	private List<T> result;
	public int getCurPage() {
		return curPage;
	}
	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public List<T> getResult() {
		return result;
	}
	public void setResult(List<T> result) {
		this.result = result;
	}


}
