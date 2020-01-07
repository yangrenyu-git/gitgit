
package com.javaxxz.core.meta;

import com.javaxxz.core.aop.AopContext;
import com.javaxxz.core.constant.ConstCurd;
import com.javaxxz.core.interfaces.ICURD;
import com.javaxxz.core.interfaces.IQuery;
import com.javaxxz.core.interfaces.IRender;
import com.javaxxz.core.toolbox.ajax.AjaxResult;


public class MetaIntercept extends MetaTool implements IQuery, IRender, ICURD{

	protected AjaxResult result = new AjaxResult();


	public void queryBefore(AopContext ac) {

	}


	public void queryAfter(AopContext ac) {

	}


	public void renderIndexBefore(AopContext ac) {

	}


	public void renderAddBefore(AopContext ac) {

	}


	public void renderEditBefore(AopContext ac) {

	}


	public void renderViewBefore(AopContext ac) {

	}


	public void saveBefore(AopContext ac) {

	}


	public boolean saveAfter(AopContext ac) {
		return true;
	}


	public AjaxResult saveSucceed(AopContext ac) {
		return result.addSuccess(ConstCurd.SAVE_SUCCESS_MSG);
	}


	public void updateBefore(AopContext ac) {

	}


	public boolean updateAfter(AopContext ac) {
		return true;
	}


	public AjaxResult updateSucceed(AopContext ac) {
		return result.addSuccess(ConstCurd.UPDATE_SUCCESS_MSG);
	}


	public void removeBefore(AopContext ac) {

	}


	public boolean removeAfter(AopContext ac) {
		return true;
	}


	public AjaxResult removeSucceed(AopContext ac) {
		return result.addSuccess(ConstCurd.DEL_SUCCESS_MSG);
	}
	

	public void delBefore(AopContext ac) {

	}


	public boolean delAfter(AopContext ac) {
		return true;
	}
	

	public AjaxResult delSucceed(AopContext ac) {
		return result.addSuccess(ConstCurd.DEL_SUCCESS_MSG);
	}
	

	public void restoreBefore(AopContext ac) {

	}


	public boolean restoreAfter(AopContext ac) {
		return true;
	}
	

	public AjaxResult restoreSucceed(AopContext ac) {
		return result.addSuccess(ConstCurd.RESTORE_SUCCESS_MSG);
	}
	
}
