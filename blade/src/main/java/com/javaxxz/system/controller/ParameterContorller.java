
package com.javaxxz.system.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.javaxxz.core.base.controller.CurdController;
import com.javaxxz.core.interfaces.IMeta;
import com.javaxxz.system.meta.factory.ParameterFactory;
import com.javaxxz.system.model.Parameter;

@Controller
@RequestMapping("/parameter")
public class ParameterContorller extends CurdController<Parameter>{

	@Override
	protected Class<? extends IMeta> metaFactoryClass() {
		
		return ParameterFactory.class;
	}
	
}
