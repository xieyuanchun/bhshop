package com.bh.user.api.service.impl;

import java.util.ArrayList;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.goods.mapper.GoAddressAreaMapper;
import com.bh.goods.pojo.GoAddressArea;
import com.bh.user.api.service.AreasService;
import com.bh.user.mapper.AreasMapper;
import com.bh.user.pojo.Areas;
import com.bh.user.pojo.MemberUserAddress;

@Service
public class AreasServiceImpl implements AreasService{
	
	@Autowired 
	private AreasMapper areasMapper;
	
	@Autowired
	private GoAddressAreaMapper goAddressAreaMapper;
	
	 public List<MemberUserAddress> getProvince() throws Exception{
		 List<MemberUserAddress> list =null;
		 list = areasMapper.getprovince();
		 return list ;
	 }
	 
	 public List<MemberUserAddress> getCity(String areaId) throws Exception{
		 List<MemberUserAddress> list =null;
		 int aId = Integer.parseInt(areaId);
		 list = areasMapper.getcity(aId);
		 return list ;
	 }
	 
	 public List<MemberUserAddress> getTown(String areaId) throws Exception{
		 List<MemberUserAddress> list =null;
		 int aId = Integer.parseInt(areaId);
		 list = areasMapper.getcity(aId);
		 return list ;
	 }
	 
	 public List<Areas> getProvince1() throws Exception{
		 List<Areas> list =null;
		 list = areasMapper.getprovince1();
		 return list ;
	 }
	 
	 public List<Areas> getCity1(String areaId) throws Exception{
		 List<Areas> list =null;
		 int aId = Integer.parseInt(areaId);
		 list = areasMapper.getcity1(aId);
		 return list ;
	 }
	 
	 public List<Areas> getTown1(String areaId) throws Exception{
		 List<Areas> list =null;
		 int aId = Integer.parseInt(areaId);
		 list = areasMapper.getcity1(aId);
		 return list ;
	 }
	 
	 public List<GoAddressArea> getJDTown(String areaId) throws Exception{
		 List<GoAddressArea> list =null;
		 int aId = Integer.parseInt(areaId);
		 GoAddressArea record = new GoAddressArea();
		 record.setParentId(aId);
		 list = goAddressAreaMapper.selectByParams(record);
		 return list ;
	 }
	 
	 
	 public List<GoAddressArea> getJDArea(String areaId) throws Exception{
		 List<GoAddressArea> list =null;
		 int aId = Integer.parseInt(areaId);
		 GoAddressArea record = new GoAddressArea();
		 record.setParentId(aId);
		 list = goAddressAreaMapper.selectByParams(record);
		 return list ;
	 }

	  
	 public List<Areas> getAllInfo() throws Exception{
		 List<Areas> areaList = new ArrayList<>();
		 areaList = getProvince1();
		 for(Areas area : areaList){
			
				List<Areas> aList = getCity1(String.valueOf(area.getAreaId()))	;
				for(Areas goods : aList){
					List<Areas> area1 = getTown1(String.valueOf(goods.getAreaId()));
				goods.setAreas(area1);
				}
				area.setAreas(aList);
			}	
		 return areaList;
	 }
	 
	public List<GoAddressArea> getAllJDInfo() throws Exception{
		 List<GoAddressArea> areaList = new ArrayList<>();
		 GoAddressArea a = new GoAddressArea();
		 a.setParentId(0);
		 areaList = goAddressAreaMapper.selectByParams(a);
		 for(GoAddressArea area : areaList){
				List<GoAddressArea> aList = getJDCity(String.valueOf(area.getId()))	;
				for(GoAddressArea alist : aList){
					List<GoAddressArea> area1 = getJDTown(String.valueOf(alist.getId()));
					for (GoAddressArea goAddressArea : area1) {
						List<GoAddressArea> area2 = getJDArea(String.valueOf(goAddressArea.getId()));
						goAddressArea.setAreas(area2);
					}
				    alist.setAreas(area1);
				}
				area.setAreas(aList);
			
			}	
		 return areaList;
	 }
	 
	 
	 public List<GoAddressArea> getJDCity(String areaId) throws Exception{
		 List<GoAddressArea> list =null;
		 int aId = Integer.parseInt(areaId);
		 GoAddressArea a = new GoAddressArea();
		 a.setParentId(aId);
		 list = goAddressAreaMapper.selectByParams(a);
		 return list ;
	 }
	 
	/**
	 * 递归获取省市区
	 */
	@Override
	public List<Areas> selectByLevel(String parentId, String fz) throws Exception {
		List<Areas> list = null;
		if(Integer.parseInt(fz)==1){
			list = areasMapper.getprovince1();
		}else if(Integer.parseInt(fz)==2){
			list = areasMapper.getcity1(Integer.parseInt(parentId));
		}else if(Integer.parseInt(fz)==3){
			list = areasMapper.gettown1(Integer.parseInt(parentId));
		}
		return list;
	}
	 
}
