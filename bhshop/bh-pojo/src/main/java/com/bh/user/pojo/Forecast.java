package com.bh.user.pojo;

import java.util.List;

public class Forecast {
	private List<DayWeather> forecast;

	public List<DayWeather> getForecast() {
		return forecast;
	}

	public void setForecast(List<DayWeather> forecast) {
		this.forecast = forecast;
	}
	
}
