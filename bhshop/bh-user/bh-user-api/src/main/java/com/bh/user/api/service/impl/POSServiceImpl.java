package com.bh.user.api.service.impl;

import java.util.Date;
import java.util.List;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.config.Contants;
import com.bh.result.BhResult;
import com.bh.user.api.service.POSService;
import com.bh.user.mapper.MemberShopMapper;
import com.bh.user.mapper.TbPosMapper;
import com.bh.user.pojo.MemberShop;
import com.bh.user.pojo.POSParam;
import com.bh.user.pojo.TbPos;
import com.bh.utils.JedisUtil;
import com.bh.utils.JsonUtils;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;

@Service
public class POSServiceImpl implements POSService{
	@Autowired
	private MemberShopMapper memberShopMapper;
	@Autowired
	private TbPosMapper tbPosMapper;
	
	
	
	public BhResult insertPosMsg(POSParam posParam) throws Exception{
		BhResult bhResult = new BhResult(400, "参数不能为空", null);
		//1已有银联pos机,需提交pos机商户号和商户名称
		List<MemberShop> shopList = memberShopMapper.selectShopByTokne(posParam.getToken());
		MemberShop memberShop = new MemberShop();
		memberShop.setExistPos(posParam.getType());
		memberShop.setToken(posParam.getToken());
		memberShop.setStep(1);
		String msg = "";
		if (posParam.getType() == 1) {
			//如果商户号为空
			if ((StringUtils.isNotEmpty(posParam.getBusName())) && (StringUtils.isNotEmpty(posParam.getBusNo())) && (StringUtils.isNotEmpty(posParam.getLinkManPhone())) && ((StringUtils.isNotEmpty(posParam.getIdNo())) && (StringUtils.isNotEmpty(posParam.getBankCardNo())))) {
				//商户号+商户名+联系号码+身份证+银行卡号
				msg = posParam.getBusNo()+","+posParam.getBusName()+","
					+ posParam.getLinkManPhone() + "," + posParam.getIdNo()+","
					+posParam.getBankCardNo();
			}
		}else if (posParam.getType() == 2) {
			//姓名，身份证，银行卡号，手机号
			if ((StringUtils.isNotEmpty(posParam.getFullName())) && (StringUtils.isNotEmpty(posParam.getIdNo())) && (StringUtils.isNotEmpty(posParam.getBankCardNo())) && (StringUtils.isNotEmpty(posParam.getPhone()))) {
				msg = posParam.getFullName() + "," + posParam.getIdNo() + "," + posParam.getBankCardNo() + "," + posParam.getPhone();
			}
		}else if (posParam.getType() == 3) {
			TbPos pos = new TbPos();
			pos.setPhone(posParam.getLinkManPhone());
			List<TbPos> posList = tbPosMapper.selectTbPosListByP(pos);
			if (posList.size() < 1) {
				//姓名，联系号码
				if ((StringUtils.isNotEmpty(posParam.getLinkManName())) && (StringUtils.isNotEmpty(posParam.getLinkManPhone()))) {
					msg = posParam.getLinkManName() + "," + posParam.getLinkManPhone()+ "," + posParam.getIdNo() + "," + posParam.getBankCardNo() + "," + posParam.getLicenseNumber();
					memberShop.setHandleStatus(0);
					memberShop.setStep(0);
					TbPos tbPos = new TbPos();
					tbPos.setShopId(shopList.get(0).getmId());
					List<TbPos> list = tbPosMapper.selectTbPosListByP(tbPos);
					TbPos pos2 = new TbPos();
					pos2.setExistPos(3);
					pos2.setHandleStatus(0);
					pos2.setName(posParam.getLinkManName());
					pos2.setPhone(posParam.getLinkManPhone());
					pos2.setBankCardNo(posParam.getBankCardNo());
					pos2.setIdentity(posParam.getIdNo());
					pos2.setLicenseNumber(posParam.getLicenseNumber());
					pos2.setShopId(shopList.get(0).getmId());
					if (list.size() > 0) {
						tbPosMapper.updateNameAndPhone(pos2);
					}else{
						long time = JedisUtil.getInstance().time();
						Date date = new Date(time);
						pos2.setAddTime(date);
						tbPosMapper.insertSelective(pos2);
					}
				}else{
					return bhResult = new BhResult(400, "参数不能为空", null);
				}
			}else {
				return bhResult = new BhResult(400, "该手机号已存在", null);
			}
		}
		memberShop.setPosMsg(msg);
		//该token已存在
		if (shopList.size() > 0) {
			memberShop.setmId(shopList.get(0).getmId());
			memberShopMapper.updateByPrimaryKeySelective(memberShop);
			bhResult = new BhResult(200, "操作成功", null);
		}else{
			memberShop.setAddtime(new Date());
			memberShopMapper.insertSelective(memberShop);
			bhResult = new BhResult(200, "操作成功", null);
		}
		return bhResult;
	}
	
	
	
