package com.bh.user.pojo;

import java.util.List;

public class LandList {
	//可用的土地=免费的+购买的
	private List<SimpleLand> usable;
	
	//不可以用的土地(未购买的)
	private List<SimpleLand> Unavailable;

	public List<SimpleLand> getUsable() {
		return usable;
	}

	public void setUsable(List<SimpleLand> usable) {
		this.usable = usable;
	}

	public List<SimpleLand> getUnavailable() {
		return Unavailable;
	}

	public void setUnavailable(List<SimpleLand> unavailable) {
		Unavailable = unavailable;
	}
	
	
}
