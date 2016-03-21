package net.datafans.common.http.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.datafans.common.http.constant.CommonAttribute;
import net.datafans.common.http.constant.CommonParameter;
import net.datafans.common.http.handler.TokenHandler;
import net.datafans.common.http.util.UrlMatcher;

import net.datafans.common.util.LogUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

public class TokenInterceptor implements HandlerInterceptor {

	@Autowired(required = false)
	private TokenHandler tokenHandler;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		if (!UrlMatcher.versionParsed(request)) {
			return true;
		}

		String token = request.getParameter(CommonParameter.TOKEN);

		if (token == null) {
			return true;
		}

		if (tokenHandler == null) {
			return true;
		}

		String platform = request.getParameter(CommonParameter.PLATFORM);
		int userId = tokenHandler.getUserId(token, platform, request.getRequestURI());
		if (userId == 0) {
			return true;
		}

		request.setAttribute(CommonAttribute.USER_ID, String.valueOf(userId));
		
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}
