package com.bh.product.api.controller;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.bh.config.Contants;
import com.bh.enums.BhResultEnum;
import com.bh.goods.pojo.AspUserGuess;
import com.bh.product.api.service.AspGuessService;
import com.bh.result.BhResult;
import com.bh.user.pojo.Member;
import com.bh.utils.LoggerUtil;

@Controller
@RequestMapping("/aspGuess")
public class AspGuessController {
	private static final Logger logger = LoggerFactory.getLogger(AspGuessController.class);
	@Autowired
	private AspGuessService aspGuessService;	
		
	/**
	 * 
	 * @Description: 更新金额与中奖状态
	 * @author xieyc
	 * @date 2018年8月24日 上午10:28:40
	 */
	@RequestMapping("/update")
	@ResponseBody
	public BhResult update(@RequestBody Map<String, String> map,HttpServletRequest request) {
		BhResult r = null;
		try {
			aspGuessService.update(Integer.valueOf(map.get("guessOne")),Integer.valueOf(map.get("guessTwo")));
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(null);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("update" + e.getMessage());
			e.printStackTrace();
		}
		return r;
	}	
	
	
	
	/**
	 * 
	 * @Description: 亚运会竞猜提交
	 * @author xieyc
	 * @date 2018年8月24日 上午10:28:40
	 */
	@RequestMapping("/predictSubmit")
	@ResponseBody
	public BhResult predictSubmit(@RequestBody AspUserGuess entity, HttpServletRequest request) {
		BhResult r = null;
		try {
			HttpSession session = request.getSession(false);
			Member member = null;
			if (session != null) {
				Object obj = session.getAttribute(Contants.USER_INFO_ATTR_KEY);
				if (obj != null) {
					member = (Member) obj; // 获取当前登录用户信息
				}
			}
			if (member != null) {
				entity.setmId(member.getId());
				int row = aspGuessService.predictSubmit(entity);
				if (row == -1) {
					r = BhResult.build(400, "输入数字不符合实际！");
				} else if (row == -2) {
					r = BhResult.build(400, "你已竞猜过,请勿重复提交！");
				} else {
					r = new BhResult();
					r.setStatus(BhResultEnum.SUCCESS.getStatus());
					r.setMsg(BhResultEnum.SUCCESS.getMsg());
					r.setData(row);
				}
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("predictSubmit" + e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	/**
	 * 
	 * @Description: 进入亚运会页面时插入数据（作废）
	 * @author xieyc
	 * @date 2018年8月24日 上午10:28:40
	 */
	@RequestMapping("/loadInsert")
	@ResponseBody
	public BhResult loadInsert(HttpServletRequest request) {
		BhResult r = null;
		try {
			HttpSession session = request.getSession(false);
			Member member = null;
			if (session != null) {
				Object obj = session.getAttribute(Contants.USER_INFO_ATTR_KEY);
				if (obj != null) {
					member = (Member) obj; // 获取当前登录用户信息
				}
			}
			if (member != null) {
				aspGuessService.loadInsert(member.getId());
			} 
			r = new BhResult();
			r.setStatus(BhResultEnum.SUCCESS.getStatus());
			r.setMsg(BhResultEnum.SUCCESS.getMsg());
			r.setData(null);
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("loadInsert" + e.getMessage());
			e.printStackTrace();
		}
		return r;
	}	
	/**
	 * 
	 * @Description: 亚运会竞猜提交(作废)
	 * @author xieyc
	 * @date 2018年8月24日 上午10:28:40
	 */
	@RequestMapping("/predictSubmit1")
	@ResponseBody
	public BhResult predictSubmit1(@RequestBody AspUserGuess entity, HttpServletRequest request) {
		BhResult r = null;
		try {
			HttpSession session = request.getSession(false);
			Member member = null;
			if (session != null) {
				Object obj = session.getAttribute(Contants.USER_INFO_ATTR_KEY);
				if (obj != null) {
					member = (Member) obj; // 获取当前登录用户信息
				}
			}
			if (member != null) {
				if(!member.getOpenid().equals("0")){
					entity.setmId(member.getId());
					int row = aspGuessService.predictSubmit1(entity);
					if(row==-1){
						r = BhResult.build(400, "2次竞猜机会已用完！");
					}else if(row==-2){
						r = BhResult.build(400, "1次竞猜机会已用完！");
					}else if(row==-3){
						r = BhResult.build(400, "输入数字不符合实际！");
					}else{
						r = new BhResult();
						r.setStatus(BhResultEnum.SUCCESS.getStatus());
						r.setMsg(BhResultEnum.SUCCESS.getMsg());
						r.setData(row);
					}
				}else{
					r = BhResult.build(400, "此活动只限微信登入用户！");
				}
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("predictSubmit" + e.getMessage());
			e.printStackTrace();
		}
		return r;
	}
	
	/**
	 * 
	 * @Description: 亚运会猜中提现接口
	 * @author xieyc
	 * @date 2018年8月24日 上午10:28:40
	 */
	@RequestMapping("/withdraw")
	@ResponseBody
	public BhResult withdraw(@RequestBody AspUserGuess entity, HttpServletRequest request) {
		BhResult r = null;
		try {
			HttpSession session = request.getSession(false);
			Member member = null;
			if (session != null) {
				Object obj = session.getAttribute(Contants.USER_INFO_ATTR_KEY);
				if (obj != null) {
					member = (Member) obj; // 获取当前登录用户信息
				}
			}
			if (member != null) {
				entity.setmId(member.getId());
				int row = aspGuessService.withdraw(entity);
				if (row == -1) {
					r = BhResult.build(400, "未中奖！");
				} else if (row == -2) {
					r = BhResult.build(400, "银行卡信息不匹配！");
				}else if (row == -3) {
					r = BhResult.build(400, "你已经申请过提现了，请勿重复申请！");
				}else if (row == -4) {
					r = BhResult.build(400, "你的可提现金额为0 ！");
				} else {
					r = new BhResult();
					r.setStatus(BhResultEnum.SUCCESS.getStatus());
					r.setMsg(BhResultEnum.SUCCESS.getMsg());
					r.setData(row);
				}
			} else {
				r = new BhResult();
				r.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				r.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
			}
		} catch (Exception e) {
			r = BhResult.build(500, "操作异常!");
			logger.error("withdraw" + e.getMessage());
			e.printStackTrace();
		}
		return r;
	}

	/**
	 * @Description: 获取用户信息活动期间消费的总金额
	 * @author fanjh
	 * @date 2018年8月24日 上午10:30:42
	 */
	@RequestMapping("/getCountMonet")
	@ResponseBody
	public BhResult getCountMonet( HttpServletRequest request) {
		BhResult result = null;
		try {
			HttpSession session = request.getSession(false);
		    Member member = null;
			if(session!=null) {
			     member = (Member) session.getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			}
			/*if(member==null){
				result = new BhResult();
				result.setStatus(BhResultEnum.LOGIN_FAIL.getStatus());
				result.setMsg(BhResultEnum.LOGIN_FAIL.getMsg());
				return result;
			}*/
			Map<String, Object> map=aspGuessService.selectByMember(member);
			if (map != null) {
				result = new BhResult(200, "查询成功", map);
			} else {
				result = BhResult.build(400, "查询失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}
	
	
	/**
	 * @Description: 获取活动用户信息
	 * @author fanjh
	 * @date 2018年8月24日 上午10:30:42
	 */
	/*@RequestMapping("/getMemberInfo")
	@ResponseBody
	public BhResult getMemberInfo(@RequestBody AspUserGuess entity, HttpServletRequest request) {
		BhResult result = null;
		try {
			HttpSession session = request.getSession(false);
		    Member member = new Member();
			if(session!=null) {
				 member = (Member) session.getAttribute(Contants.USER_INFO_ATTR_KEY); //获取当前登录用户信息
			}
			Map<String, Object> map=aspGuessService.getMemberInfo(entity,member);
			if (map != null) {
				result = new BhResult(200, "查询成功", map);
			} else {
				result = BhResult.build(400, "查询失败！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = BhResult.build(500, "数据库搜索失败!");
			LoggerUtil.getLogger().error(e.getMessage());
		}
		return result;
	}*/
	
	
	
	
}
