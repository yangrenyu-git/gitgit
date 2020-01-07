
package com.javaxxz.system.meta.intercept;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.javaxxz.common.tool.SysCache;
import com.javaxxz.core.aop.AopContext;
import com.javaxxz.core.constant.ConstConfig;
import com.javaxxz.core.constant.Cst;
import com.javaxxz.core.meta.MetaIntercept;
import com.javaxxz.core.plugins.dao.Db;
import com.javaxxz.core.toolbox.Func;
import com.javaxxz.core.toolbox.Paras;
import com.javaxxz.core.toolbox.support.BladePage;

public class AttachIntercept extends MetaIntercept {

	@SuppressWarnings("unchecked")
	public void queryAfter(AopContext ac) {
		BladePage<Map<String, Object>> page = (BladePage<Map<String, Object>>) ac.getObject();
		List<Map<String, Object>> list = page.getRows();
		for (Map<String, Object> map : list) {
			map.put("ATTACHURL", ConstConfig.DOMAIN + map.get("URL"));
			map.put("STATUSNAME", SysCache.getDictName(902, map.get("STATUS")));
			map.put("CREATERNAME", SysCache.getUserName(map.get("CREATER")));
		}
	}


	public void renderViewBefore(AopContext ac) {
		Paras ps = (Paras) ac.getObject();
		ps.set("attachUrl", ConstConfig.DOMAIN + ps.get("url"))
		.set("statusName", SysCache.getDictName(902, ps.get("status")))
		.set("createrName", SysCache.getUserName(ps.get("creater")));
	}
	

	@SuppressWarnings("unchecked")
	public void removeBefore(AopContext ac) {
		Map<String, Object> file = Db.findById("TFW_ATTACH", ac.getId().toString());
		if (Func.isEmpty(file)) {
			throw new RuntimeException("文件不存在!");
		} else {
			String url = file.get("URL").toString();
			File f = new File(Cst.me().getUploadRealPath() + url);
			if(null == f || !f.isFile()){
				throw new RuntimeException("文件不存在!");
			}
			f.delete();
		}
	}
	
}
