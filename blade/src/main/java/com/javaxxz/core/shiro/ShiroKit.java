
package com.javaxxz.core.shiro;

import java.util.Random;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ByteSource;

import com.javaxxz.common.vo.ShiroUser;


public class ShiroKit {

	private static final String NAMES_DELIMETER = ",";


	final static String hashAlgorithmName = "MD5";


	final static int hashIterations = 1024;


	public static String md5(String credentials, String saltSource) {
		ByteSource salt = new Md5Hash(saltSource);
		return new SimpleHash(hashAlgorithmName, credentials, salt, hashIterations).toString();
	}


	public static String getRandomSalt(int length) { 
	    String base = "abcdefghijklmnopqrstuvwxyz0123456789";     
	    Random random = new Random();     
	    StringBuffer sb = new StringBuffer();     
	    for (int i = 0; i < length; i++) {     
	        int number = random.nextInt(base.length());     
	        sb.append(base.charAt(number));     
	    }     
	    return sb.toString();     
	 }  
	

	public static Subject getSubject() {
		return SecurityUtils.getSubject();
	}


	public static ShiroUser getUser() {
		if (isGuest()) {
			return null;
		} else {
			return (ShiroUser) getSubject().getPrincipals().getPrimaryPrincipal();
		}
	}


	public static Session getSession() {
		return getSubject().getSession();
	}


	@SuppressWarnings("unchecked")
	public static <T> T getSessionAttr(String key) {
		Session session = getSession();
		return session != null ? (T) session.getAttribute(key) : null;
	}


	public static void setSessionAttr(String key, Object value) {
		Session session = getSession();
		session.setAttribute(key, value);
	}


	public static void removeSessionAttr(String key) {
		Session session = getSession();
		if (session != null)
			session.removeAttribute(key);
	}


	public static boolean hasRole(String roleName) {
		return getSubject() != null && roleName != null
				&& roleName.length() > 0 && getSubject().hasRole(roleName);
	}


	public static boolean lacksRole(String roleName) {
		return !hasRole(roleName);
	}


	public static boolean hasAnyRoles(String roleNames) {
		boolean hasAnyRole = false;
		Subject subject = getSubject();
		if (subject != null && roleNames != null && roleNames.length() > 0) {
			for (String role : roleNames.split(NAMES_DELIMETER)) {
				if (subject.hasRole(role.trim())) {
					hasAnyRole = true;
					break;
				}
			}
		}
		return hasAnyRole;
	}


	public static boolean hasAllRoles(String roleNames) {
		boolean hasAllRole = true;
		Subject subject = getSubject();
		if (subject != null && roleNames != null && roleNames.length() > 0) {
			for (String role : roleNames.split(NAMES_DELIMETER)) {
				if (!subject.hasRole(role.trim())) {
					hasAllRole = false;
					break;
				}
			}
		}
		return hasAllRole;
	}


	public static boolean hasPermission(String permission) {
		return getSubject() != null && permission != null
				&& permission.length() > 0
				&& getSubject().isPermitted(permission);
	}


	public static boolean lacksPermission(String permission) {
		return !hasPermission(permission);
	}


	public static boolean isAuthenticated() {
		return getSubject() != null && getSubject().isAuthenticated();
	}


	public static boolean notAuthenticated() {
		return !isAuthenticated();
	}


	public static boolean isUser() {
		return getSubject() != null && getSubject().getPrincipal() != null;
	}


	public static boolean isGuest() {
		return !isUser();
	}


	public static String principal() {
		if (getSubject() != null) {
			Object principal = getSubject().getPrincipal();
			return principal.toString();
		}
		return "";
	}
	
}
