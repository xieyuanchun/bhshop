package com.bh.admin.controller.goods;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bh.admin.pojo.goods.ExchangeGroup;
import com.bh.admin.service.ExchangeGroupService;
import com.bh.result.BhResult;


@Controller
@RequestMapping("/exchangeGroup")
public class ExchangeGroupController {

	@Autowired
	private ExchangeGroupService service;
	
	@RequestMapping("/add")
	@ResponseBody
	public BhResult add(@RequestBody ExchangeGroup eg) {
		BhResult b = null;
		int row=0;
		try {
			
			row=service.add(eg);
			if(row==1) {
				b = new BhResult(200,"添加成功",null);
			}else if(row==999) {
				b = new BhResult(400,"名称已存在",null);
			}
		}catch(Exception e) {
			e.printStackTrace();
			b = new BhResult(400,"添加失败",null);
		}
		
		return b;		
	}
	
	
	@RequestMapping("/update")
	@ResponseBody
	public BhResult update(@RequestBody ExchangeGroup eg) {
		BhResult b = null;
		try {
			
		    service.update(eg);
		    b = new BhResult(200,"修改成功",null);
		}catch(Exception e) {
			e.printStackTrace();
			b = new BhResult(400,"修改失败",null);
		}
		return b; 		
	}
	
	
	
	@RequestMapping("/delete")
	@ResponseBody
	public BhResult delete(@RequestBody ExchangeGroup eg) {
		
		BhResult b = null;
		try {
			
		    service.delete(eg);
		    b = new BhResult(200,"删除成功",null);
		}catch(Exception e) {
			e.printStackTrace();
			b = new BhResult(400,"删除失败",null);
		}
		return b; 		
	}
	
	
	
	@RequestMapping("/listPage")
	@ResponseBody
	public BhResult listPage(@RequestBody ExchangeGroup eg) {
		BhResult  b = null;
		  
		try{
	
			Map<String,Object> map = service.listPage(eg);
			b =  new BhResult(200,"获取成功",map);
			
		}catch(Exception e){
			e.printStackTrace();
			b =  new BhResult(400,"获取失败",null);
		}
		return b;		
	}
	
	//下拉框兑换分类信息
	@RequestMapping("/getAll")
	@ResponseBody
	public BhResult getAll(@RequestBody ExchangeGroup eg) {
		BhResult  b = null;
		  
		try{
	
			List<ExchangeGroup> list= service.list();
			b =  new BhResult(200,"获取成功",list);
			
		}catch(Exception e){
			e.printStackTrace();
			b =  new BhResult(400,"获取失败",null);
		}
		return b;		
	}
	
	
}
