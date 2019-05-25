package com.github.qq275860560.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Component
@Slf4j
public class MyLogoutHandler implements LogoutHandler {

	/*
	 * curl -i -X POST http://localhost:8080/logout -H "Authorization:Bearer eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTEiLCJleHAiOjE1NTc1Mzk4MTN9.FEsVLyZv_RzNnd14z1Qawq_EZ5AOQ27_4BceNuX6eTYqWRNS9IW4A6U4PcXnbG6rVwPgWm9VNq7AxcJpyaOTAqSxTZrfv7CCAxE-G-IuydNeAzUaXfsdPMjRcwZlBjt_V3DdMUR94HGpwPEEnIeT_jBsAe5ic7pDWAzzTY0W36U"
 	 */
	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
		// 可加入清除redis逻辑，实现服务端退出
		try {
			log.debug("退出");

		} catch (Exception e) {
			log.debug("退出失败", e);
		}

	}

}