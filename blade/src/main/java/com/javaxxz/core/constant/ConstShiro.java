package com.javaxxz.core.constant;

public interface ConstShiro {

	String ADMINISTRATOR = "administrator";

	String ADMIN = "admin";

	String USER = "user";

	String NO_PERMISSION = "当前用户无权操作!";
	

	String NO_USER = "无法获取当前用户,session已过期,请重新登录!";
	

	String NO_METHOD = "请求方法错误!";	
	

	String REDIRECT_UNAUTH = "redirect:/unauth";
}
