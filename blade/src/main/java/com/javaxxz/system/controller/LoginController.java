
package com.javaxxz.system.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.javaxxz.common.base.BaseController;
import com.javaxxz.core.annotation.Before;
import com.javaxxz.core.constant.Const;
import com.javaxxz.core.plugins.dao.Blade;
import com.javaxxz.core.shiro.ShiroKit;
import com.javaxxz.core.toolbox.Func;
import com.javaxxz.core.toolbox.ajax.AjaxResult;
import com.javaxxz.core.toolbox.captcha.Captcha;
import com.javaxxz.core.toolbox.kit.LogKit;
import com.javaxxz.core.toolbox.log.BladeLogManager;
import com.javaxxz.system.meta.intercept.LoginValidator;
import com.javaxxz.system.model.LoginLog;

@Controller
public class LoginController extends BaseController implements Const{

	private static Logger log = LogManager.getLogger(LoginController.class);

	@RequestMapping("/")
	public String index() {
		return INDEX_REALPATH;
	}
	

	@GetMapping("/login")
	public String login() {
		if (ShiroKit.isAuthenticated()) {
			return REDIRECT + "/";
		}
		return LOGIN_REALPATH;
	}


	@Before(LoginValidator.class)
	@ResponseBody
	@PostMapping("/login")
	public AjaxResult login(HttpServletRequest request, HttpServletResponse response) {
		String account = getParameter("account");
		String password = getParameter("password");
		String imgCode = getParameter("imgCode");
		if (!Captcha.validate(request, response, imgCode)) {
			return error("验证码错误");
		}
		Subject currentUser = ShiroKit.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(account, password.toCharArray());
		token.setRememberMe(true);
		try {
			currentUser.login(token);
			Session session = ShiroKit.getSession();
			LogKit.println("\nsessionID	: {} ", session.getId());
			LogKit.println("sessionHost	: {}", session.getHost());
			LogKit.println("sessionTimeOut	: {}", session.getTimeout());
		} catch (UnknownAccountException e) {
			log.error("账号不存在!", e);
			return error("账号不存在");
		} catch (DisabledAccountException e) {
			log.error("账号未启用!", e);
			return error("账号未启用");
		} catch (IncorrectCredentialsException e) {
			log.error("密码错误!", e);
			return error("密码错误");
		} catch (RuntimeException e) {
			log.error("未知错误,请联系管理员!", e);
			return error("未知错误,请联系管理员");
		}
		doLog(ShiroKit.getSession(), "登录");
		return success("登录成功");
	}

	@RequestMapping("/logout")
	public String logout() {
		doLog(ShiroKit.getSession(), "登出");
		Subject currentUser = ShiroKit.getSubject();
		currentUser.logout();
		return REDIRECT + "/login";
	}

	@RequestMapping("/unauth")
	public String unauth() {
		if (ShiroKit.notAuthenticated()) {
			return REDIRECT + "/login";
		}
		return NOPERMISSION_PATH;
	}

	@RequestMapping("/captcha")
	public void captcha(HttpServletResponse response) {
		Captcha.init(response).render();
	}

	public void doLog(Session session, String type){
		if(!BladeLogManager.isDoLog()){
			return;
		}
		try{
			LoginLog log = new LoginLog();
			String msg = Func.format("[sessionID]: {} [sessionHost]: {} [sessionHost]: {}", session.getId(), session.getHost(), session.getTimeout());
			log.setLogname(type);
			log.setMethod(msg);
			log.setCreatetime(new Date());
			log.setSucceed("1");
			log.setUserid(Func.toStr(ShiroKit.getUser().getId()));
			Blade.create(LoginLog.class).save(log);
		}catch(Exception ex){
			LogKit.logNothing(ex);
		}
	}
	
}
