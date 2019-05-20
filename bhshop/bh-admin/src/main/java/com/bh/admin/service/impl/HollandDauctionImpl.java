package com.bh.admin.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.bh.admin.mapper.goods.GoodsMapper;
import com.bh.admin.mapper.goods.GoodsSkuMapper;
import com.bh.admin.mapper.goods.HollandDauctionLogMapper;
import com.bh.admin.mapper.goods.HollandDauctionMapper;
import com.bh.admin.mapper.user.MemberMapper;
import com.bh.admin.pojo.goods.AuctionGoodsInfo;
import com.bh.admin.pojo.goods.Goods;
import com.bh.admin.pojo.goods.GoodsSku;
import com.bh.admin.pojo.goods.HollandDauction;
import com.bh.admin.pojo.goods.HollandDauctionLog;
import com.bh.admin.pojo.user.Member;
import com.bh.admin.service.HollandDauctionService;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;

@Service
public class HollandDauctionImpl implements HollandDauctionService{
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private GoodsSkuMapper goodsSkuMapper;
	@Autowired
	private HollandDauctionMapper hollandDauctionMapper;
	@Autowired
	private HollandDauctionLogMapper hollandDauctionLogMapper;
	
	@Autowired
	private MemberMapper memberMaper;
	
	/**
	 * @Description: 荷兰式拍卖配置保存或更新
	 * @author xieyc
	 * @date 2018年5月21日 下午4:04:24 
	 * @param   
	 * @return  
	 */
	public int saveOrUpdate(HollandDauction entity) {
		int row =0;
		Goods goods =goodsMapper.selectByPrimaryKey(entity.getGoodsId());
		if(entity.getStoreNums()<=0){
			goods.setTopicType(0);//普通商品
			goodsMapper.updateByPrimaryKeySelective(goods);
			return 2;//拍卖库存要设置成大于0
		}
		List<GoodsSku> goodsSkuList=goodsSkuMapper.selectListByGoodsId(entity.getGoodsId());
		int minStoreNums=this.getMinStoreNums(goodsSkuList);//某个商品的sku中最小库存
		if(minStoreNums<entity.getStoreNums()){//判断设置的库存是否大于商品的库存
			goods.setTopicType(0);//普通商品
			goodsMapper.updateByPrimaryKeySelective(goods);
			return -1;//该商品的实际库存小于拍卖库存,请重置库存
		}
		HollandDauction hollandDauction=hollandDauctionMapper.getListByGoodsId(entity.getGoodsId());
		if(hollandDauction!=null){//不为null更新配置表
			if(hollandDauction.getStoreNums()>entity.getStoreNums().intValue()){//库存只允许改大不允许该小
				return row;//库存只允许改大不允许该小
			}else{
				int oldStoreNums=hollandDauction.getStoreNums();//原来库存剩余量
				if(oldStoreNums==0){//修改的时候原来库存为0的时候，当前期数要+1
					entity.setCurrentPeriods(hollandDauction.getCurrentPeriods()+1);
					if(goods.getStatus()==5){//商品上架状态
						Date date = new Date();
						hollandDauction.setLoseTime(accountLoseTime(hollandDauction, date));//重新计算开始时间和流拍时间
						hollandDauction.setStartTime(date);
					}
				}
				int addPeriodsNum= entity.getStoreNums()-oldStoreNums;//增加的期数（现在设置的库存-原来剩余库存）
				entity.setTotalPeriods(hollandDauction.getTotalPeriods()+addPeriodsNum);//原来期数+增加期数
				entity.setId(hollandDauction.getId());
				row=hollandDauctionMapper.updateByPrimaryKeySelective(entity);
			}
		}else{//为空插入配置表
			entity.setTotalPeriods(entity.getStoreNums());//总期数
			row=hollandDauctionMapper.insertSelective(entity);
		}
		return row;
	}
	
