package com.bh.admin.mapper.user;

import java.util.Date;
import java.util.List;

import com.bh.admin.pojo.user.MemerScoreLog;


public interface MemerScoreLogMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(MemerScoreLog record);

    int insertSelective(MemerScoreLog record);

    MemerScoreLog selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(MemerScoreLog record);

    int updateByPrimaryKey(MemerScoreLog record);
    
    
    
    /*********cheng*********/
    List<MemerScoreLog> selectLogByParams(MemerScoreLog log);
    
    int selectScoreByParams(MemerScoreLog log);
    int deleteScoreByParams(MemerScoreLog rule);
    List<MemerScoreLog> selectLogByUserattends(MemerScoreLog log);

	int getLogByMidAndTime(Integer getmId, Date changeDay);
}