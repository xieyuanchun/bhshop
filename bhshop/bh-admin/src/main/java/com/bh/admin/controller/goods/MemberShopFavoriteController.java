package com.bh.admin.controller.goods;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.admin.pojo.user.Member;
import com.bh.admin.service.MemberShopFavoriteService;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.mysql.jdbc.StringUtils;


@Controller
@RequestMapping("/shopFavorite")
public class MemberShopFavoriteController {
	
	@Autowired
	private MemberShopFavoriteService service;
	
	/**
	 * SCJ-20171120-04
	 * 新增店铺收藏
	 * @param map
	 * @return
	 */
	@RequestMapping("/addFavorite")
	@ResponseBody
	public BhResult addFavorite(@RequestBody Map<String, String> map ,HttpServletRequest request){
		BhResult result = null;
		try {
			String shopId = map.get("shopId"); //店铺id
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY);
			int mId = 1;
			if(member!=null){
				mId = member.getId();
				int row = service.addFavorite(shopId, mId);
				if(row ==0 ){
					result = new BhResult(BhResultEnum.FAIL, null);
				}else{
					result = new BhResult(BhResultEnum.SUCCESS, null);
				}
			}else{
				result = new BhResult(BhResultEnum.LOGIN_FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * SCJ-20171120-05
	 * 取消收藏
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/deleteFavorite")
	@ResponseBody
	public BhResult deleteFavorite(@RequestBody Map<String, String> map ,HttpServletRequest request){
		BhResult result = null;
		try {
			String shopId = map.get("shopId");
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY);
			int mId = 1;
			if(member!=null){
				mId = member.getId();
				int row = service.deleteFavorite(shopId, mId);
				if(row ==0 ){
					result = new BhResult(BhResultEnum.FAIL, null);
				}else{
					result = new BhResult(BhResultEnum.SUCCESS, null);
				}
			}else{
				result = new BhResult(BhResultEnum.LOGIN_FAIL, null);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	
	/**
	 * SCJ-20171120-06
	 * 判断店铺是否被用户收藏
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/isFavorite")
	@ResponseBody
	public BhResult isFavorite(@RequestBody Map<String, String> map ,HttpServletRequest request){
		BhResult result = null;
		try {
			String shopId = map.get("shopId");
			Member member = (Member) request.getSession().getAttribute(Contants.USER_INFO_ATTR_KEY);
			int row = service.isFavorite(shopId, member);
			if(row == 1){
				result = new BhResult(BhResultEnum.GAIN_SUCCESS, true);
			}else if(row == 100){
				result = new BhResult(BhResultEnum.LOGIN_FAIL, false);
			}else{
				result = new BhResult(BhResultEnum.LOGIN_GAIN_FAIL, false);
			}
		} catch (Exception e) {
			result = new BhResult(BhResultEnum.VISIT_FAIL, null);
			LoggerUtil.getLogger().error(e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
}
