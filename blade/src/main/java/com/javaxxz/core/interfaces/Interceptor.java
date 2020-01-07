
package com.javaxxz.core.interfaces;

import com.javaxxz.core.aop.Invocation;


public interface Interceptor {
	

	Object intercept(Invocation inv);
}
