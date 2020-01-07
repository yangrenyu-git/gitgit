
package com.javaxxz.core.beetl.func;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.javaxxz.common.tool.SysCache;
import com.javaxxz.core.constant.ConstCache;
import com.javaxxz.core.constant.ConstCacheKey;
import com.javaxxz.core.constant.ConstConfig;
import com.javaxxz.core.interfaces.ILoader;
import com.javaxxz.core.plugins.dao.Db;
import com.javaxxz.core.toolbox.Func;
import com.javaxxz.core.toolbox.Paras;
import com.javaxxz.core.toolbox.kit.CacheKit;
import com.javaxxz.core.toolbox.kit.CharsetKit;
import com.javaxxz.core.toolbox.kit.DateKit;
import com.javaxxz.core.toolbox.kit.DateTimeKit;
import com.javaxxz.core.toolbox.kit.StrKit;
import com.javaxxz.core.toolbox.kit.URLKit;
import com.javaxxz.core.toolbox.support.Convert;


public class BeetlExt {


	public boolean equals(Object obj1, Object obj2) {
		return (obj1 != null) ? (obj1.equals(obj2)) : (obj2 == null);
	}


	public int length(Object obj) {
		if (obj == null) {
			return 0;
		}
		if (obj instanceof CharSequence) {
			return ((CharSequence) obj).length();
		}
		if (obj instanceof Collection) {
			return ((Collection<?>) obj).size();
		}
		if (obj instanceof Map) {
			return ((Map<?, ?>) obj).size();
		}

		int count;
		if (obj instanceof Iterator) {
			Iterator<?> iter = (Iterator<?>) obj;
			count = 0;
			while (iter.hasNext()) {
				count++;
				iter.next();
			}
			return count;
		}
		if (obj instanceof Enumeration) {
			Enumeration<?> enumeration = (Enumeration<?>) obj;
			count = 0;
			while (enumeration.hasMoreElements()) {
				count++;
				enumeration.nextElement();
			}
			return count;
		}
		if (obj.getClass().isArray() == true) {
			return Array.getLength(obj);
		}
		return -1;
	}


	public boolean contains(Object obj, Object element) {
		if (obj == null) {
			return false;
		}
		if (obj instanceof String) {
			if (element == null) {
				return false;
			}
			return ((String) obj).contains(element.toString());
		}
		if (obj instanceof Collection) {
			return ((Collection<?>) obj).contains(element);
		}
		if (obj instanceof Map) {
			return ((Map<?, ?>) obj).values().contains(element);
		}

		if (obj instanceof Iterator) {
			Iterator<?> iter = (Iterator<?>) obj;
			while (iter.hasNext()) {
				Object o = iter.next();
				if (equals(o, element)) {
					return true;
				}
			}
			return false;
		}
		if (obj instanceof Enumeration) {
			Enumeration<?> enumeration = (Enumeration<?>) obj;
			while (enumeration.hasMoreElements()) {
				Object o = enumeration.nextElement();
				if (equals(o, element)) {
					return true;
				}
			}
			return false;
		}
		if (obj.getClass().isArray() == true) {
			int len = Array.getLength(obj);
			for (int i = 0; i < len; i++) {
				Object o = Array.get(obj, i);
				if (equals(o, element)) {
					return true;
				}
			}
		}
		return false;
	}


	@SuppressWarnings("rawtypes")
	public boolean isEmpty(Object o) {
		if (o == null) {
			return true;
		}
		if (o instanceof String) {
			if (o.toString().trim().equals("")) {
				return true;
			}
		} else if (o instanceof List) {
			if (((List) o).size() == 0) {
				return true;
			}
		} else if (o instanceof Map) {
			if (((Map) o).size() == 0) {
				return true;
			}
		} else if (o instanceof Set) {
			if (((Set) o).size() == 0) {
				return true;
			}
		} else if (o instanceof Object[]) {
			if (((Object[]) o).length == 0) {
				return true;
			}
		} else if (o instanceof int[]) {
			if (((int[]) o).length == 0) {
				return true;
			}
		} else if (o instanceof long[]) {
			if (((long[]) o).length == 0) {
				return true;
			}
		}
		return false;
	}


	public boolean isOneEmpty(Object... os) {
		for (Object o : os) {
			if (isEmpty(o)) {
				return true;
			}
		}
		return false;
	}


	public boolean isAllEmpty(Object... os) {
		for (Object o : os) {
			if (!isEmpty(o)) {
				return false;
			}
		}
		return true;
	}


	public boolean isNum(Object obj) {
		try {
			Integer.parseInt(obj.toString());
		} catch (Exception e) {
			return false;
		}
		return true;
	}


	public String format(String template, Object... values) {
		return StrKit.format(template, values);
	}


	public String format(String template, Map<?, ?> map) {
		return StrKit.format(template, map);
	}


	public String toStr(Object str) {
		if (null == str) {
			return null;
		}
		return str.toString().trim();
	}


	public int toInt(Object value) {
		return toInt(value, -1);
	}


	public int toInt(Object value, int defaultValue) {
		return Convert.toInt(value, defaultValue);
	}


	public long toLong(Object value) {
		return toLong(value, -1);
	}


	public long toLong(Object value, long defaultValue) {
		return Convert.toLong(value, defaultValue);
	}

	public String encodeUrl(String url) {
		return URLKit.encode(url, CharsetKit.UTF_8);
	}

