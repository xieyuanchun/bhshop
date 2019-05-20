package com.bh.utils;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bh.bean.BankCard;
import com.bh.bean.IDCardEntity;

public class AliMarketUtil {
	public static String host = "https://dm-51.data.aliyun.com";
	public static String path = "/rest/160601/ocr/ocr_idcard.json";
	public static String appcode = "54da07f66d03475c8de4d470c69e6c4b";
    public  static IDCardEntity verifyIDCardFace(String imgStr){
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "APPCODE " + appcode);
		headers.put("Content-Type", "application/json; charset=UTF-8");
		Map<String, String> querys = new HashMap<String, String>();
		String bodys = "{\"inputs\": [{\"image\": {\"dataType\": 50,\"dataValue\": \""+imgStr+"\"},\"configure\": {\"dataType\": 50,\"dataValue\": \"{\\\"side\\\":\\\"face\\\"}\"}}]}";
		try {
			HttpResponse response = HttpUtils.doPost(host, path, headers, querys, bodys);
		//	System.out.println("resultStr-->"+EntityUtils.toString(response.getEntity()));
			IDCardEntity idEntity  = transResult(EntityUtils.toString(response.getEntity()));
			return idEntity;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	
    }
    public static IDCardEntity verifyIDCardBack(String imgStr){
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Authorization", "APPCODE " + appcode);
		headers.put("Content-Type", "application/json; charset=UTF-8");
		Map<String, String> querys = new HashMap<String, String>();
		String bodys = "{\"inputs\": [{\"image\": {\"dataType\": 50,\"dataValue\": \""+imgStr+"\"},\"configure\": {\"dataType\": 50,\"dataValue\": \"{\\\"side\\\":\\\"back\\\"}\"}}]}";
		try {
			HttpResponse response = HttpUtils.doPost(host, path, headers, querys, bodys);
			IDCardEntity idEntity  = transResult(EntityUtils.toString(response.getEntity()));
			return idEntity;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	
    }
    private static IDCardEntity transResult(String resultStr){
    	try {
    		JSONObject obj = JSON.parseObject(resultStr);
    		String value = obj.getJSONArray("outputs").getJSONObject(0).getJSONObject("outputValue").getString("dataValue");
    		IDCardEntity entity = JSON.parseObject(value, IDCardEntity.class);
    		return entity;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			// TODO: handle exception
		}
    	
    }
    public static BankCard verifyBankCard(BankCard input){
    	  String host = "https://yunyidata.market.alicloudapi.com";
  	    String path = "/bankAuthenticate4";
  	    String appcode = "232d013ef8244587a9a4f69cb2fcca47";
  	    Map<String, String> headers = new HashMap<String, String>();
  	    //最后在header中的格式(中间是英文空格)为Authorization:APPCODE 83359fd73fe94948385f570e3c139105
  	    headers.put("Authorization", "APPCODE " + appcode);
  	    //根据API的要求，定义相对应的Content-Type
  	    headers.put("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
  	    Map<String, String> querys = new HashMap<String, String>();
  	    Map<String, String> bodys = new HashMap<String, String>();
  	    bodys.put("ReturnBankInfo", "YES");
  	    bodys.put("cardNo", input.getCardNo());
  	    bodys.put("idNo", input.getIdNo());
  	    bodys.put("name", input.getName());
  	    bodys.put("phoneNo", input.getPhoneNo());


  	    try {
  	    	/**
  	    	* 重要提示如下:
  	    	* HttpUtils请从
  	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/src/main/java/com/aliyun/api/gateway/demo/util/HttpUtils.java
  	    	* 下载
  	    	*
  	    	* 相应的依赖请参照
  	    	* https://github.com/aliyun/api-gateway-demo-sign-java/blob/master/pom.xml
  	    	*/
  	    	HttpResponse response = HttpUtils.doPost(host, path, headers, querys, bodys);
  	    	String json = EntityUtils.toString(response.getEntity());
            System.out.println("bank json--->"+json);
  	    	BankCard ret = JSON.parseObject(json, BankCard.class);
  	    	return ret;
  	    }catch (Exception e) {
  	    	return null;
			// TODO: handle exception
		}
    	
    }
	public static void main(String[] args) {
		try {

			/*String imgUrl = "http://bhshop.oss-cn-shenzhen.aliyuncs.com/goods/f2e9e211-8cec-49ba-bcff-89cc83a15f4f.png";
		    String faceImgBase64Str = ImageBase64Util.getImageBase64StrByUrl(imgUrl);
			IDCardEntity idEntity  = verifyIDCardFace(faceImgBase64Str);*/
			BankCard input = new BankCard();
			input.setName("谢小进");
			input.setPhoneNo("15899953302");
			input.setCardNo("6236683320013405808");
			input.setIdNo("440882198507205758");
			BankCard ret = AliMarketUtil.verifyBankCard(input);
			System.out.println("ret result-->"+ret.getRespMessage());
			

		
		} catch (Exception e) {
			
			// TODO: handle exception
		}
		
	}
}
