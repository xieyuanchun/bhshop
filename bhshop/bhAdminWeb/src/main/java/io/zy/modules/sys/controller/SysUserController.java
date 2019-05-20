package io.zy.modules.sys.controller;

import io.zy.common.aliyun.oss.util.Upload;
import io.zy.common.annotation.SysLog;
import io.zy.common.utils.Constant;
import io.zy.common.utils.PageUtils;
import io.zy.common.utils.Query;
import io.zy.common.utils.R;
import io.zy.common.validator.Assert;
import io.zy.common.validator.ValidatorUtils;
import io.zy.common.validator.group.AddGroup;
import io.zy.common.validator.group.UpdateGroup;
import io.zy.modules.sys.entity.SysUserEntity;
import io.zy.modules.sys.service.SysUserRoleService;
import io.zy.modules.sys.service.SysUserService;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

/**
 * 系统用户
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月31日 上午10:40:10
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
	@Autowired
	private SysUserService sysUserService;
	@Autowired
	private SysUserRoleService sysUserRoleService;
	
	/**
	 * 所有用户列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("sys:user:list")
	public R list(@RequestParam Map<String, Object> params){
		//只有超级管理员，才能查看所有管理员列表
		if(getUserId() != Constant.SUPER_ADMIN){
			params.put("createUserId", getUserId());
		}
		
		//查询列表数据
		Query query = new Query(params);
		List<SysUserEntity> userList = sysUserService.queryList(query);
		int total = sysUserService.queryTotal(query);
		
		PageUtils pageUtil = new PageUtils(userList, total, query.getLimit(), query.getPage());
		
		return R.ok().put("page", pageUtil);
	}
	
	/**
	 * 获取登录的用户信息
	 */
	@RequestMapping("/info")
	public R info(){
		return R.ok().put("user", getUser());
	}
	
	/**
	 * 修改登录用户密码 、名字 ,2018.3.27 zlk 更改
	 */
// 	@SysLog("修改密码")
	@RequestMapping("/password")
	public R password(String password,String newPassword,String username){
//		Assert.isBlank(newPassword, "新密码不为能空");

		//sha256加密
	    password = new Sha256Hash(password, getUser().getSalt()).toHex();
        if(!StringUtils.isBlank(newPassword)){
		    newPassword = new Sha256Hash(newPassword, getUser().getSalt()).toHex();
        }
		//sha256加密
		//更新密码
		int count = sysUserService.updatePassword(getUserId(), password, newPassword,username);
		if(count == 0){
			return R.error("原密码不正确");
		}
	    return R.ok();
	     
	}
	
	/**
	 * 用户信息
	 */
	@RequestMapping("/info/{userId}")
	@RequiresPermissions("sys:user:info")
	public R info(@PathVariable("userId") Long userId){
		SysUserEntity user = sysUserService.queryObject(userId);
		
		//获取用户所属的角色列表
		List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
		user.setRoleIdList(roleIdList);
		
		return R.ok().put("user", user);
	}
	
	/**
	 * 保存用户
	 */
	@SysLog("保存用户")
	@RequestMapping("/save")
	@RequiresPermissions("sys:user:save")
	public R save(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, AddGroup.class);
		
		user.setCreateUserId(getUserId());
		sysUserService.save(user);
		
		return R.ok();
	}
	
	/**
	 * 修改用户 zlk 2018.3.28
	 */
	@SysLog("修改用户")
	@RequestMapping("/update")
	@RequiresPermissions("sys:user:update")
	public R update(@RequestBody SysUserEntity user){
		ValidatorUtils.validateEntity(user, UpdateGroup.class);
		SysUserEntity s = sysUserService.queryObject(user.getUserId());
		if(user.getPassword()!=null){
		     String password = new Sha256Hash(user.getPassword(), s.getSalt()).toHex();
		     user.setPassword(password);
		}
		user.setCreateUserId(getUserId());
		
		sysUserService.update(user);
		return R.ok();
	}
	/**
	 * 修改用户头像
	 */
	@RequestMapping("/updateHeadImg")
	public R updateHeadImg(MultipartFile files[],
			HttpServletRequest request){
		R r = new R();
		String path = request.getSession().getServletContext()
				.getRealPath("/");//获得files目录的绝对路径
		for(MultipartFile file:files){
			StringBuffer realPath = new StringBuffer(Constant.bucketHttp); 
			StringBuffer key = new StringBuffer();
			
			String fileName = file.getOriginalFilename();//获得上传文件的实际名称
			String[] types = fileName.split("\\.");
			String type = types[types.length - 1];
			type = "." + type;//获得文件的后缀名
			String uuid = UUID.randomUUID().toString().toUpperCase().replace("-", "");
			String targetFileName = uuid + type;
			File targetFile = new File(path, targetFileName);
			
			if (!targetFile.exists()) {
				targetFile.mkdirs();
			}
			try {
				file.transferTo(targetFile);//保存文件
				
				key.append("headimg/");
		        key.append(targetFileName);
				Upload myupload= new Upload();
		        String localFilePath = path + targetFileName;
		 		boolean bl=myupload.singleupload("aliyun",localFilePath,key.toString());
		 		realPath.append("headimg/");
				realPath.append(targetFileName);

				if(bl){
					r.put("headUrl", realPath.toString());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		return r;
	}
	
	/**
	 * 删除用户
	 */
	@SysLog("删除用户")
	@RequestMapping("/delete")
	@RequiresPermissions("sys:user:delete")
	public R delete(@RequestBody Long[] userIds){
		if(ArrayUtils.contains(userIds, 1L)){
			return R.error("系统管理员不能删除");
		}
		
		if(ArrayUtils.contains(userIds, getUserId())){
			return R.error("当前用户不能删除");
		}
		
		sysUserService.deleteBatch(userIds);
		
		return R.ok();
	}
}