	public String decodeUrl(String url) {
		return URLKit.decode(url, CharsetKit.UTF_8);
	}


	public StringBuilder builder(String... strs) {
		final StringBuilder sb = new StringBuilder();
		for (String str : strs) {
			sb.append(str);
		}
		return sb;
	}


	public boolean like(String type, String _type) {
		if (type.indexOf(_type) >= 0) {
			return true;
		} else {
			return false;
		}
	}


	public void builder(StringBuilder sb, String... strs) {
		for (String str : strs) {
			sb.append(str);
		}
	}


	public String getTime() {
		return DateKit.getTime();
	}


	public String getMsTime() {
		return DateKit.getMsTime();
	}
	

	public String getTime(Date date) {
		return DateKit.getTime(date);
	}
	

	public Date parseTime(String date) {
		return DateKit.parseTime(date);
	}


	public Date parse(String date, String pattern) {
		return DateKit.parse(date, pattern);
	}


	public String format(String pattern) {
		return DateKit.format(new Date(), pattern);
	}
	

	public String format(Date date, String pattern) {
		return DateKit.format(date, pattern);
	}
	

	public static Date parse(String dateStr) {
		return DateTimeKit.parse(dateStr).toDate();
	}


	public String getRightMenu(final Object userId, Object roleId,final String code) {
		Map<String, Object> userRole = CacheKit.get(ConstCache.MENU_CACHE, ConstCacheKey.ROLE_EXT + userId,
				new ILoader() {
					public Object load() {
						return Db.selectOne("select * from TFW_ROLE_EXT where userId=#{userId}", Paras.create().set("userId", userId));
					}
				});

		String roleIn = "0";
		String roleOut = "0";
		if (!Func.isEmpty(userRole)) {
			roleIn = Func.toStr(userRole.get("ROLEIN"));
			roleOut = Func.toStr(userRole.get("ROLEOUT"));
		}
		
		final StringBuilder sql = new StringBuilder();
		sql.append("select TFW_MENU.* ,(select name from TFW_MENU where code=#{code}) as PNAME  from TFW_MENU");
		sql.append(" where ( ");
		sql.append("	 (status=1)");
		sql.append("	 and (icon is not null and (icon like '%btn%' or icon like '%icon%' ) ) ");
		sql.append("	 and (url like '%add%' or url like '%edit%' or url like '%remove%'  or url like '%del%' or url like '%view%' ) ");
		sql.append("	 and (pCode=#{code})");
		sql.append("	 and (id in (select menuId from TFW_RELATION where roleId in ("
				+ roleId + ")) or id in (" + roleIn + "))");
		sql.append("	 and id not in(" + roleOut + ")");
		sql.append("	)");
		sql.append(" order by num");

		List<Map<String, Object>> btnList = CacheKit.get(ConstCache.MENU_CACHE, ConstCacheKey.RIGHT_MENU + code + "_" + userId, new ILoader() {
			public Object load() {
				return Db.selectList(sql.toString(), Paras.create().set("code", code));
			}
		});

		StringBuilder rightsb = new StringBuilder();
		rightsb.append("<ul style=\"width: 200px;\">");
		for (Map<String, Object> btn : btnList) {
			rightsb.append("	<li id=\"rightMenu_" + Func.toStr(btn.get("CODE")).split("_")[1] + "\"> ");
			rightsb.append("		<i class=\"ace-icon " + Func.toStr(btn.get("ICON")).split("\\|")[1].replace("bigger-120", "") + "\" style=\"width:15px;\"></i> ");
			rightsb.append("		<span style=\"font-size:12px; font-family:Verdana;color:#777;padding-left:5px; \">" + Func.toStr(btn.get("NAME")) + "</span>");
			rightsb.append("	</li>");
		}
		rightsb.append("<span style=\"padding:0px;margin:1px auto;display:block;clean:both;height:1px;border-top:1px dotted #B6C0C9;\"></span>");
		rightsb.append("	<li id=\"rightMenu_refresh\"> ");
		rightsb.append("		<i class=\"ace-icon fa fa-refresh\" style=\"width:15px;\"></i> ");
		rightsb.append("		<span style=\"font-size:12px; font-family:Verdana;color:#777;padding-left:5px;  \">刷新</span>");
		rightsb.append("	</li>");
		rightsb.append("	<li id=\"rightMenu_excel\"> ");
		rightsb.append("		<i class=\"ace-icon fa fa-file-excel-o\" style=\"width:15px;\"></i> ");
		rightsb.append("		<span style=\"font-size:12px; font-family:Verdana;color:#777;padding-left:5px; \">导出 excel</span>");
		rightsb.append("	</li>");
		rightsb.append("</ul>");
		return rightsb.toString();
	}
	
	
	public String getDictName(final Object code, final Object num) {
		return SysCache.getDictName(code, num);
	}

	public String getRoleName(final Object roleId) {
		return SysCache.getRoleName(roleId);
	}

	public String getUserName(final Object userId) {
		return SysCache.getUserName(userId);
	}

	public String getDeptName(final Object deptId) {
		return SysCache.getDeptName(deptId);
	}
	
	public boolean isOracle() {
		return (ConstConfig.DBTYPE.equals("oracle"));
	}

	public boolean isMySql() {
		return (ConstConfig.DBTYPE.equals("mysql"));
	}

	
}
