package com.bh.product.web.controller;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.bh.goods.pojo.GoodsImage;
import com.bh.product.web.service.UploadImageService;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.baidu.ueditor.ActionEnter;
@Controller
public class UploadImageController {
	@Autowired
	private UploadImageService service;
	
	@RequestMapping(value="/editor/uploadimage")
	public String uploadimage(    
            HttpServletRequest request,HttpServletResponse response) {
		  
		  BhResult result = null;
	
		  System.out.println("#################");
	        String realName = null;  
	         String uuidName = null;  
	         String realPath = null;  
	           
	         try {  
	       	  request.setCharacterEncoding( "utf-8" );
			  response.setHeader("Content-Type" , "text/html");
	             GoodsImage image  = new GoodsImage();  
	             //文件原来的名称  
	            /* realName = file.getOriginalFilename();  
	             //得到这个文件的uuidname  
	             uuidName = this.getUUIDFileName(file.getOriginalFilename());  */
	             //图片保存的工程  
	             realPath =  request.getSession().getServletContext().getRealPath("/"); 
	             String jsonFilePath=realPath+"/resources/ueditor/jsp/config.json";
	             String str = new ActionEnter( request, jsonFilePath ).exec();
	             System.out.println("str--->"+str);
	             response.getWriter().write(str);
	             System.out.println("realPath--->"+realPath);
	    
	          
	        } catch (Exception e) {  
	   
	        } 
          return null;	    
	}
	
	private String getExtName(String s, char split) {    
         int i = s.lastIndexOf(split);    
         int leg = s.length();    
         return i > 0 ? (i + 1) == leg ? " " : s.substring(i+1, s.length()) : " ";    
     }    
     
    private String getUUIDFileName(String fileName){    
          UUID uuid = UUID.randomUUID();    
          StringBuilder sb = new StringBuilder(100);    
          sb.append(uuid.toString()).append(".").append(this.getExtName(fileName, '.'));    
          return sb.toString();    
      }  
   
   
   // 文件上传路径
   @Resource(name="fileuploadPath")
   private String fileuploadPath;
  
   // 文件读取路径
   @Resource(name="httpPath")
   private String httpPath;
   @RequestMapping(value="/upload",method = RequestMethod.POST)
   public Map<String,Object> upload(HttpServletRequest req){
       Map<String,Object> result = new HashMap<String, Object>();
       
       MultipartHttpServletRequest mReq  =  null;
       MultipartFile file = null;
       InputStream is = null ;
       String fileName = "";
       // 原始文件名   UEDITOR创建页面元素时的alt和title属性
       String originalFileName = "";
       String filePath = "";
       
       try {
           mReq = (MultipartHttpServletRequest)req;
           // 从config.json中取得上传文件的ID
           file = mReq.getFile("upfile");
           // 取得文件的原始文件名称
           fileName = file.getOriginalFilename();

           originalFileName = fileName;
           
           if(!fileName.isEmpty()){
               is = file.getInputStream();
               fileName = FileUtils.reName(fileName);
               filePath = FileUtils.saveFile(fileName, is, fileuploadPath);
           } else {
               throw new IOException("文件名为空!");
           }
           
           result.put("state", "SUCCESS");// UEDITOR的规则:不为SUCCESS则显示state的内容
           result.put("url",httpPath + filePath);
           result.put("title", originalFileName);
           result.put("original", originalFileName);
       }
       catch (Exception e) {
           System.out.println(e.getMessage());
           result.put("state", "文件上传失败!");
           result.put("url","");
           result.put("title", "");
           result.put("original", "");
           System.out.println("文件 "+fileName+" 上传失败!");
       }
       
       return result;
   }
   
   
   @RequestMapping(value="/uploadTest",method = RequestMethod.POST)
	@ResponseBody
	public BhResult uploadTest(@RequestParam(value = "head", required = true) MultipartFile head,     
          HttpServletRequest request,HttpServletResponse response) {
		  BhResult result = null;
		  try {
			String filePath = FileUtils.uploadFile(head, request);
			GoodsImage image  = new GoodsImage();
			 image.setGoodsId(1);
             image.setUrl(filePath);
             int flag = service.selectInsert(image);  
             if(flag!=0){  
            	 result = new BhResult(200, "添加成功", image);
			 }else{
				 result = new BhResult(400, "添加失败", null);         
             }  
		} catch (Exception e) {
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
         return result;	    
	}

}
