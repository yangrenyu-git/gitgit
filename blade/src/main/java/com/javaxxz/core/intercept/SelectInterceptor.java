
package com.javaxxz.core.intercept;

import com.javaxxz.core.interfaces.IQuery;
import com.javaxxz.core.interfaces.ISelect;



public class SelectInterceptor implements ISelect{
	
	@Override
	public IQuery userIntercept() {
		return new QueryInterceptor();
	}

	@Override
	public IQuery deptIntercept() {
		return new QueryInterceptor();
	}

	@Override
	public IQuery dictIntercept() {
		return new QueryInterceptor();
	}

	@Override
	public IQuery roleIntercept() {
		return new QueryInterceptor();
	}
	
}
