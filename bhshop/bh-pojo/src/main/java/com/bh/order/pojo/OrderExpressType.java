package com.bh.order.pojo;

public class OrderExpressType {
    private Integer id;	

    private Integer type;//2017-10-18添加:配送类型 1 自提 2平台配送 3商家配送',

    private String note;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }
}