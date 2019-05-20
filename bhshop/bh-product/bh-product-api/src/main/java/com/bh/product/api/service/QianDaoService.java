package com.bh.product.api.service;

import java.util.List;

import com.bh.goods.pojo.OneWeek;
import com.bh.goods.pojo.UserAttendance;
import com.bh.user.pojo.MemerScoreLog;

public interface QianDaoService {
	public UserAttendance getAtt(MemerScoreLog log);
	
	public List<OneWeek> selectOneWeek(MemerScoreLog log);
}
