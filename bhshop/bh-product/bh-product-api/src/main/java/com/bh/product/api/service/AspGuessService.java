package com.bh.product.api.service;

import java.util.Map;

import com.bh.goods.pojo.AspUserGuess;
import com.bh.user.pojo.Member;

public interface AspGuessService {

	int predictSubmit1(AspUserGuess entity);

	int loadInsert(Integer id);
	
	Map<String,Object> selectByMember(Member member);
	
	//AspUserGuess getMemberInfo(Member member);

	int withdraw(AspUserGuess entity);

	int predictSubmit(AspUserGuess entity);

	int update(Integer guessOne, Integer guessTwo);
	
	
	
}
