package com.github.qq275860560.security;

import java.io.IOException;
import java.security.PublicKey;

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
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.RequestHeaderAuthenticationFilter;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Component
@Slf4j
public class MyAuthenticationFilter extends RequestHeaderAuthenticationFilter {

	@Autowired
	private PublicKey publicKey;
	@Autowired
	private MyUserDetailsService myUserDetailsService;

	@Autowired
	private MyAuthenticationEntryPoint myAuthenticationEntryPoint;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		log.info("认证");
		String header = ((HttpServletRequest) request).getHeader("Authorization");

		if (header == null || !header.startsWith("Bearer ")) {
			chain.doFilter(request, response);
			return;
		}

		try {
			// parse the token.
			String username = Jwts.parser()
					.setSigningKey(publicKey).parseClaimsJws(header.replace("Bearer ", "")).getBody().getSubject();
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

	@Override
	@Autowired
	public void setAuthenticationManager(AuthenticationManager authenticationManager) {
		super.setAuthenticationManager(authenticationManager);
	}

}