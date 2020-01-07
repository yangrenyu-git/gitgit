
package com.javaxxz.system.meta.intercept;

import com.javaxxz.core.aop.Invocation;
import com.javaxxz.core.intercept.BladeValidator;

public class LoginValidator extends BladeValidator {

	@Override
	protected void doValidate(Invocation inv) {
		validateRequired("account", "请输入您的账号");
		validateRequired("password",  "请输入您的密码");
	}
	

}
