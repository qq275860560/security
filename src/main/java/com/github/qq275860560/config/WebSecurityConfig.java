package com.github.qq275860560.config;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.vote.AbstractAccessDecisionManager;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.expression.WebExpressionVoter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.github.qq275860560.security.MyAccessDeniedHandler;
import com.github.qq275860560.security.MyAuthenticationEntryPoint;
import com.github.qq275860560.security.MyAuthenticationFailureHandler;
import com.github.qq275860560.security.MyAuthenticationSuccessHandler;
import com.github.qq275860560.security.MyFilterInvocationSecurityMetadataSource;
import com.github.qq275860560.security.MyLogoutHandler;
import com.github.qq275860560.security.MyLogoutSuccessHandler;
import com.github.qq275860560.security.MyRequestHeaderAuthenticationFilter;
import com.github.qq275860560.security.MyUserDetailsService;
import com.github.qq275860560.security.MyUsernamePasswordAuthenticationFilter;

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
	private PublicKey publicKey;
	@Autowired
	private MyAuthenticationFailureHandler myAuthenticationFailureHandler;
	@Autowired
	private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
	@Autowired
	private MyAuthenticationEntryPoint myAuthenticationEntryPoint;

	@Autowired
	private MyFilterInvocationSecurityMetadataSource myFilterInvocationSecurityMetadataSource;

	@Bean
	public AbstractAccessDecisionManager accessDecisionManager() {
		List<AccessDecisionVoter<? extends Object>> decisionVoters = Arrays.asList(new WebExpressionVoter(),
				new RoleVoter(), // 角色投票器,默认前缀为ROLE_,可以改成角色继承投票器
				new AuthenticatedVoter());
		return new AffirmativeBased(decisionVoters);

	}

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
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/**/*.html", "/**/*.css", "/**/*.woff", "/**/*.woff2", "/**/*.js", "/**/*.jpg",
				"/**/*.png", "/**/*.ico");
	}

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

		http.addFilterBefore(new MyRequestHeaderAuthenticationFilter(authenticationManagerBean(), publicKey,
				myUserDetailsService, myAuthenticationEntryPoint), UsernamePasswordAuthenticationFilter.class);

		http.addFilterBefore(new MyUsernamePasswordAuthenticationFilter(authenticationManagerBean(),
				myAuthenticationSuccessHandler, myAuthenticationFailureHandler),
				UsernamePasswordAuthenticationFilter.class);

		// 使用自定义授权策略
		// http.authorizeRequests().anyRequest().access("@myAuthorization.check(authentication,request)");

		//http.authorizeRequests().anyRequest().authenticated();
		 http.requestMatchers().antMatchers("/login","/api/**","/oauth/authorize","/oauth/token","/oauth/check_token","/oauth/confirm_access","/oauth/error")
         .and()
         .authorizeRequests()
         .antMatchers("/**").authenticated();
		
		http.authorizeRequests().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
			@Override
			public <O extends FilterSecurityInterceptor> O postProcess(O o) {
				o.setSecurityMetadataSource(myFilterInvocationSecurityMetadataSource);
				o.setAccessDecisionManager(accessDecisionManager());
				return o;
			}
		});

	}

}