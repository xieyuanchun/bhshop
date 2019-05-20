package com.bh.product.api.controller;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.bh.product.api.trans.UEditorInfo;
import com.bh.utils.StringUtil;
@Controller
@RequestMapping("/ueditor")
public class UeditorController {

	@RequestMapping("/testfile")
	@ResponseBody
	public UEditorInfo testFile(
			@RequestParam(value = "upfile", required = false) MultipartFile file,
			HttpServletRequest request) {
		System.out.println("###########upfile#########");
		//value的名字一定要叫upfile，这是ueditor写好的参数名称
		String path = request.getSession(false).getServletContext()
				.getRealPath("files");//获得files目录的绝对路径
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
		
		
		// 图片和文件的返回要求
		// original: "1.png"
		// size: "86846"
		// state: "SUCCESS"
		// title: "1484710315158045904.png"
		// type: ".png"
		// url: "/ueditor/jsp/upload/image/20170118/1484710315158045904.png"
		UEditorInfo info = new UEditorInfo();
		
		info.setState("SUCCESS");
		info.setUrl("http://192.168.0.10/bh-product-api/files/" + targetFileName);
		info.setTitle(file.getOriginalFilename());
		info.setOriginal(file.getOriginalFilename());
		info.setSize(file.getSize());
		info.setType(type);
		 Map<String, Object> result = new HashMap<String, Object>();
		result.put("state", "SUCCESS");// UEDITOR的规则:不为SUCCESS则显示state的内容
		result.put("url", "/files/" + targetFileName);
		result.put("title",file.getOriginalFilename());
		result.put("original", file.getOriginalFilename());
		result.put("size", file.getSize());
		result.put("type", type);
		return info;
	}
}