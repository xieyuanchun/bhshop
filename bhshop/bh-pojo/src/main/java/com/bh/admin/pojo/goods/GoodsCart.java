package com.bh.admin.pojo.goods;

import java.io.Serializable;
import java.util.Date;

public class GoodsCart implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Integer id;

    private Integer mId;

    private Integer shopId;

    private Integer gId;

    private Integer sellPrice;

    private Integer num;

    private String gImage;

    private Date addtime;

    private String shopName;
    
    private String goodName;//2017-9-15cheng添加

    private String descption;
    
    private Integer teamPrice;
    private double realsellPrice;
    
    private Integer isDel;//2017-9-22添加，是否删除，0不删除，1是删
    
    private Integer isStore;//2017-9-30添加，是否有货：0有货，1无货,默认是0
    
    private String storeName;
    
    private GoodsSku goodsSkus;
    
    private Integer gskuid;//2017-10-16添加，商品的skuid
    
   public Integer getTeamPrice() {
		return teamPrice;
	}

	public void setTeamPrice(Integer teamPrice) {
		this.teamPrice = teamPrice;
	}

	public Integer getIsDel() {
		return isDel;
	}

	public void setIsDel(Integer isDel) {
		this.isDel = isDel;
	}

	/* private Integer allProduct;//所有商品
    
    private Integer pcs;//商品的件sh
*/
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

    public Integer getShopId() {
        return shopId;
    }

    public void setShopId(Integer shopId) {
        this.shopId = shopId;
    }

    public Integer getgId() {
        return gId;
    }

    public void setgId(Integer gId) {
        this.gId = gId;
    }

   
    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public String getgImage() {
        return gImage;
    }

    public void setgImage(String gImage) {
        this.gImage = gImage == null ? null : gImage.trim();
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName == null ? null : shopName.trim();
    }

    public String getDescption() {
        return descption;
    }

    public void setDescption(String descption) {
        this.descption = descption == null ? null : descption.trim();
    }

	public String getGoodName() {
		return goodName;
	}

	public void setGoodName(String goodName) {
		this.goodName = goodName;
	}
	

	public Integer getIsStore() {
		return isStore;
	}

	public void setIsStore(Integer isStore) {
		this.isStore = isStore;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((addtime == null) ? 0 : addtime.hashCode());
		result = prime * result + ((descption == null) ? 0 : descption.hashCode());
		result = prime * result + ((gId == null) ? 0 : gId.hashCode());
		result = prime * result + ((gImage == null) ? 0 : gImage.hashCode());
		result = prime * result + ((goodName == null) ? 0 : goodName.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((isDel == null) ? 0 : isDel.hashCode());
		result = prime * result + ((mId == null) ? 0 : mId.hashCode());
		result = prime * result + ((num == null) ? 0 : num.hashCode());
	
		result = prime * result + ((shopId == null) ? 0 : shopId.hashCode());
		result = prime * result + ((shopName == null) ? 0 : shopName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GoodsCart other = (GoodsCart) obj;
		if (addtime == null) {
			if (other.addtime != null)
				return false;
		} else if (!addtime.equals(other.addtime))
			return false;
		if (descption == null) {
			if (other.descption != null)
				return false;
		} else if (!descption.equals(other.descption))
			return false;
		if (gId == null) {
			if (other.gId != null)
				return false;
		} else if (!gId.equals(other.gId))
			return false;
		if (gImage == null) {
			if (other.gImage != null)
				return false;
		} else if (!gImage.equals(other.gImage))
			return false;
		if (goodName == null) {
			if (other.goodName != null)
				return false;
		} else if (!goodName.equals(other.goodName))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (isDel == null) {
			if (other.isDel != null)
				return false;
		} else if (!isDel.equals(other.isDel))
			return false;
		if (mId == null) {
			if (other.mId != null)
				return false;
		} else if (!mId.equals(other.mId))
			return false;
		if (num == null) {
			if (other.num != null)
				return false;
		} else if (!num.equals(other.num))
			return false;
		
		if (shopId == null) {
			if (other.shopId != null)
				return false;
		} else if (!shopId.equals(other.shopId))
			return false;
		if (shopName == null) {
			if (other.shopName != null)
				return false;
		} else if (!shopName.equals(other.shopName))
			return false;
		return true;
	}

	

	public Integer getSellPrice() {
		return sellPrice;
	}

	public void setSellPrice(Integer sellPrice) {
		this.sellPrice = sellPrice;
	}

	@Override
	public String toString() {
		return "GoodsCart [id=" + id + ", mId=" + mId + ", shopId=" + shopId + ", gId=" + gId + ", sellPrice="
				+ sellPrice + ", num=" + num + ", gImage=" + gImage + ", addtime=" + addtime + ", shopName=" + shopName
				+ ", goodName=" + goodName + ", descption=" + descption + ", isDel=" + isDel + "]";
	}

	public GoodsSku getGoodsSkus() {
		return goodsSkus;
	}

	public void setGoodsSkus(GoodsSku goodsSkus) {
		this.goodsSkus = goodsSkus;
	}

	public Integer getGskuid() {
		return gskuid;
	}

	public void setGskuid(Integer gskuid) {
		this.gskuid = gskuid;
	}

	public double getRealsellPrice() {
		return realsellPrice;
	}

	public void setRealsellPrice(double realsellPrice) {
		this.realsellPrice = realsellPrice;
	}

	public String getStoreName() {
		return storeName;
	}

	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}




	
    
    
}