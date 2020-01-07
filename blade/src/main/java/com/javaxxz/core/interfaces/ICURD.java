
package com.javaxxz.core.interfaces;

import com.javaxxz.core.aop.AopContext;
import com.javaxxz.core.toolbox.ajax.AjaxResult;


public interface ICURD {

	void saveBefore(AopContext ac);


	boolean saveAfter(AopContext ac);


	AjaxResult saveSucceed(AopContext ac);


	void updateBefore(AopContext ac);


	boolean updateAfter(AopContext ac);


	AjaxResult updateSucceed(AopContext ac);


	void removeBefore(AopContext ac);


	boolean removeAfter(AopContext ac);


	AjaxResult removeSucceed(AopContext ac);
	

	void delBefore(AopContext ac);


	boolean delAfter(AopContext ac);
	

	AjaxResult delSucceed(AopContext ac);
	

	void restoreBefore(AopContext ac);


	boolean restoreAfter(AopContext ac);
	

	AjaxResult restoreSucceed(AopContext ac);
}
