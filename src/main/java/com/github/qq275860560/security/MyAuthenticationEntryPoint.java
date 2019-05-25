package com.github.qq275860560.security;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *  
 */

@Component
@Slf4j
public class MyAuthenticationEntryPoint implements AuthenticationEntryPoint {
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) {
		try {
			log.debug("认证失败", authException);
			response.setStatus(HttpStatus.UNAUTHORIZED.value());
			response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
			response.getWriter().write(objectMapper.writeValueAsString(new HashMap<String, Object>() {
				{
					put("code", HttpStatus.UNAUTHORIZED.value());
					put("msg", "认证失败");
					put("data", authException.getMessage());
				}
			}));
		} catch (Exception e) {
			log.error("", e);
		}
	}
}