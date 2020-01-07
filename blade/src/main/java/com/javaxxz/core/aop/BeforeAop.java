
package com.javaxxz.core.aop;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.javaxxz.core.annotation.Before;
import com.javaxxz.core.interfaces.Interceptor;
import com.javaxxz.core.toolbox.kit.HttpKit;




@Aspect
@Component
public class BeforeAop {

	@Pointcut(value = "@annotation(com.javaxxz.core.annotation.Before)")
	private void cutBefore() {

	}

	@Around("cutBefore()")
	public Object doBefore(ProceedingJoinPoint point) throws Throwable {
		HttpServletRequest request = HttpKit.getRequest();
		MethodSignature ms = (MethodSignature) point.getSignature();
		Method method = ms.getMethod();
		Object[] args = point.getArgs();
		Class<?> clazz = point.getTarget().getClass();
		Before before = method.getAnnotation(Before.class);
		Interceptor ic = before.value().newInstance();
		Object result = ic.intercept(new Invocation(clazz, method, args, request));
		if (null == result) {
			return point.proceed();
		} else {
			return result;
		}
	}
}
