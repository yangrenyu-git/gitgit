
package com.javaxxz.core.shiro;

import com.javaxxz.core.constant.Cst;
import com.javaxxz.core.interfaces.IShiro;

public class ShiroManager {
	private static ShiroManager me = new ShiroManager();
	private IShiro defaultShiroFactory = Cst.me().getDefaultShiroFactory();
	
	public static ShiroManager me() {
		return me;
	}
	
	private ShiroManager(){
		
	}

	public IShiro getDefaultShiroFactory() {
		return defaultShiroFactory;
	}

	public void setDefaultShiroFactory(IShiro defaultShiroFactory) {
		this.defaultShiroFactory = defaultShiroFactory;
	}


}
