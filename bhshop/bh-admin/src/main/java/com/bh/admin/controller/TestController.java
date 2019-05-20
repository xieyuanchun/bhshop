package com.bh.admin.controller;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.admin.service.TestService;
import com.bh.result.BhResult;
@Controller
@RequestMapping("/test")
public class TestController {
	@Autowired
	private TestService testService;
	@RequestMapping("/myTest")
	@ResponseBody
	public BhResult myTest(){
		System.out.println("######TestController######");
		testService.myTest();
		return null;
	}
	
}
