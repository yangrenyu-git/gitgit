
package com.javaxxz.core.toolbox.kit;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieKit {


	public static String getCookie(String name, HttpServletRequest request) {
		return getCookie(name, null, request);
	}


	public static String getCookie(String name, String defaultValue, HttpServletRequest request) {
		Cookie cookie = getCookieObject(name, request);
		return cookie != null ? cookie.getValue() : defaultValue;
	}
	

	public static Cookie getCookieObject(String name, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies != null)
			for (Cookie cookie : cookies)
				if (cookie.getName().equals(name))
					return cookie;
		return null;
	}
	

	public static void removeCookie(String name, HttpServletResponse response) {
		doSetCookie(name, null, 0, null, null, null, response);
	}


	public static void removeCookie(String name, String path, HttpServletResponse response) {
		doSetCookie(name, null, 0, path, null, null, response);
	}


	public static void removeCookie(String name, String path, String domain, HttpServletResponse response) {
		doSetCookie(name, null, 0, path, domain, null, response);
	}

	private static void doSetCookie(String name, String value, int maxAgeInSeconds, String path, String domain, Boolean isHttpOnly, HttpServletResponse response) {
		Cookie cookie = new Cookie(name, value);
		cookie.setMaxAge(maxAgeInSeconds);
		// set the default path value to "/"
		if (path == null) {
			path = "/";
		}
		cookie.setPath(path);

		if (domain != null) {
			cookie.setDomain(domain);
		}
		if (isHttpOnly != null) {
			cookie.setHttpOnly(isHttpOnly);
		}
		response.addCookie(cookie);
	}
	
}
