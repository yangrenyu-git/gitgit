package com.javaxxz.common.intercept;

import com.javaxxz.core.aop.AopContext;
import com.javaxxz.core.constant.ConstShiro;
import com.javaxxz.core.intercept.QueryInterceptor;
import com.javaxxz.core.shiro.ShiroKit;
import com.javaxxz.core.toolbox.kit.StrKit;

public class SelectRoleIntercept extends QueryInterceptor {

	public void queryBefore(AopContext ac) {
		if (ShiroKit.lacksRole(ConstShiro.ADMINISTRATOR)) {
			String roles = ShiroKit.getUser().getRoles() + "," + ShiroKit.getUser().getSubRoles();
			String condition = "where id in (" + StrKit.removeSuffix(roles, ",") + ")";
			ac.setCondition(condition);
		}
	}

}
