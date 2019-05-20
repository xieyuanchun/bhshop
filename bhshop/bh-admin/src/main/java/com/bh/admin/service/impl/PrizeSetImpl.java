package com.bh.admin.service.impl;

import java.util.Date;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.bh.admin.mapper.goods.PrizeRelaMapper;
import com.bh.admin.mapper.goods.PrizeSetMapper;
import com.bh.admin.mapper.user.WalletLogMapper;
import com.bh.admin.mapper.user.WalletMapper;
import com.bh.admin.pojo.goods.PrizeRela;
import com.bh.admin.pojo.goods.PrizeSet;
import com.bh.admin.pojo.user.Wallet;
import com.bh.admin.pojo.user.WalletLog;
import com.bh.admin.service.PrizeSetService;
import com.bh.config.Contants;
import com.bh.utils.PageBean;
import com.github.pagehelper.PageHelper;

@Service
public class PrizeSetImpl implements PrizeSetService{
	@Autowired
	private PrizeSetMapper mapper;
	@Autowired
	private WalletMapper walletMapper;
	@Autowired
	private WalletLogMapper walletLogMapper;
	@Autowired
	private PrizeRelaMapper prizeRelaMapper;
	
	@Override
	public int insert(PrizeSet entity) throws Exception {
		// TODO Auto-generated method stub
		return mapper.insertSelective(entity);
	}

	@Override
	public int edit(PrizeSet entity) throws Exception {
		// TODO Auto-generated method stub
		return mapper.updateByPrimaryKeySelective(entity);
	}

	@Override
	public int delete(PrizeSet entity) throws Exception {
		// TODO Auto-generated method stub
		return mapper.deleteByPrimaryKey(entity.getId());
	}

	@Override
	public PrizeSet get(PrizeSet entity) throws Exception {
		// TODO Auto-generated method stub
		return mapper.selectByPrimaryKey(entity.getId());
	}

	@Override
	public PageBean<PrizeSet> listPage(PrizeSet entity) throws Exception {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPaqe()), Contants.PAGE_SIZE, true);
		List<PrizeSet> list = mapper.listPage(entity);
		PageBean<PrizeSet> pageBean = new PageBean<>(list);
		return pageBean;
	}
	
	//中奖算法
	@Transactional
	@Override
	public String priceIsGet(PrizeSet entity, int mId) throws Exception { 
		String str = null;
		Date date = new Date();
		if(entity.getGoodsId()!=null){
			List<PrizeRela> list = prizeRelaMapper.selectByGoodsId(entity.getGoodsId());
			if(list.size()>0){
				PrizeSet prizeSet = mapper.selectByPrimaryKey(list.get(0).getPrizeId());
				if(prizeSet!=null){
					if(prizeSet.getEndTime().getTime()>date.getTime() && prizeSet.getStartTime().getTime()<date.getTime()){
						if(prizeSet.getPrizeNum()==-1){//中奖无人数限制
							str = changeBack(prizeSet, mId);
						}else if(prizeSet.getSurplusNum()>0){//待中奖人数有剩余
							str = changeBack(prizeSet, mId);
						}else{
							str = "您来晚了，奖金已领完";
						}
					}else{
						str = "活动时间已过期";
					}
				}
			}
		}else{//无商品id采用默认规则
			List<PrizeSet> defaultList = mapper.selectByIsDefault();
			if(defaultList.size()>0){
				if(defaultList.get(0).getEndTime().getTime()>date.getTime() && defaultList.get(0).getStartTime().getTime()<date.getTime()){
					if(defaultList.get(0).getPrizeNum()==-1){//中奖无人数限制
						str = changeBack(defaultList.get(0), mId);
					}else if(defaultList.get(0).getSurplusNum()>0){//待中奖人数有剩余
						str = changeBack(defaultList.get(0), mId);
					}else{
						str = "您来晚了，奖金已领完";
					}
				}else{
					str = "活动时间已过期";
				}
			}else{
				str = "暂无默认算法";
			}
		}
		return str;
	}
	//中奖金额
	private double getMoney(int begin, int end, double price){
		Random random = new Random();
		double rate = (double)(random.nextInt(begin) + end-begin+1)/100;
	    return Double.parseDouble(String.format("%.2f", price*rate));
	}
	//判断是否中奖
	private boolean isGet(int rate){
		int random = (int)(Math.random()*100);
		if(rate>random){
			return true;
		}
		return false;
	}
	//钱包日志插入
	private int insertWalltLog(int mId, int amount){
		WalletLog log = new WalletLog();
		log.setAmount(amount);
		log.setmId(mId);
		log.setInOut(0);
		log.setAddTime(new Date());
		log.setType(1);
		return walletLogMapper.insertSelective(log);
	}
	//中奖逻辑处理
	private String changeBack(PrizeSet prizeSet, int mId){
		int row = 0;
		String str = null;
		double money = 0.0;//最终中奖金额
		int begin = prizeSet.getMinAmountRate(); //中奖金额最小占比
		int end = prizeSet.getMaxAmountRate(); //中奖金额最大占比
		double price = (double)prizeSet.getPrizeAmount()/100;//中奖金额 最大面额
		int rate = prizeSet.getPrizeRate();//中奖概率
		
		boolean flag = isGet(rate);
		if(flag){
			money = getMoney(begin, end, price);//最终中奖金额
			List<Wallet> walletList = walletMapper.getWalletByUid(mId);
			if(walletList.size()>0){
				Wallet wallet = walletList.get(0);
				wallet.setMoney(wallet.getMoney()+(int)(money*100));
				row = walletMapper.updateByPrimaryKeySelective(wallet);//钱包余额增加
				if(row>0){
					insertWalltLog(mId, (int)(money*100));//插入钱包日志
				}else{
					str = "很遗憾，您未中奖";
					return str;
				}
			}else{
				str = "个人钱包信息有误！";
				return str;
			}
			if(prizeSet.getSurplusNum()>0){ //待中奖剩余人数-1
				prizeSet.setSurplusNum(prizeSet.getSurplusNum()-1);
				mapper.updateByPrimaryKeySelective(prizeSet);
			}
			str = "恭喜您， 中奖："+money+"元";
		}else{
			str = "很遗憾，您未中奖";
		}
		return str;
	}

	@Override
	public PageBean<PrizeSet> getGoodAndPrize(PrizeSet entity) throws Exception {
		PageHelper.startPage(Integer.parseInt(entity.getCurrentPaqe()), Contants.PAGE_SIZE, true);
		List<PrizeSet> list = mapper.getGoodAndPrize(entity);
		PageBean<PrizeSet> pageBean = new PageBean<>(list);
		return pageBean;
	}

}
