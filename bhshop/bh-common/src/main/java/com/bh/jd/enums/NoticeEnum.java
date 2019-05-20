package com.bh.jd.enums;

import com.bh.jd.config.JDConfig;

public enum NoticeEnum {
		GET(JDConfig.BASE_URL+"/api/message/get");
		private String method;
		private NoticeEnum(String method) {
			this.method = method;
		}
		public String getMethod() {
			return method;
		}

		public void setMethod(String method) {
			this.method = method;
		}
}
