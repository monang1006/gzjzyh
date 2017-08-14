package com.strongit.uums.security.rtxsign;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

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

import com.strongit.oa.bo.ToaImConfig;
import com.strongit.oa.im.cache.Cache;
import com.strongit.oa.im.rtx.RTX2010Service;

/**
 * RTX签名认证
 * 
 * @author strong_yangwg
 * 
 */
public class RtxSignAuthenticationProvider implements AuthenticationProvider,
		Ordered, InitializingBean {
	protected MessageSourceAccessor messages = SpringSecurityMessageSource
			.getAccessor();

	private static ToaImConfig config;

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
						RtxSignAuthenticationToken.class,
						authentication,
						this.messages
								.getMessage(
										"UserNameAndPasswordAuthenticationToken.onlySupports",
										"Only UserNameAndPasswordAuthenticationToken is supported"));

		String userName = String.valueOf(authentication.getPrincipal());

		UserDetails user;
		try {
			user = retrieveUser(userName,
					(RtxSignAuthenticationToken) authentication);
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
					(RtxSignAuthenticationToken) authentication);
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
		return RtxSignAuthenticationToken.class
				.isAssignableFrom(authentication);
	}

	public int getOrder() {
		return this.order;
	}

	public void setOrder(int i) {
		this.order = i;
	}

	protected final UserDetails retrieveUser(String userName,
			RtxSignAuthenticationToken authentication)
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
			RtxSignAuthenticationToken authentication)
			throws AuthenticationException {

		if (authentication.getCredentials() == null) {
			throw new BadCredentialsException(this.messages.getMessage(
					"UserNameAndPasswordAuthenticationProvider.badCredentials",
					"Bad credentials"),
					(this.includeDetailsObject) ? userDetails : null);
		}
		// RTX用户
		String rtxUserCode = authentication.getPrincipal().toString();
		// RTX签名
		String rtxUserSign = authentication.getCredentials().toString();
		// 验证RTX用户及签名
		if (!checkUser(rtxUserCode, rtxUserSign)) {
			System.out.println("当前用户[" + rtxUserCode + "]RTX验证失败");
			throw new BadCredentialsException(this.messages.getMessage(
					"RtxAuthenticationProvider.badCredentials",
					"Bad credentials"),
					(this.includeDetailsObject) ? userDetails : null);
		} else {
			System.out.println("当前用户[" + rtxUserCode + "]RTX验证通过");
		}
	}

	/**
	 * RTX反向登陆oa时，通过客户端发过来的用户签名和用户名验证，该用户是否合法 实现方式：调用RTX服务器上的SignAuth.cgi
	 * 经测试后，其返回值为：failed! 或 success!
	 */
	private boolean checkUser(String userid, String signCode) {
		String query = "";
		try {
			if (config == null) {
				config = Cache.get();
			}
			String urlPath = "http://" + config.getImconfigIp() + ":"
					+ config.getImconfigPort() + "/SignAuth.cgi?user=" + userid
					+ "&sign=" + java.net.URLEncoder.encode(signCode.trim());// 做URI编码，防止带“+”等字符
			URL url = new URL(urlPath);
			System.out.println("SIGN_URL:" + urlPath);
			System.out.println("实际字符:" + java.net.URLDecoder.decode(signCode));
			URLConnection urlc = url.openConnection();
			urlc.setDoOutput(true);
			urlc.setDoInput(true);
			urlc.setAllowUserInteraction(false);
			DataInputStream in = new DataInputStream(urlc.getInputStream());
			String s;
			while ((s = in.readLine()) != null) {
				query += s;
			}
			in.close();
		} catch (MalformedURLException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		if (query.indexOf("success") > -1) {
			return true;
		} else {
			return false;
		}
	}

	protected Authentication createSuccessAuthentication(Object principal,
			Authentication authentication, UserDetails user) {
		RtxSignAuthenticationToken result = new RtxSignAuthenticationToken(
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
