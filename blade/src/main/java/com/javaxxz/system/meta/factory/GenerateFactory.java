
package com.javaxxz.system.meta.factory;

import java.util.HashMap;
import java.util.Map;

import com.javaxxz.core.meta.MetaManager;
import com.javaxxz.system.model.Generate;

public class GenerateFactory extends MetaManager {
	
	public String controllerKey() {
		return "generate";
	}

	public String paraPrefix() {
		return getTableName(Generate.class);
	}

	public Map<String, String> renderMap() {
		Map<String, String> renderMap = new HashMap<>();
		renderMap.put(KEY_INDEX, "/system/generate/generate.html");
		renderMap.put(KEY_ADD, "/system/generate/generate_add.html");
		renderMap.put(KEY_EDIT, "/system/generate/generate_edit.html");
		renderMap.put(KEY_VIEW, "/system/generate/generate_view.html");
		return renderMap;
	}

	public Map<String, String> sourceMap() {
		Map<String, String> sourceMap = new HashMap<>();
		sourceMap.put(KEY_INDEX, "generate.sourceList");
		return sourceMap;
	}

}
