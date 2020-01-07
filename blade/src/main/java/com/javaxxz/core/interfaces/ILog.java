
package com.javaxxz.core.interfaces;

import com.javaxxz.core.toolbox.Paras;


public interface ILog {
	

	String[] logPatten();
	

	Paras logMaps();
	

	boolean isDoLog();
	

	boolean doLog(String logName, String msg, boolean succeed);
}
