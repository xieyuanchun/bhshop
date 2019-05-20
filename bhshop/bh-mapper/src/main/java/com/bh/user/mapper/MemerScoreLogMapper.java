package com.bh.user.mapper;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.user.pojo.MemerScoreLog;
import com.bh.user.pojo.SeedScoreRule;

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
	
	List<MemerScoreLog> selectBeanList(@Param("mId")Integer mId,@Param("currentPage")Integer currentPage,@Param("pageSize")Integer pageSize,@Param("log")String log,@Param("time")String time);
	
	List<MemerScoreLog> selectScoreLog(@Param("mId")Integer mId,@Param("param")String param);
	List<MemerScoreLog> selectMemberLog(@Param("mId")Integer mId,@Param("times")Integer times);
	List<MemerScoreLog> selectScoreLogByLimit(@Param("mId")Integer mId,@Param("pageNum")Integer pageNum);
	int selectTime(@Param("mId")Integer mId);
}