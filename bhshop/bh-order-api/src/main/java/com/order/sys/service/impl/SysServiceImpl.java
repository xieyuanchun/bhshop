package com.order.sys.service.impl;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bh.config.Contants;
import com.bh.goods.mapper.GoodsMapper;
import com.bh.goods.mapper.RobotHeadMapper;
import com.bh.goods.pojo.RobotHead;
import com.bh.order.mapper.OrderMapper;
import com.bh.order.mapper.OrderTeamMapper;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderTeam;
import com.bh.order.pojo.SysTeam;
import com.bh.user.mapper.PromoteUserMapper;
import com.bh.user.pojo.PromoteUser;
import com.order.shop.service.OrderTeamService;
import com.order.sys.service.SysService;
import com.order.user.service.UserOrderService;
import com.order.util.RandomCommonUtil;
import com.wechat.service.WechatTemplateMsgService;

@Service
public class SysServiceImpl implements SysService {
	@Autowired
	private PromoteUserMapper promoteUserMapper;
	@Autowired
	private OrderTeamMapper orderTeamMapper;
	@Autowired
	private UserOrderService userOrderService;
	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private  RobotHeadMapper robotHeadMapper;
	@Autowired
	private WechatTemplateMsgService wechatTemplateMsgService; 
	
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private SysService sysService;
	@Autowired
	private OrderTeamService orderTeamService;
	//
	@Override
	public boolean createTeam(SysTeam entity) {
		// TODO Auto-generated method stub
		try {
	        addTeam(entity,true);
	        //
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}

	}
	@Transactional
	@Override
	public boolean promoteTeam(SysTeam entity) {
		try {
			//需要拼团人数
			int needTeamNum = entity.getTeamNum() - entity.getHasNum();
			for(int i=0;i<needTeamNum;i++){
				addTeam(entity,false);
			}
			orderTeamMapper.updateByTeamNo(entity.getTeamNo());
			
			//京东下单
			/*List<OrderTeam> teamList = orderTeamMapper.getByTeamNo(entity.getTeamNo());
			for(OrderTeam team:teamList){
			  Order order = orderMapper.getOrderByOrderNo(team.getOrderNo());
			  //userOrderService.submitJdOrder(order);
			  //2018-04-02上午11点30分修改
			  if (order !=null) {
				  userOrderService.submitJdOrder(order);
				  //2018-04-09上午09点32添加代码
				  //type=0代表人工下单，1代表机器人下单
				 if (team.getType() == 0) {
					 wechatTemplateMsgService.sendGroupTemplate(String.valueOf(team.getmId()), String.valueOf(order.getId()), String.valueOf(team.getGoodsId()));
				 }
			  }
			}*/
			//2018-5-28修改
			if (orderTeamService.isTeamPromoteSucess(entity.getTeamNo())==0) {
				//京东下单
				List<OrderTeam> teamList = orderTeamMapper.getByTeamNo(entity.getTeamNo());
				for(OrderTeam team:teamList){
				  Order order = orderMapper.getOrderByOrderNo(team.getOrderNo());
				  //userOrderService.submitJdOrder(order);
				  //2018-04-02上午11点30分修改
				  if (order !=null) {
					  userOrderService.submitJdOrder(order);
					  //2018-04-09上午09点32添加代码
					  //type=0代表人工下单，1代表机器人下单
					 if (team.getType() == 0) {
						 wechatTemplateMsgService.sendGroupTemplate(String.valueOf(team.getmId()), String.valueOf(order.getId()), String.valueOf(team.getGoodsId()));
					 }
				  }
				}
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
			// TODO: handle exception
		}
		
	}
	
	/**
	 * 随机返回头像
	 */
	public String getRobotHeadImg() {
		List<RobotHead> robotHeadList=robotHeadMapper.getALL();
		String img=null;
		if(robotHeadList.size()>0){
			int index = (int) (Math.random() * robotHeadList.size());// 随机取一个下标
			RobotHead robotHead=robotHeadList.get(index);
			img=robotHead.getImg();
		}else{
			img=Contants.bucketHttp+"sendimages/FF1CD33E97B54F73981634E23297EDA0.jpg";
		}
        return  img;
	}
	@Transactional
	private void addTeam(SysTeam entity,boolean isCreate){
		PromoteUser pUser = RandomCommonUtil.randPromoteUser(getRobotHeadImg());
		promoteUserMapper.insertSelective(pUser);
		Integer teamTime = entity.getTeamTime();
		Integer timeUnit = entity.getTimeUnit();
		// 小时转分钟
		if (timeUnit == 2) {
			teamTime = 60 * teamTime;
		}
		// 天转分钟
		else if (timeUnit == 3) {
			teamTime = 24 * 60 * teamTime;
		}
		entity.setTeamTime(teamTime);
		OrderTeam orderTeam = isCreate?RandomCommonUtil.randCreateTeam(pUser.getId(),entity):RandomCommonUtil.randPromoteTeam(pUser.getId(),entity);
		orderTeamMapper.insertSelective(orderTeam);
	}
	@Override
	public void promoteTeamByOrderNo(String orderNo) {
		List<SysTeam> taskList = goodsMapper.selectPromoteTeamTaskByOrderNo(orderNo);
		for(SysTeam sysTeam:taskList){
			boolean flag = sysService.promoteTeam(sysTeam);
		}
		// TODO Auto-generated method stub
		
	}
	@Override
	public void promoteTeamByGoodsId(Integer goodsId) {
		List<SysTeam> taskList = goodsMapper.selectPromoteTeamTaskByGoodsId(goodsId);
		for(SysTeam sysTeam:taskList){
			sysService.promoteTeam(sysTeam);
		}
		
	}

}
