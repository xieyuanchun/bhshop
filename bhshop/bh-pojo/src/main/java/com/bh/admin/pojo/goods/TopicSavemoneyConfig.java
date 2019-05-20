package com.bh.admin.pojo.goods;

public class TopicSavemoneyConfig {
    private Integer id;

    private Integer tgId;

    private Integer num;

    private Integer actPrice;

    private Integer realActPrice;//单位元
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTgId() {
        return tgId;
    }

    public void setTgId(Integer tgId) {
        this.tgId = tgId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getActPrice() {
        return actPrice;
    }

    public void setActPrice(Integer actPrice) {
        this.actPrice = actPrice;
    }

	public Integer getRealActPrice() {
		return realActPrice;
	}

	public void setRealActPrice(Integer realActPrice) {
		this.realActPrice = realActPrice;
	}
    
    
}