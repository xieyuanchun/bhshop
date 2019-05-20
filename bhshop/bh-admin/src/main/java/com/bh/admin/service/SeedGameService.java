package com.bh.admin.service;


import java.util.List;

import com.bh.admin.pojo.user.BaseGameMsg;
import com.bh.admin.pojo.user.LandList;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.pojo.user.SeedGame;
import com.bh.admin.pojo.user.SeedParam;
import com.bh.admin.pojo.user.SimpleSeed;
import com.bh.result.BhResult;


public interface SeedGameService {
	Member selectMember(Integer mId) throws Exception;
	
	SeedGame selectMemberMsg(Member member) throws Exception;
	
	//收割
	BhResult shouge(SeedParam param) throws Exception;
	
	//仓库列表
	BhResult storeHouseList(SeedParam param) throws Exception;
	
	//获得游戏的基本信息
	BaseGameMsg  selectBaseGameMsg(Member member);
	//获得土地
	LandList  selectLandList(Member member);
	//获得植物列表
	List<SimpleSeed>  selectListSimpleSeed(Member Member);
	
	//给植物浇水的动作
	BhResult waterAction(SeedParam param) throws Exception;
	

}
