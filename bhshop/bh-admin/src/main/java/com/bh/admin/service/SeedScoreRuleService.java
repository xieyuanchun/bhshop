package com.bh.admin.service;

import com.bh.admin.pojo.user.SeedScoreRule;
import com.bh.result.BhResult;

public interface SeedScoreRuleService {
	//查询积分分类
	BhResult selectList(SeedScoreRule rule ) throws Exception;
	//更新分数
	BhResult updateScore(SeedScoreRule rule) throws Exception;
	//添加积分规则
	BhResult add(SeedScoreRule rule) throws Exception;
}
