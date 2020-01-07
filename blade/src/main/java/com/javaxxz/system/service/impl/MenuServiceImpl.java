
package com.javaxxz.system.service.impl;

import org.springframework.stereotype.Service;

import com.javaxxz.core.base.service.BaseService;
import com.javaxxz.core.plugins.dao.Blade;
import com.javaxxz.core.toolbox.Paras;
import com.javaxxz.system.model.Menu;
import com.javaxxz.system.service.MenuService;

@Service
public class MenuServiceImpl extends BaseService<Menu> implements MenuService {

	@Override
	public int findLastNum(String code) {
		try{
			Blade blade = Blade.create(Menu.class);
			Menu menu = blade.findFirstBy("pCode = #{pCode} order by num desc", Paras.create().set("pCode", code));
			return menu.getNum() + 1;
		}
		catch(Exception ex){
			return 1;
		}
	}

	@Override
	public boolean isExistCode(String code) {
		Blade blade = Blade.create(Menu.class);
		String sql = "select * from tfw_menu where code = #{code}";
		boolean temp = blade.isExist(sql, Paras.create().set("code", code));
		return temp;
	}

	@Override
	public boolean updateStatus(String ids, Object status) {
		Paras paras = Paras.create().set("status", status).set("ids", ids.split(","));
		Blade blade = Blade.create(Menu.class);
		boolean temp = blade.updateBy("status=#{status}", "id in (#{join(ids)})", paras);
		return temp;
	}

}
