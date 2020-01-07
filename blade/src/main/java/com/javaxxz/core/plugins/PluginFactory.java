
package com.javaxxz.core.plugins;

import java.util.ArrayList;
import java.util.List;

import com.javaxxz.core.interfaces.IPlugin;
import com.javaxxz.core.interfaces.IPluginFactroy;

public class PluginFactory implements IPluginFactroy{
	private static List<IPlugin> plugins = new ArrayList<>();
	
	private static PluginFactory me = new PluginFactory();
	
	public static PluginFactory init(){
		return me;
	}
	
	private PluginFactory(){}
	
	public void register(IPlugin plugin) {
		plugins.add(plugin);
	}

	public List<IPlugin> getPlugins() {
		return plugins;
	}

}
