package com.github.qq275860560.security;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.qq275860560.dao.RoleDao;
import com.github.qq275860560.dao.UserDao;
import com.github.qq275860560.domain.User;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Service
@Slf4j
public class MyUserDetailsService implements UserDetailsService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private RoleDao roleDao;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.info("授权:获取用户对应的角色权限");
		User user = userDao.findByUserName(username);
		if (user == null) {
			log.error(username + "账号不存在");
			throw new UsernameNotFoundException(username + "账号不存在");
		}
		String password = user.getPassword();
		boolean enabled = true;// 帐号是否可用
		boolean accountNonExpired = true;// 帐户是否过期
		boolean credentialsNonExpired = true;// 帐户密码是否过期，一般有的密码要求性高的系统会使用到，比较每隔一段时间就要求用户重置密码
		boolean accountNonLocked = true;// 帐户是否被冻结

		List<String> list = roleDao.listRoleNameByUsername(user.getUsername());
		// 初始化用户的权限
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList(StringUtils.join(list.toArray(), ","));
		// controller方法参数通过@AuthenticationPrincipal可以获得该对象
		return new org.springframework.security.core.userdetails.User(username, password, enabled, accountNonExpired,
				credentialsNonExpired, accountNonLocked, grantedAuthorities);

	}

}