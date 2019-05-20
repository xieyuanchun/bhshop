package com.bh.jd.enums;

import com.bh.jd.config.JDConfig;

public enum UtilEnum {
	    ACCESS_TOKEN(JDConfig.BASE_URL+"/oauth2/accessToken");
		private String method;
		private UtilEnum(String method) {
			this.method = method;

		}
		public String getMethod() {
			return method;
		}

		public void setMethod(String method) {
			this.method = method;
		}
}
