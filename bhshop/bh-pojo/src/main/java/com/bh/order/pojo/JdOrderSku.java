package com.bh.order.pojo;

import java.util.Date;

public class JdOrderSku {
    private Integer id;

    private Long skuId;

    private Integer num;

    private Integer price;

    private String name;

    private Long category;

    private Date addTime;

    private Date editTime;

    private Integer jdMainId;

    private Integer orderSkuId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getSkuId() {
        return skuId;
    }

    public void setSkuId(Long skuId) {
        this.skuId = skuId;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Long getCategory() {
        return category;
    }

    public void setCategory(Long category) {
        this.category = category;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getEditTime() {
        return editTime;
    }

    public void setEditTime(Date editTime) {
        this.editTime = editTime;
    }

    public Integer getJdMainId() {
        return jdMainId;
    }

    public void setJdMainId(Integer jdMainId) {
        this.jdMainId = jdMainId;
    }

    public Integer getOrderSkuId() {
        return orderSkuId;
    }

    public void setOrderSkuId(Integer orderSkuId) {
        this.orderSkuId = orderSkuId;
    }
}