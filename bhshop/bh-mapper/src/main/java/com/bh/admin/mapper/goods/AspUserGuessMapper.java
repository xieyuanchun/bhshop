package com.bh.admin.mapper.goods;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.bh.admin.pojo.goods.AspUserGuess;
import com.bh.utils.PageBean;

public interface AspUserGuessMapper {
    int deleteByPrimaryKey(Integer mId);

    int insert(AspUserGuess record);

    int insertSelective(AspUserGuess record);

    AspUserGuess selectByPrimaryKey(Integer mId);

    int updateByPrimaryKeySelective(AspUserGuess record);

    int updateByPrimaryKey(AspUserGuess record);
    

    List<AspUserGuess> selectAll(AspUserGuess aspUserGuess);
    
    List<AspUserGuess> selectAll1(AspUserGuess aspUserGuess);
}

