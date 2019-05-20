package com.wechat.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thoughtworks.xstream.XStream;
import com.wechat.vo.Article;
import com.wechat.vo.NewsMessage;
import com.wechat.vo.TextMessage;

public class MessageUtil {

	 //微信自动回复
	/** 
	   * 返回消息类型：文本 
	   */
	  public static final String RESP_MESSAGE_TYPE_TEXT = "text"; 
	  
	  /** 
	   * 返回消息类型：音乐 
	   */
	  public static final String RESP_MESSAGE_TYPE_MUSIC = "music"; 
	  
	  /** 
	   * 返回消息类型：图文 
	   */
	  public static final String RESP_MESSAGE_TYPE_NEWS = "news"; 
	  
	  /** 
	   * 请求消息类型：文本 
	   */
	  public static final String REQ_MESSAGE_TYPE_TEXT = "text"; 
	  
	  /** 
	   * 请求消息类型：图片 
	   */
	  public static final String REQ_MESSAGE_TYPE_IMAGE = "image"; 
	  
	  /** 
	   * 请求消息类型：链接 
	   */
	  public static final String REQ_MESSAGE_TYPE_LINK = "link"; 
	  
	  /** 
	   * 请求消息类型：地理位置 
	   */
	  public static final String REQ_MESSAGE_TYPE_LOCATION = "location"; 
	  
	  /** 
	   * 请求消息类型：音频 
	   */
	  public static final String REQ_MESSAGE_TYPE_VOICE = "voice"; 
	  
	  /** 
	   * 请求消息类型：推送 
	   */
	  public static final String REQ_MESSAGE_TYPE_EVENT = "event"; 
	  
	  /** 
	   * 事件类型：subscribe(订阅) 
	   */
	  public static final String EVENT_TYPE_SUBSCRIBE = "subscribe"; 
	  
	  /** 
	   * 事件类型：unsubscribe(取消订阅) 
	   */
	  public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe"; 
	  
	  /** 
	   * 事件类型：CLICK(自定义菜单点击事件) 
	   */
	  public static final String EVENT_TYPE_CLICK = "CLICK"; 
	
	  
	  
	  private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(MessageUtil.class);
	  /**
	   * xml转换为map
	   * @param request
	   * @return
	   * @throws IOException
	   */
	  @SuppressWarnings("unchecked")
	  public static Map<String, String> xmlToMap(HttpServletRequest request) throws IOException{
	    Map<String, String> map = new HashMap<String, String>();
	    SAXReader reader = new SAXReader();
	     
	    InputStream ins = null;
	    try {
	         ins = request.getInputStream();
	    } catch (IOException e1) {
	         e1.printStackTrace();
	    }
	    Document doc = null;
	    try {
	         doc = reader.read(ins);
	         Element root = doc.getRootElement();
	       
	         List<Element> list = root.elements();
	       
	         for (Element e : list) {
	           map.put(e.getName(), e.getText());
	         }
	        
	         return map;
	    } catch (DocumentException e1) {
	    	LOGGER.info("xml is null");
	      //e1.printStackTrace();
	    }finally{
	      ins.close();
	    }
	     
	    return null;
	  }
	  
	  /** 
	   * 文本消息对象转换成xml 
	   * @param <TextMessage>
	   * 
	   * @param textMessage 文本消息对象 
	   * @return xml 
	   */ 
	  public static String textMessageToXml(TextMessage textMessage){
	    XStream xstream = new XStream();
	    xstream.alias("xml", textMessage.getClass());
	    return xstream.toXML(textMessage);
	  }
	
	  /**
	   * 把图文消息转换为xml
	   */
	  public static String newsMessageToxml(NewsMessage newsMessage){
		XStream xstream = new XStream();
		xstream.alias("xml", newsMessage.getClass());
		xstream.alias("item", new Article().getClass());
		return xstream.toXML(newsMessage);  
	  }
}
