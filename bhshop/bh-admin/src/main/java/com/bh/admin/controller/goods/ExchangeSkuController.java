package com.bh.admin.controller.goods;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.admin.pojo.goods.ExchangeGroup;
import com.bh.admin.pojo.goods.ExchangeSku;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.service.ExchangeSkuService;
import com.bh.result.BhResult;

@Controller
@RequestMapping("/exchangeSku")
public class ExchangeSkuController {

	@Autowired
	private ExchangeSkuService service;
	
	
	@RequestMapping("/add")
	@ResponseBody
	public BhResult add(@RequestBody ExchangeSku es) {
		
		BhResult b = null;
		try {
			GoodsSku g = new GoodsSku();
		    g.setJdSkuNo(es.getSkuId());
		    List<GoodsSku> list = service.getByjdsku(g);
		    if(list.size()>0) {
		    	service.add(es);
			    b = new BhResult(200,"添加成功",null);
		    }else {
		    	b = new BhResult(400,"添加失败,当前商品可能没上架或者没摘取",null);
		    }
			
		}catch(Exception e) {
			e.printStackTrace();
			b = new BhResult(400,"添加失败",null);
		}
		
		return b;			
	}
	
	
	@RequestMapping("/update")
	@ResponseBody
	public BhResult update(@RequestBody ExchangeSku es) {
		BhResult b = null;
		try {
			GoodsSku g = new GoodsSku();
		    g.setJdSkuNo(es.getSkuId());
		    List<GoodsSku> list = service.getByjdsku(g);
		    if(list.size()>0) {
		         service.update(es);
		         b = new BhResult(200,"修改成功",null);
		    }else{
		    	 b = new BhResult(400,"修改失败,当前商品可能没上架或者没摘取",null);
		    }
		}catch(Exception e) {
			e.printStackTrace();
			b = new BhResult(400,"修改失败",null);
		}
		return b; 		
	}
	
	
	
	@RequestMapping("/delete")
	@ResponseBody
	public BhResult delete(@RequestBody ExchangeSku es) {
		BhResult b = null;
		try {
			
			 service.delete(es);
			 b = new BhResult(200,"删除成功",null);
		}catch(Exception e) {
			e.printStackTrace();
			b = new BhResult(400,"删除失败",null);
		}
		
		return b;		
	}
	
	
	
	@RequestMapping("/listPage")
	@ResponseBody
	public BhResult listPage(@RequestBody ExchangeSku es) {
		BhResult  b = null;
		  
		try{
	
			Map<String,Object> map = service.listPage(es);
			b =  new BhResult(200,"获取成功",map);
			
		}catch(Exception e){
			e.printStackTrace();
			b =  new BhResult(400,"获取失败",null);
		}
		return b;		
	}
	
}
