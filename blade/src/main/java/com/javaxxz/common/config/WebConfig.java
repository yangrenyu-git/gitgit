
package com.javaxxz.common.config;

import com.javaxxz.common.plugins.GlobalPlugin;
import com.javaxxz.core.constant.Cst;
import com.javaxxz.core.interfaces.IConfig;
import com.javaxxz.core.interfaces.IPluginFactroy;
import com.javaxxz.core.shiro.DefaultShiroFactroy;
import com.javaxxz.core.toolbox.file.DefaultFileProxyFactory;
import com.javaxxz.core.toolbox.grid.JqGridFactory;
import com.javaxxz.core.toolbox.kit.DateKit;
import com.javaxxz.core.toolbox.kit.Prop;
import com.javaxxz.core.toolbox.kit.PropKit;

public class WebConfig implements IConfig {


	public void globalConstants(Cst me) {
		Prop prop = PropKit.use("config/config.properties");
		
		//设定开发模式
		me.setDevMode(prop.getBoolean("config.devMode", false));
		
		//设定文件上传是否为远程模式
		me.setRemoteMode(prop.getBoolean("config.remoteMode", false));
		
		//远程上传地址
		me.setRemotePath(prop.get("config.remotePath", ""));
		
		//设定文件上传头文件夹
		me.setUploadPath(prop.get("config.uploadPath", "/upload"));
		
		//设定文件下载头文件夹
		me.setDownloadPath(prop.get("config.downloadPath", "/download"));
		
		//设定grid工厂类
		me.setDefaultGridFactory(new JqGridFactory());
		
		//设定shiro工厂类
		me.setDefaultShiroFactory(new DefaultShiroFactroy());
		
		//设定文件代理工厂类
		me.setDefaultFileProxyFactory(new DefaultFileProxyFactory());
	}


	public void registerPlugins(IPluginFactroy plugins) {
		plugins.register(new GlobalPlugin());
		
		
	}


	public void globalSettings() {
		
	}


	public void afterBladeStart() {
		System.out.println(DateKit.getMsTime() + "	after blade start, you can do something~~~~~~~~~~~~~~~~");
	}

}
