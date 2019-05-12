package com.github.qq275860560.config;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@ControllerAdvice
@Slf4j
public class ExceptionHandlerConfig {
	@ResponseBody
	@ExceptionHandler(value = Throwable.class)
	public Map handle(Throwable t, HttpServletResponse response) {
		log.trace("", t);
		response.setStatus(HttpStatus.BAD_REQUEST.value());
		response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.BAD_REQUEST.value());
				put("msg", "请求失败");
				put("data", t.getMessage());
			}
		};

	}

}
