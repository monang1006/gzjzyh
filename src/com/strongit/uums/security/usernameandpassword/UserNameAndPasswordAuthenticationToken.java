package com.strongit.uums.security.usernameandpassword;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.providers.AbstractAuthenticationToken;

public class UserNameAndPasswordAuthenticationToken extends
		AbstractAuthenticationToken {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Object credentials;

	private Object principal;

	public UserNameAndPasswordAuthenticationToken(Object principal, Object credentials) {
		super(null);
		this.principal = principal;
		this.credentials = credentials;
		setAuthenticated(false);
	}

	public UserNameAndPasswordAuthenticationToken(Object principal,
			Object credentials, GrantedAuthority[] authorities) {
		super(authorities);
		this.principal = principal;
		this.credentials = credentials;
		super.setAuthenticated(true);
	}

	public Object getCredentials() {
		return this.credentials;
	}

	public Object getPrincipal() {
		return this.principal;
	}

	public void setAuthenticated(boolean isAuthenticated)
			throws IllegalArgumentException {
		if (isAuthenticated) {
			throw new IllegalArgumentException(
					"Cannot set this token to trusted - use constructor containing GrantedAuthority[]s instead");
		}

		super.setAuthenticated(false);
	}

}
