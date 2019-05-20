package com.bh.user.api.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.bh.result.BhResult;
import com.bh.user.pojo.Member;

public interface MemberService {
	
	int insertSelective(Member member,HttpServletRequest request,HttpServletResponse response) throws Exception;
	
	int insertUser(String username,String password) throws Exception;
	
	//检查用户名是否重复
	int checkuser(Member member) throws Exception;
	
	//登录
	BhResult login(Member member,HttpServletResponse response,HttpServletRequest request) throws Exception;
	
	//通过token查询用户的cookie是否过去
	//BhResult getUserByToken() throws Exception;
	
	//找回密码
	int updatepwd(Member member) throws Exception;
	
	//用户升级成配送员或者商家
	int updateByType(Member member) throws Exception;
	

	//通过id查找对象
	Member selectById(Member member) throws Exception;
	
	//更新
	Member updateByParams(Member member) throws Exception;
	
	//2017-9-28通过手机号查找用户
	 List<Member> selectMemberByPhone(Member member) throws Exception;
	 
	 int selectCountByPhone(Member member) throws Exception;
	 
			
}
