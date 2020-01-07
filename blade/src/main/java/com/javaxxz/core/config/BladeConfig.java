
package com.javaxxz.core.config;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.beetl.sql.core.SQLManager;

import com.javaxxz.core.interfaces.IConfig;


public class BladeConfig {

	private static Map<String, SQLManager> pool = new ConcurrentHashMap<String, SQLManager>();
	
	private BladeConfig(){}
	
	private static IConfig conf = null;

	public static IConfig getConf() {
		if(null == conf){
			throw new RuntimeException("BladeConfig未注入,请在applicationContext.xml中定义bladeConfig!");
		}
		return conf;
	}

	public static Map<String, SQLManager> getPool(){
		if(null == pool){
			throw new RuntimeException("sqlManagerMap未注入,请在applicationContext.xml中定义sqlManagerMap!");
		}
		return pool;
	} 
	

	public void setConf(IConfig config) {
		conf = config;
	}
	

	public void setSqlManager(Map<String, SQLManager> map){
		pool.putAll(map);
	}
	
}
