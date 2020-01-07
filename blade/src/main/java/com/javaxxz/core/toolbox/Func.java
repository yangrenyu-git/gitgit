
package com.javaxxz.core.toolbox;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.javaxxz.core.constant.ConstConfig;
import com.javaxxz.core.toolbox.kit.CharsetKit;
import com.javaxxz.core.toolbox.kit.StrKit;
import com.javaxxz.core.toolbox.kit.URLKit;
import com.javaxxz.core.toolbox.support.Convert;


public class Func {


	public static boolean equals(Object obj1, Object obj2) {
		return (obj1 != null) ? (obj1.equals(obj2)) : (obj2 == null);
	}


	public static int length(Object obj) {
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


	public static boolean contains(Object obj, Object element) {
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
	public static boolean isEmpty(Object o) {
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


	public static boolean isOneEmpty(Object... os) {
		for (Object o : os) {
			if (isEmpty(o)) {
				return true;
			}
		}
		return false;
	}


	public static boolean isAllEmpty(Object... os) {
		for (Object o : os) {
			if (!isEmpty(o)) {
				return false;
			}
		}
		return true;
	}


	public static boolean isNum(Object obj) {
		try {
			Integer.parseInt(obj.toString());
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	

	public static Object getValue(Object str, Object defaultValue) {
		if (isEmpty(str)) {
			return defaultValue;
		}
		return str;
	}


	public static String format(String template, Object... values) {
		return StrKit.format(template, values);
	}


	public static String format(String template, Map<?, ?> map) {
		return StrKit.format(template, map);
	}


	public static String toStr(Object str) {
		return toStr(str, "");
	}
	

	public static String toStr(Object str, String defaultValue) {
		if (null == str) {
			return defaultValue;
		}
		return str.toString().trim();
	}


	public static int toInt(Object value) {
		return toInt(value, -1);
	}


	public static int toInt(Object value, int defaultValue) {
		return Convert.toInt(value, defaultValue);
	}


	public static long toLong(Object value) {
		return toLong(value, -1);
	}


	public static long toLong(Object value, long defaultValue) {
		return Convert.toLong(value, defaultValue);
	}

	public static String encodeUrl(String url) {
		return URLKit.encode(url, CharsetKit.UTF_8);
	}

	public static String decodeUrl(String url) {
		return URLKit.decode(url, CharsetKit.UTF_8);
	}


	public static Map<String, Object> caseInsensitiveMap(Map<String, Object> map) {
		Map<String, Object> tempMap = new HashMap<>();
		for (String key : map.keySet()) {
			tempMap.put(key.toLowerCase(), map.get(key));
		}
		return tempMap;
	}
	

    public static <K, V> V getFirstOrNull(Map<K, V> map) {
        V obj = null;
        for (Entry<K, V> entry : map.entrySet()) {
            obj = entry.getValue();
            if (obj != null) {
                break;
            }
        }
        return obj;
    }


	public static StringBuilder builder(String... strs) {
		final StringBuilder sb = new StringBuilder();
		for (String str : strs) {
			sb.append(str);
		}
		return sb;
	}


	public static void builder(StringBuilder sb, String... strs) {
		for (String str : strs) {
			sb.append(str);
		}
	}
	
	public static boolean isOracle() {
		return (ConstConfig.DBTYPE.equals("oracle"));
	}

	public static boolean isMySql() {
		return (ConstConfig.DBTYPE.equals("mysql"));
	}

}
