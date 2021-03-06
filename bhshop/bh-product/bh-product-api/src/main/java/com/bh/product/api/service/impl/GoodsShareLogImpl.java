package com.bh.product.api.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.GoodsShareLogMapper;
import com.bh.goods.mapper.GoodsSkuMapper;
import com.bh.goods.pojo.Goods;
import com.bh.goods.pojo.GoodsShareLog;
import com.bh.goods.pojo.GoodsSku;
import com.bh.product.api.service.GoodsShareLogService;
import com.bh.user.mapper.MemberMapper;
import com.bh.user.mapper.MemberUserMapper;
import com.bh.user.mapper.MemerScoreLogMapper;
import com.bh.user.mapper.ScoreRuleExtMapper;
import com.bh.user.mapper.SeedScoreRuleMapper;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberUser;
import com.bh.user.pojo.MemerScoreLog;
import com.bh.user.pojo.ScoreRuleExt;
import com.bh.user.pojo.SeedScoreRule;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;
import com.mysql.jdbc.StringUtils;

@Service
@Transactional
public class GoodsShareLogImpl implements GoodsShareLogService{
	@Autowired
	private GoodsShareLogMapper mapper;
	@Autowired
	private MemberMapper memberMapper;
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private SeedScoreRuleMapper seedScoreRuleMapper;
	@Autowired
	private ScoreRuleExtMapper scoreRuleExtMapper;
	@Autowired
	private MemberUserMapper memberUserMapper;
	@Autowired
	private MemerScoreLogMapper memerScoreLogMapper;	
	
	/**
	 * 插入分享日志
	 */
	@Override
	public int insertLog(int mId, String rMId, String reMId, String shareUrl, String orderNo, String teamNo, String skuId, String shareType,
			String orderType, String openId) throws Exception {
		int row = 0;
		GoodsShareLog log = mapper.getBymIdAndSkuId(mId, Integer.parseInt(skuId),teamNo);
		if(log!=null){
			log.setShareNum(log.getShareNum()+1); //分享次数+1
			row = mapper.updateByPrimaryKeySelective(log);
		}else{
			GoodsShareLog shareLog = new GoodsShareLog();
			shareLog.setmId(mId); //分享人id
			if(!StringUtils.isEmptyOrWhitespaceOnly(rMId)){
				shareLog.setrMId(Integer.parseInt(rMId));
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(teamNo)){
				shareLog.setTeamNo(teamNo);
				//List<GoodsShareLog> list = mapper.getListByTeamNo(teamNo);
			}
			
			if(!StringUtils.isEmptyOrWhitespaceOnly(reMId)){
				shareLog.setReMId(Integer.parseInt(reMId));
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(shareUrl)){
				shareLog.setShareUrl(shareUrl);
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(skuId)){
				shareLog.setSkuId(Integer.parseInt(skuId));
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(shareType)){
				shareLog.setShareType(Integer.parseInt(shareType));
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(orderType)){
				shareLog.setOrderType(Integer.parseInt(orderType));
			}
			if(!StringUtils.isEmptyOrWhitespaceOnly(openId)){
				shareLog.setOpenId(openId);
			}
			row =  mapper.insertSelective(shareLog);
			//cheng增加分享的分享的积分
			if (row == 1) {
				try {
					addShareScore(shareLog);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return row;
	}
	
	/**
	 * 后台订单分享管理
	 */
	@Override
	public PageBean<GoodsShareLog> pageList(String orderNo, String goodsName, String orderType, int pageSize,
			String currentPage) throws Exception {
		PageHelper.startPage(Integer.parseInt(currentPage), pageSize, true);
		List<GoodsShareLog> list = mapper.pageList(orderNo, goodsName, orderType);
		if(list.size()>0){
			for(GoodsShareLog shareLog : list){
				Member member = memberMapper.selectByPrimaryKey(shareLog.getmId());
				if(member!=null){
					shareLog.setmName(member.getUsername()); //分享用户昵称
				}
				if(shareLog.getReMId()!=null){
					Member rmember = memberMapper.selectByPrimaryKey(shareLog.getmId());
					if(rmember!=null){
						shareLog.setReMName(rmember.getUsername()); //推荐人昵称
					}
				}
				GoodsSku goodsSku = goodsSkuMapper.selectByPrimaryKey(shareLog.getSkuId());
				if(goodsSku!=null){
					Goods goods = goodsMapper.selectByPrimaryKey(goodsSku.getGoodsId());
					shareLog.setGoodsName(goods.getName()); //商品名称
					
					org.json.JSONObject jsonObj = new org.json.JSONObject(goodsSku.getValue()); //获取sku商品信息
					
					String skuValue = (String) jsonObj.get("value");
					shareLog.setSkuValue(skuValue); //商品sku属性值
					org.json.JSONArray personList = jsonObj.getJSONArray("url");
					shareLog.setGoodsImage(personList.get(0).toString()); //商品图片
					
				}
			}
		}
		PageBean<GoodsShareLog> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	public void addShareScore(GoodsShareLog shareLog) throws Exception{
		//获取签到规则： 1，签到 2,浇水 3,拼单 4单买 5,分享,6注册积分，7购物积分
		SeedScoreRule se = new SeedScoreRule();
		se.setScoreAction(5);
		List<SeedScoreRule> rule = seedScoreRuleMapper.selectRuleByParams(se);
		if (rule.size() > 0) {
			//状态 0关 1开
			if (rule.get(0).getStatus().equals(1)) {
				ScoreRuleExt record = new ScoreRuleExt();
				record.setSrId(5);
				List<ScoreRuleExt> extList = scoreRuleExtMapper.selectScoreRuleExtBysrId(record);
				if (extList.size() > 0) {
					//分享人Id
					MemberUser memberUser1 = memberUserMapper.selectByPrimaryKey(shareLog.getmId());
					if (memberUser1 !=null) {
						MemberUser u1 = new MemberUser();
						u1.setmId(memberUser1.getmId());
						Integer point = memberUser1.getPoint() + extList.get(0).getExtKey();
						u1.setPoint(point);
						memberUserMapper.updatePointBymId(u1);
						
						MemerScoreLog  log=new MemerScoreLog();
						log.setCreateTime(new Date());
						log.setmId(memberUser1.getmId());
						log.setIsDel(0);
						log.setSmId(-3);
						log.setSsrId(1);
						log.setScore(extList.get(0).getExtKey());
						log.setOrderseedId(0);
						memerScoreLogMapper.insertSelective(log);
					}
					//当前浏览人Id
					MemberUser memberUser2 = memberUserMapper.selectByPrimaryKey(shareLog.getrMId());
					if (memberUser2 !=null) {
						MemberUser u2 = new MemberUser();
						u2.setmId(memberUser2.getmId());
						Integer point = memberUser2.getPoint() + extList.get(0).getExtValue();
						u2.setPoint(point);
						memberUserMapper.updatePointBymId(u2);
						
						MemerScoreLog  log=new MemerScoreLog();
						log.setCreateTime(new Date());
						log.setmId(memberUser2.getmId());
						log.setIsDel(0);
						log.setSmId(-3);
						log.setSsrId(1);
						log.setScore(extList.get(0).getExtValue());
						log.setOrderseedId(0);
						memerScoreLogMapper.insertSelective(log);
					}
				}
			}
		}
	}

}
