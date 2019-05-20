package com.bh.admin.pojo.goods;

import java.util.Date;

public class AdsShop {
    private Integer id;

    private Date createtime;//创建时间

    private String name;//广告名称

    private String image;//广告图片

    private Integer sortnum;//排序

    private Integer isPc;//0-pc端图片，1-移动端图片

    private Integer skuId;//goods_sku表的id

    private Integer goodsId;//商品id

    private Integer shopId;//店铺ID
    
    private String content;//广告内容

    private String link;//超链接
    
    private Byte status;
    
    
    
    private String goodsName; //商品名称
    
    private String shopName; //店铺名称
    
    private AdsShop details; //详情
    

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public Integer getSortnum() {
        return sortnum;
    }

    public void setSortnum(Integer sortnum) {
        this.sortnum = sortnum;
    }

    public Integer getIsPc() {
        return isPc;
    }

    public void setIsPc(Integer isPc) {
        this.isPc = isPc;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public AdsShop getDetails() {
		return details;
	}

	public void setDetails(AdsShop details) {
		this.details = details;
	}
    
    
}