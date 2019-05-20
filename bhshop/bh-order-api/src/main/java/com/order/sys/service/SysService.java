package com.order.sys.service;
import com.bh.order.pojo.SysTeam;
public interface SysService {
	//创团
	boolean createTeam(SysTeam entity);
	//促团
	boolean promoteTeam(SysTeam entity);
	
	void promoteTeamByOrderNo(String orderNo);
	void promoteTeamByGoodsId(Integer goodsId);
}
