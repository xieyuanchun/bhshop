package com.order.aspect;
import com.bh.config.Contants;
import com.bh.enums.UserTypeEnum;
import com.bh.order.pojo.Order;
import com.bh.order.pojo.OrderLog;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
import com.bh.user.pojo.MemberShopAdmin;
import com.bh.utils.HttpContextUtils;
import com.order.annotaion.OrderLogAnno;
import com.order.shop.service.OrderLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * 
 * @author xxj
 *
 */
@Aspect
@Component
public class OrderLogAspect {
	@Autowired
	private OrderLogService orderLogService;
	
	@Pointcut("@annotation(com.order.annotaion.OrderLogAnno)")
	public void logPointCut() { 
		
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		Object result = point.proceed();
		long time = System.currentTimeMillis() - beginTime;
		
		Order  order = transResult(result);
		if(order!=null){
			saveOrderLog(point,order);
		}
		return result;
	}
	/**
	 * 转换结果
	 * @param result
	 * @return
	 */
    private Order transResult(Object result){
    	if(result instanceof BhResult){
			BhResult bhRlt = (BhResult)result;
			Object data = bhRlt.getData();
			if(data instanceof Order){
				Order order = (Order)data;
				return order;
			}
		}
    	return null;
    }
    /**
     * 保存订单日志
     * @param point
     * @param order
     */
	private void saveOrderLog(ProceedingJoinPoint point,Order order){
		MethodSignature signature = (MethodSignature) point.getSignature();
		Method method = signature.getMethod();
		OrderLog orderLog = new OrderLog();
		OrderLogAnno orderLogAnno = method.getAnnotation(OrderLogAnno.class);
		if(orderLogAnno != null){
			orderLog.setAction(orderLogAnno.value());
			HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
			HttpSession session = request.getSession(false);
			Object obj = (Object)session.getAttribute(Contants.USER_INFO_ATTR_KEY);
			
			if(obj!=null){
				if(obj instanceof Member || obj instanceof MemberShopAdmin){
					if(obj instanceof Member){
						Member m = (Member)obj;
						int mId = m.getId();
						orderLog.setUserId(mId);
						orderLog.setAdminUser(m.getUsername());
						int typ = m.getType();
						UserTypeEnum userTypeEnum = getUserTypEnum(typ);
						orderLog.setUserType(userTypeEnum.getDesc());
						orderLog.setUserTable(userTypeEnum.getUserTable());
					}else{
						MemberShopAdmin m = (MemberShopAdmin)obj;
						int mId = m.getId();
						orderLog.setUserId(mId);
						orderLog.setAdminUser(m.getUsername());
						orderLog.setUserType(UserTypeEnum.SELLER.getDesc());
						orderLog.setUserTable(UserTypeEnum.SELLER.getUserTable());
					}
					orderLog.setOrderId(order.getId());
					orderLog.setOrderNo(order.getOrderNo());
					orderLog.setOrderStatus(order.getStatus());
					orderLog.setAddtime(new Date());
					orderLogService.insertSelective(orderLog);
				}
			}else if(orderLogAnno.debug()){
				orderLog.setUserId(66);
				orderLog.setAdminUser("xxj");
				orderLog.setNote("调试!!!");
				orderLog.setUserType("测试用户");
				orderLog.setUserTable("测试表");
				orderLog.setOrderId(order.getId());
				orderLog.setOrderNo(order.getOrderNo());
				orderLog.setOrderStatus(order.getStatus());
				orderLog.setAddtime(new Date());
				orderLogService.insertSelective(orderLog);
			}
			
		} 
	}
	/**
	 * 获取用户类型
	 * @param typ
	 * @return
	 */
	private UserTypeEnum getUserTypEnum(int typ){
		UserTypeEnum userTypeEnum = UserTypeEnum.DEFAULT;
		switch (typ) {
		case 1:
			userTypeEnum = UserTypeEnum.BUYER;
		    break;
		case 2:
			userTypeEnum = UserTypeEnum.SELLER;
			break;
		case 3:
			userTypeEnum = UserTypeEnum.SENDER;
			break;
		default:
			break;
		}
	   return userTypeEnum;
	}
}
