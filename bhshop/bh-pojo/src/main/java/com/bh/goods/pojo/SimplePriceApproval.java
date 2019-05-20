package com.bh.goods.pojo;

import java.util.Date;
import java.util.List;

public class SimplePriceApproval {

    private Integer goodsId;//商品ID

    private Integer goodsSkuId;

    private String replyNo;//审请编号

    private Integer status;//状态 0未审核  1通过  2未通过

    private String newVal;//新值

    private String oldVal;//旧值

    private Date replyTime;//申请时间

    private Date approvalTime;//审批时间

    private Integer approvalId;//审批人ID

    private Integer replyId;//审请人ID

    private Integer type;//类型 0价格
    
    private String currentPage;//当前页
    
    private String size;//每页的大小
    
    private String goodsName ;//商品的名称
    
    private Object skuValue;//规格属性
    
    private String goodsImage;//商品的图片
    
    private String approvalName;//审批人的名称
    
    private String replyName;//审请人名称
    
    private Integer isDel;//是否删除 0不删除，1删除
    
    private String goodsSkuNo;//商品的SkuNo 2018-3-29
    
    private List<String> list;
    private List<GoodsPrice> goodsPrice;
    
    
    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsSkuId() {
        return goodsSkuId;
    }

    public void setGoodsSkuId(Integer goodsSkuId) {
        this.goodsSkuId = goodsSkuId;
    }

    public String getReplyNo() {
        return replyNo;
    }

    public void setReplyNo(String replyNo) {
        this.replyNo = replyNo == null ? null : replyNo.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getNewVal() {
        return newVal;
    }

    public void setNewVal(String newVal) {
        this.newVal = newVal == null ? null : newVal.trim();
    }

    public String getOldVal() {
        return oldVal;
    }

    public void setOldVal(String oldVal) {
        this.oldVal = oldVal == null ? null : oldVal.trim();
    }

    public Date getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    public Date getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(Date approvalTime) {
        this.approvalTime = approvalTime;
    }

    public Integer getApprovalId() {
        return approvalId;
    }

    public void setApprovalId(Integer approvalId) {
        this.approvalId = approvalId;
    }

    public Integer getReplyId() {
        return replyId;
    }

    public void setReplyId(Integer replyId) {
        this.replyId = replyId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	

	public String getGoodsImage() {
		return goodsImage;
	}

	public void setGoodsImage(String goodsImage) {
		this.goodsImage = goodsImage;
	}

	public Object getSkuValue() {
		return skuValue;
	}

	public void setSkuValue(Object skuValue) {
		this.skuValue = skuValue;
	}

	public String getApprovalName() {
		return approvalName;
	}

	public void setApprovalName(String approvalName) {
		this.approvalName = approvalName;
	}

	public String getReplyName() {
		return replyName;
	}

	public void setReplyName(String replyName) {
		this.replyName = replyName;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	public String getGoodsSkuNo() {
		return goodsSkuNo;
	}

	public void setGoodsSkuNo(String goodsSkuNo) {
		this.goodsSkuNo = goodsSkuNo;
	}

	public List<String> getList() {
		return list;
	}

	public void setList(List<String> list) {
		this.list = list;
	}

	public List<GoodsPrice> getGoodsPrice() {
		return goodsPrice;
	}

	public void setGoodsPrice(List<GoodsPrice> goodsPrice) {
		this.goodsPrice = goodsPrice;
	}

	

    
}