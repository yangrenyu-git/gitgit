
package com.javaxxz.system.meta.intercept;

import com.javaxxz.core.aop.Invocation;
import com.javaxxz.core.intercept.BladeValidator;
import com.javaxxz.core.plugins.dao.Blade;
import com.javaxxz.core.toolbox.Paras;
import com.javaxxz.core.toolbox.kit.StrKit;
import com.javaxxz.system.model.Dict;

public class DictValidator extends BladeValidator {

	@Override
	protected void doValidate(Invocation inv) {
		validateDict("该字典序号已存在!");
	}

	protected void validateDict(String errorMessage) {
		String num = request.getParameter("tfw_dict.num");
		if (StrKit.notBlank(num)) {
			String code = "";
			String id = request.getParameter("tfw_dict.id");
			if (StrKit.notBlank(id)) {
				Dict dict = Blade.create(Dict.class).findById(id);
				code = dict.getCode();
			} else {
				code = request.getParameter("tfw_dict.code");
			}
			
			boolean temp = Blade.create(Dict.class).isExist("select * from tfw_dict where code = #{code} and num = #{num}", Paras.create().set("code", code).set("num", num));
			
			if (temp) {
				addError(errorMessage);
			}
		} 
	}

}
