package com.wechat.vo;

public class Article {

	   //微信自动回复
		private String describe;//图文消息描述
		
		private String picUrl;//图片地址

		private String title;//图文标题
		
		private String url;//地址URL

		
		
		
		
		
		public String getDescribe() {
			return describe;
		}

		public void setDescribe(String describe) {
			this.describe = describe;
		}

		public String getPicUrl() {
			return picUrl;
		}

		public void setPicUrl(String picUrl) {
			this.picUrl = picUrl;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
}
