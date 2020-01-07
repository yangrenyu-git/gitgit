
package com.javaxxz.system.meta.intercept;

import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.ModelAndView;

import com.javaxxz.common.tool.SysCache;
import com.javaxxz.core.aop.AopContext;
import com.javaxxz.core.base.controller.BladeController;
import com.javaxxz.core.constant.ConstCache;
import com.javaxxz.core.constant.ConstShiro;
import com.javaxxz.core.meta.MetaIntercept;
import com.javaxxz.core.plugins.dao.Blade;
import com.javaxxz.core.shiro.ShiroKit;
import com.javaxxz.core.toolbox.Paras;
import com.javaxxz.core.toolbox.ajax.AjaxResult;
import com.javaxxz.core.toolbox.kit.CacheKit;
import com.javaxxz.core.toolbox.support.BladePage;
import com.javaxxz.system.model.Parameter;

public class ParameterIntercept extends MetaIntercept {


	public void renderIndexBefore(AopContext ac) {
		if(ShiroKit.lacksRole(ConstShiro.ADMINISTRATOR)){
			ModelAndView view = ac.getView();
			view.setViewName("redirect:/unauth");
		}
	}
	

	@SuppressWarnings("unchecked")
	public void queryAfter(AopContext ac) {
		BladePage<Map<String, Object>> page = (BladePage<Map<String, Object>>) ac.getObject();
		List<Map<String, Object>> list = page.getRows();
		for (Map<String, Object> map : list) {
			map.put("STATUSNAME", SysCache.getDictName(901, map.get("STATUS")));
		}
	}
	

	public void saveBefore(AopContext ac) {
		BladeController ctrl = ac.getCtrl();
		String code = ctrl.getParameter("tfw_parameter.code");
		int cnt = Blade.create(Parameter.class).count("code = #{code}", Paras.create().set("code", code));
		if(cnt > 0){
			throw new RuntimeException("参数编号已存在!");
		}
	}
	

	public boolean saveAfter(AopContext ac) {
		CacheKit.remove(ConstCache.SYS_CACHE, "parameter_log");
		return super.saveAfter(ac);
	}
	

	public boolean updateAfter(AopContext ac) {
		CacheKit.remove(ConstCache.SYS_CACHE, "parameter_log");
		return super.updateAfter(ac);
	}
	

	public AjaxResult removeSucceed(AopContext ac) {
		CacheKit.remove(ConstCache.SYS_CACHE, "parameter_log");
		return super.removeSucceed(ac);
	}
	

	public AjaxResult delSucceed(AopContext ac) {
		CacheKit.remove(ConstCache.SYS_CACHE, "parameter_log");
		return super.delSucceed(ac);
	}
	

	public AjaxResult restoreSucceed(AopContext ac) {
		CacheKit.remove(ConstCache.SYS_CACHE, "parameter_log");
		return super.restoreSucceed(ac);
	}
	
}
