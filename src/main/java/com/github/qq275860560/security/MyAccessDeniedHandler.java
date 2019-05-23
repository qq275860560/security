package com.github.qq275860560.security;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *  
 */

@Component
@Slf4j
public class MyAccessDeniedHandler implements AccessDeniedHandler {
	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {

		log.debug("授权失败", accessDeniedException);
		response.setStatus(HttpStatus.FORBIDDEN.value());
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.getWriter().write(objectMapper.writeValueAsString(new HashMap<String, Object>() {
			{
				put("code", HttpStatus.FORBIDDEN.value());
				put("msg", "授权失败");
				put("data", accessDeniedException.getMessage());
			}
		}));

	}
}