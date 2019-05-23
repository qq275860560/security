package com.github.qq275860560.security;

import java.util.ArrayList;
import java.util.Collection;
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
public class MyFilterInvocationSecurityMetadataSource implements FilterInvocationSecurityMetadataSource {

	@Autowired
	private SecurityService securityService;

	@Override
	public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
		log.debug("授权:获取url对应的角色权限");
		HttpServletRequest request = ((FilterInvocation) object).getHttpRequest();
		String requestURI = request.getRequestURI();
		Set<String> set = securityService.getRoleNameSetByUrI(requestURI);// 获取url及其正则对应角色/权限,比如访问路径有/api/user/updateUser,应当查询/*,/api/*,/api/user/*,/api/user/updateUser对应的角色/权限并集
		// 如果url对应的角色/权限为空或者包含ROLE_ANONYMOUS，直接放行，比如/login接口必定要放行(这种情况也可以通过WebSecurityConfigurerAdapter.configure(HttpSecurity)配置)
		if (set == null || set.isEmpty() || set.contains("ROLE_ANONYMOUS"))
			return null;
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
