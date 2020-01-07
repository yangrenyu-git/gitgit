
package com.javaxxz.core.toolbox.check;

import com.javaxxz.core.constant.Cst;
import com.javaxxz.core.interfaces.ICheck;


public class PermissionCheckManager {
	private final static PermissionCheckManager me = new PermissionCheckManager();

	private ICheck defaultCheckFactory = Cst.me().getDefaultCheckFactory();

	public static PermissionCheckManager me() {
		return me;
	}

	private PermissionCheckManager() {
	}

	public PermissionCheckManager(ICheck checkFactory) {
		this.defaultCheckFactory = checkFactory;
	}

	public void setDefaultCheckFactory(ICheck defaultCheckFactory) {
		this.defaultCheckFactory = defaultCheckFactory;
	}

	public static boolean check(Object[] permissions) {
		return me.defaultCheckFactory.check(permissions);
	}
	
	public static boolean checkAll() {
		return me.defaultCheckFactory.checkAll();
	}
}
