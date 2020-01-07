
package com.javaxxz.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.javaxxz.core.base.controller.CurdController;
import com.javaxxz.core.interfaces.IMeta;
import com.javaxxz.system.meta.factory.LLogFactory;
import com.javaxxz.system.model.LoginLog;

@Controller
@RequestMapping("/llog")
public class LLogController extends CurdController<LoginLog>{

	@Override
	protected Class<? extends IMeta> metaFactoryClass() {
		
		return LLogFactory.class;
	}

}
