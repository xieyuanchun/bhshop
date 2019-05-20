package com.bh.goods.mapper;

import java.util.List;

import com.bh.goods.pojo.AspUserGuess;

public interface AspUserGuessMapper {
    int deleteByPrimaryKey(Integer mId);

    int insert(AspUserGuess record);

    int insertSelective(AspUserGuess record);

    AspUserGuess selectByPrimaryKey(Integer mId);

    int updateByPrimaryKeySelective(AspUserGuess record);

    int updateByPrimaryKey(AspUserGuess record);

	List<AspUserGuess> getListByGoldMedal(Integer guessOne, Integer guessTwo);

	List<AspUserGuess> getUpdateListByMedal(Integer guessOne, Integer guessTwo);

	List<AspUserGuess> getUpdateListByTwo(Integer guessOne, Integer guessTwo);
}