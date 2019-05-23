package com.github.qq275860560.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.SecurityMetadataSource;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Order(value=2)
@Component
@Slf4j
public class MyFilterSecurityInterceptor extends FilterSecurityInterceptor {

	@Autowired
	private FilterInvocationSecurityMetadataSource myFilterInvocationSecurityMetadataSource;

	@Override
	public SecurityMetadataSource obtainSecurityMetadataSource() {
		return this.myFilterInvocationSecurityMetadataSource;
	}

	@Autowired
	private AccessDecisionManager accessDecisionManager;

	@Autowired
	public void setMyAccessDecisionManager() {
		super.setAccessDecisionManager(accessDecisionManager);
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		log.debug("授权=" + ((HttpServletRequest) request).getRequestURI());
		super.doFilter(request, response, chain);
		if(true)return;
		FilterInvocation filterInvocation = new FilterInvocation(request, response, chain);

		InterceptorStatusToken interceptorStatusToken = super.beforeInvocation(filterInvocation);
		try {
			filterInvocation.getChain().doFilter(filterInvocation.getRequest(), filterInvocation.getResponse());
		} finally {
			super.afterInvocation(interceptorStatusToken, null);
		}

	}

}
