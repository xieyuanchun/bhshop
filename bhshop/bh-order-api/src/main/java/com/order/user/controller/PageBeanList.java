package com.order.user.controller;

import java.io.Serializable;

import com.bh.goods.pojo.GoodsCart;
import com.bh.goods.pojo.GoodsCartListShopIdList;
import com.bh.user.pojo.Member;
import com.bh.utils.PageBean;

public class PageBeanList<T> implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 7348218869931810818L;

	private PageBean<GoodsCartListShopIdList> list;
	
	private PageBean<GoodsCart> remove;
	
	private boolean isLogin;//2017-10-25添加该字段true 登录，false未登录


	public PageBean<GoodsCartListShopIdList> getList() {
		return list;
	}

	public void setList(PageBean<GoodsCartListShopIdList> list) {
		this.list = list;
	}

	public PageBean<GoodsCart> getRemove() {
		return remove;
	}

	public void setRemove(PageBean<GoodsCart> remove) {
		this.remove = remove;
	}

	public boolean isLogin() {
		return isLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isLogin = isLogin;
	}

	
	
	
	
	
}

