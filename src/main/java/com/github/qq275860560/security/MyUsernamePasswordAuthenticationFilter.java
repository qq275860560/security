package com.github.qq275860560.security;

import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Component
@Slf4j
public class MyUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	@Autowired
	private ObjectMapper objectMapper;
/*
	public MyUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager,
			ObjectMapper objectMapper, MySimpleUrlAuthenticationSuccessHandler mySimpleUrlAuthenticationSuccessHandler,
			MySimpleUrlAuthenticationFailureHandler mySimpleUrlAuthenticationFailureHandler) {
		super.setAuthenticationManager(authenticationManager);
		super.setAuthenticationSuccessHandler(mySimpleUrlAuthenticationSuccessHandler);
		super.setAuthenticationFailureHandler(mySimpleUrlAuthenticationFailureHandler);
		this.objectMapper = objectMapper;
	}
*/
	/*
	  curl -i -H "Content-Type:application/json;charset=UTF-8" \
	  -X POST http://localhost:8080/login \
	  -d '{"username":"username1","password":"password1"}'
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		log.info("登录");
		Map<String, Object> map = null;
		try {
			map = objectMapper.readValue(request.getInputStream(), Map.class);
		} catch (Exception e) {
			log.error("", e);
			throw new UsernameNotFoundException(e.getMessage());
		}
		// 可加入图片验证码/短信验证码逻辑
		//
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
				map.get("username"), (String) map.get("password"), new ArrayList<>());
		usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		return this.getAuthenticationManager().authenticate(usernamePasswordAuthenticationToken);
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