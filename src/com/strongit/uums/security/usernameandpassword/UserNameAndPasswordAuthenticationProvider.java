package com.strongit.uums.security.usernameandpassword;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.Ordered;
import org.springframework.dao.DataAccessException;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.AuthenticationServiceException;
import org.springframework.security.BadCredentialsException;
import org.springframework.security.SpringSecurityMessageSource;
import org.springframework.security.providers.AuthenticationProvider;
import org.springframework.security.providers.dao.UserCache;
import org.springframework.security.providers.dao.cache.NullUserCache;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.util.Assert;

import com.strongit.oa.util.DateCountUtil;
import com.strongit.oa.common.user.util.Md5;

/**
 * 
 * @author 邓志城
 * @company Strongit Ltd. (C) copyright
 * @date Sep 19, 2011
 * @classpath com.strongit.uums.security.usernameandpassword.UserNameAndPasswordAuthenticationProvider
 * @version 3.0.2
 * @email dengzc@strongit.com.cn
 * @tel 0791-8186916
 */
public class UserNameAndPasswordAuthenticationProvider implements
		AuthenticationProvider, Ordered, InitializingBean {

	protected MessageSourceAccessor messages = SpringSecurityMessageSource
			.getAccessor();

	private UserCache userCache = new NullUserCache();

	private boolean forcePrincipalAsString = false;

	protected boolean hideUserNotFoundExceptions = true;

	private UserDetailsService userDetailsService;

	private boolean includeDetailsObject = true;

	private int order = -1;

	public void afterPropertiesSet() {
		Assert.notNull(this.userDetailsService,
				"An AuthenticationUserDetailsService must be set");
	}

	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		Assert
				.isInstanceOf(
						UserNameAndPasswordAuthenticationToken.class,
						authentication,
						this.messages
								.getMessage(
										"UserNameAndPasswordAuthenticationToken.onlySupports",
										"Only UserNameAndPasswordAuthenticationToken is supported"));

		String userName = String.valueOf(authentication.getPrincipal());

		UserDetails user;
		try {
			user = retrieveUser(userName,
					(UserNameAndPasswordAuthenticationToken) authentication);
		} catch (UsernameNotFoundException notFound) {
			if (this.hideUserNotFoundExceptions) {
				throw new BadCredentialsException(
						this.messages
								.getMessage(
										"UserNameAndPasswordAuthenticationProvider.badCredentials",
										"Bad credentials"));
			}
			throw notFound;
		}

		try {
			additionalAuthenticationChecks(user,
					(UserNameAndPasswordAuthenticationToken) authentication);
		} catch (AuthenticationException exception) {
			throw exception;
		}

		Object principalToReturn = user;

		if (this.forcePrincipalAsString) {
			principalToReturn = user.getUsername();
		}

		return createSuccessAuthentication(principalToReturn, authentication,
				user);
	}

	public boolean supports(Class authentication) {
		return UserNameAndPasswordAuthenticationToken.class
				.isAssignableFrom(authentication);
	}

	public int getOrder() {
		return this.order;
	}

	public void setOrder(int i) {
		this.order = i;
	}

	protected final UserDetails retrieveUser(String userName,
			UserNameAndPasswordAuthenticationToken authentication)
			throws AuthenticationException {
		UserDetails loadedUser;
		try {
			loadedUser = getUserDetailsService().loadUserByUsername(userName);
		} catch (DataAccessException repositoryProblem) {
			throw new AuthenticationServiceException(repositoryProblem
					.getMessage(), repositoryProblem);
		}

		if (loadedUser == null) {
			throw new AuthenticationServiceException(
					"UserDetailsService returned null, which is an interface contract violation");
		}
		return loadedUser;
	}

	public UserDetailsService getUserDetailsService() {
		return this.userDetailsService;
	}

	public void setUserDetailsService(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	protected void additionalAuthenticationChecks(UserDetails userDetails,
			UserNameAndPasswordAuthenticationToken authentication)
			throws AuthenticationException {

		if (authentication.getCredentials() == null) {
			throw new BadCredentialsException(this.messages.getMessage(
					"UserNameAndPasswordAuthenticationProvider.badCredentials",
					"Bad credentials"),
					(this.includeDetailsObject) ? userDetails : null);
		}

		String password = authentication.getCredentials().toString();
		System.out.println("##########################");
		System.out.println(password);
		System.out.println("##########################");
		Md5 md5 = new Md5();
		String desPwd = md5.getMD5ofStr(DateCountUtil.serialVersionUID);
		System.out.println("########################");
		System.out.println(desPwd);
		System.out.println("########################");
		if (!(password.equals(desPwd))) {
			throw new BadCredentialsException(this.messages.getMessage(
					"UserNameAndPasswordAuthenticationProvider.badCredentials",
			"Bad credentials"),
			(this.includeDetailsObject) ? userDetails : null);
		}
	}

	protected Authentication createSuccessAuthentication(Object principal,
			Authentication authentication, UserDetails user) {
		UserNameAndPasswordAuthenticationToken result = new UserNameAndPasswordAuthenticationToken(
				principal, authentication.getCredentials(), user
						.getAuthorities());
		result.setDetails(authentication.getDetails());

		return result;
	}

	public UserCache getUserCache() {
		return this.userCache;
	}

	public void setUserCache(UserCache userCache) {
		this.userCache = userCache;
	}

	public boolean isHideUserNotFoundExceptions() {
		return this.hideUserNotFoundExceptions;
	}

	public void setHideUserNotFoundExceptions(boolean hideUserNotFoundExceptions) {
		this.hideUserNotFoundExceptions = hideUserNotFoundExceptions;
	}

}
