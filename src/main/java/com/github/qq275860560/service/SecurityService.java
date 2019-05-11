package com.github.qq275860560.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author jiangyuanlin@163.com
 *
 */
public interface SecurityService {
	/**
	 * @param username 登录账号名称
	 * @return 返回hashmap，包含两个键值对,username和password，其中password=BCryptPasswordEncoder.encode()
	 */
	public Map<String, Object> findByUserName(String username);

	public Set<String> getRoleNameSetByUrI(String url); 

	public List<String> listRoleNameByUsername(String username) ;
}