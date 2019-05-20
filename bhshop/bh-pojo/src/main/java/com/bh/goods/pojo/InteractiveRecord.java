package com.bh.goods.pojo;

import java.util.Date;

public class InteractiveRecord {
    private Integer id;

    private Integer goodsmsgid;

    private String chattext;

    private Integer isShop;

    private Date createTime;

    private Integer isFlag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGoodsmsgid() {
        return goodsmsgid;
    }

    public void setGoodsmsgid(Integer goodsmsgid) {
        this.goodsmsgid = goodsmsgid;
    }

    public String getChattext() {
        return chattext;
    }

    public void setChattext(String chattext) {
        this.chattext = chattext == null ? null : chattext.trim();
    }

    public Integer getIsShop() {
        return isShop;
    }

    public void setIsShop(Integer isShop) {
        this.isShop = isShop;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getIsFlag() {
        return isFlag;
    }

    public void setIsFlag(Integer isFlag) {
        this.isFlag = isFlag;
    }
}