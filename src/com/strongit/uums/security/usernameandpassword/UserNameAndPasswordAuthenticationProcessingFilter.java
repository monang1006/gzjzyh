package com.strongit.uums.security.usernameandpassword;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationException;
import org.springframework.security.concurrent.SessionRegistry;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.event.authentication.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.ui.AbstractProcessingFilter;
import org.springframework.security.ui.rememberme.NullRememberMeServices;
import org.springframework.security.ui.rememberme.RememberMeServices;
import org.springframework.security.util.SessionUtils;
import org.springframework.security.util.TextUtils;

/**
 * 
 * @author 		邓志城
 * @company  	Strongit Ltd. (C) copyright
 * @date   		Sep 19, 2011
 * @classpath	com.strongit.uums.security.usernameandpassword.UserNameAndPasswordAuthenticationProcessingFilter
 * @version  	3.0.2
 * @email		dengzc@strongit.com.cn
 * @tel			0791-8186916
 */
public class UserNameAndPasswordAuthenticationProcessingFilter extends
		AbstractProcessingFilter {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	private boolean continueChainBeforeSuccessfulAuthentication = false;

	private boolean migrateInvalidatedSessionAttributes = true;

	private RememberMeServices rememberMeServices = new NullRememberMeServices();

	private boolean invalidateSessionOnSuccessfulAuthentication = false;

	private SessionRegistry sessionRegistry;

	public static final String SPRING_SECURITY_FROM_FLAG = "j_flag";

	public static final String SPRING_SECURITY_FORM_USERNAME = "j_username";

	public static final String SPRING_SECURITY_FROM_PASSWORD = "j_password";

	public static final String SPRING_SECURITY_LAST_USERNAME_KEY = "SPRING_SECURITY_LAST_USERNAME";

	private String flagParameter = "j_flag";

	private String userNameParameter = "j_username";

	private String passwordParameter = "j_password";

	public void doFilterHttp(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if ((requiresAuthentication(request, response))
				&& ((checkHasAuthenticated(request)))) {
			Authentication authResult;
			logger.info("用户名密码认证开始...");
			try {
				onPreAuthentication(request, response);
				authResult = attemptAuthentication(request);
			} catch (AuthenticationException failed) {
				unsuccessfulAuthentication(request, response, failed);
				return;
			}

			if (this.continueChainBeforeSuccessfulAuthentication) {
				chain.doFilter(request, response);
			}
			successfulAuthentication(request, response, authResult);
			logger.info("用户名密码认证结束...");
		}
		chain.doFilter(request, response);
	}

	protected void successfulAuthentication(HttpServletRequest request,
			HttpServletResponse response, Authentication authResult)
			throws IOException, ServletException {

		SecurityContextHolder.getContext().setAuthentication(authResult);
		if (this.invalidateSessionOnSuccessfulAuthentication) {
			SessionUtils.startNewSessionIfRequired(request,
					this.migrateInvalidatedSessionAttributes,
					this.sessionRegistry);
		}

		onSuccessfulAuthentication(request, response, authResult);

		this.rememberMeServices.loginSuccess(request, response, authResult);

		if (this.eventPublisher != null)
			this.eventPublisher
					.publishEvent(new InteractiveAuthenticationSuccessEvent(
							authResult, super.getClass()));
	}

	public Authentication attemptAuthentication(HttpServletRequest request)
			throws AuthenticationException {
		String username = obtainUserName(request);
		String password = obtainPassword(request);

		if (username == null) {
			username = "";
		}
		if (password == null) {
			password = "";
		}
		username = username.trim();

		UserNameAndPasswordAuthenticationToken authRequest = new UserNameAndPasswordAuthenticationToken(
				username, password);

		HttpSession session = request.getSession(false);

		if ((session != null) || (getAllowSessionCreation())) {
			request.getSession().setAttribute(SPRING_SECURITY_LAST_USERNAME_KEY,
					TextUtils.escapeEntities(username));
		}

		setDetails(request, authRequest);

		return getAuthenticationManager().authenticate(authRequest);
	}

	public String getDefaultFilterProcessesUrl() {
		return "/j_spring_security_usbkey_check";
	}

	public int getOrder() {
		return -1;
	}

	protected String obtainUserName(HttpServletRequest request) {
		return request.getParameter(this.userNameParameter);
	}

	protected String obtainPassword(HttpServletRequest request) {
		return request.getParameter(this.passwordParameter);
	}

	protected String obtainFlag(HttpServletRequest request) {
		return request.getParameter(this.flagParameter);
	}

	protected void setDetails(HttpServletRequest request,
			UserNameAndPasswordAuthenticationToken authRequest) {
		authRequest.setDetails(this.authenticationDetailsSource
				.buildDetails(request));
	}

	protected boolean requiresAuthentication(HttpServletRequest request,
			HttpServletResponse response) {
		String flag = obtainFlag(request);

		return ("2".equals(flag));
	}

	protected boolean checkHasAuthenticated(HttpServletRequest request) {
		String userName = obtainUserName(request);
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		if (authentication == null) {
			return false;
		}
		return (userName.equals(authentication.getName()));
	}
}