	//查询pos列表
	/*public	PageBean<MemberShop> selectPosList(POSParam param) throws Exception{
		PageHelper.startPage(Integer.parseInt(param.getCurrentPage()), Contants.SIZE, true);
		List<MemberShop> shopList = memberShopMapper.selectPosList( param);
		if (shopList.size()>0) {
			for (MemberShop memberShop : shopList) {
				POS pos = new POS();
				pos.setmId(memberShop.getmId());
				pos.setShopName(memberShop.getShopName());
				List<String> str = JsonUtils.stringToList(memberShop.getPosMsg());
				pos.setLinkManName(str.get(0));
				pos.setLinkManPhone(str.get(1));
				pos.setHandleStatus(memberShop.getHandleStatus());
				list.add(pos);
			}
		}
		PageBean<MemberShop> pageBean = new PageBean<>(shopList);
		return pageBean;
	}*/
	public PageBean<TbPos> selectPosList(POSParam posParam) throws Exception{
		PageHelper.startPage(Integer.parseInt(posParam.getCurrentPage()), Contants.SIZE, true);
		List<TbPos> tbPosList = tbPosMapper.selectPosList(posParam);
		if (tbPosList.size()>0) {
			for (TbPos tbPos : tbPosList) {
				tbPos.setmId(tbPos.getShopId());
				String msg = tbPos.getName()+","+tbPos.getPhone();
				tbPos.setPosMsg(msg);
				
			}
		}
		PageBean<TbPos> pageBean = new PageBean<>(tbPosList);
		return pageBean;
	}
	
	
	//更新处理状态
	public int updateHandleStatus(POSParam param) throws Exception{
		int row = 0;
		List<String> ids = JsonUtils.stringToList(param.getmId());
		row = memberShopMapper.updateHandleStatusBymIds(ids);
		TbPos record = new TbPos();
		record.setShopId(Integer.parseInt(param.getmId()));
		tbPosMapper.updateTbPos(record);
		return row;
	}
	
	
	public PageBean<MemberShop> promisReco(POSParam param) throws Exception{
		PageHelper.startPage(Integer.parseInt(param.getCurrentPage()), Contants.SIZE, true);
		List<MemberShop> shopList = memberShopMapper.selectPosList( param);
		PageBean<MemberShop> pageBean = new PageBean<>(shopList);
		return pageBean;
	}
	
	
	//选择memberShop
	public	MemberShop selectSimpleShop(Integer mId) throws Exception{
		MemberShop memberShop = new MemberShop();
		memberShop = memberShopMapper.selectByPrimaryKey(mId);
		return memberShop;
	}
	
	//商家免审核上架的列表
	public PageBean<MemberShop> depositReco(MemberShop memberShop) throws Exception{
		PageHelper.startPage(Integer.parseInt(memberShop.getCurrentPage()), Integer.parseInt(memberShop.getSize()), true);
		List<MemberShop> shopList = memberShopMapper.selectDepositList(memberShop);
		PageBean<MemberShop> pageBean = new PageBean<>(shopList);
		return pageBean;
	}
	
	
}
