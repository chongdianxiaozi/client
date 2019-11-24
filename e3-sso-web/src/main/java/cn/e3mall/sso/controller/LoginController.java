package cn.e3mall.sso.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.sso.service.LoginService;
/***
 * 登录处理功能
 * @author Administrator
 *
 */

@Controller
public class LoginController {
	@Autowired
	private LoginService loginService;
	@Value("${TOKEN_KEY}")
	private String TOKEN_KEY;
	
	@RequestMapping("/page/login")
	public String showLogin (String redirect, Model model) {
		model.addAttribute("redirect", redirect);
		return "login";
	}

	@RequestMapping(value="/user/login", method=RequestMethod.POST)
	@ResponseBody
	public E3Result login (String username, String password,
			HttpServletRequest request, HttpServletResponse response) {
		E3Result result = loginService.userLogin(username, password);
		if(result.getStatus() == 200) {
			String token = result.getData().toString();
			CookieUtils.setCookie(request, response, TOKEN_KEY, token);
		}
		return result;
	}
}
