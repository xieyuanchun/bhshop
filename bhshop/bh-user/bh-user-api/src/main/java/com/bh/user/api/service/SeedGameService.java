package com.bh.user.api.service;


import java.util.List;


import com.bh.result.BhResult;
import com.bh.user.pojo.BaseGameMsg;
import com.bh.user.pojo.LandList;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.SeedGame;
import com.bh.user.pojo.SeedParam;
import com.bh.user.pojo.SimpleSeed;

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
