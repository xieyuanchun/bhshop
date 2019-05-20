package com.bh.admin.controller.goods;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bh.admin.mapper.user.MemberMapper;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.pojo.user.WhiteMember;
import com.bh.admin.service.WhiteMemberService;
import com.bh.admin.util.JedisUtil;
import com.bh.admin.util.JedisUtil.Strings;
import com.bh.result.BhResult;

@Controller
@RequestMapping("/whiteMember")
public class WhiteMemberController {

	@Autowired 
	private MemberMapper memberMapper;
	
	@Autowired
	private WhiteMemberService service;
	
	@RequestMapping("/add")
	@ResponseBody
	public BhResult add(@RequestBody Map<String,Object> m) {
		BhResult b = null;
		try {
			if(StringUtils.isBlank(m.get("mId").toString())) {
				return b = new BhResult(400,"用户id不能为空",null);
			}
			Member member = memberMapper.selectByPrimaryKey(Integer.valueOf(m.get("mId").toString()));
			if(member!=null&&!member.equals("")&&member.getUsername()!=null) {				
				WhiteMember w = new WhiteMember();
			    w.setmId(Integer.valueOf(m.get("mId").toString()));
			    List<WhiteMember> list = service.getByMid(w);
			    if(list.size()>0) {
			    	return b = new BhResult(400,"当前的用户已经存在",null);
			    }
			    service.add(w);
		        b = new BhResult(200,"添加成功",null);
			}else {
				b = new BhResult(400,"添加失败，没有此用户id",null);
			}
		}catch(Exception e) {
			e.printStackTrace();
			b = new BhResult(400,"添加失败",null);
		}		
		return b;		
	}
	
	
	
	@RequestMapping("/update")
	@ResponseBody
	public BhResult update(@RequestBody WhiteMember w) {
		BhResult b = null;
		try {
			Member member = memberMapper.selectByPrimaryKey(w.getmId());
			if(member!=null&&!member.equals("")&&member.getUsername()!=null) {
               List<WhiteMember> list = service.getByMid(w);
			   if(list.size()>0) {
				    return b = new BhResult(400,"当前的用户已经存在",null);
			   }
			   service.update(w);
		       b = new BhResult(200,"修改成功",null);
			}else {
				b = new BhResult(400,"修改失败，没有此用户id",null);
			}
		}catch(Exception e) {
			e.printStackTrace();
			b = new BhResult(400,"修改失败",null);
		}
		return b; 		
	}
	
	
	
	@RequestMapping("/delete")
	@ResponseBody
	public BhResult delete(@RequestBody WhiteMember w) {
		
		BhResult b = null;
		try {		
			service.delete(w);
		    b = new BhResult(200,"删除成功",null);
		}catch(Exception e) {
			e.printStackTrace();
			b = new BhResult(400,"删除失败",null);
		}
		return b; 		
	}
	
	
	
	@RequestMapping("/listPage")
	@ResponseBody
	public BhResult listPage(@RequestBody WhiteMember w) {
		BhResult  b = null;
		  
		try{
			Map<String,Object> map = service.listPage(w);
			b =  new BhResult(200,"获取成功",map);
		}catch(Exception e){
			e.printStackTrace();
			b =  new BhResult(400,"获取失败",null);
		}
		return b;		
	}
	
	@RequestMapping("/openWhite")
	@ResponseBody
	public BhResult openWhite(@RequestBody Map<String,String> map) {
		BhResult  b = null;
		try{
		    if(map.get("whiteList").equals("on")) {
		    	String whiteMember = service.getAll();
				JedisUtil jedisUtil= JedisUtil.getInstance();  
				JedisUtil.Strings strings=jedisUtil.new Strings();
				if(StringUtils.isNotBlank(whiteMember)) {
				      strings.set("whiteMember", whiteMember);
				}else {
					  strings.set("whiteMember", ",");
				}
				b =  new BhResult(200,"打开成功",null);
		    }else if(map.get("whiteList").equals("off")){
				JedisUtil jedisUtil= JedisUtil.getInstance();  
				JedisUtil.Strings strings=jedisUtil.new Strings();
				strings.set("whiteMember", "");
				b =  new BhResult(200,"关闭成功",null);
		    }
			
		}catch(Exception e){
			e.printStackTrace();
			b =  new BhResult(400,"操作失败",null);
		}
		return b;		
	}
	
	
	@RequestMapping("/isOpen")
	@ResponseBody
	public BhResult isOpen() {
		 BhResult  b = null;
		try{
			JedisUtil jedisUtil= JedisUtil.getInstance();  
			JedisUtil.Strings strings=jedisUtil.new Strings();
		    if(StringUtils.isNotBlank(strings.get("whiteMember"))) {
		        b =  new BhResult(200,"已打开","on");
		    }else{
				b =  new BhResult(200,"已关闭","off");
		    }
			
		}catch(Exception e){
			e.printStackTrace();
			b =  new BhResult(400,"操作失败",null);
		}
		return b;		
	}
	
	
	public static void main(String[] a) {
		JedisUtil jedisUtil= JedisUtil.getInstance();  
		JedisUtil.Strings strings=jedisUtil.new Strings();
		//strings.set("whiteMember","");
		System.out.println(strings.get("whiteMember"));
	}
}
