package com.bh.admin.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.admin.mapper.goods.GoodsMapper;
import com.bh.admin.mapper.user.SysCacheDbMapper;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.user.SysCacheDb;
import com.bh.admin.service.SysCacheDbService;
import com.bh.config.Contants;
import com.bh.utils.JedisUtil;
import com.bh.utils.PageBean;
import com.bh.utils.cache.CacheConfig;
import com.github.pagehelper.PageHelper;

import net.sf.json.JSONArray;

@Service
public class SysCacheDbServiceImpl implements SysCacheDbService{
	private static final Logger logger = LoggerFactory.getLogger(SysCacheDbServiceImpl.class);
	@Autowired
	private SysCacheDbMapper mapper;
	@Autowired
	private GoodsMapper goodsMapper;

	@Override
	public int add(SysCacheDb entity) throws Exception {
		long time = JedisUtil.getInstance().time();
		Date date = new Date(time);
		entity.setAddTime(date);
		entity.setEditTime(date);
		return mapper.insertSelective(entity);
	}

	@Override
	public int delete(SysCacheDb entity) throws Exception {
		SysCacheDb conf = mapper.selectByPrimaryKey(entity.getCacheDbId());
		if(conf!=null){
			CacheConfig.remove(conf.getDbKey());
		}
		return mapper.deleteByPrimaryKey(entity.getCacheDbId());
	}

	@Override
	public int edit(SysCacheDb entity) throws Exception {
		long time = JedisUtil.getInstance().time();
		Date date = new Date(time);
		entity.setEditTime(date);
		return mapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	public SysCacheDb get(SysCacheDb entity) throws Exception {
		// TODO Auto-generated method stub
		return mapper.selectByPrimaryKey(entity.getCacheDbId());
	}

	@Override
	public PageBean<SysCacheDb> listPage(SysCacheDb entity) throws Exception {
		PageHelper.startPage(Integer.valueOf(entity.getCurrentPage()), Contants.PAGE_SIZE, true);
		List<SysCacheDb> list = mapper.listPage(entity);
		PageBean<SysCacheDb> pageBean = new PageBean<>(list);
		return pageBean;
	}

	@Override
	public int loadSysDb(String[] params) throws Exception {
		int row = 0;
		if(params.length>1){
			SysCacheDb db = new SysCacheDb();
			List<SysCacheDb> list = null;
			SysCacheDb thisConfig = null;
			//加载所有缓存
			if(params[0].equals("0") && params[1].equals("*")){
				list = mapper.listPage(db);
				if(list.size()>0){
					for(SysCacheDb entity : list){
						CacheConfig.put(entity.getDbKey(), entity.getDbVal());
					}
				}else{
					logger.info("当前数据库常量配置无缓存!");
				}
			}
			//按组名加载缓存
			else if(params[0].equals("1")){
				for (int i = 1; i < params.length; i++) {
					db.setDbGroup(params[i]);
				    list = mapper.getByDbGroup(db);
				    if(list.size()>0){
						for(SysCacheDb entity : list){
							CacheConfig.put(entity.getDbKey(), entity.getDbVal());
						}
					}else{
						logger.info("组名为："+params[i]+"下，不存在缓存数据!");
					}
				}
			}
			//按key加载缓存
			else if(params[0].equals("2")){
				for (int i = 1; i < params.length; i++) {
					db.setDbKey(params[i]);
					thisConfig = mapper.getByDbKey(db);
					if(thisConfig!=null){
						CacheConfig.put(thisConfig.getDbKey(), thisConfig.getDbVal());
					}else{
						logger.info("key为："+params[i]+"下，无缓存值");
					}
				}
			}
		}else{
			logger.info("参数设置不正确！");
		}
		return row;
	}

	@Override
	public String getValByDbKey(String key) throws Exception {
		String val = null;
		Object obj = CacheConfig.getIfPresent(key);//从缓存拿
		if(obj==null){
			SysCacheDb db = new SysCacheDb();
			db.setDbKey(key);
			SysCacheDb entity = mapper.getByDbKey(db);//从缓存表拿
			if(entity!=null){
				val = entity.getDbVal();
				//加入缓存
				CacheConfig.put(key, val);
			}else{ //根据sql重新请求数据
				String sql = "SELECT id, name ,cat_id FROM goods WHERE id = 11473";
				List<Goods> goodsList = goodsMapper.querySql(sql);
				List<Map<String, Object>> mapList = new ArrayList<>();
				Map<String, Object> map = null;
				if(goodsList.size()>0){
					for(Goods goods : goodsList){
						map = new HashMap<>();
						map.put("id", goods.getId());
						map.put("name", goods.getName());
						map.put("catId", goods.getCatId());
						mapList.add(map);
					}
				    Date date = new Date(JedisUtil.getInstance().time());
					JSONArray array = JSONArray.fromObject(mapList);
					db.setDbSql(sql);
					db.setDbVal(array.toString());
					db.setDbGroup("GOODS");
					db.setAddTime(date);
					db.setEditTime(date);
					mapper.insertSelective(db);
					//加入缓存
					CacheConfig.put(key, array.toString());
					val = array.toString();
				}else{
					logger.info("当前key，缓存中无数据，sql返回数据为null");
				}
			}
		}else{
			val = (String) obj;
		}
		return val;
	}

	@Override
	public int updateAllVal() throws Exception {
		int row = 0;
		SysCacheDb db = new SysCacheDb();
		List<SysCacheDb> list = mapper.listPage(db);
		if(list.size()>0){
			for(SysCacheDb entity : list){
				if(entity.getDbGroup().equals("GOODS")){
					List<Goods> goodsList = goodsMapper.querySql(entity.getDbSql());
					List<Map<String, Object>> mapList = new ArrayList<>();
					Map<String, Object> map = null;
					if(goodsList.size()>0){
						for(Goods goods : goodsList){
							map = new HashMap<>();
							map.put("id", goods.getId());
							map.put("name", goods.getName());
							map.put("catId", goods.getCatId());
							mapList.add(map);
						}
					    Date date = new Date(JedisUtil.getInstance().time());
						JSONArray array = JSONArray.fromObject(mapList);
						entity.setDbVal(array.toString());
						entity.setEditTime(date);
						row = mapper.updateByPrimaryKeySelective(entity);
					}
				}
			}
		}
		return row;
	}
}
