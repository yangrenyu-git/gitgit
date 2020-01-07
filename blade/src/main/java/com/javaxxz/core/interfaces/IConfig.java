
package com.javaxxz.core.interfaces;

import com.javaxxz.core.constant.Cst;




public interface IConfig {
	

	void registerPlugins(IPluginFactroy plugins);	
	

	void globalConstants(Cst me);
	

	void globalSettings();
	

	void afterBladeStart();
	
}
