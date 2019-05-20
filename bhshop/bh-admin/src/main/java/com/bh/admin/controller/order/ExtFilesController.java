package com.bh.admin.controller.order;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.bh.admin.pojo.order.ExtFiles;
import com.bh.admin.pojo.order.MyExtFiles;
import com.bh.admin.service.ExtFilesService;
import com.bh.admin.util.JedisUtil;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.JsonUtils;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;



/**
 * @Description: 后台管理系统-图库管理
 * @author xieyc
 * @date 2018年1月9日 下午3:31:25 
 */
@Controller
@RequestMapping("/extFiles")
public class ExtFilesController {
	@Autowired
	private ExtFilesService extFileService;

	/**
	 * @Description: 分页查询某种类型的文件
	 * @author xieyc
	 * @date 2018年1月9日 下午3:34:19 
	 */
	@RequestMapping("/selectExtFilesList")
	@ResponseBody
	public BhResult getExtFilesList(@RequestBody Map<String, String> map,HttpServletRequest request) {
		BhResult result = null;
		try {
			String currentPage = map.get("currentPage");// 当前第几页
			String pageSize = map.get("pageSize");// 每页显示条数
			String name=map.get("name");
			
			//默认当前页:1
			if (StringUtils.isBlank(currentPage)) {
				currentPage = Contants.PAGE+"";
			}
			//默认每页的大小:10
			if (StringUtils.isBlank(pageSize)) {
				pageSize = Contants.PAGE_SIZE+"";
			}
			//获得商家的id(shopId)
			String token = request.getHeader("token");
		    JedisUtil jedisUtil= JedisUtil.getInstance();  
		    JedisUtil.Strings strings=jedisUtil.new Strings();
		    String userJson = strings.get(token);
		    Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
		    Integer shopId = 1;
		    if(sId!=null){
		    	shopId = (Integer)sId;
		    }
			PageBean<MyExtFiles> pageExtFiles = extFileService.getExtFilesList(currentPage,pageSize,shopId,name);
			result = new BhResult(BhResultEnum.SUCCESS, pageExtFiles);
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * @Description:删除某个文件
	 * @author xieyc
	 * @date 2018年1月10日 下午12:01:40 
	 */
	@RequestMapping("/deleteExtFiles")
	@ResponseBody
	public BhResult deleteExtFiles(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String id = map.get("id");
			Integer row = extFileService.deleteExtFiles(id);
			if (row == 0) {
				result = BhResult.build(400, "请求失败");
			} else if(row==1){
				result = BhResult.build(200, "删除成功", row);
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库删除失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	

	/**
	 * @Description: 将信息保存到图库里面
	 * @author 程凤云
	 * @date 2018年1月9日 下午3:34:19 
	 */
	@RequestMapping("/insertSkuImageStore")
	@ResponseBody
	public BhResult insertSkuImageStore(@RequestBody Map<String, Object> map,HttpServletRequest request) {
		BhResult result = null;
		try {
			//获得商家的id(shopId)
			String token = request.getHeader("token");
		    JedisUtil jedisUtil= JedisUtil.getInstance();  
		    JedisUtil.Strings strings=jedisUtil.new Strings();
		    String userJson = strings.get(token);
		    Map mapOne = JSON.parseObject(userJson, Map.class);
		    Object sId = mapOne.get("shopId");
		    Integer shopId = 1;
		    if(sId!=null){
		    	shopId = (Integer)sId;
		    }
		    
		    Object object=map.get("list");
		    if (object!=null && object!="") {
				String jsonStr=JsonUtils.objectToJson(object);
				List<ExtFiles> extFileList=JsonUtils.jsonToList(jsonStr, ExtFiles.class);
				if (extFileList.size()>0) {
					for (ExtFiles extFiles : extFileList) {
						extFiles.setAddTime(new Date());
						extFiles.setFileType(0);
						extFiles.setIsDel(0);
						extFiles.setShopId(shopId);
						extFileService.insertSkuImageStore(extFiles);
					}
				}
			}
			result = new BhResult(BhResultEnum.SUCCESS, null);
		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
		}
		return result;
	}
	
	
	/**
	 * @Description:修改某个文件
	 * @author xieyc
	 * @date 2018年1月10日 下午12:01:40 
	 */
	@RequestMapping("/updateExtFiles")
	@ResponseBody
	public BhResult updateExtFiles(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String id = map.get("id");
			String name = map.get("name");
			String ext=map.get("ext");
			String fileUrl=map.get("fileUrl");
			String size=map.get("size");
			String picSize=map.get("picSize");
			
			ExtFiles extFiles=new ExtFiles();
			if (StringUtils.isNotBlank(id)) {
				extFiles.setId(Integer.parseInt(id));
			}else{
				return BhResult.build(400, "参数id不能为空");
			}
			
			if (StringUtils.isNotBlank(name)) {
				extFiles.setName(name);
			}
			if (StringUtils.isNotBlank(ext)) {
				extFiles.setExt(ext);
			}
			if (StringUtils.isNotBlank(fileUrl)) {
				extFiles.setFileUrl(fileUrl);
			}
			if (StringUtils.isNotBlank(size)) {
				extFiles.setSize(size);
			}
			if (StringUtils.isNotBlank(picSize)) {
				extFiles.setPicSize(picSize);
			}
			
			Integer row = extFileService.updateExtFiles(extFiles);
			if (row == 0) {
				result = BhResult.build(400, "请求失败");
			} else if(row==1){
				result = BhResult.build(200, "修改成功", null);
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库删除失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * 图库列表
	 */
	@RequestMapping("/selectFilesList")
	@ResponseBody
	public BhResult selectFilesList(@RequestBody Map<String, String> map,HttpServletRequest request) {
		BhResult result = null;
		try {
			String currentPage = map.get("currentPage");// 当前第几页
			String pageSize = map.get("pageSize");// 每页显示条数
			String startTime=map.get("startTime");
			String endTime=map.get("endTime");
			String cateId=map.get("cateId");
			
			//默认当前页:1
			if (StringUtils.isBlank(currentPage)) {
				currentPage = Contants.PAGE+"";
			}
			//默认每页的大小:40
			if (StringUtils.isBlank(pageSize)) {
				pageSize = Contants.PAGE_PICSIZE+"";
			}
			//获得商家的id(shopId)
			String token = request.getHeader("token");
		    JedisUtil jedisUtil= JedisUtil.getInstance();  
		    JedisUtil.Strings strings=jedisUtil.new Strings();
		    String userJson = strings.get(token);
		    Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
		    Integer shopId = 1;
		    if(sId!=null){
		    	shopId = (Integer)sId;
		    }
			PageBean<MyExtFiles> pageExtFiles = extFileService.getFilesList(currentPage,pageSize,shopId,cateId,startTime,endTime);
			result = new BhResult(BhResultEnum.SUCCESS, pageExtFiles);
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * @Description:批量新增某些文件
	 * @author xieyc
	 * @date 2018年1月10日 下午12:01:40 
	 */
	@RequestMapping("/insertExtFiles")
	@ResponseBody
	public BhResult insertExtFiles(@RequestBody Map<String, String> map,HttpServletRequest request) {
		BhResult result = null;
		try {
			String token = request.getHeader("token");
		    JedisUtil jedisUtil= JedisUtil.getInstance();  
		    JedisUtil.Strings strings=jedisUtil.new Strings();
		    String userJson = strings.get(token);
		    Map mapOne = JSON.parseObject(userJson, Map.class);
			Object sId = mapOne.get("shopId");
		    Integer shopId = 1;
		    if(sId!=null){
		    	shopId = (Integer)sId;
		    }
			String cateId = map.get("cateId");
			String fileUrl = map.get("fileUrl");
			Integer row = extFileService.insertExtFiles(cateId,fileUrl,shopId);
			if (row == 0) {
				result = BhResult.build(400, "添加失败");
			} else if(row==1){
				result = BhResult.build(200, "添加成功", row);
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库添加失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
}
