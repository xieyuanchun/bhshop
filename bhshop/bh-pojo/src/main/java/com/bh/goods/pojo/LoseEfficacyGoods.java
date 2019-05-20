package com.bh.goods.pojo;



public class LoseEfficacyGoods {
	private Integer id;
    private Integer gId;
    private Integer num;
    private String goodName;//2017-9-15cheng添加
    private double realsellPrice;
    //2018-5-19添加属性
    private Object valueObj;
    private String url;
    private long catId;
    private String msg;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getgId() {
		return gId;
	}
	public void setgId(Integer gId) {
		this.gId = gId;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public String getGoodName() {
		return goodName;
	}
	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	public double getRealsellPrice() {
		return realsellPrice;
	}
	public void setRealsellPrice(double realsellPrice) {
		this.realsellPrice = realsellPrice;
	}
	public Object getValueObj() {
		return valueObj;
	}
	public void setValueObj(Object valueObj) {
		this.valueObj = valueObj;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public long getCatId() {
		return catId;
	}
	public void setCatId(long catId) {
		this.catId = catId;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
    
    
}
