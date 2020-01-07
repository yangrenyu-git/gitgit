
package com.javaxxz.system.meta.factory;

import java.util.HashMap;
import java.util.Map;

import com.javaxxz.core.meta.MetaIntercept;
import com.javaxxz.core.meta.MetaManager;
import com.javaxxz.system.meta.intercept.LogIntercept;
import com.javaxxz.system.model.OperationLog;

public class OLogFactory extends MetaManager {

	public Class<? extends MetaIntercept> intercept() {
		return LogIntercept.class;
	}
	
	public String controllerKey() {
		return "olog";
	}

	public String paraPrefix() {
		return getTableName(OperationLog.class);
	}

	public Map<String, String> renderMap() {
		Map<String, String> renderMap = new HashMap<>();
		renderMap.put(KEY_INDEX, "/system/log/olog.html");
		renderMap.put(KEY_ADD, "/system/log/olog_add.html");
		renderMap.put(KEY_EDIT, "/system/log/olog_edit.html");
		renderMap.put(KEY_VIEW, "/system/log/olog_view.html");
		return renderMap;
	}

	public Map<String, String> sourceMap() {
		Map<String, String> sourceMap = new HashMap<>();
		sourceMap.put(KEY_INDEX, "olog.sourceList");
		return sourceMap;
	}

}
