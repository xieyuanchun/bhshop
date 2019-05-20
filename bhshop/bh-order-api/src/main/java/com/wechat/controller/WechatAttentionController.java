package com.wechat.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wechat.service.WechatAttentionService;
import com.wechat.util.SignUtil;

@Controller
@RequestMapping("/wetchat")
public class WechatAttentionController {
	@Autowired
	private WechatAttentionService wechatAttentionService;
	
	
	@RequestMapping(value="/attention",method={RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public void WechatAttention(HttpServletRequest request,HttpServletResponse response) throws IOException {
		//将请求、响应的编码均设置为UTF-8
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		
		PrintWriter pw = response.getWriter();
		try{
				String respMessage = null;
		        try {
		            respMessage = wechatAttentionService.processRequest(request,response);
		            pw.write(respMessage);
		            System.out.println("The request completed successfully");
		            System.err.println("to weixin server " + respMessage );
		          } catch (Exception e) {
		        	 e.printStackTrace();
		        	 System.err.println("to weixin server " + respMessage );
		        	 System.out.println("Failed to convert the message from weixin!"); 
		          }    
	                
		}catch(Exception e){
			System.out.println("#######Exception#######");
			e.printStackTrace();
		}finally{
			pw.close();
		}
		
	}
}
