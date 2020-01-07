
package com.javaxxz.core.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.javaxxz.core.config.BladeConfig;
import com.javaxxz.core.constant.Cst;
import com.javaxxz.core.interfaces.IPluginFactroy;
import com.javaxxz.core.plugins.PluginFactory;
import com.javaxxz.core.plugins.PluginManager;
import com.javaxxz.core.plugins.connection.ConnectionPlugin;


@Component
public class StartupListener implements ApplicationListener<ContextRefreshedEvent> {

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			globalConstants(Cst.me());
			registerPlugins();
			globalSettings();
			afterBladeStart();
		}
	}
	

	private void globalConstants(Cst me){
		BladeConfig.getConf().globalConstants(me);
	}


	private void registerPlugins() {
		IPluginFactroy plugins = PluginFactory.init();
		plugins.register(ConnectionPlugin.init());
		BladeConfig.getConf().registerPlugins(plugins);//自定义配置插件	
		PluginManager.init().start();
	}
	

	private void globalSettings(){
		BladeConfig.getConf().globalSettings();
	}
	

	private void afterBladeStart(){
		BladeConfig.getConf().afterBladeStart();
	}
	
}
