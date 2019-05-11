package com.github.qq275860560.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.qq275860560.security.MyAccessDeniedHandler;
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

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors();
		http.csrf().disable();
		// 禁用headers缓存
		http.headers().cacheControl();
		// 禁用session
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		//哪些url不需要授权
		http.authorizeRequests().antMatchers("*.html","*.css","*.woff","*.woff2","*.js","*.jpg","*.png","*.ico").permitAll();
		//哪些url需要认证授权，为了开发方便，除了/login,所有POST请求需要认证授权
		http.authorizeRequests().antMatchers("/api/*").authenticated();
		
		http.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);
		http.logout().addLogoutHandler(myLogoutHandler).logoutSuccessHandler(myLogoutSuccessHandler);

	}

}
