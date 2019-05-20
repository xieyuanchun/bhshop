package com.bh.admin.controller.goods;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bh.utils.WxJsApiUtil;


@Controller
@RequestMapping("/util")
public class UtilController {
	@RequestMapping(value = "/getWxJsSign")
	@ResponseBody
	public Map<String, String> getWxJsSign(String url, HttpServletRequest request,
			HttpServletResponse response) {
		return WxJsApiUtil.sign(url);
	}
	
	
}
