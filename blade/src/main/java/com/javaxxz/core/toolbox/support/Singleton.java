
package com.javaxxz.core.toolbox.support;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.javaxxz.core.toolbox.kit.ClassKit;
import com.javaxxz.core.toolbox.kit.StrKit;


public class Singleton {
	private static Map<Class<?>, Object> pool = new ConcurrentHashMap<Class<?>, Object>();
	private static Map<String, Object> poolName = new ConcurrentHashMap<String, Object>();

	private final static Singleton me = new Singleton();

	public static Singleton me() {
		return me;
	}

	private Singleton() {

	}


	@SuppressWarnings("unchecked")
	public static <T> T create(Class<T> clazz) {
		if (null == clazz) {
			return null;
		}
		T obj = (T) pool.get(clazz);
		if (null == obj) {
			synchronized (Singleton.class) {
				obj = (T) pool.get(clazz);
				if (null == obj) {
					obj = ClassKit.newInstance(clazz);
					pool.put(clazz, obj);
				}
			}
		}
		return obj;
	}
	

	@SuppressWarnings("unchecked")
	public static <T> T create(String className) {
		if (StrKit.isBlank(className)) {
			return null;
		}
		T obj = (T) poolName.get(className);
		if (null == obj) {
			synchronized (Singleton.class) {
				obj = (T) poolName.get(className);
				if (null == obj) {
					obj = ClassKit.newInstance(className);
					poolName.put(className, obj);
				}
			}
		}
		return obj;
	}



	public static void remove(Class<?> clazz) {
		pool.remove(clazz);
	}


	public static void destroy() {
		pool.clear();
	}

}
