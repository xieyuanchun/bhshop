package com.bh.admin.service.impl;

import org.springframework.stereotype.Service;

import com.bh.admin.service.TestService;

@Service
public class TestServiceImpl implements TestService {

	@Override
	public void myTest() {
		System.out.println("###TestServiceImpl####");
		
	}

}
