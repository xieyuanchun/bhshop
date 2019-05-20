package com.bh.jd.bean.goods;

import java.io.Serializable;
import java.util.Date;

public class MessageAddOrDelete implements Serializable{
	private static final long serialVersionUID = 1L;
	//推送id
	private Long id;
	//商品编号及状态
	private AddOrDeleteResult result;
	//类型
	private Integer type;
	//推送时间
	private Date time;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public AddOrDeleteResult getResult() {
		return result;
	}
	public void setResult(AddOrDeleteResult result) {
		this.result = result;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
}
