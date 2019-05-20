package com.bh.product.api.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bh.goods.pojo.OneWeek;
import com.bh.goods.pojo.UserAttendance;
import com.bh.product.api.service.QianDaoService;
import com.bh.product.api.util.TopicUtils;
import com.bh.user.mapper.MemberUserMapper;
import com.bh.user.mapper.MemerScoreLogMapper;
import com.bh.user.mapper.ScoreRuleExtMapper;
import com.bh.user.pojo.MemberUser;
import com.bh.user.pojo.MemerScoreLog;
import com.bh.user.pojo.ScoreRuleExt;


@Service
public class QianDaoImpl implements QianDaoService {
	@Autowired
	private ScoreRuleExtMapper scoreRuleExtMapper;
	@Autowired
	private MemerScoreLogMapper memerScoreLogMapper;
	@Autowired
	private MemberUserMapper memberUserMapper;
	
	public UserAttendance getAtt(MemerScoreLog log){
		MemberUser memberUser = memberUserMapper.selectByPrimaryKey(log.getmId());
		UserAttendance userAttendance=new UserAttendance();
		userAttendance.setOneWeek(selectOneWeek(log));
		userAttendance.setPoint(memberUser.getPoint());
		
		
		// 用户签到记录
		List<MemerScoreLog> list = memerScoreLogMapper.selectLogByUserattends(log);
		// 没有记录
		if (list.size() < 1) {
			// 0未签到,1已签到
			userAttendance.setIsAttendance(0);
			// 连续签到的次数
			userAttendance.setTimes(0);
		} else {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			// 如果已经签到过，判断是今天签到的，如果是则提示"已经签到了"
			MemerScoreLog log2 = list.get(0);
			String d1 = sdf.format(new Date());// 第一个时间
			String d2 = sdf.format(log2.getCreateTime());// 第二个时间
			boolean flage = false;
			flage = d1.equals(d2);
			if (flage) {// 已签到
				userAttendance.setIsAttendance(1);
			} else {
				userAttendance.setIsAttendance(0);
			}
			userAttendance.setTimes(list.get(0).getTimes());

		}
		return userAttendance;
	}

	public List<OneWeek> selectOneWeek(MemerScoreLog log) {
		List<OneWeek> list=new ArrayList<>();
		java.text.SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		//昨天的时间
		List<MemerScoreLog> logList=memerScoreLogMapper.selectScoreLog(log.getmId(), getNextDay(new Date()));
		if (logList.size()>0) {
			//如果表里有累计签到的记录
			Integer continueDay=logList.get(0).getTimes();
			if (continueDay<7) {
				List<MemerScoreLog> memerScoreLogList=memerScoreLogMapper.selectMemberLog(log.getmId(),continueDay);
				if (memerScoreLogList.size()>0) {
					for (MemerScoreLog memerScoreLog : memerScoreLogList) {
						OneWeek oneWeek=new OneWeek();
						oneWeek.setIsAttence(1);
						oneWeek.setScore(memerScoreLog.getScore());
						
						StringBuffer sb = new StringBuffer();
						String[] a = formatter.format(memerScoreLog.getCreateTime()).split("-");
						sb.append(a[1].toString()).append(".").append(a[2].toString());
						
						oneWeek.setDay(sb.toString());
						list.add(oneWeek);
					}
				}
				
				List<OneWeek> myList=selectWeek(logList.get(0).getCreateTime(), continueDay,log.getmId());
				if (myList.size()>0) {
					list.addAll(myList);
				}
			}else{
				//连续7天以上一直签到
				int times=memerScoreLogMapper.selectTime(log.getmId());
				int modelNum=times%7;
				List<MemerScoreLog> memerScoreLogList=memerScoreLogMapper.selectScoreLogByLimit(log.getmId(),modelNum);
				if (memerScoreLogList.size()>0) {
					for (int i=memerScoreLogList.size();i>0;i--) {
						OneWeek oneWeek=new OneWeek();
						oneWeek.setIsAttence(1);
						oneWeek.setScore(memerScoreLogList.get(i-1).getScore());
						StringBuffer sb = new StringBuffer();
						String[] a = formatter.format(memerScoreLogList.get(i-1).getCreateTime()).split("-");
						sb.append(a[1].toString()).append(".").append(a[2].toString());
						oneWeek.setDay(sb.toString());
						list.add(oneWeek);
					}
					List<OneWeek> list1=selectWeekOver(memerScoreLogList.get(0).getCreateTime(), modelNum,log.getmId(),times);
					if(list1.size()>0){
						list.addAll(list1);
					}
				}else{
					List<OneWeek> list1=selectWeekOver(getYesterday(new Date()), modelNum,log.getmId(),times);
					if(list1.size()>0){
						list.addAll(list1);
					}
				}
				
			}
		}else{
			//如果表里没有 累计签到的记录
			List<MemerScoreLog> logList1=memerScoreLogMapper.selectScoreLog(log.getmId(), formatter.format(new Date()));
			//如果今天已经签到过
			if (logList1.size()>0) {
				OneWeek oneWeek=new OneWeek();
				oneWeek.setIsAttence(1);
				oneWeek.setScore(logList1.get(0).getScore());
				
				StringBuffer sb = new StringBuffer();
				String[] a = formatter.format(logList1.get(0).getCreateTime()).split("-");
				sb.append(a[1].toString()).append(".").append(a[2].toString());
				oneWeek.setDay(sb.toString());
				list.add(oneWeek);
				List<OneWeek> list1=selectWeek(new Date(), 1,log.getmId());
				list.addAll(list1);
				
				
			}else{
				//如果今天未签到过
				list=selectWeek(getYesterday(new Date()), 0,log.getmId());
			}
		}
		
		return list;
	}
	
	
	
