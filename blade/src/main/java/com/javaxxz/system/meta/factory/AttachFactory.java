
package com.javaxxz.system.meta.factory;

import java.util.HashMap;
import java.util.Map;

import com.javaxxz.core.meta.MetaIntercept;
import com.javaxxz.core.meta.MetaManager;
import com.javaxxz.system.meta.intercept.AttachIntercept;
import com.javaxxz.system.model.Attach;

public class AttachFactory extends MetaManager {

	public Class<? extends MetaIntercept> intercept() {
		return AttachIntercept.class;
	}
	
	public String controllerKey() {
		return "attach";
	}

	public String paraPrefix() {
		return getTableName(Attach.class);
	}

	public Map<String, String> renderMap() {
		Map<String, String> renderMap = new HashMap<>();
		renderMap.put(KEY_INDEX, "/system/attach/attach.html");
		renderMap.put(KEY_ADD, "/system/attach/attach_add.html");
		renderMap.put(KEY_EDIT, "/system/attach/attach_edit.html");
		renderMap.put(KEY_VIEW, "/system/attach/attach_view.html");
		return renderMap;
	}

	public Map<String, String> sourceMap() {
		Map<String, String> sourceMap = new HashMap<>();
		sourceMap.put(KEY_INDEX, "attach.sourceList");
		return sourceMap;
	}

}
