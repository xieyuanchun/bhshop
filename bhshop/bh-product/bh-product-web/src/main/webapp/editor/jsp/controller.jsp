<%@ page language="java" contentType="text/html; charset=UTF-8"
	import="com.baidu.ueditor.ActionEnter"
    pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
<%

    request.setCharacterEncoding( "utf-8" );
	response.setHeader("Content-Type" , "text/html");
	
	String rootPath = application.getRealPath( "/" );
	System.out.println("rootPath--->"+rootPath);
	String str = new ActionEnter( request, rootPath ).exec();
	System.out.println("str--->"+str);
	out.write(str);
	
%>