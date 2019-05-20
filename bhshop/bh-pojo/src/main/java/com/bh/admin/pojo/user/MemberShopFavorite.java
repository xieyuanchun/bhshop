package com.bh.admin.pojo.user;


import java.sql.Timestamp;
import java.util.List;

import com.bh.goods.pojo.Goods;
import com.bh.utils.PageBean;

public class MemberShopFavorite {
    private Integer id;

    private Integer mId;

    private Integer shopId;


    
    private String shopName;//商家名称
    private String logo;//店铺图片
    private double Attention;//多少人关注
    private double mark;//综合评分
    private String description;
    private Timestamp addtime;
  //  private PageBean<Goods> hotGoods;
   // private PageBean<Goods> newGoods;
private List<Goods> hotGoods;
private List<Goods> newGoods;
	

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

	public Timestamp getAddtime() {
		return addtime;
	}


	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public double getAttention() {
		return Attention;
	}

	public void setAttention(double attention) {
		Attention = attention;
	}

	public double getMark() {
		return mark;
	}

	public void setMark(double mark) {
		this.mark = mark;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}
	
    
	public void setAddtime(Timestamp addtime) {
		this.addtime = addtime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Goods> getHotGoods() {
		return hotGoods;
	}

	public void setHotGoods(List<Goods> hotGoods) {
		this.hotGoods = hotGoods;
	}

	public List<Goods> getNewGoods() {
		return newGoods;
	}

	public void setNewGoods(List<Goods> newGoods) {
		this.newGoods = newGoods;
	}

	

	



	

	
	

	
	
}