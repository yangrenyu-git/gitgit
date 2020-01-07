
package com.javaxxz.core.listener;

import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

import com.javaxxz.core.plugins.PluginManager;


@Component
public class StopListener implements ApplicationListener<ContextStoppedEvent> {

	@Override
	public void onApplicationEvent(ContextStoppedEvent event) {
		if (event.getApplicationContext().getParent() == null) {
			destroyPlugin();
		}
	}


	private void destroyPlugin() {
		PluginManager.init().stop();
	}

}
