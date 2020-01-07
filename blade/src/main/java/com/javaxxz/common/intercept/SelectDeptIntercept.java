package com.javaxxz.common.intercept;

import com.javaxxz.core.aop.AopContext;
import com.javaxxz.core.constant.ConstShiro;
import com.javaxxz.core.intercept.QueryInterceptor;
import com.javaxxz.core.shiro.ShiroKit;
import com.javaxxz.core.toolbox.kit.StrKit;

public class SelectDeptIntercept extends QueryInterceptor {

	public void queryBefore(AopContext ac) {
		if (ShiroKit.lacksRole(ConstShiro.ADMINISTRATOR)) {
			String depts = ShiroKit.getUser().getDeptId() + "," + ShiroKit.getUser().getSubDepts();
			String condition = "where id in (" + StrKit.removeSuffix(depts, ",") + ")";
			ac.setCondition(condition);
		}
	}

}
