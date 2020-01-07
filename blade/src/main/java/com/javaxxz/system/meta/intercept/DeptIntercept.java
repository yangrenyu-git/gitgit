package com.javaxxz.system.meta.intercept;

import com.javaxxz.core.aop.AopContext;
import com.javaxxz.core.constant.ConstShiro;
import com.javaxxz.core.meta.PageIntercept;
import com.javaxxz.core.shiro.ShiroKit;
import com.javaxxz.core.toolbox.kit.StrKit;

public class DeptIntercept extends PageIntercept {

	public void queryBefore(AopContext ac) {
		if (ShiroKit.lacksRole(ConstShiro.ADMINISTRATOR)) {
			String depts = ShiroKit.getUser().getDeptId() + "," + ShiroKit.getUser().getSubDepts();
			String condition = "and id in (" + StrKit.removeSuffix(depts, ",") + ")";
			ac.setCondition(condition);
		}
	}

}
