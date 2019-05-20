package com.bh.jd.bean.goods;

import java.io.Serializable;
import java.util.List;

public class Price implements Serializable{
	private static final long serialVersionUID = 1L;
	private Integer max;//高价
	private Integer min;//低价
	public Integer getMax() {
		return max;
	}
	public void setMax(Integer max) {
		this.max = max;
	}
	public Integer getMin() {
		return min;
	}
	public void setMin(Integer min) {
		this.min = min;
	}

}
