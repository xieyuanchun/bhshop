package com.order.user.service;

import java.util.Map;

public interface UtilService {
     String getTicket();
     Map<String, String> getWxSign(String url);
}
