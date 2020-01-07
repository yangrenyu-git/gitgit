
package com.javaxxz.core.listener;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.javaxxz.core.constant.Const;
import com.javaxxz.core.toolbox.kit.PropKit;

public class ConfigListener implements ServletContextListener {

	public static final Map<String, String> map = new HashMap<String, String>();

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		map.clear();
	}

	@Override
	public void contextInitialized(ServletContextEvent evt) {
		ServletContext sc = evt.getServletContext();
		// 项目路径
		map.put("realPath", sc.getRealPath("/").replaceFirst("/", ""));
		map.put("contextPath", sc.getContextPath());

		Properties prop = PropKit.use(Const.PROPERTY_FILE).getProperties();
		for (Object name : prop.keySet()) {
			map.put(name.toString(), prop.get(name).toString());
		}
	}

}
