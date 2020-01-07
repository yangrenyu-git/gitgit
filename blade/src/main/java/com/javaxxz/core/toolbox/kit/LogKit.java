

package com.javaxxz.core.toolbox.kit;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.javaxxz.core.toolbox.Func;


public class LogKit {

	private static class Holder {
		private static Logger log = LogManager.getLogger(LogKit.class);
	}


	public static void synchronizeLog() {
		Holder.log = LogManager.getLogger(LogKit.class);
	}


	public static void logNothing(Throwable t) {

	}

	public static void println(String message) {
		System.out.println(message);
	}
	
	public static void println(String message, Object ... values) {
		System.out.println(Func.format(message, values));
	}
	
	public static void println(String message, Map<?, ?> map) {
		System.out.println(Func.format(message, map));
	}

	public static void report(String message) {
		System.out.println("/nBlade report ------------- "+ DateKit.getTime() + " -----------------------------");
		System.out.println("msg	: " + message);
		System.out.println("---------------------------------------------------------------------------------");
	}

	public static void debug(String message) {
		Holder.log.debug(message);
	}

	public static void debug(String message, Throwable t) {
		Holder.log.debug(message, t);
	}

	public static void info(String message) {
		Holder.log.info(message);
	}

	public static void info(String message, Throwable t) {
		Holder.log.info(message, t);
	}

	public static void warn(String message) {
		Holder.log.warn(message);
	}

	public static void warn(String message, Throwable t) {
		Holder.log.warn(message, t);
	}

	public static void error(String message) {
		Holder.log.error(message);
	}

	public static void error(String message, Throwable t) {
		Holder.log.error(message, t);
	}



	public static boolean isDebugEnabled() {
		return Holder.log.isDebugEnabled();
	}

	public static boolean isInfoEnabled() {
		return Holder.log.isInfoEnabled();
	}

}
