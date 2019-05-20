package com.bh.admin.service.impl;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.result.BhResult;
import com.bh.admin.service.MBusMSGService;
import com.bh.admin.mapper.user.MemberShopMapper;
import com.bh.admin.pojo.user.MBusEntity;

@Service
public class MBusMSGServiceImpl implements MBusMSGService{
	
	@Autowired
	private MemberShopMapper memberShopMapper;
	
	
	public MBusEntity queryByUserName(String username) throws Exception{
		return  memberShopMapper.selectAllByUsername(username);
	}
	
	
	public BhResult checkMsg(String username,String phone) throws Exception{
		BhResult bhResult = null;
		MBusEntity entity = memberShopMapper.selectAllByUsername(username);
		if (entity == null) {
			bhResult = new BhResult(400, "亲,你的账号不存在。", null);
		}else{
			String mobile = entity.getMobile();
			if (mobile !=null) {
				if (mobile.equals(phone)) {
					bhResult = new BhResult(200, "验证信息正确", null);
				}else{
					bhResult = new BhResult(400, "亲,你的手机号不匹配。", null);
				}
			}else{
				bhResult = new BhResult(400, "亲,你的手机号不匹配。", null);
			}
		}
		return bhResult;
	}
	
	public BhResult updatePwd(String username,String phone,String pwd) throws Exception{
		BhResult bhResult = new BhResult();
		BhResult result = checkMsg(username, phone);
		if (result.getStatus().equals(200)) {
			MBusEntity entity = memberShopMapper.selectAllByUsername(username);
			MBusEntity busEntity = new MBusEntity();
			busEntity.setUserId(entity.getUserId());
			String salt = RandomStringUtils.randomAlphanumeric(20);
			busEntity.setPassword(new Sha256Hash(pwd, salt).toHex());
			busEntity.setSalt(salt);
			memberShopMapper.updateSysUserPwd(busEntity);
			bhResult =new BhResult(200, "操作成功", null);
		}else {
			bhResult = result;
		}
		return bhResult;
	}
}
