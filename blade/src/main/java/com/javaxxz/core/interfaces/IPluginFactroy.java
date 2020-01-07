
package com.javaxxz.core.interfaces;

import java.util.List;


public interface IPluginFactroy {
	

	void register(IPlugin plugin);


	List<IPlugin> getPlugins();
	
}
