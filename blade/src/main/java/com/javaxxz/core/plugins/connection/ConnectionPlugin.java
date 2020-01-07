
package com.javaxxz.core.plugins.connection;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.beetl.sql.core.SQLManager;

import com.javaxxz.core.config.BladeConfig;
import com.javaxxz.core.interfaces.IPlugin;

public class ConnectionPlugin implements IPlugin{

	private static Map<String, SQLManager> pool = new ConcurrentHashMap<String, SQLManager>();
	
	public String MASTER = "master";
	
	public Map<String, SQLManager> getPool(){
		return pool;
	}
	
	private ConnectionPlugin() { }
	
	private static ConnectionPlugin me = new ConnectionPlugin();
	
	public static ConnectionPlugin init(){
		return me;
	}
	
	public void start() {
		try {
			for(String key : BladeConfig.getPool().keySet()){
				SQLManager sm = BladeConfig.getPool().get(key);
				pool.put(key, sm);
			}
			if(!pool.containsKey(MASTER)){
				throw new RuntimeException("BladeConfig必须注入key值为master的sqlManager!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stop() {
		pool.clear();
	}

}
