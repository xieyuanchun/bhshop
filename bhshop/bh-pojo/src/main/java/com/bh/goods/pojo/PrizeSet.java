package com.bh.goods.pojo;

import java.util.Date;

public class PrizeSet {
	
	//主键id
    private Integer id;
    //名称
    private String name;
    //中奖活动类型
    private Integer type;
    //开始时间
    private Date startTime;
    //结束时间
    private Date endTime;
    //中奖率
    private Integer prizeRate;
    //中奖人数 -1无限制
    private Integer prizeNum;
    //中奖金额
    private Integer prizeAmount;
    //中奖金额最小概率
    private Integer minAmountRate;
    //中奖金额最大概率
    private Integer maxAmountRate;
    //备注
    private String remark;
    //是否默认规则
    private Integer isDefault;
    //中奖剩余人数 -1无限制
    private Integer surplusNum;
    //起始页
    private String currentPaqe;
    //商品id
    private Integer goodsId;

    //商品名字
    private String goodName;
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getPrizeRate() {
        return prizeRate;
    }

    public void setPrizeRate(Integer prizeRate) {
        this.prizeRate = prizeRate;
    }

    public Integer getPrizeNum() {
        return prizeNum;
    }

    public void setPrizeNum(Integer prizeNum) {
        this.prizeNum = prizeNum;
    }

    public Integer getPrizeAmount() {
        return prizeAmount;
    }

    public void setPrizeAmount(Integer prizeAmount) {
        this.prizeAmount = prizeAmount;
    }

    public Integer getMinAmountRate() {
        return minAmountRate;
    }

    public void setMinAmountRate(Integer minAmountRate) {
        this.minAmountRate = minAmountRate;
    }

    public Integer getMaxAmountRate() {
        return maxAmountRate;
    }

    public void setMaxAmountRate(Integer maxAmountRate) {
        this.maxAmountRate = maxAmountRate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public Integer getSurplusNum() {
		return surplusNum;
	}

	public void setSurplusNum(Integer surplusNum) {
		this.surplusNum = surplusNum;
	}

	public String getCurrentPaqe() {
		return currentPaqe;
	}

	public void setCurrentPaqe(String currentPaqe) {
		this.currentPaqe = currentPaqe;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
    
	
}