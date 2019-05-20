package com.bh.user.api.service;

import java.util.List;
import com.bh.goods.pojo.CouponPojo;

public interface CouponGiftService {

	List<CouponPojo> showCouponGift(Integer id);



	int changeLogStatus(String string);



	boolean isFirstLogin(Integer id);



	boolean isGetGift(Integer id);


}
