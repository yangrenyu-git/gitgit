
package com.javaxxz.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.javaxxz.core.base.controller.CurdController;
import com.javaxxz.core.interfaces.IMeta;
import com.javaxxz.system.meta.factory.OLogFactory;
import com.javaxxz.system.model.OperationLog;

@Controller
@RequestMapping("/olog")
public class OLogController extends CurdController<OperationLog>{

	@Override
	protected Class<? extends IMeta> metaFactoryClass() {
		
		return OLogFactory.class;
	}

}
