
package com.javaxxz.core.aop;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

public class Invocation {
	private Class<?> clazz;
	private Method method;
	private Object[] args;
	private HttpServletRequest request;

	public Invocation(Class<?> clazz, Method method, Object[] args,
			HttpServletRequest request) {
		this(method, args, request);
		this.clazz = clazz;
	}

	public Invocation(Method method, Object[] args, HttpServletRequest request) {
		super();
		this.method = method;
		this.args = args;
		this.request = request;
	}

	public Class<?> getClazz() {
		return clazz;
	}

	public void setClazz(Class<?> clazz) {
		this.clazz = clazz;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

}
