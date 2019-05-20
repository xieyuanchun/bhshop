package com.bh.product.api.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.MemberUserAccessLog;
import com.bh.product.api.service.GoodsFavoriteService;
import com.bh.product.api.service.MemberUserAccessLogService;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
import com.bh.utils.LoggerUtil;
import com.bh.utils.PageBean;
import com.mysql.jdbc.StringUtils;


@Controller
@RequestMapping("/favorite")
public class GoodsFavoriteController {
	
	@Autowired
	private GoodsFavoriteService service;
	
	/**
	 * SCJ-20171023-01
	 * 新增用户收藏
	 * @param map
	 * @return
	 */
	@RequestMapping("/addFavorite")
	@ResponseBody
	public BhResult addFavorite(@RequestBody Map<String, String> map ,HttpServletRequest request){
		BhResult result = null;
		try {
			String goodsId = map.get("skuId"); //商品skuid
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			int mId = 1;
			if(member!=null){
				mId = member.getId();
				int row = service.addFavorite(goodsId, mId);
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
	 * SCJ-20171024-01
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
			String goodsId = map.get("goodsId");
			Member member = (Member) request.getSession(false).getAttribute(Contants.USER_INFO_ATTR_KEY);
			int mId = 1;
			if(member!=null){
				mId = member.getId();
				int row = service.deleteFavorite(goodsId, mId);
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
	 * SCJ-20171024-03
	 * 判断商品是否被用户收藏
	 * @param map
	 * @param request
	 * @return
	 */
	@RequestMapping("/isFavorite")
	@ResponseBody
	public BhResult isFavorite(@RequestBody Map<String, String> map ,HttpServletRequest request){
		BhResult result = null;
		try {
			String goodsId = map.get("goodsId");
			HttpSession session = request.getSession(false);
		    Member member = null;
			if(session!=null) {
				Object obj = session.getAttribute(Contants.USER_INFO_ATTR_KEY);
				if(obj!=null){
					member = (Member)obj; //获取当前登录用户信息
				}
			}			
			int row = service.isFavorite(goodsId, member);
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
