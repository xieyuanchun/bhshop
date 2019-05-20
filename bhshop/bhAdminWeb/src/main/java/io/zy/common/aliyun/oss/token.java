package io.zy.common.aliyun.oss;

public class token {

	public static String[] setKey() 
	{
		String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
		String accessKeyId = "LTAIp2FTTphzWyCM";
		String accessKeySecret = "x5jyKIA2yih5DfG2HIKyGquqgWrHoO";
		String bucketName = "bhshop";
		String[] keyNa = { endpoint, accessKeyId, accessKeySecret, bucketName };
		return keyNa;
	}

}
