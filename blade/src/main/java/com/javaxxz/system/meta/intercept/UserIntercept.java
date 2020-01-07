
package com.javaxxz.system.meta.intercept;

import java.util.List;
import java.util.Map;

import com.javaxxz.common.tool.SysCache;
import com.javaxxz.core.aop.AopContext;
import com.javaxxz.core.meta.PageIntercept;
import com.javaxxz.core.toolbox.support.BladePage;

public class UserIntercept extends PageIntercept {


	@SuppressWarnings("unchecked")
	public void queryAfter(AopContext ac) {
		BladePage<Map<String, Object>> page = (BladePage<Map<String, Object>>) ac.getObject();
		List<Map<String, Object>> list = page.getRows();
		for (Map<String, Object> map : list) {
			map.put("ROLENAME", SysCache.getRoleName(map.get("ROLEID")));
			map.put("STATUSNAME", SysCache.getDictName(901, map.get("STATUS")));
			map.put("SEXNAME", SysCache.getDictName(101, map.get("SEX")));
			map.put("DEPTNAME", SysCache.getDeptName(map.get("DEPTID")));
		}
	}
}
