package com.order.extend.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bh.enums.BhResultEnum;
import com.bh.order.pojo.ExtFiles;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.mysql.jdbc.StringUtils;
import com.order.extend.service.ExtFilesService;

/**
 * @Description: 后台管理系统-扩展-图片管理
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
	@RequestMapping("/getExtFilesList")
	@ResponseBody
	public BhResult getExtFilesList(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String currentPage = map.get("currentPage");// 当前第几页
			String pageSize = map.get("pageSize");// 每页显示条数
			String file_type=map.get("file_type");
			if (StringUtils.isEmptyOrWhitespaceOnly(currentPage)) {
				currentPage = "1";
			}
			if (StringUtils.isEmptyOrWhitespaceOnly(pageSize)) {
				pageSize = "10";
			}
			PageBean<ExtFiles> pageExtFiles = extFileService.getExtFilesList(currentPage,pageSize,file_type);

			if (pageExtFiles != null) {
				result = new BhResult(BhResultEnum.SUCCESS, pageExtFiles);
			} else {
				result = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
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
	 * @Description: 分页查询某个分类下的所有文件
	 * @author xieyc
	 * @date 2018年1月9日 下午3:34:19 
	 */
	@RequestMapping("/getExtFilesByCateId")
	@ResponseBody
	public BhResult getExtFilesByCateId(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String cate_id=map.get("currentPage");//文件分类id
			String currentPage = map.get("currentPage");// 当前第几页
			String pageSize = map.get("pageSize");// 每页显示条数
			if (StringUtils.isEmptyOrWhitespaceOnly(currentPage)) {
				currentPage = "1";
			}
			if (StringUtils.isEmptyOrWhitespaceOnly(pageSize)) {
				pageSize = "10";
			}
			PageBean<ExtFiles> pageExtFiles = extFileService.getExtFilesByCateId(cate_id,currentPage,pageSize);

			if (pageExtFiles != null) {
				result = new BhResult(BhResultEnum.SUCCESS, pageExtFiles);
			} else {
				result = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
}
