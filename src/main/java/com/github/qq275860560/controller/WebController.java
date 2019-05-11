package com.github.qq275860560.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.qq275860560.respository.UserRespository;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Controller
@ResponseBody
@Slf4j
public class WebController {



	@Autowired
	private UserRespository userRespository;

	/* curl -i -H "Content-Type:application/json;charset=UTF-8" \
	   -H "Authorization:Bearer eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTEiLCJleHAiOjE1NTc2NDAwMzF9.hp5ayhStNG6TD9V00JzwEvlZW9gCaSUgk3SHmq3Fcv9aN3X6rH2b_Brv4WYkBzDNaud6jW0wQrlOu9tNdkiyJsacL6FY-6D4mRbB3cXcGqnH53x-a0IZM8AjIbTM_doF73CnuRFK_5ZP3gYha7nHEvtKJqqylU55ZeRaje-vDoo" \
	   -X POST http://localhost:8080/api/github/qq275860560/web/pageUser \
	   -d '{"pageNum":1,"pageSize":10}'
	*/
	@RequestMapping(value = "/api/github/qq275860560/web/pageUser", method =RequestMethod.POST)
	public Map<String, Object> pageUser(@RequestBody Map<String, Object> requestMap)  throws Exception{
		String username=(String)SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("当前登录用户=" + username);
		Map<String, Object> data = userRespository.pageUser();
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK);
				put("msg", "分页搜索成功");
				put("data", data);
			}
		};
	}

	
	/* curl -i -H "Content-Type:application/json;charset=UTF-8" \
	   -H "Authorization:Bearer eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTEiLCJleHAiOjE1NTc1Mzk4MTN9.FEsVLyZv_RzNnd14z1Qawq_EZ5AOQ27_4BceNuX6eTYqWRNS9IW4A6U4PcXnbG6rVwPgWm9VNq7AxcJpyaOTAqSxTZrfv7CCAxE-G-IuydNeAzUaXfsdPMjRcwZlBjt_V3DdMUR94HGpwPEEnIeT_jBsAe5ic7pDWAzzTY0W36U" \
	   -X POST http://localhost:8080/api/github/qq275860560/web/listUser \
	   -d '{}'
	*/
	@RequestMapping(value = "/api/github/qq275860560/web/listUser", method =RequestMethod.POST)
	public Map<String, Object> listUser(@RequestBody Map<String, Object> requestMap)  throws Exception{
		String username=(String)SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("当前登录用户=" + username);
		List<Map<String, Object>> data = userRespository.listUser();
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK);
				put("msg", "获取列表成功");
				put("data", data);
			}
		};
	}
	
	/* curl -i -H "Content-Type:application/json;charset=UTF-8" \
	   -H "Authorization:Bearer eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTEiLCJleHAiOjE1NTc1Mzk4MTN9.FEsVLyZv_RzNnd14z1Qawq_EZ5AOQ27_4BceNuX6eTYqWRNS9IW4A6U4PcXnbG6rVwPgWm9VNq7AxcJpyaOTAqSxTZrfv7CCAxE-G-IuydNeAzUaXfsdPMjRcwZlBjt_V3DdMUR94HGpwPEEnIeT_jBsAe5ic7pDWAzzTY0W36U" \
	   -X POST http://localhost:8080/api/github/qq275860560/web/getUser \
	   -d '{}'
	*/
	@RequestMapping(value = "/api/github/qq275860560/web/getUser", method =RequestMethod.POST)
	public Map<String, Object> getUser(@RequestBody Map<String, Object> requestMap)  throws Exception{
		String username=(String)SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("当前登录用户=" + username);
		Map<String, Object> data=userRespository.getUser("");
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK);
				put("msg", "获取成功");
				put("data", data);
			}
		};
	}
	
	
	/* curl -i -H "Content-Type:application/json;charset=UTF-8" \
	   -H "Authorization:Bearer eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTEiLCJleHAiOjE1NTc1Mzk4MTN9.FEsVLyZv_RzNnd14z1Qawq_EZ5AOQ27_4BceNuX6eTYqWRNS9IW4A6U4PcXnbG6rVwPgWm9VNq7AxcJpyaOTAqSxTZrfv7CCAxE-G-IuydNeAzUaXfsdPMjRcwZlBjt_V3DdMUR94HGpwPEEnIeT_jBsAe5ic7pDWAzzTY0W36U" \
	   -X POST http://localhost:8080/api/github/qq275860560/web/saveUser \
	   -d '{}'
	*/
	@RequestMapping(value = "/api/github/qq275860560/web/saveUser", method =RequestMethod.POST)
	public Map<String, Object> saveUser(@RequestBody Map<String, Object> requestMap)  throws Exception{
		String username=(String)SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("当前登录用户=" + username);
		userRespository.saveUser(requestMap);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK);
				put("msg", "保存成功");
				put("data", null);
			}
		};
	}
	
	
	
	/* curl -i -H "Content-Type:application/json;charset=UTF-8" \
	   -H "Authorization:Bearer eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTEiLCJleHAiOjE1NTc2ODU3MDR9.nTn12YMZ5vx2ej5rMOeDc4RmHfkqD0xATvrkwGX7cTpJXTcqvMtAsxvn00dmqBfaBO6uzyKZ85KW9Ze3UCcfQTd6gq3nI6nKYZK_sJAw4wrzhN2aiG1Xj5FLqqZ75XRpCD_WpS4mXOBhTDK0ob34QOxUCc7beJdBjVgjs8BhK9M" \
	   -X POST http://localhost:8080/api/github/qq275860560/web/deleteUser \
	   -d '{}'
	*/
	@RequestMapping(value = "/api/github/qq275860560/web/deleteUser", method =RequestMethod.POST)
	public Map<String, Object> deleteUser(
			@RequestBody Map<String, Object> requestMap)  throws Exception{
		String username=(String)SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("当前登录用户=" + username);
		userRespository.deleteUser("");
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK);
				put("msg", "删除成功");
				put("data", null);
			}
		};
	}
	
	
	
	/* curl -i -H "Content-Type:application/json;charset=UTF-8" \
	   -H "Authorization:Bearer   eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTEiLCJleHAiOjE1NTc2ODkwNTN9.nvE-OXuXhUZ20XXTjWxEU-ZxnOIVSA0GIl1nCqavGe0JzlzlIUNv8ysLF5ldVuODESHh-WEkOzIG6EYt-l4BcwF7ncI3EkijOQde-wWMuxtIeg0RXiFHIilmQAGL5qcx6oASBiRIxtzg9oGr7sTca2PNA8Q0m0cur__5fKkJhWE" \
	   -X POST http://localhost:8080/api/github/qq275860560/web/updateUser \
	   -d '{}'
	*/
	@RequestMapping(value = "/api/github/qq275860560/web/updateUser", method =RequestMethod.POST)
	public Map<String, Object> updateUser(
			@RequestBody Map<String, Object> requestMap)  throws Exception{
		String username=(String)SecurityContextHolder.getContext().getAuthentication().getName();
		log.info("当前登录用户=" + username);
		userRespository.updateUser(requestMap);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK);
				put("msg", "更新成功");
				put("data", null);
			}
		};
	}

}
