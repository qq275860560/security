package com.github.qq275860560.security;

import java.util.Collection;
import java.util.Iterator;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Component
@Slf4j
public class MyAccessDecisionManager implements AccessDecisionManager {
	// 比较用户的角色/权限名称 和 url的角色权限，如果至少一个相同，则放行

	@Override
	public void decide(Authentication authentication, Object object, Collection<ConfigAttribute> configAttributes)
			throws AccessDeniedException, InsufficientAuthenticationException {
		log.trace("授权:决策");
		Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();

		for (Iterator<ConfigAttribute> iterator = configAttributes.iterator(); iterator.hasNext();) {// 遍历url所对应的角色/权限
			ConfigAttribute configAttribute = iterator.next();
			String authority = configAttribute.getAttribute();
			for (GrantedAuthority grantedAuthority : grantedAuthorities) {// 遍历用户所拥有的角色/权限
				if (authority.trim().equals(grantedAuthority.getAuthority())) {
					return;// 匹配则放行
				}
			}
		}
		throw new AccessDeniedException("没有权限");
	}

	@Override
	public boolean supports(ConfigAttribute attribute) {
		return true;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}
}
