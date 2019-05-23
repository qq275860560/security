package com.github.qq275860560.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.qq275860560.security.MyAccessDeniedHandler;
import com.github.qq275860560.security.MyAuthenticationEntryPoint;
import com.github.qq275860560.security.MyLogoutHandler;
import com.github.qq275860560.security.MyLogoutSuccessHandler;
import com.github.qq275860560.security.MyUserDetailsService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Configuration
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private MyUserDetailsService myUserDetailsService;
	@Autowired
	private MyLogoutSuccessHandler myLogoutSuccessHandler;
	@Autowired
	private MyLogoutHandler myLogoutHandler;
	@Autowired
	private MyAccessDeniedHandler myAccessDeniedHandler;

	@Autowired
	private MyAuthenticationEntryPoint myAuthenticationEntryPoint;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder);
	}

//	@Override
//	public void configure(WebSecurity web) throws Exception {
//		web.ignoring().antMatchers("/**/*.html", "/**/*.css", "/**/*.woff", "/**/*.woff2", "/**/*.js", "/**/*.jpg","/**/*.png", "/**/*.ico");
//	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors();
		http.csrf().disable();
		// 解决不允许显示在iframe的问题
		http.headers().frameOptions().disable();
		// 禁用headers缓存
		http.headers().cacheControl();
		// 禁用session
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);
		http.logout().addLogoutHandler(myLogoutHandler).logoutSuccessHandler(myLogoutSuccessHandler);
		http.httpBasic().authenticationEntryPoint(myAuthenticationEntryPoint);
	}

}