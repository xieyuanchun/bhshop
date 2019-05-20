package com.bh.goods.pojo;

import java.util.Date;

public class ActZoneGoods {
	 private Integer id;

	    private Integer zoneId;

	    private Integer goodsId;

	    private Integer skuId;

	    private Integer sortNum;

	    private Date addtime;

	    private Date edittime;

	    private String failuretime;
	    
	    public Integer getId() {
	        return id;
	    }

	    public void setId(Integer id) {
	        this.id = id;
	    }

	    public Integer getZoneId() {
	        return zoneId;
	    }

	    public void setZoneId(Integer zoneId) {
	        this.zoneId = zoneId;
	    }

	    public Integer getGoodsId() {
	        return goodsId;
	    }

	    public void setGoodsId(Integer goodsId) {
	        this.goodsId = goodsId;
	    }

	    public Integer getSkuId() {
	        return skuId;
	    }

	    public void setSkuId(Integer skuId) {
	        this.skuId = skuId;
	    }

	    public Integer getSortNum() {
	        return sortNum;
	    }

	    public void setSortNum(Integer sortNum) {
	        this.sortNum = sortNum;
	    }

	    public Date getAddtime() {
	        return addtime;
	    }

	    public void setAddtime(Date addtime) {
	        this.addtime = addtime;
	    }

	    public Date getEdittime() {
	        return edittime;
	    }

	    public void setEdittime(Date edittime) {
	        this.edittime = edittime;
	    }

		public String getFailuretime() {
			return failuretime;
		}

		public void setFailuretime(String failuretime) {
			this.failuretime = failuretime;
		}
	    
	    
	    
}