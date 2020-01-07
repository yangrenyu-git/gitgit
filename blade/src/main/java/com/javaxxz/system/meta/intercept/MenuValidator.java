
package com.javaxxz.system.meta.intercept;

import com.javaxxz.core.aop.Invocation;
import com.javaxxz.core.intercept.BladeValidator;
import com.javaxxz.core.plugins.dao.Blade;
import com.javaxxz.core.toolbox.Paras;
import com.javaxxz.core.toolbox.kit.StrKit;
import com.javaxxz.system.model.Menu;

public class MenuValidator extends BladeValidator {

	@Override
	protected void doValidate(Invocation inv) {
		
		if (inv.getMethod().toString().indexOf("update") == -1) {
			validateRequired("tfw_menu.pcode", "请输入菜单父编号");
			validateCode("tfw_menu.code", "菜单编号已存在!");
		}
		validateSql("tfw_menu.source", "含有非法字符,请仔细检查!");
		
	}

	protected void validateCode(String field, String errorMessage) {
		String code = request.getParameter(field);
		if (StrKit.isBlank(code)) {
			addError("请输入菜单编号!");
		}
		Blade blade = Blade.create(Menu.class);
		String sql = "select * from tfw_menu where code = #{code}";
		boolean temp = blade.isExist(sql, Paras.create().set("code", code));
		if (temp) {
			addError(errorMessage);
		}
	}

}
