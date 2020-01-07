
package com.javaxxz.core.beetl.func;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.beetl.core.GroupTemplate;

import com.javaxxz.common.vo.ShiroUser;

public class ShiroExt {
	private static final String NAMES_DELIMETER = ",";
	

	protected static Subject getSubject() {
		return SecurityUtils.getSubject();
	}


	public ShiroUser getUser() {
		if (isGuest()) {
			return null;
		} else {
			return (ShiroUser) getSubject().getPrincipals().getPrimaryPrincipal();
		}
	}
 

	public boolean hasRole(String roleName) {
		return getSubject() != null && roleName != null
				&& roleName.length() > 0 && getSubject().hasRole(roleName);
	}


	public boolean lacksRole(String roleName) {
		return !hasRole(roleName);
	}


	public boolean hasAnyRoles(String roleNames) {
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


	public boolean hasAllRoles(String roleNames) {
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


	public boolean hasPermission(String permission) {
		return getSubject() != null && permission != null
				&& permission.length() > 0
				&& getSubject().isPermitted(permission);
	}


	public boolean lacksPermission(String permission) {
		return !hasPermission(permission);
	}


	public boolean authenticated() {
		return getSubject() != null && getSubject().isAuthenticated();
	}


	public boolean notAuthenticated() {
		return !authenticated();
	}


	public boolean isUser() {
		return getSubject() != null && getSubject().getPrincipal() != null;
	}


	public boolean isGuest() {
		return !isUser();
	}


	public String principal() {
		if (getSubject() != null) {
			Object principal = getSubject().getPrincipal();
			return principal.toString();
		}
		return "";
	}
	
    public static void main(String[] args) {
        GroupTemplate gt = new GroupTemplate();
        gt.registerFunctionPackage("shiro", new ShiroExt());
 
    }
}
