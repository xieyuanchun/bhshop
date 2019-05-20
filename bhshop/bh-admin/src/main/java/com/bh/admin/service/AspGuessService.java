package com.bh.admin.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bh.admin.pojo.goods.AspUserGuess;
import com.bh.utils.PageBean;

public interface AspGuessService {
	
	PageBean<AspUserGuess> getWinUser(AspUserGuess aspUserGuess);
	
	PageBean<AspUserGuess> getWin(AspUserGuess aspUserGuess);
    
	int updateStatus(AspUserGuess aspUserGuess,Integer userId);

	void excelExport(AspUserGuess aspUserGuess,HttpServletRequest request, HttpServletResponse response) throws Exception;
}
