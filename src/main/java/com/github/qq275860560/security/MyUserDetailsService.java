package com.github.qq275860560.security;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.github.qq275860560.service.SecurityService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Service
@Slf4j
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private SecurityService securityService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("登录或认证:获取用户对应的角色权限");
		String password = securityService.getPasswordByUserName(username);
		if (StringUtils.isEmpty(password)) {
			log.error(username + "用户不存在");
			throw new UsernameNotFoundException(username + "用户不存在");
		}
		boolean enabled = true;// 帐号是否可用
		boolean accountNonExpired = true;// 帐户是否过期
		boolean credentialsNonExpired = true;// 帐户密码是否过期，一般有的密码要求性高的系统会使用到，比较每隔一段时间就要求用户重置密码
		boolean accountNonLocked = true;// 帐户是否被冻结

		Set<String> set = securityService.getRoleNamesByUsername(username);
		// 初始化用户的权限
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList(StringUtils.arrayToDelimitedString(set.toArray(), ","));
		// controller方法参数通过@AuthenticationPrincipal可以获得该对象
		return new org.springframework.security.core.userdetails.User(username, password, enabled, accountNonExpired,
				credentialsNonExpired, accountNonLocked, grantedAuthorities);

	}

}