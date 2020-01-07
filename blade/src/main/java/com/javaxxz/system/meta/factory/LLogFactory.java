
package com.javaxxz.system.meta.factory;

import java.util.HashMap;
import java.util.Map;

import com.javaxxz.core.meta.MetaIntercept;
import com.javaxxz.core.meta.MetaManager;
import com.javaxxz.system.meta.intercept.LogIntercept;
import com.javaxxz.system.model.LoginLog;

public class LLogFactory extends MetaManager {

	public Class<? extends MetaIntercept> intercept() {
		return LogIntercept.class;
	}
	
	public String controllerKey() {
		return "llog";
	}

	public String paraPrefix() {
		return getTableName(LoginLog.class);
	}

	public Map<String, String> renderMap() {
		Map<String, String> renderMap = new HashMap<>();
		renderMap.put(KEY_INDEX, "/system/log/llog.html");
		renderMap.put(KEY_ADD, "/system/log/llog_add.html");
		renderMap.put(KEY_EDIT, "/system/log/llog_edit.html");
		renderMap.put(KEY_VIEW, "/system/log/llog_view.html");
		return renderMap;
	}

	public Map<String, String> sourceMap() {
		Map<String, String> sourceMap = new HashMap<>();
		sourceMap.put(KEY_INDEX, "llog.sourceList");
		return sourceMap;
	}

}
