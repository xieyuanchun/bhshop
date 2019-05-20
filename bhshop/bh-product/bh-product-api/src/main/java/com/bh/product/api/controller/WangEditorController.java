package com.bh.product.api.controller;
import java.io.File;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bh.config.Contants;
import com.bh.product.api.trans.WangEditorInfo;
import com.bh.utils.StringUtil;
import com.control.file.upload;
/**
 * wangEditor编辑器上传图片
 * @author xxj
 *
 */
@Controller
@RequestMapping("/wangEditor")
public class WangEditorController {
	@Value(value = "${oss}")
	private String oss;
	@RequestMapping("/uploadImg")
	@ResponseBody
	public WangEditorInfo uploadImg(MultipartFile files[],
			HttpServletRequest request) {
		String path = request.getSession(false).getServletContext()
				.getRealPath("/");//获得files目录的绝对路径
		WangEditorInfo w = new WangEditorInfo();
		w.setErrno(0);
		ArrayList<String> list = new ArrayList<>();
		for(MultipartFile file:files){
			StringBuffer realPath = new StringBuffer(Contants.bucketHttps); 
			StringBuffer key = new StringBuffer();
			
			String fileName = file.getOriginalFilename();//获得上传文件的实际名称
			String[] types = fileName.split("\\.");
			String type = types[types.length - 1];
			type = "." + type;//获得文件的后缀名
			String targetFileName = StringUtil.genUuId() + type;
			File targetFile = new File(path, targetFileName);
			
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			try {
				file.transferTo(targetFile);//保存文件
				
				key.append("goods/");
		        key.append(targetFileName);
				upload myupload= new upload();
		        String localFilePath = path + targetFileName;
		 		boolean bl=myupload.singleupload(oss,localFilePath,key.toString());
		 		
		 		realPath.append("goods/");
				realPath.append(targetFileName);

				if(bl){
					list.add(realPath.toString());
				}else{
					w.setErrno(400);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		w.setData(list);
		return w;
	}
	
	/**
	 * 机器人头像上传 xieyc
	 */
	
	@RequestMapping("/uploadRobotHead")
	@ResponseBody
	public WangEditorInfo uploadRobotHead(MultipartFile files[],
			HttpServletRequest request) {
		String path = request.getSession(false).getServletContext()
				.getRealPath("/");//获得files目录的绝对路径
		WangEditorInfo w = new WangEditorInfo();
		w.setErrno(0);
		ArrayList<String> list = new ArrayList<>();
		for(MultipartFile file:files){
			StringBuffer realPath = new StringBuffer(Contants.bucketHttps); 
			StringBuffer key = new StringBuffer();
			
			String fileName = file.getOriginalFilename();//获得上传文件的实际名称
			String[] types = fileName.split("\\.");
			String type = types[types.length - 1];
			type = "." + type;//获得文件的后缀名
			String targetFileName = StringUtil.genUuId() + type;
			File targetFile = new File(path, targetFileName);
			
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			try {
				file.transferTo(targetFile);//保存文件
				
				key.append("robotHead/");
		        key.append(targetFileName);
				upload myupload= new upload();
		        String localFilePath = path + targetFileName;
		 		boolean bl=myupload.singleupload(oss,localFilePath,key.toString());
		 		
		 		realPath.append("robotHead/");
				realPath.append(targetFileName);

				if(bl){
					list.add(realPath.toString());
				}else{
					w.setErrno(400);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		w.setData(list);
		return w;
	}
	
	
	
	
	/*@RequestMapping("/uploadImg")
	@ResponseBody
	public WangEditorInfo uploadImg(MultipartFile files[],
			HttpServletRequest request) {
		String path = request.getSession(false).getServletContext()
				.getRealPath("files");//获得files目录的绝对路径
		WangEditorInfo w = new WangEditorInfo();
		w.setErrno(0);
		ArrayList<String> list = new ArrayList<>();
		for(MultipartFile file:files){
			String fileName = file.getOriginalFilename();//获得上传文件的实际名称
			String[] types = fileName.split("\\.");
			String type = types[types.length - 1];
			type = "." + type;//获得文件的后缀名
			String targetFileName = StringUtil.genUuId() + type;
			File targetFile = new File(path, targetFileName);
	
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			try {
				file.transferTo(targetFile);//保存文件
			} catch (Exception e) {
				e.printStackTrace();
			}
			list.add("http://192.168.0.10/bh-product-api/files/" + targetFileName);
			
		}
		w.setData(list);
		return w;
	}*/
}