package com.github.qq275860560.respository;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Repository
@Slf4j
public class RoleRespository {

	public Set<String> getRoleNameSetByUrI(String url) {

		org.springframework.util.AntPathMatcher antPathMatcher = new org.springframework.util.AntPathMatcher();
		List<String> list = Arrays.asList(url);// 遍历系统的所有url（包括正则表达式）

		Set<String> set = new HashSet<>();
		for (String tmp : list) {
			if (antPathMatcher.match(tmp, url)) {
				List<String> tmpList = listRoleNameByUrI(tmp);
				if (tmpList == null || tmpList.isEmpty()) {// 至少有一个匹配url不需要权限时，立即放行
					return null;
				} else {
					set.addAll(tmpList);
				}
			}
		}
		return set;
	}

	public List<String> listRoleNameByUrI(String url) {

		if (url.equals("/")) {
			return Arrays.asList("ROLE_HOME");
		} else if (url.equals("/api/github/qq275860560/web/updateUser")) {
			return Arrays.asList("ROLE_ADMIN");
		} else if (url.equals("/api/github/qq275860560/web/listUser")) {
			return Arrays.asList("ROLE_ADMIN", "ROLE_USER");
			// return null;
		} else {
			return null;
		}
	}

	public List<String> listRoleNameByUsername(String username) {
		if (username.equals("username1")) {
			return Arrays.asList("ROLE_ADMIN", "ROLE_HOME");
		} else if (username.equals("admin")) {
			return Arrays.asList("ROLE_ADMIN");
		} else {
			return null;
		}
	}
}
