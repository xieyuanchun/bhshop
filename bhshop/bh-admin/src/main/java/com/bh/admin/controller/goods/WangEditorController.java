package com.bh.admin.controller.goods;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bh.admin.vo.WangEditorInfo;
import com.bh.config.Contants;
import com.bh.result.BhResult;
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
		String path = request.getSession().getServletContext()
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
		String path = request.getSession().getServletContext()
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
	
	
	
	
	@RequestMapping("/insertSkuStore")
	@ResponseBody
	public BhResult getImageInfoByImageReader(MultipartFile files[],HttpServletRequest request) {
		try {
			String path = request.getSession().getServletContext()
					.getRealPath("/");//获得files目录的绝对路径
			WangEditorInfo w = new WangEditorInfo();
			w.setErrno(0);

			List<Map<String, Object>> myList=new ArrayList<>();
			for(MultipartFile file:files){
				Map<String, Object> myMap=new HashMap<>();
				StringBuffer sb=new StringBuffer();
				 BufferedImage sourceImg =ImageIO.read(file.getInputStream()); 
				 //获取图片宽度，单位px   ,获取图片高度，单位px
				sb.append(sourceImg.getWidth()+"").append("*").append(sourceImg.getHeight()+"");
				myMap.put("picSize", sb.toString());
				myMap.put("size", file.getSize()+"");//获取图片的大小，单位kb
				StringBuffer realPath = new StringBuffer(Contants.bucketHttps); 
				StringBuffer key = new StringBuffer();
				
				String fileName = file.getOriginalFilename();//获得上传文件的实际名称
				String[] types = fileName.split("\\.");
				String type = types[types.length - 1];
				myMap.put("ext", type);
				
				type = "." + type;//获得文件的后缀名
				String targetFileName = StringUtil.genUuId() + type;
				File targetFile = new File(path, targetFileName);
				
				if (!targetFile.exists()) {
					targetFile.mkdirs();
				}
				
				file.transferTo(targetFile);//保存文件
				
				key.append("skuStore/");
		        key.append(targetFileName);
				upload myupload= new upload();
		        String localFilePath = path + targetFileName;
		 		boolean bl=myupload.singleupload(oss,localFilePath,key.toString());
		 		
		 		realPath.append("skuStore/");
				realPath.append(targetFileName);

				if(bl){
					myMap.put("fileUrl", realPath.toString());
				}else{
					return BhResult.build(400, "上传图片失败,请检查网络或者是重新上传", null);
				}
				myList.add(myMap);
			}
			return BhResult.build(200, "操作成功", myList);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return BhResult.build(500, "上传图片失败,请检查网络或者是重新上传", null);
		}
       
       
    }
	
	
}