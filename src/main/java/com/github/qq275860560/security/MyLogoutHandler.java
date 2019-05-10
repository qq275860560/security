package com.github.qq275860560.security;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Component
@Slf4j
public class MyLogoutHandler implements LogoutHandler {

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		// 可加入清除redis逻辑，实现服务端退出
		try {
			log.info("退出");	
			throw new Exception("Ext");
		}catch (Exception e) {
			log.info("退出失败",e);	 
		}
		 
	}

	 
}