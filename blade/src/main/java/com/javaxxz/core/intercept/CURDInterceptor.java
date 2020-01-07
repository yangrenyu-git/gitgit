
package com.javaxxz.core.intercept;

import com.javaxxz.core.aop.AopContext;
import com.javaxxz.core.interfaces.ICURD;
import com.javaxxz.core.toolbox.ajax.AjaxResult;


public class CURDInterceptor implements ICURD{

	@Override
	public void saveBefore(AopContext ac) {
		
	}

	@Override
	public boolean saveAfter(AopContext ac) {
		
		return true;
	}

	@Override
	public AjaxResult saveSucceed(AopContext ac) {
		
		return null;
	}

	@Override
	public void updateBefore(AopContext ac) {
		
	}

	@Override
	public boolean updateAfter(AopContext ac) {
		
		return true;
	}

	@Override
	public AjaxResult updateSucceed(AopContext ac) {
		
		return null;
	}

	@Override
	public void removeBefore(AopContext ac) {
		
	}

	@Override
	public boolean removeAfter(AopContext ac) {
		
		return true;
	}

	@Override
	public AjaxResult removeSucceed(AopContext ac) {
		
		return null;
	}

	@Override
	public void delBefore(AopContext ac) {
		
	}

	@Override
	public boolean delAfter(AopContext ac) {
		
		return true;
	}

	@Override
	public AjaxResult delSucceed(AopContext ac) {
		
		return null;
	}

	@Override
	public void restoreBefore(AopContext ac) {
		
	}

	@Override
	public boolean restoreAfter(AopContext ac) {

		return true;
	}

	@Override
	public AjaxResult restoreSucceed(AopContext ac) {

		return null;
	}

}
