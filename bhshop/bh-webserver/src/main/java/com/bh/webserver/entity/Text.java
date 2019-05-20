package com.bh.webserver.entity;



import java.io.Serializable;
import java.time.LocalDateTime;

import com.bh.webserver.util.DateUtil;
import com.bh.webserver.util.FormatUtil;

/**
 * Created by 一线生 on 2015/11/22.
 * 说明：消息实体类
 */
public class Text implements Serializable {

	private static final long serialVersionUID = 1L;
	private String userName;
	private String auctionPrice;
	private String headImg;
	private String message;
	private Integer mId;
    private Integer goodsId;
    private Integer  orderId;
	private String curTime;
    //消息类型 0拍卖消息  1待支付消息
    private Integer type;
    public String getCurTime() {
		return curTime;
	}
	public void setCurTime(String curTime) {
		this.curTime = curTime;
	}

    public Integer getOrderId() {
		return orderId;
	}
	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}
    public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Text() {
    	
    }
    public Text(String userName, String message) {
        this.userName = userName;
        this.message = message;
    }
    public Integer getmId() {
		return mId;
	}
	public void setmId(Integer mId) {
		this.mId = mId;
	}
    
	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}
	public String getHeadImg() {
		return headImg;
	}
	public void setHeadImg(String headImg) {
		this.headImg = headImg;
	}
    public String getAuctionPrice() {
		return auctionPrice;
	}
	public void setAuctionPrice(String auctionPrice) {
		this.auctionPrice = auctionPrice;
	}
	
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = FormatUtil.formatScript(userName);
    }

    public String getMessage() {
        return FormatUtil.formatScript(message);
    }

    public void setMessage(String message) {
        this.message = FormatUtil.formatScript(message);
    }

    public static void main(String[] args) {
        System.out.println(LocalDateTime.now().toString().replace("T", " "));
    }
}
