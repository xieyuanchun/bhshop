package com.bh.admin.pojo.goods;

import java.util.Date;

public class JdGoods {
	//主键id
    private Integer id;
    //京东商品编码
    private Long jdSkuNo;
    //商品池编号
    private String poolNum;
    //商品名称
    private String goodsName;
    //商品图片
    private String goodsImage;
    //品牌图片
    private String brandName;
    //商品分类id
    private String catId;
    //京东价
    private Integer jdPrice;
    //进货价
    private Integer stockPrice;
    //是否上架 0下架，1上架
    private Integer isUp;
    //是否删除 0正常，1已删除
    private Integer isDelete;
    //是否入库 0否，1是
    private Integer isGet;
    //添加时间
    private Date addTime;
    //编辑时间
    private Date editTime;
    //起始页
    private String currentPage;
    //每页显示多少条
    private String pageSize;
    //查询条件-京东起始价
    private Double startJdPrice;
    //查询条件-京东结束价
    private Double endJdPrice;
    //查询条件-进货起始价
    private Double startStockPrice;
    //查询条件-进货结束价
    private Double endStockPrice;
    //京东价
    private Double realJdPrice;
    //进货价
    private Double realStockPrice;
    //查询条件-符号1 ">",2"=",3"<"
    private Integer symbol;
    //查询条件-京东价与协议价之差
    private Double differPrice;
    //按差价从大到小排序
    private Integer sortByDiffer;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Long getJdSkuNo() {
		return jdSkuNo;
	}

	public void setJdSkuNo(Long jdSkuNo) {
		this.jdSkuNo = jdSkuNo;
	}

	public String getPoolNum() {
        return poolNum;
    }

    public void setPoolNum(String poolNum) {
        this.poolNum = poolNum == null ? null : poolNum.trim();
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName == null ? null : goodsName.trim();
    }

    public String getGoodsImage() {
        return goodsImage;
    }

    public void setGoodsImage(String goodsImage) {
        this.goodsImage = goodsImage == null ? null : goodsImage.trim();
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName == null ? null : brandName.trim();
    }

    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId == null ? null : catId.trim();
    }

    public Integer getJdPrice() {
        return jdPrice;
    }

    public void setJdPrice(Integer jdPrice) {
        this.jdPrice = jdPrice;
    }

    public Integer getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(Integer stockPrice) {
        this.stockPrice = stockPrice;
    }

    public Integer getIsUp() {
        return isUp;
    }

    public void setIsUp(Integer isUp) {
        this.isUp = isUp;
    }

    public Integer getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    public Integer getIsGet() {
        return isGet;
    }

    public void setIsGet(Integer isGet) {
        this.isGet = isGet;
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

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getPageSize() {
		return pageSize;
	}

	public void setPageSize(String pageSize) {
		this.pageSize = pageSize;
	}

	public Double getStartJdPrice() {
		return startJdPrice;
	}

	public void setStartJdPrice(Double startJdPrice) {
		this.startJdPrice = startJdPrice;
	}

	public Double getEndJdPrice() {
		return endJdPrice;
	}

	public void setEndJdPrice(Double endJdPrice) {
		this.endJdPrice = endJdPrice;
	}

	public Double getStartStockPrice() {
		return startStockPrice;
	}

	public void setStartStockPrice(Double startStockPrice) {
		this.startStockPrice = startStockPrice;
	}

	public Double getEndStockPrice() {
		return endStockPrice;
	}

	public void setEndStockPrice(Double endStockPrice) {
		this.endStockPrice = endStockPrice;
	}

	public Double getRealJdPrice() {
		return realJdPrice;
	}

	public void setRealJdPrice(Double realJdPrice) {
		this.realJdPrice = realJdPrice;
	}

	public Double getRealStockPrice() {
		return realStockPrice;
	}

	public void setRealStockPrice(Double realStockPrice) {
		this.realStockPrice = realStockPrice;
	}

	public Integer getSymbol() {
		return symbol;
	}

	public void setSymbol(Integer symbol) {
		this.symbol = symbol;
	}

	public Double getDifferPrice() {
		return differPrice;
	}

	public void setDifferPrice(Double differPrice) {
		this.differPrice = differPrice;
	}

	public Integer getSortByDiffer() {
		return sortByDiffer;
	}

	public void setSortByDiffer(Integer sortByDiffer) {
		this.sortByDiffer = sortByDiffer;
	}
	
}