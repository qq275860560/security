package com.github.qq275860560.security;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Slf4j
public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	// curl -i -X POST "http://localhost:8080/login?username=username1&password=password1"
	public MyUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,
			MyAuthenticationSuccessHandler myAuthenticationSuccessHandler,
			MyAuthenticationFailureHandler myAuthenticationFailureHandler) {
		super();
		super.setPostOnly(false);
		super.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login"));
		super.setAuthenticationManager(authenticationManager);
		super.setAuthenticationSuccessHandler(myAuthenticationSuccessHandler);
		super.setAuthenticationFailureHandler(myAuthenticationFailureHandler);

	}

}