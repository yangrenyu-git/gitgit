
package com.javaxxz.core.interfaces;

import com.javaxxz.core.aop.AopContext;


public interface IQuery {


	void queryBefore(AopContext ac);


	void queryAfter(AopContext ac);
	
}
