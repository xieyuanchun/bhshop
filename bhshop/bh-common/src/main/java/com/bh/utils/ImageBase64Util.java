package com.bh.utils;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
public class ImageBase64Util{
	/** 
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理 
     * @param imgFilePath 图片路径 
     * @return String 
     */  
    public static String getImageBase64Str(String imgFilePath) {  
        byte[] data = null;  
        // 读取图片字节数组  
        try {  
            InputStream in = new FileInputStream(imgFilePath);  
            data = new byte[in.available()];  
            in.read(data);  
            in.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        // 对字节数组Base64编码  
        return Base64.encodeBase64String(data);// 返回Base64编码过的字节数组字符串  
    } 
    public static String getImageBase64StrByUrl(String imgUrl){
    	try {
    		URL u = new URL(imgUrl);
    		BufferedImage image = ImageIO.read(u);
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		String subFix = imgUrl.substring(imgUrl.lastIndexOf(".")+1,imgUrl.length());
    		ImageIO.write( image, subFix, baos);
    		byte[] imgByts = baos.toByteArray();
    		String faceImgBase64Str = Base64.encodeBase64String(imgByts);
    		return faceImgBase64Str;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
			// TODO: handle exception
		}
    	
    }
    /** 
     * 对字节数组字符串进行Base64解码并生成图片 
     * @param imgStr Base64字符串 
     * @param imgFilePath 生成图片保存路径  
     * @return boolean 
     */  
    public static boolean genImage(String imgStr, String imgFilePath) {  
        if (imgStr == null) // 图像数据为空  
            return false;  
        try {  
            // Base64解码  
            byte[] bytes = Base64.decodeBase64(imgStr.getBytes("UTF-8"));  
            for (int i = 0; i < bytes.length; ++i) {  
                if (bytes[i] < 0) {// 调整异常数据  
                    bytes[i] += 256;  
                }  
            }  
            //生成图片  
            OutputStream out = new FileOutputStream(imgFilePath);  
            out.write(bytes);  
            out.flush();  
            out.close();  
            return true;  
        } catch (Exception e) {  
            return false;  
        }  
    }  
      
    /** 
     * 对字节数组字符串进行Base64解码并生成图片 
     * @param imgStr 图片字符串 
     * @return byte[] 
     */  
    public static byte[] getStrToBytes(String imgStr) {   
        if (imgStr == null) // 图像数据为空  
            return null;  
        try {  
            // Base64解码  
            byte[] bytes = Base64.decodeBase64(imgStr);  
            for (int i = 0; i < bytes.length; ++i) {  
                if (bytes[i] < 0) {// 调整异常数据  
                    bytes[i] += 256;  
                }  
            }  
            // 生成图片  
            return bytes;  
        } catch (Exception e) {  
            return null;  
        }  
    }  
}
