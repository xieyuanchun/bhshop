package com.bh.admin.pojo.user;

public class GiftMember {
    private Integer id;

    private Integer couponGiftId;

    private Integer mId;

    private String currentPage;
    
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCouponGiftId() {
        return couponGiftId;
    }

    public void setCouponGiftId(Integer couponGiftId) {
        this.couponGiftId = couponGiftId;
    }

    public Integer getmId() {
        return mId;
    }

    public void setmId(Integer mId) {
        this.mId = mId;
    }

	public String getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(String currentPage) {
		this.currentPage = currentPage;
	}
    
    
}