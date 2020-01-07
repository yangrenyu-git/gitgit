
package com.javaxxz.core.meta;

import java.util.Map;

import com.javaxxz.core.constant.ConstCurd;
import com.javaxxz.core.interfaces.IMeta;

public abstract class MetaManager extends MetaTool implements IMeta,ConstCurd{

	public Class<? extends MetaIntercept> intercept() {
		return null;
	}

	public String paraPrefix() {
		return null;
	}

	public Map<String, Object> switchMap() {
		return null;
	}

}
