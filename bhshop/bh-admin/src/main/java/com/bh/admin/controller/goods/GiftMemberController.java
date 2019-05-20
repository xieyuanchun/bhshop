package com.bh.admin.controller.goods;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bh.admin.mapper.goods.CouponAndGiftMapper;
import com.bh.admin.mapper.goods.CouponGiftMapper;
import com.bh.admin.mapper.user.MemberMapper;
import com.bh.admin.pojo.goods.CouponAndGift;
import com.bh.admin.pojo.goods.CouponGift;
import com.bh.admin.pojo.user.GiftMember;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.service.GiftMemberService;
import com.bh.result.BhResult;

@Controller
@RequestMapping("/giftMember")
public class GiftMemberController {

	@Autowired
	private GiftMemberService giftMemberService;
	
	@Autowired 
	private MemberMapper memberMapper;
	
	@Autowired
	private CouponGiftMapper GiftMapper;
	
	@RequestMapping("/add")
	@ResponseBody
	public BhResult add(@RequestBody Map<String,Object> m) {
		BhResult b = null;
		try {
			Member member = memberMapper.selectByPrimaryKey(Integer.valueOf(m.get("mId").toString()));
			if(member!=null&&!member.equals("")&&member.getUsername()!=null) {
				CouponGift ga = GiftMapper.selectByPrimaryKey(Integer.valueOf(m.get("couponGiftId").toString()));
			    if(ga!=null&&!ga.equals("")) {
				
				   GiftMember g = new GiftMember();
			       g.setmId(Integer.valueOf(m.get("mId").toString()));
			       g.setCouponGiftId(Integer.valueOf(m.get("couponGiftId").toString()));
			       giftMemberService.add(g);
		           b = new BhResult(200,"添加成功",null);
			    }else {
			      b = new BhResult(400,"添加失败，没有此大礼包",null);
			    }
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
	public BhResult update(@RequestBody GiftMember g) {
		BhResult b = null;
		try {
			Member member = memberMapper.selectByPrimaryKey(g.getmId());
			if(member!=null&&!member.equals("")&&member.getUsername()!=null) {
				CouponGift ga = GiftMapper.selectByPrimaryKey(g.getCouponGiftId());
				if(ga!=null&&!ga.equals("")) {
			          giftMemberService.update(g);
		              b = new BhResult(200,"修改成功",null);
				}else {
					b = new BhResult(400,"修改失败，没有此大礼包",null);
				}
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
	public BhResult delete(@RequestBody GiftMember g) {
		
		BhResult b = null;
		try {
			
			giftMemberService.delete(g);
		    b = new BhResult(200,"删除成功",null);
		}catch(Exception e) {
			e.printStackTrace();
			b = new BhResult(400,"删除失败",null);
		}
		return b; 		
	}
	
	
	
	@RequestMapping("/listPage")
	@ResponseBody
	public BhResult listPage(@RequestBody GiftMember eg) {
		BhResult  b = null;
		  
		try{
	
			Map<String,Object> map = giftMemberService.listPage(eg);
			b =  new BhResult(200,"获取成功",map);
			
		}catch(Exception e){
			e.printStackTrace();
			b =  new BhResult(400,"获取失败",null);
		}
		return b;		
	}
	
}
