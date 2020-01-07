
package com.javaxxz.core.beetl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;

import com.javaxxz.core.beetl.func.BeetlExt;
import com.javaxxz.core.beetl.func.ShiroExt;
import com.javaxxz.core.beetl.tag.DropDownTag;
import com.javaxxz.core.beetl.tag.FootTag;
import com.javaxxz.core.beetl.tag.HotBlogsTag;
import com.javaxxz.core.beetl.tag.SelectTag;
import com.javaxxz.core.beetl.tag.SideBarTag;
import com.javaxxz.core.constant.ConstConfig;
import com.javaxxz.core.constant.Cst;
import com.javaxxz.core.toolbox.Paras;


public class BeetlTemplate {
	private static GroupTemplate gt;
	
	public static GroupTemplate getGt() {
		return gt;
	}

	static {
		if (gt == null) {
			StringTemplateResourceLoader resourceLoader = new StringTemplateResourceLoader();
			Configuration cfg = null;
			try {
				cfg = Configuration.defaultConfiguration();
			} catch (IOException e) {
				e.printStackTrace();
			}
			gt = new GroupTemplate(resourceLoader, cfg);
			registerTemplate(gt);
		}
	}

	public static void registerTemplate(GroupTemplate groupTemplate){
		Map<String, Object> sharedVars = new HashMap<String, Object>();
		sharedVars.put("startTime", new Date());
		sharedVars.put("domain", ConstConfig.DOMAIN);
		groupTemplate.setSharedVars(sharedVars);

		groupTemplate.registerTag("hot", HotBlogsTag.class);
		groupTemplate.registerTag("select", SelectTag.class);
		groupTemplate.registerTag("sidebar", SideBarTag.class);
		groupTemplate.registerTag("dropdown", DropDownTag.class);
		groupTemplate.registerTag("foot", FootTag.class);

		groupTemplate.registerFunctionPackage("func", new BeetlExt());
		groupTemplate.registerFunctionPackage("shiro", new ShiroExt());
	}
	
	public static String build(String str, Map<String, Object> paras) {
		Template t = gt.getTemplate(str);
		if (null == paras) {
			paras = Paras.create();
		}
		paras.put("ctxPath", Cst.me().getContextPath());
		for (String o : paras.keySet()) {
			t.binding(o, paras.get(o));
		}
		return t.render();
	}

	public static void buildTo(String str, Map<String, Object> paras, PrintWriter pw) {
		Template t = gt.getTemplate(str);
		if (null == paras) {
			paras = Paras.create();
		}
		paras.put("ctxPath", Cst.me().getContextPath());
		for (String o : paras.keySet()) {
			t.binding(o, paras.get(o));
		}
		t.renderTo(pw);
	}
}
