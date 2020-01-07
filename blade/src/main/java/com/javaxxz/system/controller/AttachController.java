
package com.javaxxz.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.javaxxz.core.base.controller.CurdController;
import com.javaxxz.core.interfaces.IMeta;
import com.javaxxz.system.meta.factory.AttachFactory;
import com.javaxxz.system.model.Attach;

@Controller
@RequestMapping("/attach")
public class AttachController extends CurdController<Attach>{

	@Override
	protected Class<? extends IMeta> metaFactoryClass() {
		
		return AttachFactory.class;
	}

}
