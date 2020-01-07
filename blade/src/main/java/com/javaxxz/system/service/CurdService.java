
package com.javaxxz.system.service;

import com.javaxxz.core.base.controller.BladeController;
import com.javaxxz.core.meta.MetaIntercept;

public interface CurdService {

	public boolean save(BladeController ctrl, Object model, Class<?> modelClass, MetaIntercept intercept);


	public boolean update(BladeController ctrl,Object model, Class<?> modelClass, MetaIntercept intercept);


	public boolean deleteByIds(BladeController ctrl,String ids, Class<?> modelClass, MetaIntercept intercept);
	

	public boolean delByIds(BladeController ctrl,String ids, Class<?> modelClass, MetaIntercept intercept);
	

	public boolean restoreByIds(BladeController ctrl,String ids, Class<?> modelClass, MetaIntercept intercept);
	

	public Object paginate(Integer page, Integer rows, String source, String para, String sort, String order, MetaIntercept intercept, BladeController ctrl);
}
