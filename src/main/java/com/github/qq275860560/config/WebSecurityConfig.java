package com.github.qq275860560.config;

import java.security.PublicKey;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	/*@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.cors();
		http.csrf().disable();// 关闭csrf// 关闭csrf防护

		http.authorizeRequests()// 定义哪些URL需要被保护、哪些不需要被保护

				.antMatchers("/css/**").permitAll()// 直接放行
				.antMatchers("/js/**").permitAll()// 直接放行
				.antMatchers("/images/**").permitAll() // 直接放行

				.anyRequest().authenticated(); // 任何请求,登录后可以访问

		http.formLogin()// 登录配置
				.usernameParameter("username").passwordParameter("password").loginPage("/login")// 自定义登录页

				.loginProcessingUrl("/login") // 自定义登录接口，默认/login
				.defaultSuccessUrl("/")// 登录成功后的页面（前台需要post方法提交）
				.failureUrl("/login?error")// 登录失败后的页面
				.permitAll(); // 登录页面直接放行
		// <form th:action="@{/login}" method="POST">
		// <input type="text" name="username" />
		// <input type="password" name="password" />
		// <input type="checkbox" name="rember"/>
		// <input type="submit" value="login" />
		// </form>

		http.logout().logoutSuccessUrl("/") // 退出设置
				.permitAll(); // 退出及退出成功后权限
		// <form th:action="@{/logout}" method="post">
		// <input type="submit" value="注销"/>
		// </form>
		http.rememberMe().rememberMeParameter("rember");

	}*/

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.cors();
		http.csrf().disable();
		// 禁用headers缓存
		http.headers().cacheControl();

		// 禁用session
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

		http.authorizeRequests().anyRequest().authenticated();

		http.addFilterAt(new MyUsernamePasswordAuthenticationFilter(authenticationManager(), objectMapper,
				    mySimpleUrlAuthenticationSuccessHandler,
				mySimpleUrlAuthenticationFailureHandler), UsernamePasswordAuthenticationFilter.class);
		http.addFilter(new MyAuthenticationFilter(authenticationManager(),  publicKey,
				myUserDetailsService));

		http.httpBasic().authenticationEntryPoint(myAuthenticationEntryPoint);
		http.exceptionHandling().accessDeniedHandler(myAccessDeniedHandler);
		// http.logout().logoutSuccessHandler(myLogoutSuccessHandler) .
		// deleteCookies("JSESSIONID").invalidateHttpSession(true) ;

	}

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

	@Value("${expirationSeconds}")
	private long expirationSeconds;

}
