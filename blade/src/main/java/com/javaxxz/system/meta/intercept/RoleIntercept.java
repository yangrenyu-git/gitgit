package com.javaxxz.system.meta.intercept;

import com.javaxxz.core.aop.AopContext;
import com.javaxxz.core.constant.ConstShiro;
import com.javaxxz.core.meta.PageIntercept;
import com.javaxxz.core.shiro.ShiroKit;
import com.javaxxz.core.toolbox.kit.StrKit;

public class RoleIntercept extends PageIntercept {

	public void queryBefore(AopContext ac) {
		if (ShiroKit.lacksRole(ConstShiro.ADMINISTRATOR)) {
			String roles = ShiroKit.getUser().getRoles() + "," + ShiroKit.getUser().getSubRoles();
			String condition = "and id in (" + StrKit.removeSuffix(roles, ",") + ")";
			ac.setCondition(condition);
		}
	}

}
