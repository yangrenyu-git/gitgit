package com.javaxxz.common.intercept;

import com.javaxxz.core.intercept.SelectInterceptor;
import com.javaxxz.core.interfaces.IQuery;

public class DefaultSelectFactory extends SelectInterceptor {
	
	public IQuery deptIntercept() {
		return new SelectDeptIntercept();
	}
	
	@Override
	public IQuery roleIntercept() {
		return new SelectRoleIntercept();
	}
	
}
