package com.github.qq275860560.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.qq275860560.service.SecurityService;

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
	private RsaSigner rsaSigner;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		log.debug("登录成功");
		String payload = objectMapper.writeValueAsString(new HashMap<String, Object>() {
			{
				put("exp", System.currentTimeMillis() / 1000 + securityService.getAccessTokenValiditySeconds());
				put("user_name", authentication.getName());

				put("authorities", new ArrayList<String>() {
					{
						addAll(securityService.getRoleNamesByUsername(authentication.getName()));
					}
				});
				put("jti", UUID.randomUUID());
				// token中的client_id和scope字段不是必须的，加入是为了考虑兼容oauth2的密码模式,前提是实现getClientByUsername接口
				Map<String, Object> client = securityService.getClientByUsername(authentication.getName());
				if (client!=null && !StringUtils.isEmpty(client.get("clientId")) && !StringUtils.isEmpty(client.get("scope")) ) {
					put("client_id", client.get("clientId"));
					put("scope", ((String) client.get("scope")).split(","));					 
				}

			}
		});
		String token = JwtHelper.encode(payload, rsaSigner).getEncoded();
		response.addHeader("Authorization", "Bearer " + token);
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		response.getWriter().write(objectMapper.writeValueAsString(new HashMap<String, Object>() {
			{
				put("access_token", token);
				put("code", HttpStatus.OK.value());
				put("msg", "登录成功");
				put("token_type", "bearer");
				put("expires_in", System.currentTimeMillis() / 1000 + securityService.getAccessTokenValiditySeconds());

			}
		}));
	}
}