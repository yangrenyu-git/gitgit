
package com.javaxxz.core.toolbox.log;

import com.javaxxz.core.constant.Cst;
import com.javaxxz.core.interfaces.ILog;
import com.javaxxz.core.toolbox.Paras;


public class BladeLogManager {
	private final static BladeLogManager me = new BladeLogManager();

	private ILog defaultLogFactory = Cst.me().getDefaultLogFactory();

	public static BladeLogManager me() {
		return me;
	}

	private BladeLogManager() {
	}

	public BladeLogManager(ILog checkFactory) {
		this.defaultLogFactory = checkFactory;
	}
	
	public ILog getDefaultLogFactory() {
		return defaultLogFactory;
	}

	public void setDefaultLogFactory(ILog defaultLogFactory) {
		this.defaultLogFactory = defaultLogFactory;
	}

	public static String[] logPatten() {
		return me.defaultLogFactory.logPatten();
	}
	
	public static Paras logMaps(){
		return me.defaultLogFactory.logMaps();
	}
	
	public static boolean isDoLog(){
		return me.defaultLogFactory.isDoLog();
	}
	
	public static boolean doLog(String logName, String msg, boolean succeed) {
		return me.defaultLogFactory.doLog(logName, msg, succeed);
	}
}
