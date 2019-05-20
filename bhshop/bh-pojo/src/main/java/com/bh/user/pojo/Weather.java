package com.bh.user.pojo;

import java.util.List;

public class Weather {
	private String date;
	
	private String message;
	
	private Forecast data;
	

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Forecast getData() {
		return data;
	}

	public void setData(Forecast data) {
		this.data = data;
	}

	
	
	
}
