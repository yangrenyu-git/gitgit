
package com.javaxxz.system.meta.intercept;

import com.javaxxz.core.aop.Invocation;
import com.javaxxz.core.intercept.BladeValidator;
import com.javaxxz.core.plugins.dao.Blade;
import com.javaxxz.core.toolbox.Paras;
import com.javaxxz.core.toolbox.kit.StrKit;
import com.javaxxz.system.model.User;

public class UserValidator extends BladeValidator {

	@Override
	protected void doValidate(Invocation inv) {
		
		if (inv.getMethod().toString().indexOf("update") == -1) {
			validateAccount("TFW_USER.ACCOUNT", "账号已存在");
			validateStringExt("TFW_USER.ACCOUNT",  "含有非法字符,请检查");
			validateRequired("TFW_USER.ACCOUNT",  "请输入账号");
			validateString("TFW_USER.ACCOUNT", 5, 11,  "请输入5~11位的账号");
		}
		validateStringExt("TFW_USER.NAME",  "含有非法字符,请检查");
		validateRequired("TFW_USER.NAME",  "请输入姓名");
		
		validateRequired("TFW_USER.BIRTHDAY",  "请选择出生日期");
		validateDate("TFW_USER.BIRTHDAY",  "请输入正确的日期格式");
		
		validateRequired("TFW_USER.PASSWORD",  "请输入密码");
		
		validateRequired("password",  "请输入确认密码");
		validateTwoEqual("TFW_USER.PASSWORD", "password",  "两次密码不相同");
	}

	protected void validateAccount(String field, String errorMessage) {
		String account = request.getParameter(field);
		if (StrKit.isBlank(account)) {
			addError("请输入账号!");
		}
		if (Blade.create(User.class).isExist("SELECT * FROM tfw_user WHERE account = #{account} and status=1", Paras.create().set("account", account))) {
			addError(errorMessage);
		}
	}

}
