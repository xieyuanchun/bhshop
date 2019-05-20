package com.bh.admin.pojo.user;

import java.util.List;

public class SeedScoreRule {
	/*******种子积分规则*********/
    private Integer id;

    private String name;//规则名称

    private Integer scoreAction;//积分动作 1，签到 2,浇水 3,拼单 4单买 5,分享,6注册积分，7购物积分

    private Integer score;//积分
    
    private Integer config;//2018-01-15 配置

    private Integer status;//2018-01-15 状态 0关 1开
    
    private Integer shopId;//
    private List<ScoreRuleExt> list;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getScoreAction() {
        return scoreAction;
    }

    public void setScoreAction(Integer scoreAction) {
        this.scoreAction = scoreAction;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

	public Integer getConfig() {
		return config;
	}

	public void setConfig(Integer config) {
		this.config = config;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<ScoreRuleExt> getList() {
		return list;
	}

	public void setList(List<ScoreRuleExt> list) {
		this.list = list;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
	
    
    
}