	//获取昨天的时间
	public static String getNextDay(Date date) {
		try {
			java.text.SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			date = calendar.getTime();
			return formatter.format(date);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public int selectScoreRuleExt(int day) {
		ScoreRuleExt record = new ScoreRuleExt();
		record.setSrId(1);
		record.setExtKey(day);
		List<ScoreRuleExt> extList = scoreRuleExtMapper.selectScoreRuleExtBysrIdAndKey(record);
		if (extList.size()>0) {
			return extList.get(0).getExtValue();
		}else{
			return 0;
		}
		
	}
	
	public List<OneWeek> selectWeekOver(Date oneDay,Integer modelNum,Integer mId,Integer continueDay){
		List<OneWeek> oneWeekList=new ArrayList<>();
		List<String> days = TopicUtils.dateToWeekByDay(oneDay,modelNum);//获取某一天的后7天时间
		if (days.size()>0) {
			for (int i=0;i<days.size();i++) {
				StringBuffer sb = new StringBuffer();
				String[] a = days.get(i).split("-");
				sb.append(a[1].toString()).append(".").append(a[2].toString());
				
				OneWeek oneWeek=new OneWeek();
				int score=selectScoreRuleExt(i+continueDay+1);
				oneWeek.setIsAttence(0);
				oneWeek.setScore(score);
				oneWeek.setDay(sb.toString());
				
				
				// 现在时间
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-M-d");
				Date d = new Date();
				String date = sdf.format(d);
				String[] strs = date.split("-");
				StringBuffer sb1 = new StringBuffer();
				sb1.append(strs[1].toString()).append(".").append(strs[2].toString());
				String now = sb1.toString();
				if(now.equals(oneWeek.getDay())){
					oneWeek.setIsAttence(1);
				}else{
					oneWeek.setIsAttence(0);
				}
				
				oneWeekList.add(oneWeek);
			}
		}
		return oneWeekList;
	}
	
	
	public List<OneWeek> selectWeek(Date oneDay,Integer continueDay,Integer mId){
		List<OneWeek> oneWeekList=new ArrayList<>();
		List<String> days = TopicUtils.dateToWeekByDay(oneDay,continueDay);//获取某一天的后7天时间
		if (days.size()>0) {
			for (int i=0;i<days.size();i++) {
				List<MemerScoreLog> logList=memerScoreLogMapper.selectScoreLog(mId, days.get(i));
				
				if (logList.size()<1) {
					StringBuffer sb = new StringBuffer();
					String[] a = days.get(i).split("-");
					sb.append(a[1].toString()).append(".").append(a[2].toString());
					
					OneWeek oneWeek=new OneWeek();
					int score=selectScoreRuleExt(i+continueDay+1);
					oneWeek.setIsAttence(0);
					oneWeek.setScore(score);
					oneWeek.setDay(sb.toString());
					oneWeekList.add(oneWeek);
				}
				
			}
		}
		return oneWeekList;
	}
	
	
	//获取昨天的时间
	public static Date getYesterday(Date date) {
		try {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			calendar.add(Calendar.DAY_OF_MONTH, -1);
			date = calendar.getTime();
			return date;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
