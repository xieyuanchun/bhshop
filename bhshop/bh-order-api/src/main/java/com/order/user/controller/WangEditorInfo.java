package com.order.user.controller;

import java.io.Serializable;
import java.util.ArrayList;

public class WangEditorInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	private int errno;
	private ArrayList<String> data;
	public int getErrno() {
		return errno;
	}
	public void setErrno(int errno) {
		this.errno = errno;
	}
	public ArrayList<String> getData() {
		return data;
	}
	public void setData(ArrayList<String> data) {
		this.data = data;
	}
	

    
}
