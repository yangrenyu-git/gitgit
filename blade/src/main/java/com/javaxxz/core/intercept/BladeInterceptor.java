
package com.javaxxz.core.intercept;

import com.javaxxz.core.interfaces.Interceptor;
import com.javaxxz.core.toolbox.ajax.AjaxResult;

public abstract class BladeInterceptor implements Interceptor{
	protected AjaxResult result = new AjaxResult();

	protected Object invoke(){
		return null;
	}
	
}
