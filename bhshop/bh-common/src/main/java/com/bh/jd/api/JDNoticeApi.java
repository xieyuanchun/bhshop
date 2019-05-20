package com.bh.jd.api;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.bh.jd.bean.JdResult;
import com.bh.jd.bean.notice.GoodsPriceNotice;
import com.bh.jd.bean.notice.JdNoticeResult;
import com.bh.jd.enums.NoticeEnum;
import com.bh.jd.util.JDUtil;
import com.bh.utils.HttpUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class JDNoticeApi {
	//获取商品价格通知
	public static JdResult<List<JdNoticeResult<GoodsPriceNotice>>> getGoodsPriceNotice(String type){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("token",JDUtil.getAccessToken());
		jsonObject.put("type",type);
		String	retJsonStr = HttpUtils.doPost(NoticeEnum.GET.getMethod(), jsonObject);
		Gson gson = new Gson();
		JdResult<List<JdNoticeResult<GoodsPriceNotice>>> jdResult = gson.fromJson(retJsonStr, new TypeToken<JdResult<List<JdNoticeResult<GoodsPriceNotice>>>>(){}.getType());
		return jdResult;
	}
	public static void main(String[] args) {
		
		System.out.println("size--->"+getGoodsPriceNotice("6").getResult().size());
	}
	
	
}
