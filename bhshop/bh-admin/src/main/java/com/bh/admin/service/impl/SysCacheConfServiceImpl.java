package com.bh.admin.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.admin.mapper.user.SysCacheConfMapper;
import com.bh.admin.pojo.user.SysCacheConf;
import com.bh.admin.service.SysCacheConfService;
import com.bh.config.Contants;
import com.bh.utils.JedisUtil;
import com.bh.utils.PageBean;
import com.bh.utils.cache.CacheConfig;
import com.github.pagehelper.PageHelper;
@Service
public class SysCacheConfServiceImpl implements SysCacheConfService{
	private static final Logger logger = LoggerFactory.getLogger(SysCacheConfServiceImpl.class);
	@Autowired
	private SysCacheConfMapper mapper;
	
	@Override
	public int add(SysCacheConf entity) throws Exception {
		long time = JedisUtil.getInstance().time();
		Date date = new Date(time);
		entity.setAddTime(date);
		entity.setEditTime(date);
		return mapper.insertSelective(entity);
	}

	@Override
	public int delete(SysCacheConf entity) throws Exception {
		SysCacheConf conf = mapper.selectByPrimaryKey(entity.getCacheConfId());
		if(conf!=null){
			CacheConfig.remove(conf.getConfigKey());
		}
		return mapper.deleteByPrimaryKey(entity.getCacheConfId());
	}

	@Override
	public int edit(SysCacheConf entity) throws Exception {
		long time = JedisUtil.getInstance().time();
		Date date = new Date(time);
		entity.setEditTime(date);
		return mapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	public SysCacheConf get(SysCacheConf entity) throws Exception {
		// TODO Auto-generated method stub
		return mapper.selectByPrimaryKey(entity.getCacheConfId());
	}

	@Override
	public PageBean<SysCacheConf> listPage(SysCacheConf entity) throws Exception {
		PageHelper.startPage(Integer.valueOf(entity.getCurrentPage()), Contants.PAGE_SIZE, true);
		List<SysCacheConf> list = mapper.listPage(entity);
		PageBean<SysCacheConf> pageBean = new PageBean<>(list);
		return pageBean;
	}

	@Override
	public int loadSysCacheConf(String[] params) throws Exception {
		int row = 0;
		if(params.length>1){
			SysCacheConf conf = new SysCacheConf();
			List<SysCacheConf> list = null;
			SysCacheConf thisConfig = null;
			//加载所有缓存
			if(params[0].equals("0") && params[1].equals("*")){
				list = mapper.listPage(conf);
				if(list.size()>0){
					for(SysCacheConf entity : list){
						CacheConfig.put(entity.getConfigKey(), entity.getConfigVal());
					}
				}else{
					logger.info("当前数据库常量配置无缓存!");
				}
			}
			//按组名加载缓存
			else if(params[0].equals("1")){
				for (int i = 1; i < params.length; i++) {
					conf.setConfigGroup(params[i]);
				    list = mapper.getByConfigGroup(conf);
				    if(list.size()>0){
						for(SysCacheConf entity : list){
							CacheConfig.put(entity.getConfigKey(), entity.getConfigVal());
						}
					}else{
						logger.info("组名为："+params[i]+"下，不存在缓存数据!");
					}
				}
			}
			//按key加载缓存
			else if(params[0].equals("2")){
				for (int i = 1; i < params.length; i++) {
					conf.setConfigKey(params[i]);
					thisConfig = mapper.getByConfigKey(conf);
					if(thisConfig!=null){
						CacheConfig.put(thisConfig.getConfigKey(), thisConfig.getConfigVal());
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
	public String getValByConfigKey(String key) throws Exception {
		String val = null;
		Object obj = CacheConfig.getIfPresent(key);
		if(obj==null){
			SysCacheConf conf = new SysCacheConf();
			conf.setConfigKey(key);
			SysCacheConf entity = mapper.getByConfigKey(conf);
			if(entity!=null){
				val = entity.getConfigVal();
			}
			//加入缓存
			CacheConfig.put(key, val);
		}else{
			val = (String) obj;
		}
		return val;
	}
}
