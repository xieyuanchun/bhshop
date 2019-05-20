package com.bh.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.mix.utils.MixUrl;

public class myConfig {

	public static String value = null;
	
	public static final Properties prop = new Properties();
	
	/**
	 * 首次加载配置文件
	 */
	static{
	    String path=myConfig.class.getResource("/resource/resource.properties").getFile();
		try {
			InputStream in =  new FileInputStream(path);
			prop.load(in);
		} catch (IOException e) {
			LoggerUtil.error(e);
		}
		
	}

	public static String config(String name) throws Exception {		
		return MixUrl.decrypt(prop.getProperty(name));
	}
	
	public static String webDB() {
		try {
			value = config("webDB");
		} catch (Exception e) {
			LoggerUtil.error(e);
		}
		return value;
	}
	
	public static String myConnIP() {
		try {
			value = config("myConnIP");
		} catch (Exception e) {
			LoggerUtil.error(e);
		}
		return value;
	}
	public static String DomainName() {
		try {
			value = config("DomainName");
		} catch (Exception e) {
			LoggerUtil.error(e);
		}
		return value;
	}
	
}
