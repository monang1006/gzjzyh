package com.strongit.uums.security.rtxsign;

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
 * @author strong_yangwg
 * 
 */
public class RtxSignProcessingFilter extends AbstractProcessingFilter {

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

	private String rtxUserParameter = "j_username";

	private String rtxSignParameter = "j_usersign";

	public void doFilterHttp(HttpServletRequest request,
			HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		if ("rtx".equalsIgnoreCase(request.getParameter(this.flagParameter))) {
			Authentication authResult;
			logger.info("RTX用户签名认证开始:"
					+ request.getParameter(this.rtxUserParameter));
			System.out.println("RTX用户签名认证开始:"
					+ request.getParameter(this.rtxUserParameter));
			try {
				onPreAuthentication(request, response);
				authResult = attemptAuthentication(request);
			} catch (AuthenticationException failed) {
				System.out.println("注意：失败了");
				unsuccessfulAuthentication(request, response, failed);
				return;
			}

			if (this.continueChainBeforeSuccessfulAuthentication) {
				chain.doFilter(request, response);
			}
			successfulAuthentication(request, response, authResult);
			logger.info("RTX用户签名认证结束...");
			System.out.println("RTX用户签名认证结束...");
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
		String userName = obtainUserName(request);
		String userSign = obtainUserSign(request);

		if (userName == null) {
			userName = "";
		}
		if (userSign == null) {
			userSign = "";
		}
		userName = userName.trim();

		RtxSignAuthenticationToken authRequest = new RtxSignAuthenticationToken(
				userName, userSign);

		HttpSession session = request.getSession(false);

		if ((session != null) || (getAllowSessionCreation())) {
			request.getSession().setAttribute(
					SPRING_SECURITY_LAST_USERNAME_KEY,
					TextUtils.escapeEntities(userName));
		}

		setDetails(request, authRequest);

		return getAuthenticationManager().authenticate(authRequest);
	}

	public String getDefaultFilterProcessesUrl() {
		return "/j_spring_security_rtx_check";
	}

	public int getOrder() {
		return -1;
	}

	protected String obtainUserName(HttpServletRequest request) {
		return request.getParameter(this.rtxUserParameter);
	}

	protected String obtainUserSign(HttpServletRequest request) {
		return request.getParameter(this.rtxSignParameter);
	}

	protected String obtainFlag(HttpServletRequest request) {
		return request.getParameter(this.flagParameter);
	}

	protected void setDetails(HttpServletRequest request,
			RtxSignAuthenticationToken authRequest) {
		authRequest.setDetails(this.authenticationDetailsSource
				.buildDetails(request));
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
