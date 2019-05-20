package com.bh.admin.controller.goods;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.admin.pojo.goods.MemberUserAccessLog;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.service.MemberUserAccessLogService;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;


@Controller
@RequestMapping("/accessLog")
public class MemberUserAccessLogController {
	
	@Autowired
	private MemberUserAccessLogService service;
	
	/**
	 * SCJ-20171017-01
	 * 移动、PC获取用户最近浏览商品记录
	 * @param map
	 * @return
	 */
	@RequestMapping("/getPageBymId")
	@ResponseBody
	public BhResult getPageBymId(@RequestBody Map<String, String> map ,HttpServletRequest request){
		BhResult result = null;
		try {
			String currentPage = map.get("currentPage");
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY);
			
			PageBean<MemberUserAccessLog> list = service.getPageBymId(member, currentPage, Contants.PAGE_SIZE);
			if(list != null){
				result = new BhResult(BhResultEnum.GAIN_SUCCESS, list);
			}else{
				result = new BhResult(BhResultEnum.GAIN_FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
}
