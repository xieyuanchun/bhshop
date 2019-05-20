package com.bh.product.api.service;

import java.util.List;

import com.bh.user.vo.NavigationVo;

public interface NavigationService {
	public List<NavigationVo> getNavigationMsg(Integer usingObject);
}
