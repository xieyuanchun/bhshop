package com.bh.admin.controller.goods;

import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bh.admin.pojo.goods.TopicNav;
import com.bh.admin.service.TopicNavService;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.PageBean;

@RestController
@RequestMapping("/topicNav")
public class TopicNavController {
	@Autowired
	private TopicNavService topicNavService;
	
	/**
	 * @Description: 导航增加
	 * @author xieyc
	 * @date 2018年1月26日 下午5:52:38 
	 */
	@ResponseBody
	@RequestMapping("/add")
	public BhResult add(@RequestBody TopicNav entity,HttpServletRequest request) {
		BhResult r = null;
		try {
			int row = topicNavService.add(entity);
			r = new BhResult();
			r.setStatus(BhResultEnum.FAIL.getStatus());
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
	 * @Description: 导航更新
	 * @author xieyc
	 * @date 2018年1月26日 下午6:00:13 
	 */
	@ResponseBody
	@RequestMapping("/update")
	public BhResult update(@RequestBody TopicNav entity) {
		BhResult r = null;
		try {
			int row = topicNavService.update(entity);
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
	 * @Description: 导航获取
	 * @author xieyc
	 * @date 2018年1月26日 下午6:01:33 
	 */
	@ResponseBody
	@RequestMapping("/get")
	public BhResult get(@RequestBody TopicNav entity) {
		BhResult r = null;
		try {
			TopicNav e = topicNavService.get(entity.getId());
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(e);
			return r;
		} catch (Exception e) {
			e.printStackTrace();
			r = BhResult.build(500, "操作异常!");
			return r;
			// TODO: handle exception
		}

		
	}
	/**
	 * @Description: 导航列表
	 * @author xieyc
	 * @date 2018年1月26日 下午6:09:52 
	 */
	@ResponseBody
	@RequestMapping("/listPage")
	public BhResult listPage(@RequestBody TopicNav entity) {
		BhResult r = null;
		try {
			PageBean<TopicNav> page = topicNavService.listPage(entity);
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
	 * @Description: 导航删除
	 * @author xieyc
	 * @date 2018年1月26日 下午6:05:19 
	 */
	@ResponseBody
	@RequestMapping("/delete")
	public BhResult delete(@RequestBody TopicNav entity) { 
		BhResult r = null;
		try {
			int row =topicNavService.delete(entity.getId());
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
}
