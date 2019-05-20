package com.bh.goods.pojo;

import java.util.Date;

public class JdGoodsNotice {
    private Integer id;

    private Long jdSku;

    private String name;

    private Integer goodsId;

    private String msg;

    private Integer msgType;

    private Date addTime;

    private Integer isRead;
    
    private Integer oldJdPrice;

    private Integer oldJdProtocolPrice;

    private Integer oldSellPrice;

    private Integer oldTeamPrice;

    private Integer newJdPrice;

    private Integer newJdProtocolPrice;

    private Integer newSellPrice;

    private Integer newTeamPrice;
    
    
    private Integer saleType; //商品销售类型
    
    private Integer goodsStatus; //商品状态
    
    private Double realOldJdPrice;

    private Double realOldJdProtocolPrice;

    private Double realOldSellPrice;

    private Double realOldTeamPrice;

    private Double realNewJdPrice;

    private Double realNewJdProtocolPrice;

    private Double realNewSellPrice;

    private Double realNewTeamPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

	public Long getJdSku() {
		return jdSku;
	}

	public void setJdSku(Long jdSku) {
		this.jdSku = jdSku;
	}

	public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg == null ? null : msg.trim();
    }

    public Integer getMsgType() {
        return msgType;
    }

    public void setMsgType(Integer msgType) {
        this.msgType = msgType;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getIsRead() {
        return isRead;
    }

    public void setIsRead(Integer isRead) {
        this.isRead = isRead;
    }

	public Integer getOldJdPrice() {
		return oldJdPrice;
	}

	public void setOldJdPrice(Integer oldJdPrice) {
		this.oldJdPrice = oldJdPrice;
	}

	public Integer getOldJdProtocolPrice() {
		return oldJdProtocolPrice;
	}

	public void setOldJdProtocolPrice(Integer oldJdProtocolPrice) {
		this.oldJdProtocolPrice = oldJdProtocolPrice;
	}

	public Integer getOldSellPrice() {
		return oldSellPrice;
	}

	public void setOldSellPrice(Integer oldSellPrice) {
		this.oldSellPrice = oldSellPrice;
	}

	public Integer getOldTeamPrice() {
		return oldTeamPrice;
	}

	public void setOldTeamPrice(Integer oldTeamPrice) {
		this.oldTeamPrice = oldTeamPrice;
	}

	public Integer getNewJdPrice() {
		return newJdPrice;
	}

	public void setNewJdPrice(Integer newJdPrice) {
		this.newJdPrice = newJdPrice;
	}

	public Integer getNewJdProtocolPrice() {
		return newJdProtocolPrice;
	}

	public void setNewJdProtocolPrice(Integer newJdProtocolPrice) {
		this.newJdProtocolPrice = newJdProtocolPrice;
	}

	public Integer getNewSellPrice() {
		return newSellPrice;
	}

	public void setNewSellPrice(Integer newSellPrice) {
		this.newSellPrice = newSellPrice;
	}

	public Integer getNewTeamPrice() {
		return newTeamPrice;
	}

	public void setNewTeamPrice(Integer newTeamPrice) {
		this.newTeamPrice = newTeamPrice;
	}

	public Integer getSaleType() {
		return saleType;
	}

	public void setSaleType(Integer saleType) {
		this.saleType = saleType;
	}

	public Integer getGoodsStatus() {
		return goodsStatus;
	}

	public void setGoodsStatus(Integer goodsStatus) {
		this.goodsStatus = goodsStatus;
	}

	public Double getRealOldJdPrice() {
		return realOldJdPrice;
	}

	public void setRealOldJdPrice(Double realOldJdPrice) {
		this.realOldJdPrice = realOldJdPrice;
	}

	public Double getRealOldJdProtocolPrice() {
		return realOldJdProtocolPrice;
	}

	public void setRealOldJdProtocolPrice(Double realOldJdProtocolPrice) {
		this.realOldJdProtocolPrice = realOldJdProtocolPrice;
	}

	public Double getRealOldSellPrice() {
		return realOldSellPrice;
	}

	public void setRealOldSellPrice(Double realOldSellPrice) {
		this.realOldSellPrice = realOldSellPrice;
	}

	public Double getRealOldTeamPrice() {
		return realOldTeamPrice;
	}

	public void setRealOldTeamPrice(Double realOldTeamPrice) {
		this.realOldTeamPrice = realOldTeamPrice;
	}

	public Double getRealNewJdPrice() {
		return realNewJdPrice;
	}

	public void setRealNewJdPrice(Double realNewJdPrice) {
		this.realNewJdPrice = realNewJdPrice;
	}

	public Double getRealNewJdProtocolPrice() {
		return realNewJdProtocolPrice;
	}

	public void setRealNewJdProtocolPrice(Double realNewJdProtocolPrice) {
		this.realNewJdProtocolPrice = realNewJdProtocolPrice;
	}

	public Double getRealNewSellPrice() {
		return realNewSellPrice;
	}

	public void setRealNewSellPrice(Double realNewSellPrice) {
		this.realNewSellPrice = realNewSellPrice;
	}

	public Double getRealNewTeamPrice() {
		return realNewTeamPrice;
	}

	public void setRealNewTeamPrice(Double realNewTeamPrice) {
		this.realNewTeamPrice = realNewTeamPrice;
	}
}