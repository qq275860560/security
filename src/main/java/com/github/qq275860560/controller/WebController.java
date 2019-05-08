package com.github.qq275860560.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.qq275860560.dao.UserDao;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jiangyuanlin@163.com
 *
 */
@Controller
@Slf4j
public class WebController {

	/**
	 * @param userDetail
	 * @param model
	 * @return
	 */
	@RequestMapping("/")
	public String index(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetail,
			Model model) {
		log.info("当前用户=" + userDetail);
		model.addAttribute("entity", new HashMap<String, Object>() {
			{
				put("username", userDetail.getUsername());
				put("authorities", userDetail.getAuthorities());
				put("isEnable", userDetail.isEnabled());

			}
		});
		return "home";
	}

	@RequestMapping("/login")
	public String login(Model model) {
		return "login";
	}

	@Autowired
	private UserDao userDao;

	// curl -i -H "Content-Type:application/json;charset=UTF-8" -H
	// "Authorization:Bearer
	// eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJ1c2VybmFtZTEiLCJleHAiOjE1NTY3Nzk2Nzh9.ed1SXt85vLTmQ4LSKvjvNjcJ9af347YVPgSCVAP5rO9LT1z6L1f_JCFLnbu4c9VYvp5dllNQIdXEphE2qVhQkJ4-qTMakvvkqq4GhWXYYen_AQBngB4XQU788RxfQQXmRQsU3JoUdlrbSNoVS7_M_fioid8ci4SlQIc_-Ph_DyY"
	// -X POST http://localhost:8080/api/github/qq275860560/web/listUser
	// @Secured({"ROLE_ADMIN","ROLE_USER1"})
	@RequestMapping(value = "/api/github/qq275860560/web/listUser", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> listUser(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetail) {
		log.info("当前用户=" + userDetail);
		List<Map<String, Object>> list = userDao.listUser();
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK);
				put("msg", "查询成功");
				put("data", list);
			}
		};
	}

	@RequestMapping(value = "/api/github/qq275860560/web/saveUser", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> saveUser(@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetail) {
		log.info("当前用户=" + userDetail);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK);
				put("msg", "保存成功");
				put("data", null);
			}
		};
	}

	@RequestMapping(value = "/api/github/qq275860560/web/updateUser", method = { RequestMethod.GET, RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> updateUser(
			@AuthenticationPrincipal org.springframework.security.core.userdetails.User userDetail) {
		log.info("当前用户=" + userDetail);
		return new HashMap<String, Object>() {
			{
				put("code", HttpStatus.OK);
				put("msg", "更新成功");
				put("data", null);
			}
		};
	}

}
