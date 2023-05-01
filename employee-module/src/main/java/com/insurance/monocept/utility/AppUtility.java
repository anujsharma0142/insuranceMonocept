package com.insurance.monocept.utility;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.insurance.monocept.entity.User;

public class AppUtility {

	public static User getCurrentUser() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(null != auth) {
			User user = (User) auth.getPrincipal();
			return user;
		}
		return null;
	}

}
