package com.bh.utils.bankType;


import java.util.HashMap;
import java.util.Map;
import com.bh.utils.HttpClientUtil;


public class CNBankCard {
	
	
	public String  getBankType(String bankCardNo){
		String bankType=null;
		try {
			Map<String, String> param=new HashMap<String, String>();
			param.put("_input_charset", "utf-8");
			param.put("cardNo", bankCardNo);
			param.put("cardBinCheck", "true");
			bankType=HttpClientUtil.doGet("https://ccdcapi.alipay.com/validateAndCacheCardInfo.json",param);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bankType;
	}
	
	public static void main(String[] args) {
		CNBankCard card=new CNBankCard();
		card.getBankType("6236683320023723448");
		
		
	}

}
