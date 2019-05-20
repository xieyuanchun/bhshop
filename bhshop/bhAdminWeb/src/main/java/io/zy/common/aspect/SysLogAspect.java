package io.zy.common.aspect;

import com.alibaba.druid.support.json.JSONUtils;
import com.google.gson.Gson;
import io.zy.common.annotation.SysLog;
import io.zy.common.utils.HttpContextUtils;
import io.zy.common.utils.IPUtils;
import io.zy.common.utils.RedisUtils;
import io.zy.common.utils.ShiroUtils;
import io.zy.modules.sys.entity.SysLogEntity;
import io.zy.modules.sys.entity.SysUserEntity;
import io.zy.modules.sys.service.SysLogService;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.Map;


/**
 * 系统日志，切面处理类
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017年3月8日 上午11:07:35
 */
@Aspect
@Component
public class SysLogAspect {
	@Autowired
	private SysLogService sysLogService;
	@Autowired
	private RedisUtils redisUtils;
	
	@Pointcut("@annotation(io.zy.common.annotation.SysLog)")
	public void logPointCut() { 
		
	}

	@Around("logPointCut()")
	public Object around(ProceedingJoinPoint point) throws Throwable {
		long beginTime = System.currentTimeMillis();
		//执行方法
		Object result = point.proceed();
		//执行时长(毫秒)
		long time = System.currentTimeMillis() - beginTime;

		//保存日志
		saveSysLog(point, time);

		return result;
	}

	private void saveSysLog(ProceedingJoinPoint joinPoint, long time) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		Method method = signature.getMethod();

		SysLogEntity sysLog = new SysLogEntity();
		SysLog syslog = method.getAnnotation(SysLog.class);
		if(syslog != null){
			//注解上的描述
			sysLog.setOperation(syslog.value());
		}

		//请求的方法名
		String className = joinPoint.getTarget().getClass().getName();
		String methodName = signature.getName();
		sysLog.setMethod(className + "." + methodName + "()");

		//请求的参数
		Object[] args = joinPoint.getArgs();
		try{
			String params = new Gson().toJson(args[0]);
			sysLog.setParams(params);
		}catch (Exception e){

		}

		//获取request
		HttpServletRequest request = HttpContextUtils.getHttpServletRequest();
		//设置IP地址
		sysLog.setIp(IPUtils.getIpAddr(request));

		//用户名
		Subject sub=SecurityUtils.getSubject();
		Object obj=sub.getPrincipal();
		if(obj!=null) {
			String username = ((SysUserEntity) SecurityUtils.getSubject().getPrincipal()).getUsername();
			sysLog.setUsername(username);

			sysLog.setTime(time);
			sysLog.setCreateDate(new Date());
			//保存系统日志
			sysLogService.save(sysLog);
		}
		
	}

	
}
