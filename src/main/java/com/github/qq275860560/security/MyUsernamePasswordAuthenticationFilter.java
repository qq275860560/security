package com.github.qq275860560.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Component
@Slf4j
public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	 // curl -i -X POST "http://localhost:8080/login?username=username1&password=password1"
	public MyUsernamePasswordAuthenticationFilter() {
    	super();
		super.setPostOnly(false);
		super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login"));
	}

	@Override
	@Autowired
	public void setAuthenticationSuccessHandler(
			AuthenticationSuccessHandler successHandler) {
		super.setAuthenticationSuccessHandler(successHandler);
 
	}
	
	@Override
	@Autowired
	public void setAuthenticationFailureHandler(
			AuthenticationFailureHandler failureHandler) {
		super.setAuthenticationFailureHandler(failureHandler);
	}
	
	@Override
	@Autowired
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}
}