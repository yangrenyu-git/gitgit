
package com.javaxxz.core.plugins;

import java.util.List;

import com.javaxxz.core.interfaces.IPlugin;

public class PluginManager implements IPlugin{
	private static List<IPlugin> plugins = PluginFactory.init().getPlugins();
	
	private static PluginManager me = new PluginManager();
	
	public static PluginManager init(){
		return me;
	}
	
	private PluginManager(){}

	public void start() {
		for(IPlugin plugin : plugins){
			plugin.start();
		}
	}

	public void stop() {
		for(IPlugin plugin : plugins){
			plugin.stop();
		}
		plugins.clear();
	}

}
