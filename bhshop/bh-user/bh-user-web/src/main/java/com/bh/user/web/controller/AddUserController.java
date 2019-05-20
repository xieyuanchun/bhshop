package com.bh.user.web.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.user.web.service.AddUserService;
import com.bh.result.BhResult;

@Controller
public class AddUserController {

	@Autowired
	private AddUserService addUserService;

	@RequestMapping("/addUser")
	public String addUser() {
		return "addUser";
	}

	@RequestMapping(value = "/addUser1", method = RequestMethod.POST)
	@ResponseBody
	public BhResult addUser1(@RequestBody Map<String, String> map) {
		BhResult Result = addUserService.adduserid(map);
		return Result;
	}

}
