package com.bh.admin.pojo.goods;

import java.util.Date;

public class MemberUserAccessLog {
    private Integer id;

    private Integer mId;

    private Integer goodsId;

    private Date addtime;

    private String note;
    
    private Goods goods;
    private String subAddtime;//截取成yyyy-MM-dd的时间
    private GoodsSku goodsSku;
    public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

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

    public Integer getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(Integer goodsId) {
        this.goodsId = goodsId;
    }

    public Date getAddtime() {
        return addtime;
    }

    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note == null ? null : note.trim();
    }

	public String getSubAddtime() {
		return subAddtime;
	}

	public void setSubAddtime(String subAddtime) {
		this.subAddtime = subAddtime;
	}

	public GoodsSku getGoodsSku() {
		return goodsSku;
	}

	public void setGoodsSku(GoodsSku goodsSku) {
		this.goodsSku = goodsSku;
	}
	
    
}