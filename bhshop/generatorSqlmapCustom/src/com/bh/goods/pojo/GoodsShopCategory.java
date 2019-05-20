package com.bh.goods.pojo;

public class GoodsShopCategory {
    private Integer id;

    private String name;

    private Integer reid;

    private Short sortnum;

    private String image;

    private Integer shopId;

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

    public Integer getReid() {
        return reid;
    }

    public void setReid(Integer reid) {
        this.reid = reid;
    }

    public Short getSortnum() {
        return sortnum;
    }

    public void setSortnum(Short sortnum) {
        this.sortnum = sortnum;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }
}