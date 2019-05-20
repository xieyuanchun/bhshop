package com.bh.admin.controller.order;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bh.admin.pojo.order.ExtFileCate;
import com.bh.admin.service.ExtFileCateService;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;



/**
 * @Description: 后台管理系统-扩展-多媒体分类
 * @author xieyc
 * @date 2018年1月9日 下午3:31:25
 */
@Controller
@RequestMapping("/extFileCate")
public class ExtFileCateController {
	@Autowired
	private ExtFileCateService extFileCateService;

	@Value(value = "${pageSize}")
	private String pageSize;

	/**
	 * @Description: 分页查询所有文件分类
	 * @author xieyc
	 * @date 2018年1月9日 下午5:02:39
	 */
	@RequestMapping("/getExtFileCateList")
	@ResponseBody
	public BhResult getExtFileCateList() {
		BhResult result = null;
		try {
			Integer pageSize=20;
			Integer currentPage=1;
			PageBean<ExtFileCate> pageExtFileCate = extFileCateService.getExtFileCateList(currentPage,pageSize);

			if (pageExtFileCate != null) {
				result = new BhResult(BhResultEnum.SUCCESS, pageExtFileCate);
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
	 * 
	 * @Description: 添加文件分类
	 * @author xieyc
	 * @date 2018年1月9日 下午5:41:39
	 */
	@RequestMapping("/addExtFileCate")
	@ResponseBody
	public BhResult addExtFileCate(@RequestBody ExtFileCate extFileCate) {
		BhResult result = null;
		try {
			int row = extFileCateService.addExtFileCate(extFileCate);
			if (row == 1) {
				result = new BhResult(BhResultEnum.SUCCESS, row);
			} else {
				result = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

	/**
	 * @Description: 更新某个文件分类
	 * @author xieyc
	 * @date 2018年1月9日 下午5:41:39
	 */
	@RequestMapping("/updateExtFileCate")
	@ResponseBody
	public BhResult updateExtFileCate(@RequestBody ExtFileCate extFileCate) {
		BhResult result = null;
		try {
			int row = extFileCateService.updateExtFileCate(extFileCate);
			if (row == 1) {
				result = new BhResult(BhResultEnum.SUCCESS, row);
			} else {
				result = new BhResult(BhResultEnum.FAIL, null);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			
		}
		return result;
	}

	/**
	 * @Description:删除某个文件分类
	 * @author xieyc
	 * @date 2018年1月9日 下午5:22:27
	 */
	@RequestMapping("/deleteExtFileCate")
	@ResponseBody
	public BhResult deleteExtFileCate(@RequestBody Map<String, String> map) {
		BhResult result = null;
		try {
			String id = map.get("id");
			Integer row = extFileCateService.deleteExtFileCate(id);
			if (row == 0) {
				result = BhResult.build(400, "请求失败");
			} else if (row == -1) {
				result = BhResult.build(400, "分类名称被引用,不能删除");
			} else if(row==1){
				result = BhResult.build(200, "删除成功", row);
			}
		} catch (Exception e) {
			result = BhResult.build(500, "数据库删除失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}

}
