package com.bh.admin.service.impl;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bh.admin.mapper.user.ScoreRuleExtMapper;
import com.bh.admin.mapper.user.SeedScoreRuleMapper;
import com.bh.admin.pojo.user.ScoreRuleExt;
import com.bh.admin.pojo.user.SeedScoreRule;
import com.bh.admin.service.SeedScoreRuleService;
import com.bh.enums.BhResultEnum;
import com.bh.result.BhResult;

@Service
public class SeedScoreRuleServiceImpl implements SeedScoreRuleService {

	@Autowired
	private SeedScoreRuleMapper seedScoreRuleMapper;
	@Autowired
	private ScoreRuleExtMapper scoreRuleExtMapper;

	// 查询积分分类
	public BhResult selectList(SeedScoreRule rule) throws Exception {
		BhResult bhResult = null;
		SeedScoreRule rule2 = new SeedScoreRule();
		rule2 = seedScoreRuleMapper.selectByPrimaryKey(rule.getId());
		if (rule2 == null) {
			for(int i=1;i<=7;i++){
				SeedScoreRule rule3 = new SeedScoreRule();
				rule3.setId(i);
				rule3.setScoreAction(i);
				rule3.setScore(0);
				rule3.setStatus(0);
				switch (rule3.getId()) {
				case 1:
					rule3.setName("签到积分");
					ScoreRuleExt ext = new ScoreRuleExt();
					ext.setSrId(1);
					ext.setExtKey(1);
					ext.setExtValue(1);
					ext.setIsDel(0);
					ext.setIsSeries(0);
					scoreRuleExtMapper.insertSelective(ext);
					break;
				case 2:
					rule3.setName("浇水");
					break;
				case 3:
					rule3.setName("拼单");
				    break;
				case 4:
					rule3.setName("单买");
					break;
				case 5:
					rule3.setName("分享积分");
					break;
				case 6:
					rule3.setName("注册积分");
					break;
				case 7:
					rule3.setName("购物积分");
					break;
				default:
					rule3.setName("");
					break;
				}
				SeedScoreRule r = seedScoreRuleMapper.selectByPrimaryKey(i);
				if (r == null) {
					seedScoreRuleMapper.insertSelective(rule3);
				}else {
					seedScoreRuleMapper.updateByPrimaryKeySelective(rule3);
				}
			}
			rule2 = seedScoreRuleMapper.selectByPrimaryKey(rule.getId());
		}
		
		if (rule.getId().equals(1)) {
			ScoreRuleExt ext = new ScoreRuleExt();
			ext.setSrId(rule.getId());
			List<ScoreRuleExt> list = scoreRuleExtMapper.selectScoreRuleExtBysrId(ext);
			rule2.setList(list);
		} 
		
		bhResult = new BhResult(200, "查询成功", rule2);

		return bhResult;
	}

	// 更新分数
	public BhResult updateScore(SeedScoreRule rule) throws Exception {
		BhResult bhResult = null;

		if (rule.getStatus() == null) {
			bhResult = new BhResult(400, "参数status不能为空", null);
		} else if (rule.getScore() == null) {
			bhResult = new BhResult(400, "参数score不能为空", null);
		} else {
			if (rule.getId().equals(1)) {
				if (rule.getList() != null) {
					scoreRuleExtMapper.updateExtByIsDel();
					// 如果有多条扩展规则传过来
					List<ScoreRuleExt> extList = rule.getList();
					// 如果不超过11条，则继续下一步
					if (extList.size() < 11) {
						for (int i = 0; i < extList.size(); i++) {
							if (extList.get(i).getIsSeries() == null) {
								extList.get(i).setIsSeries(0);
							}
							ScoreRuleExt ext1 = new ScoreRuleExt();
							ext1.setExtKey(extList.get(i).getExtKey());
							ext1.setSrId(rule.getId());
							// 如果该积分已存在并且有分数
							List<ScoreRuleExt> ext = scoreRuleExtMapper.selectScoreRuleExtBysrId(ext1);
							if (ext.size() > 0) {
								if (ext.get(0).getExtValue() < extList.get(i).getExtValue()) {
									ScoreRuleExt e = new ScoreRuleExt();
									e.setId(ext.get(0).getId());
									e.setExtValue(extList.get(i).getExtValue());
									e.setIsSeries(extList.get(i).getIsSeries());
									scoreRuleExtMapper.updateScoreRuleExtByValue(e);
								}
							} // 如果该积分不存在
							else {
								ext1.setIsDel(0);
								ext1.setExtValue(extList.get(i).getExtValue());
								ext1.setIsSeries(extList.get(i).getIsSeries());
								scoreRuleExtMapper.insertSelective(ext1);
							}
							
							if (extList.get(i).getIsSeries().equals(1)) {
								//是否N天连续以上0否，1是,如果是1的话则不插入的数据库了，废弃
								//break用于完全结束一个循环，跳出循环体执行循环后面的语句。continue是跳过当次循环中剩下的语句，执行下一次循环。
								break;
							}
						}
						seedScoreRuleMapper.updateByPrimaryKeySelective(rule);

						bhResult = new BhResult(200, "设置成功", null);
					} else {
						// 否则给响应的提示：最多可以添加10条扩展规则
						bhResult = new BhResult(400, "最多可以添加10条扩展规则", null);
					}
				} else {
					scoreRuleExtMapper.updateExtByIsDel();
					seedScoreRuleMapper.updateByPrimaryKeySelective(rule);
					bhResult = new BhResult(200, "设置成功", null);
				}
			} 
			seedScoreRuleMapper.updateByPrimaryKeySelective(rule);
			bhResult = new BhResult(200, "设置成功", null);
			
		}

		return bhResult;
	}
	
	
	//添加积分规则
	public	BhResult add(SeedScoreRule rule) throws Exception{
		BhResult bhResult = null;
		int row = seedScoreRuleMapper.insertSelective(rule);
		bhResult = new BhResult(BhResultEnum.SUCCESS, null);
		return bhResult;
    }
}
