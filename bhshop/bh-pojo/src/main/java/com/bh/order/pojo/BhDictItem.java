package com.bh.order.pojo;

public class BhDictItem {
    private Integer itemId;

    private String itemName;

    private String itemValue;

    private Integer itemSortNum;

    private Integer dicId;

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName == null ? null : itemName.trim();
    }

    public String getItemValue() {
        return itemValue;
    }

    public void setItemValue(String itemValue) {
        this.itemValue = itemValue == null ? null : itemValue.trim();
    }

    public Integer getItemSortNum() {
        return itemSortNum;
    }

    public void setItemSortNum(Integer itemSortNum) {
        this.itemSortNum = itemSortNum;
    }

    public Integer getDicId() {
        return dicId;
    }

    public void setDicId(Integer dicId) {
        this.dicId = dicId;
    }
}