package cn.e3mall.cart.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import cn.e3mall.common.utils.CookieUtils;
import cn.e3mall.common.utils.E3Result;
import cn.e3mall.pojo.TbUser;
import cn.e3mall.sso.service.TokenService;

public class LoginInterceptor implements HandlerInterceptor {

	@Autowired
	private TokenService tokenService;
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 前处理，执行handler之前执行此方法
		/**
		 * 1.从cookie中取出token
		 * 2.如果没有token，未登陆状态，直接放行
		 * 3.取到token，需要调用sso系统服务，根据token取用户信息
		 * 4.没有取到用户信息。登陆过期，直接放行。
		 * 5.取到用户信息。登陆状态。
		 * 6.把用户信息放到request中。只需要在Controller中判断request中是否包含user信息。放行。
		 */
		String token = CookieUtils.getCookieValue(request, "token");
		if(StringUtils.isBlank(token)) {
			return true;
		}
		E3Result result = tokenService.getUserByToken(token);
		if(result.getStatus() != 200) {
			return true;
		}
		TbUser user = (TbUser) result.getData();
		request.setAttribute("user", user);
		return true;
	}
	
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
		
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

}