	private Date accountLoseTime(HollandDauction hd, Date endTime){
		//计算新一期流拍时间
		int middle = hd.getDauctionPrice() - hd.getLowPrice();
		int remainder = middle % hd.getScopePrice(); //余数
		int result = middle / hd.getScopePrice();  //去余数结果
		int num = remainder == 0 ? result+1:result+2;
		Long thisTimeStamp = (long) (num * hd.getTimeSection()*60000);
		Long reusltTimeStamp = endTime.getTime() + thisTimeStamp;
		Date date = new Date(reusltTimeStamp);			
		return date;
	}
	
	/**
	 * @Description: 取list中某个字段的最小值
	 * @author xieyc
	 * @date 2018年5月21日 下午4:04:24 
	 * @param   
	 * @return  
	 */
	public int getMinStoreNums(List<GoodsSku> goodsSkuList){
		int minStoreNums=goodsSkuList.get(0).getStoreNums();
		for (int i = 0; i < goodsSkuList.size(); i++) { 
			 if(goodsSkuList.get(i).getStoreNums()<minStoreNums){
				 minStoreNums=goodsSkuList.get(i).getStoreNums();
			 }	
	    }   
		return minStoreNums;
	}

	/**
	 * @Description: 拍卖配置详情
	 * @author xieyc
	 * @date 2018年5月21日 下午5:47:37 
	 */
	public HollandDauction hollandDauctionDetail(int goodsId) {
		HollandDauction hollandDauction=hollandDauctionMapper.getListByGoodsId(goodsId);
		if(hollandDauction!=null){
			double realLowPrice=(double)hollandDauction.getLowPrice()/100;//最低价
			double realDauctionPrice=(double)hollandDauction.getDauctionPrice()/100;//拍卖价
			double realScopePrice=(double)hollandDauction.getScopePrice()/100;//将价值
			hollandDauction.setRealDauctionPrice(realDauctionPrice);
			hollandDauction.setRealLowPrice(realLowPrice);
			hollandDauction.setRealScopePrice(realScopePrice);
		}
		return hollandDauction;
	}
	/**
	 * @Description: 拍卖列表记录
	 * @author xieyc
	 * @date 2018年5月21日 下午7:19:40 
	 */
	public PageBean<HollandDauctionLog> hollandDauctionLogList(HollandDauctionLog entity) {
		PageHelper.startPage(entity.getCurrentPage(), entity.getPageSize(), true);
		List<HollandDauctionLog> list = hollandDauctionLogMapper.getLogList(entity);
		for (HollandDauctionLog hollandDauctionLog : list) {
			double price=(double)hollandDauctionLog.getPrice()/100;
			hollandDauctionLog.setRealPrice(price);
			Goods goods=goodsMapper.selectByPrimaryKey(hollandDauctionLog.getGoodsId());
			hollandDauctionLog.setGoodsName(goods.getName());
			hollandDauctionLog.setImgUrl(goods.getImage());
			if(hollandDauctionLog.getmId()>0){
				Member m=memberMaper.selectByPrimaryKey(hollandDauctionLog.getmId());
				hollandDauctionLog.setUserName(m.getUsername());
			}
		}
		PageBean<HollandDauctionLog> page = new PageBean<>(list);
		return page;
	}

	/**
	 * @Description: 拍卖时获取商品信息接口
	 * @author xieyc
	 * @date 2018年5月25日 下午12:00:54 
	 */
	public AuctionGoodsInfo getBhShopGoodsInfo(Integer goodsId) {
		AuctionGoodsInfo auctionGoodsInfo=new AuctionGoodsInfo();
		auctionGoodsInfo.setGoodsId(goodsId);//商品id
		List<GoodsSku> skuList=goodsSkuMapper.selectListByGoodsId(goodsId);
		Goods goods=goodsMapper.selectByPrimaryKey(goodsId);
		if(skuList.size()>0 &&goods!=null){
			auctionGoodsInfo.setGoodsName(goods.getName());//商品名字
			auctionGoodsInfo.setSkuId(skuList.get(0).getId());//商品skuid
			auctionGoodsInfo.setValue(skuList.get(0).getValue());//商品规格属性
			auctionGoodsInfo.setStoreNums(skuList.get(0).getStoreNums());//商品库存
		}
		return auctionGoodsInfo;
	}
}
