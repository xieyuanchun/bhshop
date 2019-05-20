package com.order.util.smallAppPay;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;

/**
 * 
 * @author louiseliu
 *
 */
public class PayUtils {
	
	public static PayCallbackNotify payCallbackNotify(InputStream inputStream){
		try {
			String content = IOUtils.toString(inputStream);
			System.out.println("smallApp content------>"+content);
			XmlMapper xmlMapper = new XmlMapper();
			PayCallbackNotify payCallbackNotify = xmlMapper.readValue(content, PayCallbackNotify.class);
			if(payCallbackNotify.getResult_code().equals("SUCCESS")
					&& payCallbackNotify.getReturn_code().equals("SUCCESS")){
				payCallbackNotify.setPaySuccess(true);
			}
			return payCallbackNotify;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	

}
