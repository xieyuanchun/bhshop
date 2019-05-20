package com.bh.utils;

import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.QuerySendDetailsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendBatchSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendBatchSmsResponse;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 
 * @Description: 短信API产品的DEMO程序,工程中包含了一个SmsDemo类，直接通过执行main函数即可体验短信产品API功能(只需要将AK替换成开通了云通信-短信产品功能的AK即可)
 * @author xieyc
 * @date 2018年8月22日 下午4:40:07 
 */
public class SmsSend {

    //产品名称:云通信短信API产品,开发者无需替换
    static final String product = "Dysmsapi";
    //产品域名,开发者无需替换
    static final String domain = "dysmsapi.aliyuncs.com";

    // TODO 此处需要替换成开发者自己的AK(在阿里云访问控制台寻找)
    static final String accessKeyId = "LTAIwTl3ukIDWej6";
    static final String accessKeySecret = "CgGk3rbO0Qp9fcpfeM2lYIvTyBYJew";
    
    
    
    /**
     * 
     * @Description: 单条短信发送
     * @author xieyc
     * @date 2018年8月22日 下午5:04:11   
     *
     */
    public static SendSmsResponse sendSmsOne() throws ClientException {
        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象-具体描述见控制台-文档部分内容
        SendSmsRequest request = new SendSmsRequest();
        //必填:待发送手机号
        request.setPhoneNumbers("17744905215");
       // request.setPhoneNumberJson("[\"1500000000\",\"1500000001\"]");
        //必填:短信签名-可在短信控制台中找到
        request.setSignName("滨惠商城");
        //必填:短信模板-可在短信控制台中找到
        request.setTemplateCode("SMS_141581074");
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        request.setTemplateParam("{\"username\":\"xieyc\", \"password\":\"123456\"}");

        //选填-上行短信扩展码(无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");

        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //request.setOutId("yourOutId");

        //hint 此处可能会抛出异常，注意catch
        SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
        if(sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK")) {
        	//请求成功
        	System.out.println(sendSmsResponse.getMessage());
        }
        return sendSmsResponse;
    }
    
    /**
     * 
     * @Description: 多条短信发送
     * @author xieyc
     * @date 2018年8月22日 下午5:04:11   
     *
     */
    public static SendBatchSmsResponse sendSmsBatch() throws ClientException {
    	
    	//设置超时时间-可自行调整
    	System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
    	System.setProperty("sun.net.client.defaultReadTimeout", "10000");

    	//初始化ascClient,暂时不支持多region（请勿修改）
    	IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId,accessKeySecret);
    	DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
    	IAcsClient acsClient = new DefaultAcsClient(profile);
    	 //组装请求对象
    	 SendBatchSmsRequest request = new SendBatchSmsRequest();
    	 //使用post提交
    	 request.setMethod(MethodType.POST);
    	 //必填:待发送手机号。支持JSON格式的批量调用，批量上限为100个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式
    	 request.setPhoneNumberJson("[\"17744905215\",\"18806522884\"]");
    	 //必填:短信签名-支持不同的号码发送不同的短信签名
    	 request.setSignNameJson("[\"滨惠商城\",\"滨惠商城\"]");
    	 //必填:短信模板-可在短信控制台中找到
    	 request.setTemplateCode("SMS_141581074");
    	 //必填:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
    	 //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
    	 request.setTemplateParamJson("[{\"username\":\"xieyc\", \"password\":\"123456\"},{\"username\":\"xieyc11\", \"password\":\"123456\"}]");
    	 //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
    	 //request.setSmsUpExtendCodeJson("[\"90997\",\"90998\"]");
    	//请求失败这里会抛ClientException异常
    	SendBatchSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
    	System.out.println(sendSmsResponse.getMessage());
    	
    	return sendSmsResponse;
    }
    
       
    /**
     * 
     * @Description: 查明细
     * @author xieyc
     * @date 2018年8月22日 下午5:04:26 
     * 
     */  
    public static QuerySendDetailsResponse querySendDetails(String bizId) throws ClientException {

        //可自助调整超时时间
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //初始化acsClient,暂不支持region化
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient acsClient = new DefaultAcsClient(profile);

        //组装请求对象
        QuerySendDetailsRequest request = new QuerySendDetailsRequest();
        //必填-号码
        request.setPhoneNumber("15000000000");
        //可选-流水号
        request.setBizId(bizId);
        //必填-发送日期 支持30天内记录查询，格式yyyyMMdd
        SimpleDateFormat ft = new SimpleDateFormat("yyyyMMdd");
        request.setSendDate(ft.format(new Date()));
        //必填-页大小
        request.setPageSize(10L);
        //必填-当前页码从1开始计数
        request.setCurrentPage(1L);

        //hint 此处可能会抛出异常，注意catch
        QuerySendDetailsResponse querySendDetailsResponse = acsClient.getAcsResponse(request);

        return querySendDetailsResponse;
    }

    public static void main(String[] args) throws ClientException, InterruptedException {
    	
    	//sendSmsBatch();
    	sendSmsOne();
    

      /*  //发短信
        SendSmsResponse response = sendSmsOne();
        System.out.println("短信接口返回的数据----------------");
        System.out.println("Code=" + response.getCode());
        System.out.println("Message=" + response.getMessage());
        System.out.println("RequestId=" + response.getRequestId());
        System.out.println("BizId=" + response.getBizId());

       Thread.sleep(3000L);

        //查明细
        if(response.getCode() != null && response.getCode().equals("OK")) {
            QuerySendDetailsResponse querySendDetailsResponse = querySendDetails(response.getBizId());
            System.out.println("短信明细查询接口返回数据----------------");
            System.out.println("Code=" + querySendDetailsResponse.getCode());
            System.out.println("Message=" + querySendDetailsResponse.getMessage());
            int i = 0;
            for(QuerySendDetailsResponse.SmsSendDetailDTO smsSendDetailDTO : querySendDetailsResponse.getSmsSendDetailDTOs())
            {
                System.out.println("SmsSendDetailDTO["+i+"]:");
                System.out.println("Content=" + smsSendDetailDTO.getContent());
                System.out.println("ErrCode=" + smsSendDetailDTO.getErrCode());
                System.out.println("OutId=" + smsSendDetailDTO.getOutId());
                System.out.println("PhoneNum=" + smsSendDetailDTO.getPhoneNum());
                System.out.println("ReceiveDate=" + smsSendDetailDTO.getReceiveDate());
                System.out.println("SendDate=" + smsSendDetailDTO.getSendDate());
                System.out.println("SendStatus=" + smsSendDetailDTO.getSendStatus());
                System.out.println("Template=" + smsSendDetailDTO.getTemplateCode());
            }
            System.out.println("TotalCount=" + querySendDetailsResponse.getTotalCount());
            System.out.println("RequestId=" + querySendDetailsResponse.getRequestId());
        }*/

    }
}
