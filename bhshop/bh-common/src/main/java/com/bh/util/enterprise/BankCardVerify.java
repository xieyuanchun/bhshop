package com.bh.util.enterprise;

import com.alibaba.fastjson.JSON;
import com.bh.util.enterprise.pojo.BankCardVerifyPojo;
import com.bh.utils.HttpUtils;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 银行卡号实名认证
 * @author xieyc
 * @date 2018年7月31日 上午10:58:07
 */
public class BankCardVerify {
	public static final String host = "https://yunyidata.market.alicloudapi.com";
	public static final String path = "/bankAuthenticate4";
	public static final String appcode = "232d013ef8244587a9a4f69cb2fcca47";

	public static BankCardVerifyPojo bankCardVerify(BankCardVerifyPojo entity) {
		BankCardVerifyPojo bankCardVerifyPojo = null;

		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "APPCODE " + appcode);
		headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
		Map<String, String> querys = new HashMap<String, String>();
		Map<String, String> bodys = new HashMap<String, String>();
		bodys.put("ReturnBankInfo", "YES");
		bodys.put("cardNo", entity.getCardNo());
		bodys.put("idNo", entity.getIdNo());
		bodys.put("name", entity.getName());
		bodys.put("phoneNo", entity.getPhoneNo());
		try {
			HttpResponse response = HttpUtils.doPost(host, path, headers, querys, bodys);
			String json = EntityUtils.toString(response.getEntity());
			bankCardVerifyPojo = JSON.parseObject(json, BankCardVerifyPojo.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bankCardVerifyPojo;
	}

	/**
	 * @Description: 验证银行卡号
	 * @author xieyc
	 * @date 2018年7月31日 下午5:31:30
	 */
	public static boolean bankCardVerify (String name,String phoneNo,String cardNo,String idNo) {
		boolean flag =false;
		BankCardVerifyPojo  entity=new BankCardVerifyPojo();
		entity.setName(name);//卡主
		entity.setPhoneNo(phoneNo);//预留号码
		entity.setCardNo(cardNo);//卡号
		entity.setIdNo(idNo);//身份证号码
		BankCardVerifyPojo  bankCardVerifyPojo=BankCardVerify.bankCardVerify(entity);
		if(bankCardVerifyPojo.getRespCode().equals("0000")){
			flag =true;//验证成功
		}else{
			flag =false;//验证失败
		}
		return flag;
	}

	/**
	 * 
	 * @Description: ret.getRespCode().equals("0000") 为true表示认证成功  false认证失败
	 * @author xieyc
	 * @date 2018年7月31日 上午11:28:55 
	 * @param   
	 * @return  
	 *
	 */
	public static void main(String[] args) {
		try {
			BankCardVerifyPojo  entity=new BankCardVerifyPojo();
			entity.setName("谢小进");
			entity.setPhoneNo("15899953302");
			entity.setCardNo("6236683320013405808");
			entity.setIdNo("440882198507205758");
			BankCardVerifyPojo  bankCardVerifyPojo=BankCardVerify.bankCardVerify(entity);
			System.out.println(bankCardVerifyPojo.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

}
