package com.javaxxz.core.toolbox.support;

import java.net.MalformedURLException;
import java.util.regex.Pattern;

import com.javaxxz.core.toolbox.Func;
import com.javaxxz.core.toolbox.kit.ReKit;
import com.javaxxz.core.toolbox.kit.StrKit;


public class FieldValidator {


	public final static Pattern GENERAL = Pattern.compile("^\\w+$");

	public final static Pattern NUMBER = Pattern.compile("\\d+");

	public final static Pattern GROUP_VAR = Pattern.compile("\\$(\\d+)");

	public final static Pattern IPV4 = Pattern
			.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");

	public final static Pattern MONEY = Pattern.compile("^(\\d+(?:\\.\\d+)?)$");

	public final static Pattern EMAIL = Pattern.compile("(\\w|.)+@\\w+(\\.\\w+){1,2}");

	public final static Pattern MOBILE = Pattern.compile("1\\d{10}");

	public final static Pattern CITIZEN_ID = Pattern.compile("[1-9]\\d{5}[1-2]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}(\\d|X|x)");

	public final static Pattern ZIP_CODE = Pattern.compile("\\d{6}");

	public final static Pattern BIRTHDAY = Pattern.compile("(\\d{4})(/|-|\\.)(\\d{1,2})(/|-|\\.)(\\d{1,2})日?$");

	public final static Pattern URL = Pattern.compile("(https://|http://)?([\\w-]+\\.)+[\\w-]+(/[\\w- ./?%&=]*)?");

	public final static Pattern GENERAL_WITH_CHINESE = Pattern.compile("^[\\u0391-\\uFFE5\\w]+$");

	public final static Pattern UUID = Pattern.compile("^[0-9a-z]{8}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{4}-[0-9a-z]{12}$");


	public static <T> boolean isEmpty(T value) {
		return (null == value || (value instanceof String && StrKit.isBlank((String) value)));
	}


	public static boolean equals(Object t1, Object t2) {
		return Func.equals(t1, t2);
	}


	public static boolean isByRegex(String regex, String value) {
		return ReKit.isMatch(regex, value);
	}


	public static boolean isByRegex(Pattern pattern, String value) {
		return ReKit.isMatch(pattern, value);
	}


	public static boolean isGeneral(String value) {
		return isByRegex(GENERAL, value);
	}


	public static boolean isGeneral(String value, int min, int max) {
		String reg = "^\\w{" + min + "," + max + "}$";
		if (min < 0) {
			min = 0;
		}
		if (max <= 0) {
			reg = "^\\w{" + min + ",}$";
		}
		return isByRegex(reg, value);
	}


	public static boolean isGeneral(String value, int min) {
		return isGeneral(value, min, 0);
	}


	public static boolean isNumber(String value) {
		if (StrKit.isBlank(value)) {
			return false;
		}
		return isByRegex(NUMBER, value);
	}


	public static boolean isMoney(String value) {
		return isByRegex(MONEY, value);
	}


	public static boolean isZipCode(String value) {
		return isByRegex(ZIP_CODE, value);
	}


	public static boolean isEmail(String value) {
		return isByRegex(EMAIL, value);
	}


	public static boolean isMobile(String value) {
		return isByRegex(MOBILE, value);
	}


	public static boolean isCitizenId(String value) {
		return isByRegex(CITIZEN_ID, value);
	}


	public static boolean isBirthday(String value) {
		if (isByRegex(BIRTHDAY, value)) {
			int year = Integer.parseInt(value.substring(0, 4));
			int month = Integer.parseInt(value.substring(5, 7));
			int day = Integer.parseInt(value.substring(8, 10));

			if (month < 1 || month > 12) {
				return false;
			}
			if (day < 1 || day > 31) {
				return false;
			}
			if ((month == 4 || month == 6 || month == 9 || month == 11) && day == 31) {
				return false;
			}
			if (month == 2) {
				boolean isleap = (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0));
				if (day > 29 || (day == 29 && !isleap)) {
					return false;
				}
			}
		}
		return true;
	}


	public static boolean isIpv4(String value) {
		return isByRegex(IPV4, value);
	}


	public static boolean isUrl(String value) {
		try {
			new java.net.URL(value);
		} catch (MalformedURLException e) {
			return false;
		}
		return true;
	}


	public static boolean isChinese(String value) {
		return isByRegex("^" + ReKit.RE_CHINESE + "+$", value);
	}


	public static boolean isGeneralWithChinese(String value) {
		return isByRegex(GENERAL_WITH_CHINESE, value);
	}


	public static boolean isUUID(String value) {
		return isByRegex(UUID, value);
	}

}
