
package com.javaxxz.system.meta.intercept;

import java.util.List;
import java.util.Map;

import com.javaxxz.common.tool.SysCache;
import com.javaxxz.core.aop.AopContext;
import com.javaxxz.core.constant.ConstCache;
import com.javaxxz.core.meta.MetaIntercept;
import com.javaxxz.core.toolbox.Func;
import com.javaxxz.core.toolbox.Paras;
import com.javaxxz.core.toolbox.kit.CacheKit;
import com.javaxxz.core.toolbox.support.BladePage;

public class LogIntercept extends MetaIntercept {

	@SuppressWarnings("unchecked")
	public void queryAfter(AopContext ac) {
		BladePage<Map<String, Object>> page = (BladePage<Map<String, Object>>) ac.getObject();
		List<Map<String, Object>> list = page.getRows();
		for (Map<String, Object> map : list) {
			String succeedName = (Func.toInt(map.get("SUCCEED"), 1) == 1) ? "成功" : "失败";
			map.put("SUCCEEDNAME", succeedName);
			map.put("USERNAME", SysCache.getUserName(map.get("USERID")));
		}
	}


	public void renderViewBefore(AopContext ac) {
		Paras rd = (Paras) ac.getObject();
		String succeedName = (rd.getInt("succeed") == 1) ? "成功" : "失败";
		rd.set("succeedName", succeedName).set("userName", SysCache.getUserName(rd.get("userid")));
	}
	
	
	

	public boolean saveAfter(AopContext ac) {
		CacheKit.removeAll(ConstCache.SYS_CACHE);
		return true;
	}
	

	public boolean updateAfter(AopContext ac) {
		CacheKit.removeAll(ConstCache.SYS_CACHE);
		return true;
	}

	

	public boolean removeAfter(AopContext ac) {
		CacheKit.removeAll(ConstCache.SYS_CACHE);
		return true;
	}
	
}
