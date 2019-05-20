package com.bh.product.api.controller;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.RobotHead;
import com.bh.product.api.service.RobotHeadService;
import com.bh.result.BhResult;
import com.bh.user.pojo.PromoteUser;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;


@Controller
@RequestMapping("/robot")
public class RobotHeadController {
	
	@Autowired
	private RobotHeadService robotHeadService;
	
	/**
	 * 保存机器人头像
	 * XIEYC
	 */
	@RequestMapping("/sava")
	@ResponseBody
	public BhResult sava( @RequestBody Map<String, String> map){
		BhResult result = null;
		try {
			String imgs=map.get("imgs");
			int row=robotHeadService.save(imgs);
			if(row==1){
				result = new BhResult(200, "成功",row);
			}else{
				result = BhResult.build(400, "操作失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "操作失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	/**
	 * @Description: 机器人头像更新
	 * @author xieyc
	 * @date 2018年3月21日 下午6:00:13 
	 */
	@ResponseBody
	@RequestMapping("/update")
	public BhResult update(@RequestBody RobotHead robotHead) {
		BhResult r = null;
		try {
			int row = robotHeadService.update(robotHead);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(row);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}

	}
	/**
	 * @Description: 机器人头像列表
	 * @author xieyc
	 * @date 2018年3月21日 下午6:09:52 
	 */
	@ResponseBody
	@RequestMapping("/listPage")
	public BhResult listPage(@RequestBody RobotHead robotHead) {
		BhResult r = null;
		try {
			PageBean<RobotHead> page = robotHeadService.listPage(robotHead);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return r;
		}
	}
	/**
	 * @Description: 机器人头像删除（批量操作）
	 * @author xieyc
	 * @date 2018年3月21日 下午6:05:19 
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public BhResult delete(@RequestBody Map<String, String> map) { 
		BhResult r = null;
		String ids=map.get("ids");
		try {
			int row =robotHeadService.delete(ids);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(row);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}
	}
	
	
	
	/**
	 * @Description: 机器人信息列表
	 * @author xieyc
	 * @date 2018年3月21日 下午6:09:52 
	 */
	@ResponseBody
	@RequestMapping("/list")
	public BhResult list(@RequestBody PromoteUser promoteUser) {
		BhResult r = null;
		try {
			PageBean<PromoteUser> page = robotHeadService.list(promoteUser);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(page);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			// TODO: handle exception
			return r;
		}
	}
	
	
	/**
	 * @Description: 机器人昵好更新
	 * @author xieyc
	 * @date 2018年3月21日 下午6:00:13 
	 */
	@ResponseBody
	@RequestMapping("/updateName")
	public BhResult updateName(@RequestBody PromoteUser promoteUser) {
		BhResult r = null;
		try {
			int row = robotHeadService.updateName(promoteUser);
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(row);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
		}

	}
	
	
}
