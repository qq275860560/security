package com.github.qq275860560.security;

import java.io.IOException;
import java.security.PrivateKey;
import java.util.Date;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.qq275860560.service.SecurityService;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Component
@Slf4j
public class MyAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private SecurityService securityService;
	
 

	@Autowired
	private PrivateKey privateKey;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		log.debug("登录成功");
		String token = Jwts.builder().setSubject(authentication.getName())
				.setExpiration(new Date(System.currentTimeMillis() + securityService.getExpirationSeconds() * 1000))
				.signWith(SignatureAlgorithm.RS256, privateKey).compact();
		response.addHeader("Authorization", "Bearer " + token);

		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.getWriter().write(objectMapper.writeValueAsString(new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK.value());
				put("msg", "登录成功");
				put("data", new HashMap<String,Object>() {{
					put("access_token", token);
					put("token_type", "bearer");
					put("refresh_token", "");
					put("expires_in", securityService.getExpirationSeconds());
					put("scope", "");//加载所有权限.暂略
				}});
			}
		}));
	}
}