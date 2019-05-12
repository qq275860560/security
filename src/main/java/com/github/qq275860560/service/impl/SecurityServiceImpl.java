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
public class SecurityServiceImpl extends SecurityService {
	


	
	/**用户密码加密策略(如果使用spring默认的springBCryptPasswordEncoder,不需要重写该方法)
	 * @param rawPassword
	 * @return
	 */  
	/*@Override
	public String encode(CharSequence rawPassword) {
		return new BCryptPasswordEncoder().encode(rawPassword);// spring推荐使用该方法加密
		// return DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes());
	}*/

	/**用户密码核对策略(如果使用spring默认的springBCryptPasswordEncoder,不需要重写该方法)
	 * @param rawPassword
	 * @param encodedPassword
	 * @return
	 */
	/*@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return new BCryptPasswordEncoder().matches(rawPassword, encodedPassword);
		// return DigestUtils.md5DigestAsHex(rawPassword.toString().getBytes()).equals(encodedPassword);
	}*/

	private Map<String, String> user_password = new HashMap<String, String>() {
		{
			put("username1", new BCryptPasswordEncoder().encode("password1"));
			// put("username1", DigestUtils.md5DigestAsHex("password1".getBytes()));
			put("admin", new BCryptPasswordEncoder().encode("admin"));
			// put("admin", DigestUtils.md5DigestAsHex("admin".getBytes()));

		}
	};

	/**根据登录账号查询密码
	  * 根据登录账号查询密码，此密码非明文密码，而是PasswordEncoder对明文加密后的密码，因为
    * spring security框架中数据库默认保存的是PasswordEncoder对明文加密后的密码
	 * 
	 * @param username 登录账号名称
	 * @return 返回字符串
	 */	
	@Override
	public String getPasswordByUserName(String username) {
		// 从缓存或数据库中查找
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

	/**
	 *   根据请求路径查询对应的角色名称列表，
	 *   登录用户至少拥有一个角色，才能访问
	 *   如果返回null或空集合或包含ROLE_ANONYMOUS，代表该url不需要权限控制，任何用户(包括匿名)用户都可以访问
	 *   如果url符合某个正则表达式，应当把正则表达式的角色也返回，比如/api/a的角色为ROLE_1,ROLE_2, 而数据库中还存在/api/*的角色为ROLE_3,ROLE_4；由于/api/a属于正则表达式/api/*,所以应当返回ROLE_1,ROLE_2,ROLE_3,ROLE_4
	 * @param url 请求路径（ip端口之后的路径）
	 * @return
	 */	
	@Override
	public Set<String> getRoleNameSetByUrI(String url) {
		// 从缓存或数据库中查找
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

	/**
	 *   根据登录用户查询对应的角色名称列表，
	 *   如果返回null或空集合，代表该用户没有权限，这类用户其实跟匿名用户没有什么区别
	 *   如果username隶属于某高层次的角色或组织，应当把高层次的角色或组织对应的角色也返回，比如username的角色为ROLE_1, ROLE_1继承ROLE_2角色，并且username属于A部门，A部门拥有角色ROLE_3；所以应当返回ROLE_1,ROLE_2,ROLE_3
	 * @param username 登录用户名称
	 * @return
	 */
	@Override
	public Set<String> getRoleNameSetByUsername(String username) {
		// 从缓存或数据库中查找
		return user_role.get(username);
	}

}
