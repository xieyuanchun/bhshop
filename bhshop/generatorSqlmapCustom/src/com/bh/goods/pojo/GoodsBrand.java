package com.bh.goods.pojo;

public class GoodsBrand {
    private Integer id;

    private String name;

    private String logo;

    private Short sortnum;

    private String catId;

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo == null ? null : logo.trim();
    }

    public Short getSortnum() {
        return sortnum;
    }

    public void setSortnum(Short sortnum) {
        this.sortnum = sortnum;
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId == null ? null : catId.trim();
    }
}