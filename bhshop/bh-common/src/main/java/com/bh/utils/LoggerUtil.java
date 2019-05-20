package com.bh.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

public class LoggerUtil  {
	private static final Logger logger = LoggerFactory.getLogger(LoggerUtil.class);

	public static Logger getLogger(){
		return logger;
	}
	
	public static void error(Object object){
		 logger.error("message:", object);
	}
	
	
}
