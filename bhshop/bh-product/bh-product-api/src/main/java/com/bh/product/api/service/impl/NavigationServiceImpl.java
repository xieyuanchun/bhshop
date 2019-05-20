package com.bh.product.api.service.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.config.Contants;
import com.bh.goods.mapper.NavigationMapper;
import com.bh.product.api.service.NavigationService;
import com.bh.product.api.util.JedisUtil;
import com.bh.user.vo.NavigationVo;
import com.bh.utils.JsonUtils;

@Service
public class NavigationServiceImpl implements NavigationService{
	@Autowired
	private NavigationMapper navigationMapper;
		
	public List<NavigationVo> getNavigationMsg(Integer usingObject) {
		// 120分钟
		int sec = 10800;
		String key="";
		if (usingObject==1) {
			key = Contants.getRedisKey("navigation_key1");
		}else if (usingObject==2) {
			key = Contants.getRedisKey("navigation_key2");
		}else if (usingObject==3) {
			key = Contants.getRedisKey("navigation_key3");
		}else{
			key = Contants.getRedisKey("navigation_key_default");
		}
		JedisUtil jedisUtil= JedisUtil.getInstance();  
		JedisUtil.Strings strings=jedisUtil.new Strings();
		String obj = strings.get(key);
		if(StringUtils.isBlank(obj)){
			List<NavigationVo> list=navigationMapper.selectNavigationMsg(usingObject);
			String str=JsonUtils.objectToJson(list);
			if(StringUtils.isNotEmpty(str)) {
                strings.setEx(key, sec, str);
            }
			return list;
		}else{
			List<NavigationVo> voList=JsonUtils.jsonToList(obj, NavigationVo.class);
			return voList;
		}
		
	}
}
