
package com.javaxxz.core.beetl.tag;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.beetl.core.Tag;

import com.javaxxz.common.vo.TreeNode;
import com.javaxxz.core.constant.ConstCache;
import com.javaxxz.core.constant.Cst;
import com.javaxxz.core.plugins.dao.Db;
import com.javaxxz.core.toolbox.Func;
import com.javaxxz.core.toolbox.Paras;

public class SideBarTag extends Tag {
	
	private List<TreeNode> nodeList = new ArrayList<TreeNode>();
	
	@Override
	@SuppressWarnings("unchecked")
	public void render() {
		try {
			String MENU_CACHE = ConstCache.MENU_CACHE;
			Map<String, String> param = (Map<String, String>) args[1];

			final Object userId = param.get("userId");
			final Object roleId = param.get("roleId");
			String ctxPath =Cst.me().getContextPath();

			Map<String, Object> userRole = Db.selectOneByCache(MENU_CACHE, "role_ext_" + userId, "select * from TFW_ROLE_EXT where USERID=#{userId}", Paras.create().set("userId", userId));

			String roleIn = "0";
			String roleOut = "0";
			if (!Func.isEmpty(userRole)) {
				Paras rd = Paras.parse(userRole);
				roleIn = rd.getStr("ROLEIN");
				roleOut = rd.getStr("ROLEOUT");
			}
			final StringBuilder sql = new StringBuilder();
			
			sql.append("select * from TFW_MENU  ");
			sql.append(" where ( ");
			sql.append("	 (status=1)");
			sql.append("	 and (icon is not null and icon not LIKE '%btn%' and icon not LIKE '%icon%') ");
			sql.append("	 and (id in (select menuId from TFW_RELATION where roleId in (#{join(roleId)})) or id in (#{join(roleIn)}))");
			sql.append("	 and id not in(#{join(roleOut)})");
			sql.append("	)");
			sql.append(" order by levels,pCode,num");
			
			@SuppressWarnings("rawtypes")
			List<Map> sideBar = Db.selectListByCache(MENU_CACHE, "sideBar_" + userId, sql.toString(),
					Paras.create()
					.set("roleId", roleId.toString().split(","))
					.set("roleIn", roleIn.split(","))
					.set("roleOut", roleOut.split(",")));
			
			for (Map<String, Object> side : sideBar) {
				TreeNode node = new TreeNode();
				Paras rd = Paras.parse(side);
				node.setId(rd.getStr("CODE"));
				node.setParentId(rd.getStr("PCODE"));
				node.setName(rd.getStr("NAME"));
				node.setIcon(rd.getStr("ICON"));
				node.setIsParent(false);
				nodeList.add(node);
			}

			new TreeNode().buildNodes(nodeList);

			StringBuilder sb = new StringBuilder();

			for (Map<String, Object> side : sideBar) {
				if (Func.toInt(side.get("LEVELS")) == 1) {
					String firstMenu = "";
					String subMenu = "";
					String href = Func.isEmpty(side.get("URL")) ? "#" : ctxPath + side.get("URL") + "";
					String addtabs = Func.isEmpty(side.get("URL")) ? "" : "data-addtabs=\"" + side.get("CODE") + "\"";

					firstMenu += "<li >";
					firstMenu += "	<a data-url=\"" + href + "\" " + addtabs + " data-title=\"" + side.get("NAME") + "\" data-icon=\"fa " + side.get("ICON") + "\" class=\"" + getDropDownClass(Func.toStr(side.get("CODE")),"dropdown-toggle") + " tmsp-pointer\">";
					firstMenu += "		<i class=\"menu-icon fa " + side.get("ICON") + "\"></i>";
					firstMenu += "		<span class=\"menu-text\">" + side.get("NAME") + "</span>";
					firstMenu += "		<b class=\"arrow " + getDropDownClass(Func.toStr(side.get("CODE")),"fa fa-angle-down") + "\"></b>";
					firstMenu += "	</a>";
					firstMenu += "	<b class=\"arrow\"></b>";

					subMenu = this.reloadMenu(sideBar, Func.toStr(side.get("CODE")), firstMenu, 1, ctxPath);// 寻找子菜单

					sb.append(subMenu);
				}
			}
			
			ctx.byteWriter.writeString(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String reloadMenu(List<Map> sideBar, String pCode, String pStr, int levels, String ctxPath) {
		String Str = "";
		String subStr = "";
		for (Map subside : sideBar) {
			Paras rd = Paras.parse(subside);
			int _levels = rd.getInt("LEVELS");
			String _code = rd.getStr("CODE");
			String _pCode = rd.getStr("PCODE");
			String _url = rd.getStr("URL");
			String _icon = rd.getStr("ICON");
			String _name = rd.getStr("NAME");
			if ((_pCode.equals(pCode) && _levels > levels)) {
				String href = Func.isEmpty(_url) ? "#" : ctxPath + _url + "";
				String addtabs = Func.isEmpty(_url) ? "" : "data-addtabs=\"" + _code + "\"";

				Str += "<li>";
				Str += "	<a data-url=\"" + href + "\" " + addtabs + " data-title=\"" + _name + "\" data-icon=\"fa " + _icon + "\" class=\"" + getDropDownClass(_code, "dropdown-toggle") + " tmsp-pointer\">";
				Str += "		<i class=\"menu-icon fa " + _icon + "\"></i>";
				Str += _name;
				Str += "		<b class=\"arrow " + getDropDownClass(_code,"fa fa-angle-down") + "\"></b>";
				Str += "	</a>";
				Str += "	<b class=\"arrow\"></b>";

				subStr = this.reloadMenu(sideBar, _code, Str, _levels, ctxPath);// 递归寻找子菜单

				Str = Func.isEmpty(subStr) ? Str : subStr;
			}

		}
		if (Str.length() > 0) {
			pStr += (Func.isEmpty(pStr)) ? Str : "<ul class=\"submenu\">" + Str + "</ul>";
			pStr += "</li>";
			return pStr;
		} else {
			return "";
		}

	}
	
	public String getDropDownClass(String code,String dropdownclass){
		Iterator<TreeNode> it = nodeList.iterator();
		while (it.hasNext()) {
			TreeNode n = (TreeNode) it.next();
			if(n.getId().equals(code)&&n.isParent()){
				return dropdownclass;
			}
		}
		return "";
	}

}
