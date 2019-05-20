package com.bh.utils;
import java.io.File;
import java.io.InputStream;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.DeleteObjectsRequest;
import com.aliyun.oss.model.GenericRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.UploadFileRequest;

/**
 * 
 * @author xxj
 *
 */
public class AliyunOssUtil {
	static OSSClient ossClient = null;
	private static class OSSConfig{
		public static String endpoint = "https://oss-cn-shenzhen.aliyuncs.com";
		public static String accessKeyId = "LTAIp2FTTphzWyCM";
		public static String accessKeySecret = "x5jyKIA2yih5DfG2HIKyGquqgWrHoO";
		public static String bucketName="bhshop";
	}
	private static synchronized OSSClient getOSSClient(){
		if(ossClient==null){
			ossClient = new OSSClient(OSSConfig.endpoint, OSSConfig.accessKeyId, OSSConfig.accessKeySecret);
		}
		return ossClient;
	}
	public static class Image{
		//缩放
		public static void resize(String key,float width,float height,String saveFileLocalPath){
			//  String style = "image/resize,m_fixed,w_"+width+",h_"+height;  
			  String style = "image/resize,m_fixed,w_100,h_100";  
			  setProcessStyle(key,style,saveFileLocalPath);
		}
		//裁剪
		public static void crop(String key,float width,float height,float x,float y,float r,String saveFileLocalPath){
			String style = "image/crop,w_"+width+",h_"+height+",x_"+x+",y_"+y+",r_"+r; 
			setProcessStyle(key,style,saveFileLocalPath);
		}
		//旋转
		public static void rotate(String key,float angle,String saveFileLocalPath){
			String style = "image/rotate,"+angle; 
			setProcessStyle(key,style,saveFileLocalPath);
		}
		//锐化
	    public static void sharpen(String key,int sharValue,String saveFileLocalPath){
	    	 String style = "image/sharpen,"+sharValue; 
	    	 setProcessStyle(key,style,saveFileLocalPath);
	    }
	    //水印
	    public static void watermark(String key,String base64Txt,String saveFileLocalPath){
	    	 String style = "image/watermark,text_"+base64Txt; 
	    	 setProcessStyle(key,style,saveFileLocalPath);
	    }
	    //设置样式
	    private static void setProcessStyle(String key,String style,String saveFileLocalPath){
	    	 GetObjectRequest   request = new GetObjectRequest(OSSConfig.bucketName, key);
	         request.setProcess(style);
	         File  file = new File(saveFileLocalPath);
	         ossClient = getOSSClient();
	         ossClient.getObject(request,file);
	         ossClient.shutdown();
	         //url = config.getEndpoint().replaceFirst("http://","http://"+config.getBucketName()+".")+"/"+fileName;
	         // http://image-demo.oss-cn-hangzhou.aliyuncs.com/example.jpg?x-oss-process=image/resize,h_100
	    }
		
	}

	/**
	 * 上传文件
	 * @param file
	 * @param fileType
	 * @param fileName
	 * @return
	 */
    public  static String uploadLocalFile(String fileLocalPath,String ossFolder){  
           String key = null;
    	   try {
    		   ossClient = getOSSClient();
    		   String suffix = fileLocalPath.substring(fileLocalPath.lastIndexOf("."), fileLocalPath.length());
    		   String fileName = StringUtil.genUuId()+suffix; 
    		   key = ossFolder+"/"+fileName;
               UploadFileRequest uploadFileRequest = new UploadFileRequest(OSSConfig.bucketName, key);
               // 待上传的本地文件
               uploadFileRequest.setUploadFile(fileLocalPath);
               // 设置并发下载数，默认1
               uploadFileRequest.setTaskNum(5);
               // 设置分片大小，默认100KB
               uploadFileRequest.setPartSize(1024 * 1024 * 1);
               // 开启断点续传，默认关闭
               uploadFileRequest.setEnableCheckpoint(true);
               ossClient.uploadFile(uploadFileRequest);
            
           } catch (OSSException oe) {
        	   System.out.println("Error Message: " + oe.getMessage());
             
           } catch (ClientException ce) {
               System.out.println("Error Message: " + ce.getMessage());
           } catch (Throwable e) {
               e.printStackTrace();
           } finally {
               ossClient.shutdown();
           }
    	   return key;
    } 
    public static boolean uploadFileInputStream(InputStream inputStream,String key){
	    try {
	    	// 创建OSSClient实例
	       ossClient = getOSSClient();
	     	// 上传文件流
	      //	InputStream inputStream = new FileInputStream("localFile");
	     	ossClient.putObject(OSSConfig.bucketName,key, inputStream);
	     	// 关闭client
	     	ossClient.shutdown();
	     	return true;
		} catch (Exception e) {
			return false;
		}
    	
    }
    /**
     * 替换文件  
     * @param fileLocalPath
     * @param ossFolder
     * @param oldKey
     * @return
     */
    public static String replaceFile(String fileLocalPath,String ossFolder,String oldKey){  
        boolean flag = deleteFile(oldKey);//先删除原文件 
        if(flag){
           return uploadLocalFile(fileLocalPath, ossFolder);
        }
        return null;
    }
	/**
	 * 删除文件
	 * @param key
	 * @return 是否删除成功
	 */
    public static boolean deleteFile(String key){ 
        try {  
        	ossClient = getOSSClient();
            GenericRequest request = new DeleteObjectsRequest(OSSConfig.bucketName).withKey(key);  
            ossClient.deleteObject(request);  
        } catch (Exception oe) {  
            oe.printStackTrace();  
            return false;  
        } finally {  
            ossClient.shutdown();  
        }  
        return true;  
    }
   
    public static void main(String args[]) throws Exception{
    	 //上传文件
     AliyunOssUtil.uploadLocalFile("D:/95bOOOPIC38.jpg","20171012xxjTest");
    	//删除文件
    	// AliyunOssUtil.deleteFile("xxjTest/967629169EBE4DF494B5F63F602B0CC7.jpg");
    	// AliyunOssUtil.uploadFileInputStream(new FileInputStream(new File("D:/95bOOOPIC38.jpg")), "xxjTest/xxj001.jpg");
    	// AliyunOssUtil.Image.resize("xxjTest/tesMyJpg.jpg", 100, 100);
    }
}
