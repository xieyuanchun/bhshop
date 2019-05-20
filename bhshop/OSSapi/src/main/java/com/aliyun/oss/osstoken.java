package com.aliyun.oss;

public class osstoken {

	public static String[] setKey() 
	{
		String endpoint = "http://oss-cn-shenzhen.aliyuncs.com";
		String accessKeyId = "LTAIp2FTTphzWyCM";
		String accessKeySecret = "x5jyKIA2yih5DfG2HIKyGquqgWrHoO";
		String bucketName = "bhshoptest3";
		String[] keyNa = { endpoint, accessKeyId, accessKeySecret, bucketName };
		return keyNa;
	}
}
