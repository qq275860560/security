package com.github.qq275860560.security;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Component("myAuthorization")
@Slf4j
public class MyAuthorization {

	public boolean check(Authentication authentication, HttpServletRequest request) {
		Object principal = authentication.getPrincipal();
		log.info("授权");
		return true;
	}
}