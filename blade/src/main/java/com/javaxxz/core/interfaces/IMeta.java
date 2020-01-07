
package com.javaxxz.core.interfaces;

import java.util.Map;

import com.javaxxz.core.meta.MetaIntercept;


public interface IMeta {
	

	Class<? extends MetaIntercept> intercept();
	

	String controllerKey();
	

	String paraPrefix();


	Map<String, Object> switchMap();


	Map<String, String> renderMap();


	Map<String, String> sourceMap();

}
