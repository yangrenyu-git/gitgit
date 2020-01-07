
package com.javaxxz.core.interfaces;

import com.javaxxz.core.aop.AopContext;


public interface IRender {


	public void renderIndexBefore(AopContext ac);


	public void renderAddBefore(AopContext ac);


	public void renderEditBefore(AopContext ac);


	public void renderViewBefore(AopContext ac);
}
