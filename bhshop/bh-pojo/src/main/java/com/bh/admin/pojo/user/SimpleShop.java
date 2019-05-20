package com.bh.admin.pojo.user;


public class SimpleShop {
		private  Integer shopId;
	    private String shopName;//名称
	    private String logo;//店铺图片
	    private Integer favorite;//是否收藏-0未收藏，1已收藏
	    private String favoriteName;//0收藏-1已收藏
		public String getShopName() {
			return shopName;
		}
		public void setShopName(String shopName) {
			this.shopName = shopName;
		}
		public String getLogo() {
			return logo;
		}
		public void setLogo(String logo) {
			this.logo = logo;
		}
		public Integer getFavorite() {
			return favorite;
		}
		public void setFavorite(Integer favorite) {
			this.favorite = favorite;
		}
		public String getFavoriteName() {
			 switch (favoriteName) {
				case "0":  return "收藏";
				case "1":  return "已收藏";
				default: return "";
			}
		}
		public void setFavoriteName(String favoriteName) {
			this.favoriteName = favoriteName;
		}
		public Integer getShopId() {
			return shopId;
		}
		public void setShopId(Integer shopId) {
			this.shopId = shopId;
		}
	    
	    
	    

}
