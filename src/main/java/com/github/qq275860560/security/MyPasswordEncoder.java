package com.github.qq275860560.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.github.qq275860560.service.SecurityService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Component
@Slf4j
public class MyPasswordEncoder extends BCryptPasswordEncoder {
	@Autowired
	private SecurityService securityService;

	public String encode(CharSequence rawPassword) {
		return securityService.encode(rawPassword);
	}

	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return securityService.matches(rawPassword, encodedPassword);
	}

}