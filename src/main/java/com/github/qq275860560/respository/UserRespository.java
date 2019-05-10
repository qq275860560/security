package com.github.qq275860560.respository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import com.github.qq275860560.domain.User;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Repository
@Slf4j
public class UserRespository {
	public User findByUserName(String username)  {
		if (username.equals("username1")) {
			return User.builder().username("username1").password(new BCryptPasswordEncoder().encode("password1"))
					.build();
		} else if (username.equals("admin")) {
			return User.builder().username("admin").password(new BCryptPasswordEncoder().encode("admin")).build();
		} else {
			return null;
		}
	}

	public Map<String, Object> pageUser() throws Exception {
		Map<String, Object> map = new HashMap<>();
		map.put("total", 2);	
		List<Map<String, Object>> list= Arrays.asList(new HashMap<String, Object>() {
			{
				put("userId", "1");
				put("username", "admin");
				put("roles", "ROLE_ADMIN");
			}
		}, new HashMap<String, Object>() {
			{
				put("userId", "2");
				put("username", "admin2");
				put("roles", "ROLE_ADMIN");
			}
		});
		map.put("list",list);
		return map;
	}
	
	public List<Map<String, Object>> listUser() throws Exception  {
		return Arrays.asList(new HashMap<String, Object>() {
			{
				put("userId", "1");
				put("username", "admin");
				put("roles", "ROLE_ADMIN");
			}
		}, new HashMap<String, Object>() {
			{
				put("userId", "2");
				put("username", "admin2");
				put("roles", "ROLE_ADMIN");
			}
		});
	}
	
	
	public Map<String, Object> getUser(String id )  throws Exception{
		return new HashMap<String, Object>() {
			{
				put("userId", id);
				put("username", "username"+id);
				put("roles", "ROLE_ADMIN");
			}
		} ;
		 
	}
	
	public int saveUser(Map<String, Object> map )  throws Exception{
		return 1;
	}
	public int deleteUser(String id )  throws Exception{
		return 1;
	}
	public int updateUser(Map<String, Object> map )  throws Exception {
		return 1;
	}
	
	
	
	
}