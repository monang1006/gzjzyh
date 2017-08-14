package com.strongit.uums.security.rtxsign;

import org.springframework.security.GrantedAuthority;
import org.springframework.security.providers.AbstractAuthenticationToken;

/**
 * 
 * @author strong_yangwg
 *
 */
public class RtxSignAuthenticationToken extends AbstractAuthenticationToken {
	private static final long serialVersionUID = 1L;
	// rtx用户名
	private Object principal;
	// rtx用户签名
	private Object credentials;
	
	/**
	 * 
	 * @param principal RTX用户
	 * @param credentials RTX签名
	 */
	public RtxSignAuthenticationToken(Object principal, Object credentials) {
		super(null);
		this.principal = principal;
		this.credentials = credentials;
		setAuthenticated(false);
	}

	public RtxSignAuthenticationToken(Object principal,
			Object credentials, GrantedAuthority[] authorities) {
		super(authorities);
		this.principal = principal;
		this.credentials = credentials;
		super.setAuthenticated(true);
	}

	/**
	 * 得到当前用户
	 */
	public Object getPrincipal() {
		return this.principal;
	}
	
	/**
	 * 得到当前签名
	 */
	public Object getCredentials() {
		return this.credentials;
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
