package com.github.qq275860560.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */

@Slf4j
public class MyRequestHeaderAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

 
	private RsaVerifier rsaVerifier;
 
	private MyUserDetailsService myUserDetailsService;

 
	private MyAuthenticationEntryPoint myAuthenticationEntryPoint;

	public MyRequestHeaderAuthenticationFilter(AuthenticationManager authenticationManager, RsaVerifier rsaVerifier,
			MyUserDetailsService myUserDetailsService, MyAuthenticationEntryPoint myAuthenticationEntryPoint

	) {

		super.setAuthenticationManager(authenticationManager);
		this.rsaVerifier = rsaVerifier;
		this.myUserDetailsService = myUserDetailsService;
		this.myAuthenticationEntryPoint = myAuthenticationEntryPoint;

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		log.debug("认证");
		String header = ((HttpServletRequest) request).getHeader("Authorization");

		if (header == null || !header.startsWith("Bearer ")) {
			chain.doFilter(request, response);
			return;
		}

		try {
			// eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.dXNlcm5hbWUx.bp4hmd1qwHyeZWHd9yqlNAeb6fyA0ZucxRlMg_YP2XkZZB8IHn-zewIndgudnr01c3M4Nb0_FMx6e5LqhOJwaQ
			String token = header.replaceAll("Bearer\\s+", "");
			String username = JwtHelper.decodeAndVerify(token, rsaVerifier).getClaims();
			UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

			UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
					userDetails.getPassword(), userDetails.getAuthorities());
			// 初始化UserDetail
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails((HttpServletRequest) request));

			SecurityContextHolder.getContext().setAuthentication(authentication);
		} catch (Exception e) {
			myAuthenticationEntryPoint.commence((HttpServletRequest) request, (HttpServletResponse) response,
					new BadCredentialsException(e.getMessage(), e));
			return;
		}

		chain.doFilter(request, response);
	}

}