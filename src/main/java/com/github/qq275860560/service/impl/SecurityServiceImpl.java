package com.github.qq275860560.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import com.github.qq275860560.service.SecurityService;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Component
public class SecurityServiceImpl implements SecurityService {

	public SecurityServiceImpl() {

	}

	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public String encode(CharSequence rawPassword) {
		return passwordEncoder.encode(rawPassword);// spring推荐使用该方法加密
		// return DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes());
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return passwordEncoder.matches(rawPassword, encodedPassword);
		// return
		// DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes()).equals(encodedPassword);
	}

	private Map<String, String> user_password = new HashMap<String, String>() {
		{
			put("username1", passwordEncoder.encode("password1"));
			// put("username1", DigestUtils.md5DigestAsHex("password1".getBytes()));
			put("admin", passwordEncoder.encode("admin"));
			// put("admin", DigestUtils.md5DigestAsHex("admin".getBytes()));

		}
	};

	// 从缓存或数据库中查找
	@Override
	public String getPasswordByUserName(String username) {
		return user_password.get(username);
	}

	private List<String> urls = Arrays.asList("/api/github/qq275860560/web/pageUser",
			"/api/github/qq275860560/web/listUser", "/api/github/qq275860560/web/getUser",
			"/api/github/qq275860560/web/saveUser", "/api/github/qq275860560/web/deleteUser",
			"/api/github/qq275860560/web/updateUser", "/api/github/qq275860560/web/*");
	private Map<String, List<String>> url_role = new HashMap<String, List<String>>() {
		{
			put("/api/github/qq275860560/web/pageUser", Arrays.asList("ROLE_USER"));
			put("/api/github/qq275860560/web/listUser", Arrays.asList("ROLE_USER"));
			put("/api/github/qq275860560/web/getUser", Arrays.asList("ROLE_USER"));
			put("/api/github/qq275860560/web/saveUser", Arrays.asList("ROLE_ADMIN", "ROLE_USER"));
			put("/api/github/qq275860560/web/deleteUser", Arrays.asList("ROLE_ADMIN", "ROLE_USER"));
			put("/api/github/qq275860560/web/updateUser", Arrays.asList("ROLE_ADMIN", "ROLE_USER"));
			put("/api/github/qq275860560/web/*", Arrays.asList("ROLE_ADMIN"));

		}
	};

	// 从缓存或数据库中查找
	@Override
	public Set<String> getRoleNameSetByUrI(String url) {
		AntPathMatcher antPathMatcher = new AntPathMatcher();
		Set<String> set = new HashSet<>();
		for (String pattern : urls) {
			if (antPathMatcher.match(pattern, url)) {
				List<String> tmpList = url_role.get(pattern);
				if (tmpList == null || tmpList.isEmpty()) {// 至少有一个匹配url不需要权限时，立即放行
					return Collections.EMPTY_SET;
				} else {
					set.addAll(tmpList);
				}
			}
		}
		return set;
	}

	private Map<String, Set<String>> user_role = new HashMap<String, Set<String>>() {
		{
			put("username1", new HashSet<String>() {
				{
					add("ROLE_ADMIN");
					add("ROLE_HOME");
				}
			});
			put("admin", new HashSet<String>() {
				{
					add("ROLE_ADMIN");
				}
			});
		}
	};

	// 从缓存或数据库中查找
	@Override
	public Set<String> getRoleNameSetByUsername(String username) {
		return user_role.get(username);
	}

}
