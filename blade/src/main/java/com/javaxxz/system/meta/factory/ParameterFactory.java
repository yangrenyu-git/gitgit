
package com.javaxxz.system.meta.factory;

import java.util.HashMap;
import java.util.Map;

import com.javaxxz.core.meta.MetaIntercept;
import com.javaxxz.core.meta.MetaManager;
import com.javaxxz.system.meta.intercept.ParameterIntercept;
import com.javaxxz.system.model.Parameter;

public class ParameterFactory extends MetaManager {

	public Class<? extends MetaIntercept> intercept() {
		return ParameterIntercept.class;
	}

	public String controllerKey() {
		return "parameter";
	}

	public String paraPrefix() {
		return getTableName(Parameter.class);
	}

	public Map<String, String> renderMap() {
		Map<String, String> renderMap = new HashMap<>();
		renderMap.put(KEY_INDEX, "/system/parameter/parameter.html");
		renderMap.put(KEY_ADD, "/system/parameter/parameter_add.html");
		renderMap.put(KEY_EDIT, "/system/parameter/parameter_edit.html");
		renderMap.put(KEY_VIEW, "/system/parameter/parameter_view.html");
		return renderMap;
	}

	public Map<String, String> sourceMap() {
		Map<String, String> sourceMap = new HashMap<>();
		sourceMap.put(KEY_INDEX, "parameter.sourceList");
		return sourceMap;
	}

}
