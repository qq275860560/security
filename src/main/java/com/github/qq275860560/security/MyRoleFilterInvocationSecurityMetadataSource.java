package com.github.qq275860560.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;

import com.github.qq275860560.service.SecurityService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Component
@Slf4j
public class MyRoleFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	@Autowired
	private SecurityService securityService;

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		log.debug("授权:获取url对应的角色权限");
		HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
		String requestURI = request.getRequestURI();
		Set<String> set = securityService.getRoleNamesByUrI(requestURI);// 获取url及其正则对应角色/权限(ROLE_开头或SCOPE_开头),比如访问路径有/api/user/updateUser,应当查询/*,/api/*,/api/user/*,/api/user/updateUser对应的角色/权限并集
		if (set == null) {
			return Collections.EMPTY_LIST;
		}
		Collection<ConfigAttribute> configAttributes = new ArrayList<>();
		for (String roleName : set) {
			ConfigAttribute configAttribute = new SecurityConfig(roleName);
			configAttributes.add(configAttribute);
		}
		return configAttributes;
	}

	@Override
	public Collection<ConfigAttribute> getAllConfigAttributes() {
		return null;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return true;
	}
}
