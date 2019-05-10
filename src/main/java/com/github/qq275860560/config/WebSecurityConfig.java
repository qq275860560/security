package com.github.qq275860560.config;

import java.security.PublicKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.qq275860560.security.MyAccessDeniedHandler;
import com.github.qq275860560.security.MyAuthenticationEntryPoint;
import com.github.qq275860560.security.MyAuthenticationFilter;
import com.github.qq275860560.security.MyLogoutSuccessHandler;
import com.github.qq275860560.security.MySimpleUrlAuthenticationFailureHandler;
import com.github.qq275860560.security.MySimpleUrlAuthenticationSuccessHandler;
import com.github.qq275860560.security.MyUserDetailsService;
import com.github.qq275860560.security.MyUsernamePasswordAuthenticationFilter;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Configuration
@EnableWebSecurity
// @EnableGlobalMethodSecurity(securedEnabled = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	MyUserDetailsService myUserDetailsService;
	@Autowired
	private MyLogoutSuccessHandler myLogoutSuccessHandler;
	@Autowired
	private MyAuthenticationEntryPoint myAuthenticationEntryPoint;
	@Autowired
	private MyAccessDeniedHandler myAccessDeniedHandler;
	@Autowired
	private MySimpleUrlAuthenticationSuccessHandler mySimpleUrlAuthenticationSuccessHandler;
	@Autowired
	private MySimpleUrlAuthenticationFailureHandler mySimpleUrlAuthenticationFailureHandler;

	@Autowired
	private PublicKey publicKey;
	@Autowired
	private ObjectMapper objectMapper;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();// 自定义加密方式，需要实现passwordEncoder接口
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.cors();
		http.csrf().disable();
		// 禁用headers缓存
		http.headers().cacheControl();

		// 禁用session
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeRequests().anyRequest().authenticated();

		http.addFilterAt(
				new MyUsernamePasswordAuthenticationFilter(authenticationManager(), objectMapper,
						mySimpleUrlAuthenticationSuccessHandler, mySimpleUrlAuthenticationFailureHandler),
				UsernamePasswordAuthenticationFilter.class);
		http.addFilter(new MyAuthenticationFilter(authenticationManager(), publicKey, myUserDetailsService));

		http.httpBasic().authenticationEntryPoint(myAuthenticationEntryPoint);
		http.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);
		// http.logout().logoutSuccessHandler(myLogoutSuccessHandler) .
		// deleteCookies("JSESSIONID").invalidateHttpSession(true) ;

	}

}
