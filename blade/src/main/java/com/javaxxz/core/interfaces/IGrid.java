
package com.javaxxz.core.interfaces;

import com.javaxxz.core.base.controller.BladeController;


public interface IGrid {

	Object paginate(String slaveName, Integer page, Integer rows, String source, String para, String sort, String order, IQuery intercept, BladeController ctrl);
}
