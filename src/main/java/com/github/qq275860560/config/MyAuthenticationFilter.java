package com.github.qq275860560.config;

import java.io.IOException;
import java.security.PublicKey;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Slf4j
public class MyAuthenticationFilter extends BasicAuthenticationFilter {

 
	private PublicKey publicKey = null;

 

	private MyUserDetailsService myUserDetailsService;

	public MyAuthenticationFilter(AuthenticationManager authenticationManager, ObjectMapper objectMapper ,
			PublicKey publicKey, MyUserDetailsService myUserDetailsService) {
		super(authenticationManager);
 		this.publicKey = publicKey;
		this.myUserDetailsService = myUserDetailsService;

	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		log.info("认证");
		String header = request.getHeader("Authorization");

		if (header == null || !header.startsWith("Bearer ")) {
			chain.doFilter(request, response);
			return;
		}

		// parse the token.
		String username = Jwts.parser()
				// .setSigningKey(key)
				.setSigningKey(publicKey).parseClaimsJws(header.replace("Bearer ", "")).getBody().getSubject();
		UserDetails userDetails = myUserDetailsService.loadUserByUsername(username);

		UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
				userDetails.getPassword(), userDetails.getAuthorities());
		// 初始化UserDetail
		authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);

		chain.doFilter(request, response);
	}

}