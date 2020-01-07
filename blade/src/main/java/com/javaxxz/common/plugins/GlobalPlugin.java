
package com.javaxxz.common.plugins;

import com.javaxxz.core.interfaces.IPlugin;

public class GlobalPlugin implements IPlugin {

	public void start() {
		System.out.println("\n插件启动测试");
	}

	public void stop() {
		System.out.println("\n插件关闭测试");
	}

}
